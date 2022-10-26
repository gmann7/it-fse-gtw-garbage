package it.finanze.sanita.fse2.ms.gtw.garbage.repository.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import it.finanze.sanita.fse2.ms.gtw.garbage.config.Constants;
import it.finanze.sanita.fse2.ms.gtw.garbage.config.RetentionCFG;
import it.finanze.sanita.fse2.ms.gtw.garbage.exceptions.BusinessException;
import it.finanze.sanita.fse2.ms.gtw.garbage.repository.IValidatedDocumentRepo;
import it.finanze.sanita.fse2.ms.gtw.garbage.repository.entity.ValidatedDocumentEventsETY;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class ValidatedDocumentRepo implements IValidatedDocumentRepo {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7642250403832399384L;

	@Autowired
	@Qualifier("mongo-template-valdoc")
	private transient MongoTemplate mongoTemplate;

	@Autowired
	private transient RetentionCFG retentionCfg;

	@Override
	public int deleteOldValidatedDocument(final List<ObjectId> idsToRemove) {
		Long output = null;

		try {
			Document query = new Document();

			query.append("_id", new Document("$in", idsToRemove));

			String targetCollection = Constants.ComponentScan.Collections.VALIDATED_DOCUMENTS;
			targetCollection = Constants.Profile.TEST_PREFIX + targetCollection;

			output = mongoTemplate.getCollection(targetCollection).deleteMany(query).getDeletedCount();

		} catch (Exception e) {
			log.error("Errore nel tentativo di eliminare la lista di ids nella collection 'validated_documents': ", e);
			throw new BusinessException(
					"Errore nel tentativo di eliminare la lista di ids nella collection 'validated_documents': ", e);
		}

		return output.intValue();
	}

	@Override
	public List<ValidatedDocumentEventsETY> findOldValidatedDocument(Date oldToRemove) {
		List<ValidatedDocumentEventsETY> output = new ArrayList<>();

		try {

			Query query = new Query();
			query.fields().include("insertion_date");
			query.addCriteria(Criteria.where("insertion_date").lt(oldToRemove));
			query.limit(retentionCfg.getQueryLimit());

			output = mongoTemplate.find(query, ValidatedDocumentEventsETY.class);

		} catch (Exception e) {
			log.error("Errore nel tentativo di recuperare i documents");
			throw new BusinessException("Errore nel tentativo di recuperare i documents", e);
		}

		return output;
	}

}
