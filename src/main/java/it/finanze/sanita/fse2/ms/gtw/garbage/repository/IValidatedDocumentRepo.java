package it.finanze.sanita.fse2.ms.gtw.garbage.repository;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;

public interface IValidatedDocumentRepo extends Serializable {

	Integer deleteValidatedDocuments(Date oldToRemove);
	
	int deleteOldValidatedDocument(List<ObjectId> idsToRemove);
	
}