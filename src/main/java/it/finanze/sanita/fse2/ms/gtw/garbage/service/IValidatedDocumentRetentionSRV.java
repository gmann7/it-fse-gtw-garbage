/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.gtw.garbage.service;

import java.io.Serializable;
import java.util.Map;

public interface IValidatedDocumentRetentionSRV extends Serializable {

	/**
	 * Metodo che determina le regole di retention e le applica eliminando dati sul
	 * gtw-fse-db.
	 * 
	 */
	void deleteValidatedDocuments(final int day);

	/**
	 * @return
	 */
	Map<String, Integer> readConfigurations();

}
