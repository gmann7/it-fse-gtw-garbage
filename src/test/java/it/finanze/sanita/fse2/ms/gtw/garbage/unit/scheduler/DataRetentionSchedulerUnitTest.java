/**
 * 
 */
package it.finanze.sanita.fse2.ms.gtw.garbage.unit.scheduler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static org.mockito.BDDMockito.given;

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
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.CollectionUtils;

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
 * @author AndreaPerquoti
 *
 */
@Slf4j
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DisplayName("Data Retention Scheduler Unit Test")
@ComponentScan(basePackages = {Constants.ComponentScan.BASE})
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

	@MockBean
	ConfigItemsClient configClient;

	@BeforeEach
	void setup() {
		dataTemplate.dropCollection(DATA_COLLECTION);
		dataTemplate.dropCollection(TransactionEventsETY.class);
	}
	
	@ParameterizedTest
	@DisplayName("Deletion of transactions and data - Same size")
	@ValueSource(ints = {5000, 15000, 100000})
	void runDeleteTransactions(final int size) {
		
		mockConfigurationItems(0, 0);
		given(retentionCFG.getQueryLimit()).willReturn(size);

		log.info(" START DATA PREPARATION... ");
		transactionsPreparationItems(size, true, getHoursAfterInsertion());
		
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

	private void mockConfigurationItems(final Integer success, final Integer error) {
		List<ConfigItemETY> items = new ArrayList<>();
		Map<String, String> configItems = new HashMap<>();
		configItems.put(Constants.ConfigItems.SUCCESS_TRANSACTION_RETENTION_HOURS, String.valueOf(success));
		configItems.put(Constants.ConfigItems.BLOCKING_ERROR_TRANSACTION_RETENTION_HOURS, String.valueOf(error));
		configItems.put(Constants.ConfigItems.NON_BLOCKING_ERROR_TRANSACTION_RETENTION_HOURS, String.valueOf(error));

		items.add(new ConfigItemETY("GARBAGE", configItems));
		given(configClient.getConfigurationItems()).willReturn(items);
	}

	@Test
	@DisplayName("Action only on items that passed threshold")
	void noDeletion() {
		final int size = 500;
		mockConfigurationItems(getHoursAfterInsertion() + 1, 0);
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
		mockConfigurationItems(getHoursAfterInsertion() + 1, 0);
		given(retentionCFG.getQueryLimit()).willReturn(size);

		log.info(" START DATA PREPARATION... ");
		transactionsPreparationItems(size, true, getHoursAfterInsertion());
		transactionsPreparationItems(size, true, getHoursAfterInsertion() + 2); // Oldest, ones to be deleted
		
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
		mockConfigurationItems(getHoursAfterInsertion() + 1, getHoursAfterInsertion());
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
		assertTrue(CollectionUtils.isEmpty(transactions), "All items should have been deleted");
	}

	@Test
	@DisplayName("Action on items items in success or in error with different time")
	void deleteOkState() {
		final int size = 500;
		mockConfigurationItems(getHoursAfterInsertion(), getHoursAfterInsertion()* 2);
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
		assertEquals(size, transactions.size(), "Only items in SUCCESS state should have been deleted");

		transactions.forEach(transaction -> assertEquals(Constants.ConfigItems.BLOCKING_ERROR_TRANSACTION_RETENTION_HOURS, transaction.getEventStatus(), "All remaining items should have be in error"));

		data = dataTemplate.find(new Query(), Document.class, DATA_COLLECTION);
		assertEquals(size, data.size(), "Only half of data should have been deleted");
	}

	void transactionsPreparationItems(final int size, final boolean isSuccessful, final int hourseAfterInsertion) {
		
		Date oldDate = DateUtility.getDateCondition(hourseAfterInsertion);

		List<Document> data = new ArrayList<>();
		List<TransactionEventsETY> dataList = new ArrayList<>();
		for (int i = 0; i < size; i++) {
			TransactionEventsETY transaction = new TransactionEventsETY();
			final String id = UUID.randomUUID().toString();
			data.add(new Document().append("workflow_instance_id", id));
			transaction.setWorkflowInstanceId(id);
			transaction.setEventDate(oldDate);
			transaction.setEventStatus(isSuccessful ? Constants.ConfigItems.SUCCESS_TRANSACTION_RETENTION_HOURS : Constants.ConfigItems.BLOCKING_ERROR_TRANSACTION_RETENTION_HOURS);
			dataList.add(transaction);
		}

		transactionTemplate.insertAll(dataList);
		dataTemplate.insert(data, DATA_COLLECTION);
	}

}
