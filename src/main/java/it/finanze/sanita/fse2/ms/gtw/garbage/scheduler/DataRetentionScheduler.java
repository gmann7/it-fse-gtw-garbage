/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.gtw.garbage.scheduler;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import it.finanze.sanita.fse2.ms.gtw.garbage.service.IDataRetentionSRV;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;

/**
 *
 */
@Slf4j
@Component
public class DataRetentionScheduler {
	
    @Autowired
    private IDataRetentionSRV retentionSRV;
    
    @Scheduled(cron = "${scheduler.data-retention}")
	@SchedulerLock(name = "invokeDataRetentionScheduler" , lockAtMostFor = "60m")
	public Map<String,Integer> action() {
		return run(); 
	}

    public Map<String,Integer> run() {
    	Map<String,Integer> output = new HashMap<>();
    	log.debug("Data Retention Scheduler - Retention Scheduler starting");
    	try {
    		retentionSRV.deleteTransactionData();
    	} catch (Exception e) {
    		log.warn("Data Retention Scheduler - Error while executing data retention", e);
    	}

    	log.debug("Data Retention Scheduler - Retention Scheduler finished");
    	return output;
    }
    
     
}
