/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.gtw.garbage.repository;

import java.util.Date;

public interface ICfgItemsRetentionRepo {

	Integer deleteCfgItems(Date dateToRemove, Class<?> clazz);
	
	Integer deleteTerminology(Date dateToRemove);

	Integer deleteEngines(Date dateToRemove);
}
