/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.gtw.garbage.repository;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public interface IValidatedDocumentRepo extends Serializable {

	List<String> deleteValidatedDocuments(Date oldToRemove);
}
