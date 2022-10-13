package it.finanze.sanita.fse2.ms.gtw.garbage.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface IValidatedDocumentRetentionSRV extends Serializable {

	/**
	 * Metodo che determina le regole di retention e le applica eliminando dati sul
	 * gtw-fse-db.
	 * 
	 * @return Lista di Ids da eliminare.
	 */
	List<String> deleteOnValDocDB(final int day);

	/**
	 * @return
	 */
	Map<String, Integer> readConfigurations();

}
