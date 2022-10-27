/**
 * 
 */
package it.finanze.sanita.fse2.ms.gtw.garbage.service;

import java.io.Serializable;
import java.util.Map;

/**
 * @author AndreaPerquoti
 *
 */
public interface ICfgItemsRetentionRepoSRV extends Serializable {

	/**
	 * Metodo che determina le regole di retention e le applica eliminando dati sul
	 * gtw-rules-db.
	 * 
	 * @return true in caso di riuscita.
	 */
	void deleteCFGItems(int day);

	/**
	 * @return
	 */
	Map<String, Integer> readConfigurations();

}
