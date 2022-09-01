/**
 * 
 */
package it.finanze.sanita.fse2.ms.gtw.garbage.utility;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 * @author AndreaPerquoti
 *
 */
public class DateUtility implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 135890017796561808L;

	public static Date getDateCondition(final int nHours) {
		final Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.HOUR_OF_DAY, -nHours);
		return cal.getTime();
	}
}
