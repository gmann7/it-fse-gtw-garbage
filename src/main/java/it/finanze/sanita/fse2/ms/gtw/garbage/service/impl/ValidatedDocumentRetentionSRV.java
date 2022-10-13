package it.finanze.sanita.fse2.ms.gtw.garbage.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.finanze.sanita.fse2.ms.gtw.garbage.client.IConfigItemsClient;
import it.finanze.sanita.fse2.ms.gtw.garbage.config.Constants;
import it.finanze.sanita.fse2.ms.gtw.garbage.exceptions.BusinessException;
import it.finanze.sanita.fse2.ms.gtw.garbage.repository.IValidatedDocumentRepo;
import it.finanze.sanita.fse2.ms.gtw.garbage.repository.entity.ValidatedDocumentEventsETY;
import it.finanze.sanita.fse2.ms.gtw.garbage.service.IValidatedDocumentRetentionSRV;
import it.finanze.sanita.fse2.ms.gtw.garbage.utility.DateUtility;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ValidatedDocumentRetentionSRV implements IValidatedDocumentRetentionSRV {

	@Autowired
	private IValidatedDocumentRepo valDocRepo;

	@Autowired
	private IConfigItemsClient configClient;

	@Override
	public List<String> deleteOnValDocDB(final int day) {
		List<String> output = new ArrayList<>();

		try {
			log.debug("DELETE DATA ON TRANSACTIONS-DB - starting...");

			// Find
			log.debug("DELETE DATA ON TRANSACTIONS-DB - find records to delete...");
			Date dateToRemove = DateUtility.getDateCondition(day);
			List<ValidatedDocumentEventsETY> entities = valDocRepo.findOldValidatedDocument(dateToRemove);

			List<ObjectId> ids = new ArrayList<>();
			for (ValidatedDocumentEventsETY e : entities) {
				output.add(e.getWorkflowInstanceId());
				ids.add(new ObjectId(e.getId()));
			}

			// Delete
			log.debug("DELETE DATA ON FSE-DB - delete record...");
			int recordDeleted = valDocRepo.deleteOldValidatedDocument(ids);

			log.debug("DELETE DATA ON FSE-DB- Records deleted {}.", recordDeleted);
			log.debug("DELETE DATA ON FSES-DB - finished.");
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
			output.put(Constants.ConfigItems.SUCCESS_FSE_RETENTION_HOURS,
					Integer.parseInt(items.get(Constants.ConfigItems.SUCCESS_FSE_RETENTION_HOURS)));
			output.put(Constants.ConfigItems.BLOCKING_ERROR_FSE_RETENTION_HOURS,
					Integer.parseInt(items.get(Constants.ConfigItems.BLOCKING_ERROR_FSE_RETENTION_HOURS)));
			output.put(Constants.ConfigItems.NON_BLOCKING_ERROR_FSE_RETENTION_HOURS,
					Integer.parseInt(items.get(Constants.ConfigItems.NON_BLOCKING_ERROR_FSE_RETENTION_HOURS)));
		} catch (Exception e) {
			log.error("Errore durante la lettura delle configurazioni necessarie per la Fse Retention.", e);
			throw new BusinessException(
					"Errore durante la lettura delle configurazioni necessarie per la Fse Retention. ", e);
		}

		return output;
	}

}
