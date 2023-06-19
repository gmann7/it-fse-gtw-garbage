/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.gtw.garbage.repository.entity;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Model to read dictionary.
 */
@Document(collection = "#{@dictionaryBean}")
@Data
@NoArgsConstructor
public class DictionaryETY {

}
