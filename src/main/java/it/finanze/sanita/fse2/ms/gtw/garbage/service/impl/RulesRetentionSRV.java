package it.finanze.sanita.fse2.ms.gtw.garbage.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.finanze.sanita.fse2.ms.gtw.garbage.client.IConfigItemsClient;
import it.finanze.sanita.fse2.ms.gtw.garbage.config.Constants;
import it.finanze.sanita.fse2.ms.gtw.garbage.exceptions.BusinessException;
import it.finanze.sanita.fse2.ms.gtw.garbage.repository.IRulesRepo;
import it.finanze.sanita.fse2.ms.gtw.garbage.service.IRulesRetentionSRV;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class RulesRetentionSRV implements IRulesRetentionSRV {

	private static final long serialVersionUID = -667918997674784439L;

	@Autowired
	private IRulesRepo rulesRepo;

	@Autowired
	private IConfigItemsClient configClient;

	@Override
	public Boolean deleteOnRulesDB(boolean deletedCollection) {

		try {
			log.debug("DELETE DATA ON DATA-DB - starting...");
			if (deletedCollection == false) {
				return rulesRepo.deleteOldSchemas(deletedCollection);
			} else {
				return false;
			}

		} catch (Exception e) {
			log.error("Errore durante esecuzione Engine Data Retention per il contenuto di 'ini_eds_invocation': ", e);
			throw new BusinessException(
					"Errore durante esecuzione Engine Data Retention per il contenuto di 'ini_eds_invocation': ", e);
		}
	}

	@Override
	public Map<String, Integer> readConfigurations() {
		Map<String, Integer> output = new HashMap<>();

		try {
			final Map<String, String> items = configClient.getConfigurationItems().get(0).getItems();
			output.put(Constants.ConfigItems.SUCCESS_TRANSACTION_RETENTION_HOURS,
					Integer.parseInt(items.get(Constants.ConfigItems.SUCCESS_TRANSACTION_RETENTION_HOURS)));
			output.put(Constants.ConfigItems.BLOCKING_ERROR_TRANSACTION_RETENTION_HOURS,
					Integer.parseInt(items.get(Constants.ConfigItems.BLOCKING_ERROR_TRANSACTION_RETENTION_HOURS)));
			output.put(Constants.ConfigItems.NON_BLOCKING_ERROR_TRANSACTION_RETENTION_HOURS,
					Integer.parseInt(items.get(Constants.ConfigItems.NON_BLOCKING_ERROR_TRANSACTION_RETENTION_HOURS)));
		} catch (Exception e) {
			log.error("Errore durante la lettura delle configurazioni necessarie per la Data Retention. ", e);
			throw new BusinessException(
					"Errore durante la lettura delle configurazioni necessarie per la Data Retention. ", e);
		}

		return output;
	}
}
