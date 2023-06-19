/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 * 
 * Copyright (C) 2023 Ministero della Salute
 * 
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License along with this program. If not, see <https://www.gnu.org/licenses/>.
 */
package it.finanze.sanita.fse2.ms.gtw.garbage.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.finanze.sanita.fse2.ms.gtw.garbage.client.IConfigItemsClient;
import it.finanze.sanita.fse2.ms.gtw.garbage.config.Constants;
import it.finanze.sanita.fse2.ms.gtw.garbage.exceptions.BusinessException;
import it.finanze.sanita.fse2.ms.gtw.garbage.repository.IDataRepo;
import it.finanze.sanita.fse2.ms.gtw.garbage.repository.IValidatedDocumentRepo;
import it.finanze.sanita.fse2.ms.gtw.garbage.service.IValidatedDocumentRetentionSRV;
import it.finanze.sanita.fse2.ms.gtw.garbage.utility.DateUtility;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ValidatedDocumentRetentionSRV implements IValidatedDocumentRetentionSRV {

	@Autowired
	private IValidatedDocumentRepo valDocRepo;
	

	@Autowired
	private IConfigItemsClient configClient;
	
	@Autowired
	private IDataRepo dataRepo;

	@Override
	public void deleteValidatedDocuments(final int day) {
		try {
			Date dateToRemove = DateUtility.addDay(new Date(), -day);
			List<String> wiiDeleted = valDocRepo.deleteValidatedDocuments(dateToRemove);
			dataRepo.deleteIds(wiiDeleted);
			log.debug("DELETE VALIDATED-DOCUMENT-DB:" + wiiDeleted.size());
		} catch (Exception e) {
			log.error("Errore durante esecuzione Engine Fse Retention per il contenuto di 'validated_documents': ", e);
			throw new BusinessException("Errore durante esecuzione Engine Fse Retention per il contenuto di 'validated_documents': ", e);
		}
	}

	@Override
	public Map<String, Integer> readConfigurations() {
		Map<String, Integer> output = new HashMap<>();

		try {
			final Map<String, String> items = configClient.getConfigurationItems().get(0).getItems();
			output.put(Constants.ConfigItems.VALIDATED_DOCUMENT_RETENTION_DAY,
					Integer.parseInt(items.get(Constants.ConfigItems.VALIDATED_DOCUMENT_RETENTION_DAY)));
		} catch (Exception e) {
			log.error("Errore durante la lettura delle configurazioni per la retention di un documento.", e);
			throw new BusinessException("Errore durante la lettura delle configurazioni per la retention di un documento. ", e);
		}

		return output;
	}

}
