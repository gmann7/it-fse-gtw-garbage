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
public interface IRulesRetentionSRV extends Serializable {

	/**
	 * Metodo che determina le regole di retention e le applica eliminando dati sul
	 * gtw-rules-db.
	 * 
	 * @return true in caso di riuscita.
	 */
	Boolean deleteOnRulesDB(boolean deletedCollection);

	/**
	 * @return
	 */
	Map<String, Integer> readConfigurations();

}
