package com.matheus.servermanager.utils;

import java.util.Calendar;

public class TimeUtils {

	public static int getCurrentHour() {
		Calendar cal = Calendar.getInstance();
		return cal.get(Calendar.HOUR_OF_DAY);
	}
}
