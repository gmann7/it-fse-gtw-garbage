package it.finanze.sanita.fse2.ms.gtw.garbage.repository;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;

import it.finanze.sanita.fse2.ms.gtw.garbage.repository.entity.ValidatedDocumentEventsETY;

public interface IValidatedDocumentRepo extends Serializable {

	List<ValidatedDocumentEventsETY> findOldValidatedDocument(Date oldToRemove);

	int deleteOldValidatedDocument(List<ObjectId> idsToRemove);
}
