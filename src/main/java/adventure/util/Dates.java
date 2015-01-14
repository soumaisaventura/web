package adventure.util;

import static java.util.Calendar.DAY_OF_MONTH;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.time.DateUtils;

public final class Dates {

	private static final DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

	private Dates() {
	}

	public static Date ignoreTime(Date date) throws Exception {
		return DateUtils.truncate(new Date(), DAY_OF_MONTH);
	}

	public static String parse(Date date) {
		return formatter.format(date);
	}

	public static Date parse(String date) throws ParseException {
		return formatter.parse(date);
	}
}
