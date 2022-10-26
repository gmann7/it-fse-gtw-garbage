package it.finanze.sanita.fse2.ms.gtw.garbage.repository.impl;

import java.util.Calendar;
import java.util.Date;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import it.finanze.sanita.fse2.ms.gtw.garbage.config.Constants;
import it.finanze.sanita.fse2.ms.gtw.garbage.exceptions.BusinessException;
import it.finanze.sanita.fse2.ms.gtw.garbage.repository.IRulesRepo;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class RulesRepo implements IRulesRepo {

	/**
	 * Serial version uid.
	 */
	private static final long serialVersionUID = 8523735389685074232L;
	
	@Qualifier("mongo-template-rules")
	@Autowired
	MongoTemplate mongoTemplate;

	@Override
	public boolean deleteOldSchematrons(boolean alreadyDeleted) {
		Document query = new Document();

		try {

			Date date = new Date();
			Calendar c = Calendar.getInstance();
			c.setTime(date);
			c.add(Calendar.DATE, -5);
			date = c.getTime();

			query.append("deleted", new Document("$eq", false)).append("last_update_date", new Document("$lt", date));

			String targetCollection = Constants.Collections.SCHEMATRON;
			targetCollection = Constants.Profile.TEST_PREFIX + targetCollection;

			long result = mongoTemplate.getCollection(targetCollection).deleteMany(query).getDeletedCount();

			if (result != 0)
				return true;
			else
				return false;
		} catch (Exception e) {
			log.error("error");
			throw new BusinessException("error");
		}
	}

	@Override
	public boolean deleteOldSchemas(boolean alreadyDeleted) {
		Document query = new Document();

		try {
			Date date = new Date();
			Calendar c = Calendar.getInstance();
			c.setTime(date);
			c.add(Calendar.DATE, -5);
			date = c.getTime();

			query.append("deleted", new Document("$eq", false)).append("last_update", new Document("$lt", date));
			String targetCollection = Constants.Collections.SCHEMA;
			targetCollection = Constants.Profile.TEST_PREFIX + targetCollection;

			long result = mongoTemplate.getCollection(targetCollection).deleteMany(query).getDeletedCount();

			if (result != 0)
				return true;
			else
				return false;
		} catch (Exception e) {
			log.error("error");
			throw new BusinessException("error");
		}
	}

	@Override
	public boolean deleteOldTransforms(boolean alreadyDeleted) {

		Document query = new Document();

		try {
			Date date = new Date();
			Calendar c = Calendar.getInstance();
			c.setTime(date);
			c.add(Calendar.DATE, -5);
			date = c.getTime();

			query.append("document.deleted",
					new Document("$eq", false)).append("document.last_update_date", new Document("$lt", date));

			String targetCollection = Constants.Collections.FHIR_TRANSFORM;
			targetCollection = Constants.Profile.TEST_PREFIX + targetCollection;

			long result = mongoTemplate.getCollection(targetCollection).deleteMany(query).getDeletedCount();

			if (result != 0)
				return true;
			else
				return false;
		} catch (Exception e) {
			log.error("error");
			throw new BusinessException("error");
		}
	}

}
