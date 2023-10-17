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
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.finanze.sanita.fse2.ms.gtw.garbage.client.IConfigItemsClient;
import it.finanze.sanita.fse2.ms.gtw.garbage.config.Constants;
import it.finanze.sanita.fse2.ms.gtw.garbage.exceptions.BusinessException;
import it.finanze.sanita.fse2.ms.gtw.garbage.repository.ICfgItemsRetentionRepo;
import it.finanze.sanita.fse2.ms.gtw.garbage.repository.entity.SchemaETY;
import it.finanze.sanita.fse2.ms.gtw.garbage.repository.entity.SchematronETY;
import it.finanze.sanita.fse2.ms.gtw.garbage.repository.entity.TerminologyETY;
import it.finanze.sanita.fse2.ms.gtw.garbage.repository.entity.TransformETY;
import it.finanze.sanita.fse2.ms.gtw.garbage.service.ICfgItemsRetentionRepoSRV;
import it.finanze.sanita.fse2.ms.gtw.garbage.utility.DateUtility;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CfgItemsRetentionSRV implements ICfgItemsRetentionRepoSRV {

	private static final long serialVersionUID = -667918997674784439L;

	@Autowired
	private transient ICfgItemsRetentionRepo rulesRepo;

	@Autowired
	private transient IConfigItemsClient configClient;
 
	@Override
	public void deleteCFGItems(final int day) {
		try {
			Date dateToRemove = DateUtility.addDay(new Date(), -day);
			Integer schemaDeletedRecords = rulesRepo.deleteCfgItems(dateToRemove, SchemaETY.class);
			log.debug("DELETE SCHEMA: " + schemaDeletedRecords);
			Integer schematronDeletedRecords = rulesRepo.deleteCfgItems(dateToRemove, SchematronETY.class);
			log.debug("DELETE SCHEMATRON: " + schematronDeletedRecords);
			Integer transformDeletedRecords = rulesRepo.deleteCfgItems(dateToRemove, TransformETY.class);
			log.debug("DELETE TRANSFORM: " + transformDeletedRecords);
			Integer terminologyDeletedRecords = rulesRepo.deleteCfgItems(dateToRemove, TerminologyETY.class);
			log.debug("DELETE TERMINOLOGY: " + terminologyDeletedRecords);
			Integer dictionaryDeletedRecords = rulesRepo.deleteTerminology(dateToRemove);
			log.debug("DELETE DICTIONARY: " + dictionaryDeletedRecords);
		} catch (Exception e) {
			log.error("Error while perform delete cfg items", e);
			throw new BusinessException("Error while perform delete cfg items", e);
		}
	}

	@Override
	public Map<String, Integer> readConfigurations() {
		Map<String, Integer> output = new HashMap<>();

		try {
			final Map<String, String> items = configClient.getConfigurationItems().get(0).getItems();
			output.put(Constants.ConfigItems.CFG_ITEMS_RETENTION_DAY,
					Integer.parseInt(items.get(Constants.ConfigItems.CFG_ITEMS_RETENTION_DAY)));
		} catch (Exception e) {
			log.error("Errore durante la lettura delle configurazioni necessarie per la Data Retention. ", e);
			throw new BusinessException(
					"Errore durante la lettura delle configurazioni necessarie per la Data Retention. ", e);
		}

		return output;
	}
}
