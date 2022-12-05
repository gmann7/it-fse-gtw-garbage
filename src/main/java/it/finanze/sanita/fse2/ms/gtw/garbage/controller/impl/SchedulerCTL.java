/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
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
