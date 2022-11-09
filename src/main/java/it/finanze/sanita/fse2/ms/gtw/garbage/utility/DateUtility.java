/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.gtw.garbage.utility;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

import it.finanze.sanita.fse2.ms.gtw.garbage.exceptions.BusinessException;
import lombok.extern.slf4j.Slf4j;

/**
 *
 */
@Slf4j
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
	
	public static Date addDay(final Date date, final Integer nDays) {
		Calendar c = Calendar.getInstance();
		try {
			c.setTime(date);
			c.add(Calendar.DATE, nDays);
		} catch(Exception ex) {
			log.error("Error while perform addDay : " , ex);
			throw new BusinessException("Error while perform addDay : " , ex);
		}
		return c.getTime();
		
	}

	/**
	 * 
	 * @param docDate   date of document
	 * @param daysLimit limit of days, after these documents is to delete
	 * @return boolean value, True to delete or False not to delete
	 * 
	 */

	public static boolean dateOlderThan(String dateToCompare, int maxDaysToValidation) throws Exception {
		try {
			DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			LocalDate dateParser = LocalDate.parse(dateToCompare, df);
			int dayOfDocument = dateParser.getDayOfMonth();

			LocalDate todayDate = LocalDate.now();
			int todayDay = todayDate.getDayOfMonth();

			int daysAway = todayDay - dayOfDocument;

			if (daysAway > maxDaysToValidation) {
				return true;
			} else {
				return false;
			}
		} catch (Exception logException) {

			log.error("error", logException);
			throw new BusinessException(logException);
		}
	}
}
