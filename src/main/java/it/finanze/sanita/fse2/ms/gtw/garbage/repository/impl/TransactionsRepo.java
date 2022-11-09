/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
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

import it.finanze.sanita.fse2.ms.gtw.garbage.config.RetentionCFG;
import it.finanze.sanita.fse2.ms.gtw.garbage.exceptions.BusinessException;
import it.finanze.sanita.fse2.ms.gtw.garbage.repository.ITransactionsRepo;
import it.finanze.sanita.fse2.ms.gtw.garbage.repository.entity.TransactionEventsETY;
import lombok.extern.slf4j.Slf4j;

/**
 *
 */
@Slf4j
@Repository
public class TransactionsRepo implements ITransactionsRepo {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7642250403832399384L;

	@Autowired
	@Qualifier("mongo-template-transaction")
	private transient MongoTemplate mongoTemplate;

    @Autowired
	private transient RetentionCFG retentionCfg;

//	@Autowired
//	private transient ProfileUtility profileUtility;
    
	@Override
	public int deleteOldTransactions(final List<ObjectId> idsToRemove) {
		Long output = null;
				
		try {
			Document query = new Document();
			 
			query.append("_id", new Document("$in", idsToRemove));
//			String targetCollection = Constants.ComponentScan.Collections.TRANSACTION_DATA;
//			if (profileUtility.isTestProfile()) {
//				targetCollection = Constants.Profile.TEST_PREFIX + targetCollection;
//			}
			output = mongoTemplate.getCollection(mongoTemplate.getCollectionName(TransactionEventsETY.class)).deleteMany(query).getDeletedCount();
			
		} catch (Exception e) {
			log.error("Errore nel tentativo di eliminare la lista di ids nella collection 'transaction_data': " , e);
			throw new BusinessException("Errore nel tentativo di eliminare la lista di ids nella collection 'transaction_data': " , e);
		}
		
		return output.intValue();
	}

	@Override
	public List<TransactionEventsETY> findOldTransactions(String state, Date oldToRemove) {
		List<TransactionEventsETY> output = new ArrayList<>();

		try {
			
			Query query = new Query();
			query.fields().include("_id", "workflow_instance_id");
			query.addCriteria(Criteria.where("eventStatus").is(state).and("eventDate").lte(oldToRemove));
			query.limit(retentionCfg.getQueryLimit());
			
			output = mongoTemplate.find(query, TransactionEventsETY.class);
			
		} catch (Exception e) {
			log.error("Errore nel tentativo di recuperare le transactions con 'eventStatus': {} " , state);
			throw new BusinessException("Errore nel tentativo di recuperare le transactions" , e);
		}
		
		return output;
	}

}
