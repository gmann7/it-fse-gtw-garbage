/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.gtw.garbage.unit.scheduler;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bson.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import it.finanze.sanita.fse2.ms.gtw.garbage.client.impl.ConfigItemsClient;
import it.finanze.sanita.fse2.ms.gtw.garbage.client.response.ConfigItemETY;
import it.finanze.sanita.fse2.ms.gtw.garbage.config.Constants;
import it.finanze.sanita.fse2.ms.gtw.garbage.config.RetentionCFG;
import it.finanze.sanita.fse2.ms.gtw.garbage.repository.entity.TransactionEventsETY;
import it.finanze.sanita.fse2.ms.gtw.garbage.scheduler.DataRetentionScheduler;
import it.finanze.sanita.fse2.ms.gtw.garbage.utility.DateUtility;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 *
 */
@Slf4j
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
@ActiveProfiles(Constants.Profile.TEST)
@DisplayName("Data Retention Scheduler Unit Test")
class DataRetentionSchedulerUnitTest {

	/**
	 * Collection name of data.
	 */
	static final String DATA_COLLECTION = "test_ini_eds_invocation";

	@Getter
	static final int hoursAfterInsertion = 120;

	@Autowired
	DataRetentionScheduler retentionScheduler;

	@Autowired
	@Qualifier("mongo-template-data")
	MongoTemplate dataTemplate;	
	
	@Autowired
	@Qualifier("mongo-template-transaction")
	MongoTemplate transactionTemplate;

	@MockBean
	RetentionCFG retentionCFG;

	@SpyBean
	RestTemplate restTemplate;

	@BeforeEach
	void setup() {
		dataTemplate.dropCollection(DATA_COLLECTION);
		dataTemplate.dropCollection(TransactionEventsETY.class);
	}
	
	@ParameterizedTest
	@DisplayName("Deletion of transactions and data - Same size")
	@ValueSource(ints = {5000, 15000, 100000})
	void runDeleteTransactions(final int size) {
		
		mockConfigurationItems(0, 0, HttpStatus.OK);
		given(retentionCFG.getQueryLimit()).willReturn(size);

		log.info(" START DATA PREPARATION... ");
		transactionsPreparationItems(size, false, getHoursAfterInsertion());
		
		List<TransactionEventsETY> transactions = transactionTemplate.find(new Query(), TransactionEventsETY.class);
		assumeTrue(!CollectionUtils.isEmpty(transactions), "Transactions should be inserted before testing the deletion");

		List<Document> data = dataTemplate.find(new Query(), Document.class, DATA_COLLECTION);
		assumeTrue(!CollectionUtils.isEmpty(data), "Data should be inserted before testing the deletion.");
		retentionScheduler.run();

		transactions = transactionTemplate.find(new Query(), TransactionEventsETY.class);
		assertTrue(CollectionUtils.isEmpty(transactions));

		data = dataTemplate.find(new Query(), Document.class, DATA_COLLECTION);
		assertTrue(CollectionUtils.isEmpty(data));
	}

	private void mockConfigurationItems(final Integer success, final Integer error, final HttpStatus status) {
		List<ConfigItemETY> items = new ArrayList<>();
		Map<String, String> configItems = new HashMap<>();
		configItems.put(Constants.ConfigItems.SUCCESS_TRANSACTION_RETENTION_HOURS, String.valueOf(success));
		items.add(new ConfigItemETY("GARBAGE", configItems));

		ConfigItemsClient.ConfigItemDTO configItemDTO = new ConfigItemsClient.ConfigItemDTO();
		configItemDTO.setConfigurationItems(items);
		configItemDTO.setSize(items.size());

		if (status.is4xxClientError()) {
			doThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST)).when(restTemplate).exchange(
					anyString(),
					eq(HttpMethod.GET),
					any(HttpEntity.class),
					eq(ConfigItemsClient.ConfigItemDTO.class));
		} else if (status.is5xxServerError()) {
			doThrow(new ResourceAccessException("")).when(restTemplate).exchange(
					anyString(),
					eq(HttpMethod.GET),
					any(HttpEntity.class),
					eq(ConfigItemsClient.ConfigItemDTO.class));
		} else {
			doReturn(new ResponseEntity<>(configItemDTO, HttpStatus.OK)).when(restTemplate).exchange(
					anyString(),
					eq(HttpMethod.GET),
					any(HttpEntity.class),
					eq(ConfigItemsClient.ConfigItemDTO.class)
			);
		}

	}

	@Test
	@DisplayName("Error test if config items client not available")
	void errorTest() {
		final int size = 500;
		mockConfigurationItems(getHoursAfterInsertion() + 1, 0, HttpStatus.BAD_REQUEST);
		given(retentionCFG.getQueryLimit()).willReturn(size);

		log.info(" START DATA PREPARATION... ");
		transactionsPreparationItems(size, true, getHoursAfterInsertion());

		List<TransactionEventsETY> transactions = transactionTemplate.find(new Query(), TransactionEventsETY.class);
		assumeTrue(!CollectionUtils.isEmpty(transactions), "Transactions should be inserted before testing the deletion");

		List<Document> data = dataTemplate.find(new Query(), Document.class, DATA_COLLECTION);
		assumeTrue(!CollectionUtils.isEmpty(data), "Data should be inserted before testing the deletion.");
		assertDoesNotThrow(() -> retentionScheduler.run());

		mockConfigurationItems(getHoursAfterInsertion() + 1, 0, HttpStatus.INTERNAL_SERVER_ERROR);
		assertDoesNotThrow(() -> retentionScheduler.run());
	}

	@Test
	@DisplayName("Action only on items that passed threshold")
	void noDeletion() {
		final int size = 500;
		mockConfigurationItems(getHoursAfterInsertion() + 1, 0, HttpStatus.OK);
		given(retentionCFG.getQueryLimit()).willReturn(size);

		log.info(" START DATA PREPARATION... ");
		transactionsPreparationItems(size, true, getHoursAfterInsertion());

		List<TransactionEventsETY> transactions = transactionTemplate.find(new Query(), TransactionEventsETY.class);
		assumeTrue(!CollectionUtils.isEmpty(transactions), "Transactions should be inserted before testing the deletion");

		List<Document> data = dataTemplate.find(new Query(), Document.class, DATA_COLLECTION);
		assumeTrue(!CollectionUtils.isEmpty(data), "Data should be inserted before testing the deletion.");
		retentionScheduler.run();

		transactions = transactionTemplate.find(new Query(), TransactionEventsETY.class);
		assertEquals(size, transactions.size(), "No item should have been deleted");
	}

	@Test
	@DisplayName("Action only over items that passed threshold, no action on others")
	void multipleItems() {
		final int size = 500;
		mockConfigurationItems(getHoursAfterInsertion() + 1, 0, HttpStatus.OK);
		given(retentionCFG.getQueryLimit()).willReturn(size);

		log.info(" START DATA PREPARATION... ");
		transactionsPreparationItems(size, false, getHoursAfterInsertion());
		transactionsPreparationItems(size, false, getHoursAfterInsertion() + 2); // Oldest, ones to be deleted
		
		List<TransactionEventsETY> transactions = transactionTemplate.find(new Query(), TransactionEventsETY.class);
		assumeTrue(transactions.size() == size*2, "Transactions should be inserted before testing the deletion");

		List<Document> data = dataTemplate.find(new Query(), Document.class, DATA_COLLECTION);
		assumeTrue(data.size() == size*2, "Data should be inserted before testing the deletion.");
		retentionScheduler.run();

		transactions = transactionTemplate.find(new Query(), TransactionEventsETY.class);
		assertEquals(size, transactions.size(), "Only half of items should have been deleted");
	}

	@Test
	@DisplayName("Action on items items in success or in error with different time")
	void multipleItemStates() {
		final int size = 500;
		mockConfigurationItems(getHoursAfterInsertion() + 1, getHoursAfterInsertion(), HttpStatus.OK);
		given(retentionCFG.getQueryLimit()).willReturn(size);

		log.info(" START DATA PREPARATION... ");
		transactionsPreparationItems(size, true, getHoursAfterInsertion() + 2);
		transactionsPreparationItems(size, false, getHoursAfterInsertion() + 1);
		
		List<TransactionEventsETY> transactions = transactionTemplate.find(new Query(), TransactionEventsETY.class);
		assumeTrue(transactions.size() == size*2, "Transactions should be inserted before testing the deletion");

		List<Document> data = dataTemplate.find(new Query(), Document.class, DATA_COLLECTION);
		assumeTrue(data.size() == size*2, "Data should be inserted before testing the deletion.");
		retentionScheduler.run();

		transactions = transactionTemplate.find(new Query(), TransactionEventsETY.class);
		assertEquals(size, transactions.size(), "Only items in BLOCKING state should have been deleted");
	}

	@Test
	@DisplayName("Action on items items in success or in error with different time")
	void deleteOkState() {
		final int size = 500;
		mockConfigurationItems(getHoursAfterInsertion(), getHoursAfterInsertion()* 2, HttpStatus.OK);
		given(retentionCFG.getQueryLimit()).willReturn(size);

		log.info(" START DATA PREPARATION... ");
		transactionsPreparationItems(size, true, getHoursAfterInsertion() + 1);
		transactionsPreparationItems(size, false, getHoursAfterInsertion() + 1);
		
		List<TransactionEventsETY> transactions = transactionTemplate.find(new Query(), TransactionEventsETY.class);
		assumeTrue(transactions.size() == size*2, "Transactions should be inserted before testing the deletion");

		List<Document> data = dataTemplate.find(new Query(), Document.class, DATA_COLLECTION);
		assumeTrue(data.size() == size*2, "Data should be inserted before testing the deletion.");
		retentionScheduler.run();

		transactions = transactionTemplate.find(new Query(), TransactionEventsETY.class);
		assertEquals(size, transactions.size(), "Only items in BLOCKING state should have been deleted");

		transactions.forEach(transaction -> assertEquals(Constants.ConfigItems.SUCCESS_TRANSACTION_RETENTION_HOURS, transaction.getEventStatus(), "All remaining items should have be in error"));

		data = dataTemplate.find(new Query(), Document.class, DATA_COLLECTION);
		assertEquals(size, data.size(), "Only half of data should have been deleted");
	}

	void transactionsPreparationItems(final int size, final boolean isSuccessful, final int hoursAfterInsertion) {
		
		Date oldDate = DateUtility.getDateCondition(hoursAfterInsertion);

		List<Document> data = new ArrayList<>();
		List<TransactionEventsETY> dataList = new ArrayList<>();
		for (int i = 0; i < size; i++) {
			TransactionEventsETY transaction = new TransactionEventsETY();
			final String id = UUID.randomUUID().toString();
			data.add(new Document().append("workflow_instance_id", id));
			transaction.setWorkflowInstanceId(id);
			transaction.setEventDate(oldDate);
			transaction.setExpiringDate(oldDate);
			transaction.setEventStatus(Constants.ConfigItems.SUCCESS_TRANSACTION_RETENTION_HOURS);
			dataList.add(transaction);
		}

		transactionTemplate.insertAll(dataList);
		dataTemplate.insert(data, DATA_COLLECTION);
	}
}
