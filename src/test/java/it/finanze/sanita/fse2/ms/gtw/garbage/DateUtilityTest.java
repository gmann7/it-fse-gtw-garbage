/*
 * SPDX-License-Identifier: AGPL-3.0-or-later
 */
package it.finanze.sanita.fse2.ms.gtw.garbage;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import it.finanze.sanita.fse2.ms.gtw.garbage.config.Constants;
import it.finanze.sanita.fse2.ms.gtw.garbage.utility.DateUtility;

@SpringBootTest(classes = { DateUtility.class })
@ExtendWith(SpringExtension.class)
@ComponentScan(basePackages = { "it.sanita.garbage" })
@ActiveProfiles(Constants.Profile.TEST)
class DateUtilityTest {

	@Test
	@DisplayName("date verified")
	void dateOlderThan() throws Exception {

		int x = 4;
		final Calendar calTrue = Calendar.getInstance();
		calTrue.setTime(new Date());
		calTrue.add(Calendar.DAY_OF_MONTH, -5);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String trueString = sdf.format(calTrue.getTime());
		
		
		int y = 1;
		final Calendar calFalse = Calendar.getInstance();
		calFalse.setTime(new Date());
		calFalse.add(Calendar.DAY_OF_MONTH, -1);
		String falseString = sdf.format(calFalse.getTime());

		assertEquals(true, DateUtility.dateOlderThan(trueString, x));
		assertEquals(false, DateUtility.dateOlderThan(falseString, y));

	}

}
