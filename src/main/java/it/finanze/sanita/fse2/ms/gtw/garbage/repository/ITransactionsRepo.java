/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.gtw.garbage.repository;

import java.util.List;

import it.finanze.sanita.fse2.ms.gtw.garbage.repository.entity.TransactionEventsETY;

public interface ITransactionsRepo {
	
	List<TransactionEventsETY> findExpiringTransactionData(String eventType);
	
	Integer deleteExpiringTransactionData(List<String> wii);
}
