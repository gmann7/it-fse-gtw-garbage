/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.gtw.garbage.repository;

import java.util.List;

import org.bson.types.ObjectId;

import it.finanze.sanita.fse2.ms.gtw.garbage.repository.entity.TransactionEventsETY;

public interface ITransactionsRepo {
	
	int deleteOldTransactions(List<ObjectId> idsToRemove);
	
	List<TransactionEventsETY> deleteExpiringTransactionData(String eventType);
	
}
