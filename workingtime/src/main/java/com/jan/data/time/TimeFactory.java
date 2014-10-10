package com.jan.data.time;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.jan.MyTouchKitUI;
import com.jan.data.Settings;
import com.jan.data.WorkingDay;

public class TimeFactory {

	public int getWorkedHoursUsingSettings() {
		// Check if the Settings are enabled
		Settings settings = MyTouchKitUI.getManager().getXmlStorage().getSettings();
		if (settings == null || !settings.isEnabled()) {
			return -1;
		}

		// Check if there are some workingdays
		List<WorkingDay> workingDays = MyTouchKitUI.getManager().getWorkingDays();
		int hours = 0;
		for (WorkingDay workingDay : workingDays) {
			if (!isDateBetweenDateDistance(workingDay.getDate(), settings.getStartDate(), settings.getEndDate())) {
				continue;
			}

			for (String times : workingDay.getWorkingTimes()) {
				String[] splittedHours = times.split(" - ");
				hours += getHoursBetweenTimes(Long.valueOf(splittedHours[0]), Long.valueOf(splittedHours[1]));
			}
		}
		return hours;
	}

	public boolean isDateBetweenDateDistance(Date date, Date startDate, Date endDate) {
		if (endDate.after(date) && startDate.before(date)) {
			return true;
		} 
		
		// Check if the date is equals using the simpledateformat, because date includes time and seconds.
		SimpleDateFormat dateFormat = getDateFormat();
		if (dateFormat.format(startDate).equalsIgnoreCase(dateFormat.format(date))) {
			return true;
		}
		if(dateFormat.format(endDate).equalsIgnoreCase(dateFormat.format(date))) {
			return true;
		}
		return false;
	}

	public int getHoursBetweenTimes(long startMillis, long endMillis) {
		long difference = (endMillis - startMillis);
		if (difference < 0) {
			return 0;
		}

		int hours = (int) ((difference / 1000) / 3600);
		return hours;
	}

	public SimpleDateFormat getTimeFormat() {
		return new SimpleDateFormat("HH:mm");
	}
	
	public SimpleDateFormat getDateFormat() {
		return new SimpleDateFormat("dd.MM.yyyy");
	}
	
	public String toReadableTime(long millis) {
		Date date = new Date(millis);
		DateFormat timeFormat = DateFormat.getTimeInstance(DateFormat.SHORT, Locale.GERMAN);
		return timeFormat.format(date);
	}
	
	public Date toDate(Date date, String hours, String minutes) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
		try {
			return dateFormat.parse(getDateFormat().format(date) + " " + hours + ":" + minutes);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public long toCurrentTimeMillis(Date date) {
		return date.getTime();
	}

}
