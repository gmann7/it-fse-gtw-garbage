package it.finanze.sanita.fse2.ms.gtw.garbage.scheduler;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import it.finanze.sanita.fse2.ms.gtw.garbage.service.IAuditIniRetentionSRV;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;

@Slf4j
@Component
public class AuditIniRetentionScheduler {

    @Autowired
    private IAuditIniRetentionSRV auditIniRetentionSRV;


    @Scheduled(cron = "${scheduler.audit.ini-retention}")
    @SchedulerLock(name = "invokeRulesRetentionScheduler", lockAtMostFor = "60m")
    public void action() {
        run();
    }

    public void run() {
        log.debug("Audit-ini Retention Scheduler - Retention Scheduler starting");
        try {
            log.info("Checking for expired ini audit...");
            auditIniRetentionSRV.deleteAudit(new Date());
        } catch (Exception e) {
            log.warn("Audit-ini Scheduler - Error while executing audit-ini data retention", e);
        }
        log.debug("Audit-ini Retention Scheduler - Retention Scheduler finished");
    }
}
