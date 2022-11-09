/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.gtw.garbage.scheduler;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
public class DataRetentionScheduler implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4184197312700021073L;

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
        	
        	// Lettura Config remote.
        	Map<String, Integer> configs = retentionSRV.readConfigurations();

        	// Eliminazione Transactions in base alle configurazioni recuperate.
        	Integer transactionDeleted = 0;
        	Integer iniEdsDeleted = 0;
        	for (Entry<String, Integer> config : configs.entrySet()) {
        		List<String> wfii = retentionSRV.deleteOnTransactionDB(config.getKey(), config.getValue());
        		transactionDeleted+=wfii.size();
            	// Eliminazione Data&Metadata relazionate con le Transactions.
        		iniEdsDeleted+= retentionSRV.deleteOnDataDB(wfii);
			}
        	output.put("transaction_deleted", transactionDeleted);
        	output.put("ini_eds_deleted", iniEdsDeleted);
        	
        } catch (Exception e) {
            log.warn("Data Retention Scheduler - Error while executing data retention", e);
        }
        
        log.debug("Data Retention Scheduler - Retention Scheduler finished");
        return output;
    }
}
