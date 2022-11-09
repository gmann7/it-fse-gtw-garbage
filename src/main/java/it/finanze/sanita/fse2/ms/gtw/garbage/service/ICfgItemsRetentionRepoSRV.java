/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.gtw.garbage.service;

import java.io.Serializable;
import java.util.Map;

/**
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
