/**
 * 
 */
package it.finanze.sanita.fse2.ms.gtw.garbage.utility;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import it.finanze.sanita.fse2.ms.gtw.garbage.exceptions.BusinessException;
import lombok.extern.slf4j.Slf4j;

/**
 * @author AndreaPerquoti
 *
 */
@Slf4j
public class DateUtility implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 135890017796561808L;

    private static final String DATE_MONGO_FORMAT = "yyyy-MM-dd'T'hh:mm:ss.SSS";
	
//	public static Date getDateCondition(final int hoursToRemove) {
//		Date dateTarget = null;
//		Date dateSource = new Date();
//		SimpleDateFormat sdf = new SimpleDateFormat(DATE_MONGO_FORMAT);
//		
//		try {
//			
//			log.info("DATA SOURCE: {}", sdf.format(dateSource));
//			
//			Calendar cal = Calendar.getInstance();
//	        cal.setTime(dateSource);
//	        
//	        cal.add(Calendar.HOUR_OF_DAY, -hoursToRemove);
//	        String dateTargetStr = sdf.format(cal.getTime());
//	        dateTarget = sdf.parse(dateTargetStr);
//	        
//	        log.info("DATA TARGET: {}", sdf.format(dateTarget));
//	        
//		} catch (Exception e) {
//			log.error("Errore durante il tentativo di sottrarre {} ore partendo dalla data {} .", hoursToRemove, dateSource.toString());
//			throw new BusinessException("Errore durante il tentativo di sottrarre ore partendo dalla data odierna.", e);
//		}
//		
//		
//		return dateTarget;
//	}
	
	public static Date getDateCondition(final int nHours) {
		final Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.HOUR_OF_DAY, -nHours);
		return cal.getTime();
	}
}
