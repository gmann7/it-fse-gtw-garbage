/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.gtw.garbage.repository.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.mongodb.client.result.DeleteResult;

import it.finanze.sanita.fse2.ms.gtw.garbage.exceptions.BusinessException;
import it.finanze.sanita.fse2.ms.gtw.garbage.repository.ICfgItemsRetentionRepo;
import lombok.extern.slf4j.Slf4j;

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
			query.addCriteria(Criteria.where("deleted").is(true).and("last_update_date").lt(dateToRemove));

			DeleteResult dRes = mongoTemplate.remove(query, clazz);
			cfgItemsDeleted = (int)dRes.getDeletedCount();
		} catch (Exception ex) {
			log.error("Error while perform delete cfg items" , ex);
			throw new BusinessException("Error while perform delete cfg items" , ex);
		}
		return cfgItemsDeleted;
	}

	

}
