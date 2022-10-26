package it.finanze.sanita.fse2.ms.gtw.garbage.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
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

	@Autowired
	private IValidatedDocumentRepo valDocRepo;
	@Autowired
	MongoTemplate mongoTemplate;

	@Autowired
	private IConfigItemsClient configClient;

	@Override
	public List<String> deleteOnValDocDB(final int day) {
		List<String> output = new ArrayList<>();

		try {
			log.debug("DELETE DATA ON VALIDATED-DOCUMENT-DB - starting...");

			Date dateToRemove = DateUtility.getDateCondition(day);
			valDocRepo.findOldValidatedDocument(dateToRemove);

			log.debug("DELETE DATA ON VALIDATED-DOCUMENT-DB - finished.");
		} catch (Exception e) {
			log.error("Errore durante esecuzione Engine Fse Retention per il contenuto di 'validated_documents': ", e);
			throw new BusinessException(
					"Errore durante esecuzione Engine Fse Retention per il contenuto di 'validated_documents': ", e);
		}

		return output;
	}

	@Override
	public Map<String, Integer> readConfigurations() {
		Map<String, Integer> output = new HashMap<>();

		try {
			final Map<String, String> items = configClient.getConfigurationItems().get(0).getItems();
			output.put(Constants.ConfigItems.SUCCESS_VALDOC_RETENTION_DAY,
					Integer.parseInt(items.get(Constants.ConfigItems.VALIDATED_DOCUMENT_DAYS)));
		} catch (Exception e) {
			log.error("Errore durante la lettura delle configurazioni necessarie per la validazione del documento.", e);
			throw new BusinessException(
					"Errore durante la lettura delle configurazioni necessarie per la validazione del documento. ", e);
		}

		return output;
	}

}
