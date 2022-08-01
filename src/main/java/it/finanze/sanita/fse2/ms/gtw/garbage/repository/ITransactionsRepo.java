/**
 * 
 */
package it.finanze.sanita.fse2.ms.gtw.garbage.repository;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;

import it.finanze.sanita.fse2.ms.gtw.garbage.repository.entity.TransactionEventsETY;

/**
 * @author AndreaPerquoti
 *
 */
public interface ITransactionsRepo extends Serializable {
	
	List<TransactionEventsETY> findOldTransactions(String state, Date oldToRemove);

	int deleteOldTransactions(List<ObjectId> idsToRemove);
	
}
