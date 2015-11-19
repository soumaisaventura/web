package adventure.util;

import static java.util.Calendar.DAY_OF_MONTH;
import static org.apache.commons.lang.time.DateUtils.truncate;
import static org.apache.commons.lang.time.DateUtils.truncatedCompareTo;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class Dates {

	private static final DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

	private Dates() {
	}

	public static Date ignoreTime(Date date) {
		return truncate(new Date(), DAY_OF_MONTH);
	}

	public static boolean before(Date date, Date other) {
		return truncatedCompareTo(date, other, DAY_OF_MONTH) < 0;
	}

	public static boolean beforeOrSame(Date date, Date other) {
		return truncatedCompareTo(date, other, DAY_OF_MONTH) <= 0;
	}

	public static boolean after(Date date, Date other) {
		return truncatedCompareTo(date, other, DAY_OF_MONTH) > 0;
	}

	public static boolean afterOrSame(Date date, Date other) {
		return truncatedCompareTo(date, other, DAY_OF_MONTH) >= 0;
	}

	public static boolean between(Date date, Date min, Date max) {
		return afterOrSame(date, min) && beforeOrSame(date, max);
	}

	public static String parse(Date date) {
		return formatter.format(date);
	}

	public static Date parse(String date) throws ParseException {
		return formatter.parse(date);
	}
}
