package it.finanze.sanita.fse2.ms.gtw.garbage;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.mongodb.assertions.Assertions;

import it.finanze.sanita.fse2.ms.gtw.garbage.config.Constants;
import it.finanze.sanita.fse2.ms.gtw.garbage.repository.IValidatedDocumentRepo;
import it.finanze.sanita.fse2.ms.gtw.garbage.repository.entity.ValidatedDocumentEventsETY;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
@ActiveProfiles(Constants.Profile.TEST)
@DisplayName("Data Retention Scheduler Unit Test")
@ComponentScan(basePackages = { Constants.ComponentScan.BASE })
class ValidatedDocumentRepoTest {

	@Autowired
	@Qualifier("mongo-template-valdoc")
	MongoTemplate mongoTemplate;

	@Autowired
	private IValidatedDocumentRepo valDocRepo;

	@BeforeEach
	public void setup() {
		mongoTemplate.dropCollection(ValidatedDocumentEventsETY.class);
	}

	@Test
	@DisplayName("findOldDate")
	void findOldValidatedDocument() throws Exception {

		ValidatedDocumentEventsETY ety = new ValidatedDocumentEventsETY();

		ety.setId("1");
		ety.setInsertionDate(new Date());
		mongoTemplate.insert(ety);

		final Calendar oldDate = Calendar.getInstance();
		oldDate.setTime(new Date());
		oldDate.add(Calendar.DATE, +1);

		List<ValidatedDocumentEventsETY> entities = valDocRepo.findOldValidatedDocument(oldDate.getTime());

		Assertions.assertNotNull(valDocRepo.findOldValidatedDocument(oldDate.getTime()));
		assertEquals(ety, entities.get(0));

	}

	@Test
	@DisplayName("deleteOldValidatedDocument")
	void deleteOldValidatedDocument() {

		ValidatedDocumentEventsETY ety = new ValidatedDocumentEventsETY();
		ObjectId obj = new ObjectId("63482399c8705744d1fd1efd");

		ety.setId(obj.toHexString());
		ety.setInsertionDate(new Date());
		mongoTemplate.insert(ety);

		final Calendar oldDate = Calendar.getInstance();
		oldDate.setTime(new Date());
		oldDate.add(Calendar.DAY_OF_MONTH, +1);
		List<ValidatedDocumentEventsETY> entities = valDocRepo.findOldValidatedDocument(oldDate.getTime());

		List<ObjectId> ids = new ArrayList<>();
		for (ValidatedDocumentEventsETY e : entities) {

			ids.add(new ObjectId(e.getId()));
		}

		int count = valDocRepo.deleteOldValidatedDocument(ids);

		final Calendar oldDate2 = Calendar.getInstance();
		oldDate2.setTime(new Date());
		oldDate2.add(Calendar.DAY_OF_MONTH, -1);
		List<ValidatedDocumentEventsETY> entities2 = valDocRepo.findOldValidatedDocument(oldDate2.getTime());

		List<ObjectId> ids2 = new ArrayList<>();
		for (ValidatedDocumentEventsETY e : entities2) {

			ids2.add(new ObjectId(e.getId()));
		}

		int count2 = valDocRepo.deleteOldValidatedDocument(ids2);

		Assertions.assertNotNull(ids);
		Assertions.assertNotNull(count);
		assertEquals(1, count);
		assertEquals(0, count2);
	}

}
