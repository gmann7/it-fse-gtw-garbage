/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.gtw.garbage.repository.impl;

import java.util.Date;

import it.finanze.sanita.fse2.ms.gtw.garbage.repository.entity.EngineETY;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.mongodb.client.result.DeleteResult;

import it.finanze.sanita.fse2.ms.gtw.garbage.exceptions.BusinessException;
import it.finanze.sanita.fse2.ms.gtw.garbage.repository.ICfgItemsRetentionRepo;
import it.finanze.sanita.fse2.ms.gtw.garbage.repository.entity.DictionaryETY;
import lombok.extern.slf4j.Slf4j;

import static it.finanze.sanita.fse2.ms.gtw.garbage.repository.entity.EngineETY.FIELD_LAST_SYNC;
import static it.finanze.sanita.fse2.ms.gtw.garbage.repository.entity.EngineETY.MIN_ENGINE_AVAILABLE;
import static org.springframework.data.domain.Sort.Direction.*;
import static org.springframework.data.mongodb.core.query.Criteria.*;

@Slf4j
@Repository
public class CfgItemsRetentionRepo implements ICfgItemsRetentionRepo {

	
	@Qualifier("mongo-template-rules")
	@Autowired
	MongoTemplate mongoTemplate;
 
 
	@Override
	public Integer deleteCfgItems(final Date dateToRemove, final Class<?> clazz) {
		Integer cfgItemsDeleted = 0;

		try {
			Query query = new Query();
			query.addCriteria(where("deleted").is(true).and("last_update_date").lt(dateToRemove));

			DeleteResult dRes = mongoTemplate.remove(query, clazz);
			cfgItemsDeleted = (int)dRes.getDeletedCount();
		} catch (Exception ex) {
			log.error("Error while perform delete cfg items" , ex);
			throw new BusinessException("Error while perform delete cfg items" , ex);
		}
		return cfgItemsDeleted;
	}
	
	@Override
	public Integer deleteTerminology(final Date dateToRemove) {
		Integer cfgItemsDeleted = 0;

		try {
			Query query = new Query();
			query.addCriteria(where("deleted").is(true).and("creation_date").lt(dateToRemove));

			DeleteResult dRes = mongoTemplate.remove(query, DictionaryETY.class);
			cfgItemsDeleted = (int)dRes.getDeletedCount();
		} catch (Exception ex) {
			log.error("Error while perform delete terminology" , ex);
			throw new BusinessException("Error while perform delete terminology" , ex);
		}
		return cfgItemsDeleted;
	}

	@Override
	public Integer deleteEngines(Date dateToRemove) {

		// Cases:
		// [1] If one engine available, do nothing
		// [2] If multiple engines but all expired, leave the most recent
		// [3] If no engine expired, do nothing
		// [4] If some engines expired and some aren't, remove the expired ones

		int engines = 0;

		// Retrieve all expired engines, sort by last_sync
		Query q = new Query(
			where(EngineETY.FIELD_LAST_SYNC).lt(dateToRemove)
		).with(Sort.by(DESC, FIELD_LAST_SYNC));

		try {
			DeleteResult res;
			// Verify how many engines we got
			// We always want to keep at least one engine
			long size = mongoTemplate.count(new Query(), EngineETY.class);
			// If we have more than one engine
			if(size > MIN_ENGINE_AVAILABLE) {
				// Count the expired engines
				long expired = mongoTemplate.count(q, EngineETY.class);
				// Do not perform queries if no engine expired
				if(expired > 0) {
					if(expired == size) {
						// If all engines are expired, remove everything but keep the most recent
						res = mongoTemplate.remove(q.skip(MIN_ENGINE_AVAILABLE), EngineETY.class);
					} else {
						// Otherwise remove only the expired ones
						res = mongoTemplate.remove(q, EngineETY.class);
					}
					// Save the delete count
					engines = (int) res.getDeletedCount();
				}
			}
		} catch (Exception e) {
			log.error("Error while perform delete engines" , e);
			throw new BusinessException("Error while perform delete engines" , e);
		}

		return engines;
	}


}
