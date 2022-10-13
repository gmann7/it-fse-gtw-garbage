package it.finanze.sanita.fse2.ms.gtw.garbage;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import com.mongodb.assertions.Assertions;

import it.finanze.sanita.fse2.ms.gtw.garbage.config.Constants;
import it.finanze.sanita.fse2.ms.gtw.garbage.repository.IValidatedDocumentRepo;
import it.finanze.sanita.fse2.ms.gtw.garbage.repository.entity.ValidatedDocumentEventsETY;
import it.finanze.sanita.fse2.ms.gtw.garbage.repository.impl.ValidatedDocumentRepo;

@Configuration
@SpringBootTest(classes = ValidatedDocumentRepo.class)
@ExtendWith(SpringExtension.class)
@ComponentScan(basePackages = { "it.sanita.garbage" })
@ActiveProfiles("test")
public class ValidatedDocumentRepoTest {

	@Autowired
	@Qualifier("mongo-template-valdoc")
	MongoTemplate mongoTemplate;

	@Autowired
	private IValidatedDocumentRepo valDocRepo;

	@BeforeEach
	public void setup() {
		mongoTemplate.dropCollection(ValidatedDocumentEventsETY.class);
	}

	public void populateQry() {
		ValidatedDocumentEventsETY ety = new ValidatedDocumentEventsETY();
		ety.setId("id");
		ety.setPrimaryKeySchema("primary_key_schema");
		ety.setPrimaryKeySchematron("primary_key-schematron");
		ety.setPrimaryKeyTransf("primary_key_transf");
		ety.setWorkflowInstanceId("w_id");
		ety.setInsertionDate(new Date());

		mongoTemplate.insert(ety);
	}

	@Test
	@DisplayName("findOldDate")
	void findOldValidatedDocument() throws Exception {

		Document document = new Document();
		document.append("id", "1");
		document.append("w_id", "w1");
		document.append("insertion_date", new Date(2022 / 10 / 12));

		String collection = Constants.ComponentScan.Collections.VALIDATED_DOCUMENTS;

		mongoTemplate.getCollection(collection).insertOne(document);

		Document document2 = new Document();
		document2.append("id", "2");
		document2.append("w_id", "w2");
		document2.append("insertion_date", new Date(2022 / 10 / 7));

		mongoTemplate.getCollection(collection).insertOne(document2);

		final Calendar oldDate = Calendar.getInstance();
		oldDate.setTime(new Date());
		oldDate.add(Calendar.DAY_OF_MONTH, -4);

		Assertions.assertNotNull(valDocRepo.findOldValidatedDocument(oldDate.getTime()));
		assertEquals(document2.get("insertion_date"), valDocRepo.findOldValidatedDocument(oldDate.getTime()));

	}

	@Test
	@DisplayName("deleteOldValidatedDocument")
	void deleteOldValidatedDocument() {

		Date date = new Date(2022 / 10 / 8);

		List<ValidatedDocumentEventsETY> entities = valDocRepo.findOldValidatedDocument(date);
		List<ObjectId> ids = new ArrayList<>();
		for (ValidatedDocumentEventsETY e : entities) {

			ids.add(new ObjectId(e.getId()));
		}

		int count = valDocRepo.deleteOldValidatedDocument(ids);

		assertEquals(1, count);
	}

}
