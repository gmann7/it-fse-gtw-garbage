/**
 * 
 */
package it.finanze.sanita.fse2.ms.gtw.garbage.repository;

import java.io.Serializable;
import java.util.List;

/**
 * @author AndreaPerquoti
 *
 */
public interface IDataRepo extends Serializable {
	
	int deleteIds(final List<String> ids);
	
}
