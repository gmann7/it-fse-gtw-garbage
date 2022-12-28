/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.gtw.garbage.repository.impl;

import it.finanze.sanita.fse2.ms.gtw.garbage.config.RetentionCFG;
import it.finanze.sanita.fse2.ms.gtw.garbage.exceptions.BusinessException;
import it.finanze.sanita.fse2.ms.gtw.garbage.repository.IValidatedDocumentRepo;
import it.finanze.sanita.fse2.ms.gtw.garbage.repository.entity.ValidatedDocumentsETY;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
	public List<String> deleteValidatedDocuments(final Date oldToRemove) {
		List<String> output = new ArrayList<>();
		try {
			Query query = new Query();
			query.fields().include("w_id");
			query.addCriteria(Criteria.where("insertion_date").lt(oldToRemove));
			query.limit(retentionCfg.getQueryLimit());

			List<ValidatedDocumentsETY> validatedDocuments = mongoTemplate.findAllAndRemove(query, ValidatedDocumentsETY.class);
			if (!CollectionUtils.isEmpty(validatedDocuments)) {
				output = validatedDocuments.stream().map(ValidatedDocumentsETY::getWorkflowInstanceId).collect(Collectors.toList());
			}

		} catch (Exception e) {
			log.error("Errore nel tentativo di recuperare i documents");
			throw new BusinessException("Errore nel tentativo di recuperare i documents", e);
		}

		return output;
	}

}
