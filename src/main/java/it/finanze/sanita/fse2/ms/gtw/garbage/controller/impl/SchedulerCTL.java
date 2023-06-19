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
package it.finanze.sanita.fse2.ms.gtw.garbage.controller.impl;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import it.finanze.sanita.fse2.ms.gtw.garbage.controller.ISchedulerCTL;
import it.finanze.sanita.fse2.ms.gtw.garbage.dto.DeletedSchedulerResDTO;
import it.finanze.sanita.fse2.ms.gtw.garbage.scheduler.CFGItemsRetentionScheduler;
import it.finanze.sanita.fse2.ms.gtw.garbage.scheduler.DataRetentionScheduler;
import it.finanze.sanita.fse2.ms.gtw.garbage.scheduler.ValidatedDocumentRetentionScheduler;


@RestController
public class SchedulerCTL extends AbstractCTL implements ISchedulerCTL {

	@Autowired
	private DataRetentionScheduler dataRetentionScheduler;
	
	@Autowired
	private ValidatedDocumentRetentionScheduler validatedDocsScheduler;
	
	@Autowired
	private CFGItemsRetentionScheduler cfgItemsScheduler;
	
	@Override
	public DeletedSchedulerResDTO runSchedulerDataRetention(HttpServletRequest request) {
		Map<String,Integer> out = dataRetentionScheduler.action();
		return new DeletedSchedulerResDTO(getLogTraceInfo(), out);
	}
	
	@Override
	public void runSchedulerValidatedDocuments(HttpServletRequest request) {
		validatedDocsScheduler.action();
	}
	
	@Override
	public void runSchedulerCfgItems(HttpServletRequest request) {
		cfgItemsScheduler.action();
	}

	 
}
