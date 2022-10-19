package it.finanze.sanita.fse2.ms.gtw.garbage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.bson.Document;
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

import it.finanze.sanita.fse2.ms.gtw.garbage.config.Constants;
import it.finanze.sanita.fse2.ms.gtw.garbage.repository.IRulesRepo;
import it.finanze.sanita.fse2.ms.gtw.garbage.repository.entity.SchemaETY;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
@ActiveProfiles(Constants.Profile.TEST)
@DisplayName("Rules Retention Scheduler Unit Test")
@ComponentScan(basePackages = { Constants.ComponentScan.BASE })
public class RulesRepoTest {

	@Autowired
	@Qualifier("mongo-template-rules")
	MongoTemplate mongoTemplate;
	
	@Autowired
	IRulesRepo rulesRepo;
	
	@BeforeEach
	public void setup() {
		mongoTemplate.dropCollection(SchemaETY.class);
		mongoTemplate.dropCollection("test_transform");
	}

	@Test
	void deleteOldSchemas() {
		
		SchemaETY ety = new SchemaETY();
		ety.setLastUpdateDate(new Date());
		
		Date oldDate = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(oldDate);
		c.add(Calendar.DATE, -6);
		oldDate = c.getTime();
		
		SchemaETY ety2 = new SchemaETY();
		ety2.setLastUpdateDate(oldDate);
		
		mongoTemplate.insert(ety);
		mongoTemplate.insert(ety2);
		
		assertTrue(rulesRepo.deleteOldSchemas(false));
		
		List<SchemaETY> response = mongoTemplate.findAll(SchemaETY.class);
		
		assertEquals(1, response.size());
		
		for (SchemaETY schemaEty : response) {
			log.info(schemaEty.toString());
		}
		
	}
	
	@Test
	void deleteOldTransforms() {
		
		Document insideDoc1 = new Document();
		insideDoc1.put("deleted", false);
		insideDoc1.put("last_update_date", new Date());
		
		Document doc1 = new Document();
		doc1.put("document", insideDoc1);
		
		Date oldDate = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(oldDate);
		c.add(Calendar.DATE, -6);
		oldDate = c.getTime();
		
		Document insideDoc2 = new Document();
		insideDoc2.put("deleted", false);
		insideDoc2.put("last_update_date", oldDate);
		
		Document doc2 = new Document();
		doc2.put("document", insideDoc2);
		
		mongoTemplate.insert(doc1, "test_transform");
		mongoTemplate.insert(doc2, "test_transform");
		
		assertTrue(rulesRepo.deleteOldTransforms(false));
		
		List<Document> response = mongoTemplate.findAll(Document.class, "test_transform");
		
		assertEquals(1, response.size());
		
		for (Document doc : response) {
			log.info(doc.toString());
		}
		
	}
	
}
