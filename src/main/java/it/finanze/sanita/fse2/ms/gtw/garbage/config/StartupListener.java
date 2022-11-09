/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.gtw.garbage.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 *
 */
@Component
public class StartupListener {

	@Value("${retention.transactions-query.limit}")
	private Integer queryLimit;

	@EventListener(ApplicationReadyEvent.class)
	public void runAfterStartup() {
		ensureProperties();
	}

	private void ensureProperties() {
		if (this.queryLimit == null || this.queryLimit == 0) {
			throw new IllegalArgumentException("Number of records limit to use to execute retention must be set");
		}
	}
	
}
