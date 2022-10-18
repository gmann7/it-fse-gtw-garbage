/**
 * 
 */
package it.finanze.sanita.fse2.ms.gtw.garbage.repository.impl;

import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import it.finanze.sanita.fse2.ms.gtw.garbage.exceptions.BusinessException;
import it.finanze.sanita.fse2.ms.gtw.garbage.repository.IDataRepo;
import it.finanze.sanita.fse2.ms.gtw.garbage.repository.entity.IniEdsInvocationETY;
import lombok.extern.slf4j.Slf4j;

/**
 * @author AndreaPerquoti
 *
 */
@Slf4j
@Repository
public class DataRepo implements IDataRepo {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5438541055594366103L;

	@Autowired
	@Qualifier("mongo-template-data")
	private transient MongoTemplate mongoTemplate;

//	@Autowired
//	private transient ProfileUtility profileUtility;

	@Override
	public int deleteIds(final List<String> ids) {
		Long output = null;
				
		try { 
			Document query = new Document();
		  
			query.append("workflow_instance_id", new Document("$in", ids));
//			String targetCollection = Constants.ComponentScan.Collections.INI_EDS_INVOCATION;
//			if (profileUtility.isTestProfile()) {
//				targetCollection = Constants.Profile.TEST_PREFIX + targetCollection;
//			}

			output = mongoTemplate.getCollection(mongoTemplate.getCollectionName(IniEdsInvocationETY.class)).deleteMany(query).getDeletedCount();
			
		} catch (Exception e) {
			log.error("Errore nel tentativo di eliminare la lista di ids nella collection 'ini_eds_invocation': " , e);
			throw new BusinessException("Errore nel tentativo di eliminare la lista di ids nella collection 'ini_eds_invocation': " , e);
		}
		
		return output.intValue();
	}

}
