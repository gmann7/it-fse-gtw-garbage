/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
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
import it.finanze.sanita.fse2.ms.gtw.garbage.repository.IDataRepo;
import it.finanze.sanita.fse2.ms.gtw.garbage.repository.ITransactionsRepo;
import it.finanze.sanita.fse2.ms.gtw.garbage.repository.entity.TransactionEventsETY;
import it.finanze.sanita.fse2.ms.gtw.garbage.service.IDataRetentionSRV;
import it.finanze.sanita.fse2.ms.gtw.garbage.utility.DateUtility;
import lombok.extern.slf4j.Slf4j;

/**
 * @author AndreaPerquoti
 *
 */
@Slf4j
@Service
public class DataRetentionSRV implements IDataRetentionSRV {

	private static final long serialVersionUID = -667918997674784439L;
	
	@Autowired
	private IDataRepo dataRepo;
	
	@Autowired
	private ITransactionsRepo transactionsRepo;

	@Autowired
	private IConfigItemsClient configClient;
    
	@Override
	public Integer deleteOnDataDB(final List<String> idsToDelete) {
		Integer output = 0;
		try {
			log.debug("DELETE DATA ON DATA-DB - starting...");
			
			output = dataRepo.deleteIds(idsToDelete);
			
			log.debug("DELETE DATA ON DATA-DB - Records deleted {}.", output);
			log.debug("DELETE DATA ON DATA-DB - finished.");
		} catch (Exception e) {
			log.error("Errore durante esecuzione Engine Data Retention per il contenuto di 'ini_eds_invocation': " , e);
			throw new BusinessException("Errore durante esecuzione Engine Data Retention per il contenuto di 'ini_eds_invocation': " , e);
		}
		return output;
		
	}

	@Override
	public List<String> deleteOnTransactionDB(final String state, final int hoursTransactionsDB) {
		List<String> output = new ArrayList<>();
		
		try {
			log.debug("DELETE DATA ON TRANSACTIONS-DB - starting...");
			
			// Find.
			log.debug("DELETE DATA ON TRANSACTIONS-DB - find records to delete...");
			Date dateToRemove = DateUtility.getDateCondition(hoursTransactionsDB);
			List<TransactionEventsETY> entities = transactionsRepo.findOldTransactions(state, dateToRemove);
			
			List<ObjectId> ids = new ArrayList<>();
			for (TransactionEventsETY e : entities) {
				output.add(e.getWorkflowInstanceId());
				ids.add(new ObjectId(e.getId()));
			}

			// Delete.
			log.debug("DELETE DATA ON TRANSACTIONS-DB - delete record with state {}...", state);
			int recordDeleted = transactionsRepo.deleteOldTransactions(ids);

			log.debug("DELETE DATA ON TRANSACTIONS-DB- Records deleted {} with STATE {}.", recordDeleted, state);
			log.debug("DELETE DATA ON TRANSACTIONS-DB - finished.");
		} catch (Exception e) {
			log.error("Errore durante esecuzione Engine Data Retention per il contenuto di 'transaction_data': " , e);
			throw new BusinessException("Errore durante esecuzione Engine Data Retention per il contenuto di 'transaction_data': " , e);
		}
		
		return output;
	}

	@Override
	public Map<String, Integer> readConfigurations() {
		Map<String, Integer> output = new HashMap<>();
		
		try {
			final Map<String, String> items = configClient.getConfigurationItems().get(0).getItems();
			output.put(Constants.ConfigItems.SUCCESS_TRANSACTION_RETENTION_HOURS, Integer.parseInt(items.get(Constants.ConfigItems.SUCCESS_TRANSACTION_RETENTION_HOURS)));
			output.put(Constants.ConfigItems.BLOCKING_ERROR_TRANSACTION_RETENTION_HOURS, Integer.parseInt(items.get(Constants.ConfigItems.BLOCKING_ERROR_TRANSACTION_RETENTION_HOURS)));
			output.put(Constants.ConfigItems.NON_BLOCKING_ERROR_TRANSACTION_RETENTION_HOURS, Integer.parseInt(items.get(Constants.ConfigItems.NON_BLOCKING_ERROR_TRANSACTION_RETENTION_HOURS)));
		} catch (Exception e) {
			log.error("Errore durante la lettura delle configurazioni necessarie per la Data Retention. " , e);
			throw new BusinessException("Errore durante la lettura delle configurazioni necessarie per la Data Retention. " , e);
		}
		
		return output;
	}

}
