package br.com.vah.protocolo.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * A simple date formatting utility class
 * @author Emre Simtay <emre@simtay.com>
 */

public class DateUtility {

	public static final String DATE_FORMAT = "dd/MM/yyyy HH:mm:ss";

	public static String getCurrentDateTime() {
            DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
            Date date = new Date();
            return dateFormat.format(date);
	}

  public static Date zeroHour(Date date) {
    Calendar cld = Calendar.getInstance();
    if (date != null) {
      cld.setTime(date);
    }
    cld.set(Calendar.HOUR_OF_DAY, 0);
    cld.set(Calendar.MINUTE, 0);
    cld.set(Calendar.SECOND, 0);
    return cld.getTime();
  }

  public static Date lastHour(Date date) {
    Calendar cld = Calendar.getInstance();
    if (date != null) {
      cld.setTime(date);
    }
    cld.set(Calendar.HOUR_OF_DAY, 23);
    cld.set(Calendar.MINUTE, 59);
    cld.set(Calendar.SECOND, 59);
    return cld.getTime();
  }


}
