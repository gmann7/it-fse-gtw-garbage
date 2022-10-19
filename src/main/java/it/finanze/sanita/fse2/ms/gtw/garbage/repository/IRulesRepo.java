package it.finanze.sanita.fse2.ms.gtw.garbage.repository;

import java.io.Serializable;

public interface IRulesRepo extends Serializable {
	boolean deleteOldSchematrons(boolean alreadyDeleted);

	boolean deleteOldSchemas(boolean alreadyDeleted);

	boolean deleteOldTransforms(boolean alreadyDeleted);
}
