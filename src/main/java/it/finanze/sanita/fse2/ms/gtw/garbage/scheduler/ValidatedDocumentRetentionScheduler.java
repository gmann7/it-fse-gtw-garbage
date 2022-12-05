/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.gtw.garbage.scheduler;

import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import it.finanze.sanita.fse2.ms.gtw.garbage.service.IValidatedDocumentRetentionSRV;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;

@Slf4j
@Component
public class ValidatedDocumentRetentionScheduler {


	@Autowired
	private IValidatedDocumentRetentionSRV retentionSRV;

	@Scheduled(cron = "${scheduler.valdoc-retention}")
	@SchedulerLock(name = "invokeFseRetentionScheduler", lockAtMostFor = "60m")
	public void action() {
		run();
	}

	public void run() {
		log.debug("ValidatedDocument Retention Scheduler - Retention Scheduler starting");
		try {
			// Lettura Config remote.
			Map<String, Integer> configs = retentionSRV.readConfigurations();

			// Eliminazione Validated Document in base alle configurazioni recuperate.
			for (Entry<String, Integer> config : configs.entrySet()) {
				retentionSRV.deleteValidatedDocuments(config.getValue());
			}

		} catch (Exception e) {
			log.error("ValidatedDocument Retention Scheduler - Error while executing data retention", e);
		}

		log.debug("ValidatedDocument Retention Scheduler - Retention Scheduler finished");
	}

}
