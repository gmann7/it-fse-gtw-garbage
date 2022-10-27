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

import com.mongodb.client.result.DeleteResult;

import it.finanze.sanita.fse2.ms.gtw.garbage.config.Constants;
import it.finanze.sanita.fse2.ms.gtw.garbage.config.RetentionCFG;
import it.finanze.sanita.fse2.ms.gtw.garbage.exceptions.BusinessException;
import it.finanze.sanita.fse2.ms.gtw.garbage.repository.IValidatedDocumentRepo;
import it.finanze.sanita.fse2.ms.gtw.garbage.repository.entity.ValidatedDocumentsETY;
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

			String targetCollection = Constants.Collections.VALIDATED_DOCUMENTS;
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
	public Integer deleteValidatedDocuments(final Date oldToRemove) {
		Integer deletedRecords = 0;
		try {
			Query query = new Query();
			query.addCriteria(Criteria.where("insertion_date").lt(oldToRemove));
			query.limit(retentionCfg.getQueryLimit());

			DeleteResult dRes = mongoTemplate.remove(query, ValidatedDocumentsETY.class);
			deletedRecords = (int)dRes.getDeletedCount();

		} catch (Exception e) {
			log.error("Errore nel tentativo di recuperare i documents");
			throw new BusinessException("Errore nel tentativo di recuperare i documents", e);
		}

		return deletedRecords;
	}

}
