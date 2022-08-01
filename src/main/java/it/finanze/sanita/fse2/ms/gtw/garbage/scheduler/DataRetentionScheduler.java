/**
 * 
 */
package it.finanze.sanita.fse2.ms.gtw.garbage.scheduler;

import java.io.Serializable;
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
 * @author AndreaPerquoti
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
	public void action() {
		run(); 
	}

    public void run() {
    	
        log.info("DATA RETENTION SCHEDULER - Retention Scheduler starting...");
        try {
        	
        	// Lettura Config remote.
        	Map<String, Integer> configs = retentionSRV.readConfigurations();

        	// Eliminazione Transactions in base alle configurazioni recuperate.
        	for (Entry<String, Integer> config : configs.entrySet()) {
        		List<String> wfii = retentionSRV.deleteOnTransactionDB(config.getKey(), config.getValue());
            	// Eliminazione Data&Metadata relazionate con le Transactions.
        		retentionSRV.deleteOnDataDB(wfii);
			}
        	
        } catch (Exception e) {
            log.error("DATA RETENTION SCHEDULER - Error executing InvokeEDSClientScheduler", e);
        }
        
        log.info("DATA RETENTION SCHEDULER - Retention Scheduler finished");
        
    }
}
