/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.gtw.garbage.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.finanze.sanita.fse2.ms.gtw.garbage.exceptions.BusinessException;
import it.finanze.sanita.fse2.ms.gtw.garbage.repository.IDataRepo;
import it.finanze.sanita.fse2.ms.gtw.garbage.repository.ITransactionsRepo;
import it.finanze.sanita.fse2.ms.gtw.garbage.repository.entity.TransactionEventsETY;
import it.finanze.sanita.fse2.ms.gtw.garbage.service.IDataRetentionSRV;
import lombok.extern.slf4j.Slf4j;

/**
 *
 */
@Slf4j
@Service
public class DataRetentionSRV implements IDataRetentionSRV {


	@Autowired
	private IDataRepo dataRepo;

	@Autowired
	private ITransactionsRepo transactionsRepo;

	@Override
	public Integer deleteOnDataDB(final List<String> idsToDelete) {
		Integer output = 0;
		try {
			log.debug("DELETE DATA ON DATA-DB - starting...");
			
			output = dataRepo.deleteIds(idsToDelete);
			
			log.debug("DELETE DATA ON DATA-DB - Records deleted {}.", output);
			log.debug("DELETE DATA ON DATA-DB - finished.");
		} catch (Exception e) {
			log.error("Errore durante esecuzione Engine Data Retention per il contenuto di 'ini_eds_invocation': ", e);
			throw new BusinessException(
					"Errore durante esecuzione Engine Data Retention per il contenuto di 'ini_eds_invocation': ", e);
		}
		return output;
		
	}


	@Override
	public void deleteTransactionData() {

		try {
			String eventType = "EDS_WORKFLOW";
			List<TransactionEventsETY> transactionDataDeleted = transactionsRepo.deleteExpiringTransactionData(eventType);
			
			if(transactionDataDeleted!=null) {
				List<String> transactiondeleted = transactionDataDeleted.stream().map(e->e.getWorkflowInstanceId()).collect(Collectors.toList());
				dataRepo.deleteIds(transactiondeleted);
			}
		} catch (Exception e) {
			log.error("Errore durante esecuzione Engine Data Retention per il contenuto di 'transaction_data': ", e);
			throw new BusinessException(
					"Errore durante esecuzione Engine Data Retention per il contenuto di 'transaction_data': ", e);
		}

	}
}
