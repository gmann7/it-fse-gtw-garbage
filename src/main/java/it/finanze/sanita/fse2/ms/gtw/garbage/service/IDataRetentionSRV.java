/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.gtw.garbage.service;

/**
 *
 */
public interface IDataRetentionSRV {

	/**
	 * Metodo che determina le regole di retention e le applica eliminando dati sul
	 * gtw-transactions-db.
	 * 
	 * @return Lista di Ids da eliminare.
	 */
	void deleteTransactionData();
	

}
