/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.gtw.garbage.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.finanze.sanita.fse2.ms.gtw.garbage.client.IConfigItemsClient;
import it.finanze.sanita.fse2.ms.gtw.garbage.config.Constants;
import it.finanze.sanita.fse2.ms.gtw.garbage.exceptions.BusinessException;
import it.finanze.sanita.fse2.ms.gtw.garbage.repository.IValidatedDocumentRepo;
import it.finanze.sanita.fse2.ms.gtw.garbage.service.IValidatedDocumentRetentionSRV;
import it.finanze.sanita.fse2.ms.gtw.garbage.utility.DateUtility;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ValidatedDocumentRetentionSRV implements IValidatedDocumentRetentionSRV {

	/**
	 * Serial version uid.
	 */
	private static final long serialVersionUID = -7417678306790314579L;
	
	@Autowired
	private IValidatedDocumentRepo valDocRepo;
	

	@Autowired
	private IConfigItemsClient configClient;

	@Override
	public void deleteValidatedDocuments(final int day) {
		try {
			Date dateToRemove = DateUtility.addDay(new Date(), -day);
			Integer deletedRecords = valDocRepo.deleteValidatedDocuments(dateToRemove);
			log.debug("DELETE VALIDATED-DOCUMENT-DB:" + deletedRecords);
		} catch (Exception e) {
			log.error("Errore durante esecuzione Engine Fse Retention per il contenuto di 'validated_documents': ", e);
			throw new BusinessException("Errore durante esecuzione Engine Fse Retention per il contenuto di 'validated_documents': ", e);
		}
	}

	@Override
	public Map<String, Integer> readConfigurations() {
		Map<String, Integer> output = new HashMap<>();

		try {
			final Map<String, String> items = configClient.getConfigurationItems().get(0).getItems();
			output.put(Constants.ConfigItems.VALIDATED_DOCUMENT_RETENTION_DAY,
					Integer.parseInt(items.get(Constants.ConfigItems.VALIDATED_DOCUMENT_RETENTION_DAY)));
		} catch (Exception e) {
			log.error("Errore durante la lettura delle configurazioni per la retention di un documento.", e);
			throw new BusinessException("Errore durante la lettura delle configurazioni per la retention di un documento. ", e);
		}

		return output;
	}

}
