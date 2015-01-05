package br.com.fbca.util;

import static java.util.Calendar.DAY_OF_MONTH;

import java.util.Date;

import org.apache.commons.lang.time.DateUtils;

public final class Dates {

	private Dates() {
	}

	public static Date ignoreTime(Date date) throws Exception {
		return DateUtils.truncate(new Date(), DAY_OF_MONTH);
	}
}
