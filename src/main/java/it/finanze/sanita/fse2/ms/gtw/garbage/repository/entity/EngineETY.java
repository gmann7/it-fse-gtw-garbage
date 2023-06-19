/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.gtw.garbage.repository.entity;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "#{@engineBean}")
public class EngineETY {
    public static final int MIN_ENGINE_AVAILABLE = 1;
    public static final String FIELD_LAST_SYNC = "last_sync";
}
