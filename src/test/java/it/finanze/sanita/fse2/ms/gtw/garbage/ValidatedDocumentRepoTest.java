/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.gtw.garbage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
import it.finanze.sanita.fse2.ms.gtw.garbage.repository.entity.ValidatedDocumentsETY;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
@ActiveProfiles(Constants.Profile.TEST)
@DisplayName("Data Retention Scheduler Unit Test")
@ComponentScan(basePackages = { Constants.ComponentScan.BASE })
class ValidatedDocumentRepoTest {

	@Autowired
	@Qualifier("mongo-template-valdoc")
	MongoTemplate mongoTemplate;

 

	@BeforeEach
	public void setup() {
		mongoTemplate.dropCollection(ValidatedDocumentsETY.class);
	}
 
}
