/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.gtw.garbage.repository.impl;

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

	@Autowired
	@Qualifier("mongo-template-transaction")
	private MongoTemplate mongoTemplate;

    @Autowired
	private RetentionCFG retentionCfg;

    
	@Override
	public int deleteOldTransactions(final List<ObjectId> idsToRemove) {
		Long output = null;
				
		try {
			Document query = new Document();
			 
			query.append("_id", new Document("$in", idsToRemove));
			output = mongoTemplate.getCollection(mongoTemplate.getCollectionName(TransactionEventsETY.class)).deleteMany(query).getDeletedCount();
			
		} catch (Exception e) {
			log.error("Errore nel tentativo di eliminare la lista di ids nella collection 'transaction_data': " , e);
			throw new BusinessException("Errore nel tentativo di eliminare la lista di ids nella collection 'transaction_data': " , e);
		}
		
		return output.intValue();
	}
	
	
	@Override
	public List<TransactionEventsETY> deleteExpiringTransactionData(final String eventType) {
		List<TransactionEventsETY> output = null;
		try {
			Query query = new Query();
			query.fields().include("workflow_instance_id");
			
			Criteria criteria = new Criteria();
			criteria.orOperator(Criteria.where("eventType").is(eventType).and("eventStatus").is("SUCCESS"), 
					Criteria.where("eventStatus").is("BLOCKING_ERROR"));
			criteria.and("expiring_date").lt(new Date());
			query.addCriteria(criteria);
			query.limit(retentionCfg.getQueryLimit());
			output = mongoTemplate.findAllAndRemove(query, TransactionEventsETY.class);
		} catch (Exception ex) {
			log.error("Error while perform deleteExpiringTransactionData" , ex);
			throw new BusinessException("Error while perform deleteExpiringTransactionData" , ex);
		}

		return output;
	}

}
