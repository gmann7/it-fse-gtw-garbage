package it.finanze.sanita.fse2.ms.gtw.garbage.controller.impl;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import it.finanze.sanita.fse2.ms.gtw.garbage.controller.ITestCTL;
import it.finanze.sanita.fse2.ms.gtw.garbage.scheduler.DataRetentionScheduler;
import lombok.extern.slf4j.Slf4j;


@RestController
public class TestCTL implements ITestCTL {

	/**
	 * Serial version uid.
	 */
	private static final long serialVersionUID = 5310603052337351897L;
	
	@Autowired
	private DataRetentionScheduler dataRetentionScheduler;
	
	@Override
	public void runScheduler(HttpServletRequest request) {
		dataRetentionScheduler.action();
	}

	 
}
