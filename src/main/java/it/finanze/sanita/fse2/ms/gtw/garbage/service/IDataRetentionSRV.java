/**
 * 
 */
package it.finanze.sanita.fse2.ms.gtw.garbage.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author AndreaPerquoti
 *
 */
public interface IDataRetentionSRV extends Serializable {
	
	/**
	 * Metodo che determina le regole di retention e le applica eliminando dati sul gtw-data-db.
	 * 
	 * @return true in caso di riuscita.
	 */
	Boolean deleteOnDataDB(final List<String> idsToDelete);
	
	/**
	 * Metodo che determina le regole di retention e le applica eliminando dati sul gtw-transactions-db.
	 * 
	 * @return Lista di Ids da eliminare.
	 */	
	List<String> deleteOnTransactionDB(final String state, final int hoursTransactionsDB);

	/**
	 * @return
	 */
	Map<String, Integer> readConfigurations();

}