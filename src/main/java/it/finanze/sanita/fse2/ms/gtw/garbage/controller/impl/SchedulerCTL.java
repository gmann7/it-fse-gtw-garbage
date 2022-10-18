package it.finanze.sanita.fse2.ms.gtw.garbage.controller.impl;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import it.finanze.sanita.fse2.ms.gtw.garbage.controller.ISchedulerCTL;
import it.finanze.sanita.fse2.ms.gtw.garbage.dto.DeletedSchedulerResDTO;
import it.finanze.sanita.fse2.ms.gtw.garbage.scheduler.DataRetentionScheduler;


@RestController
public class SchedulerCTL extends AbstractCTL implements ISchedulerCTL {

	/**
	 * Serial version uid.
	 */
	private static final long serialVersionUID = 5310603052337351897L;
	
	@Autowired
	private DataRetentionScheduler dataRetentionScheduler;
	
	@Override
	public DeletedSchedulerResDTO runScheduler(HttpServletRequest request) {
		Map<String,Integer> out = dataRetentionScheduler.action();
		return new DeletedSchedulerResDTO(getLogTraceInfo(), out);
	}

	 
}
