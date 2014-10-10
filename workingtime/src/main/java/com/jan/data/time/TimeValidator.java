package com.jan.data.time;


public class TimeValidator {

	// Include the full workingtime (NOT CHECKED IF VALID)
	private String workingTime;

	// Contains the splitted workingtime after validation
	private String hour;
	private String minute;

	public TimeValidator(String workingTime) {
		this.workingTime = workingTime;
	}

	public boolean isValid() throws NotValidTimeException {
		if (!workingTime.contains(":")) {
			throw new NotValidTimeException("The time does not contains ':'");
		}

		// Split the time into to categorys
		String[] splittedWorkingTime = workingTime.split(":");
		String _hour, _minute;
		try {
			_hour = splittedWorkingTime[0];
			_minute = splittedWorkingTime[1];
		} catch (IndexOutOfBoundsException e) {
			throw new NotValidTimeException("The time could not be converted cause an IndexOutOfBoundsException");
		}

		// Convert the categorys into integers
		int hour, minute;
		try {
			hour = Integer.valueOf(_hour);
			minute = Integer.valueOf(_minute);
		} catch (NumberFormatException e) {
			throw new NotValidTimeException("Some of the time is not a number");
		}

		// Check if the time is not to low or to high
		if (hour > 23 || minute > 59) {
			throw new NotValidTimeException("The hour or the minute value is to high. Max allowed 23:59");
		}

		this.hour = _hour;
		this.minute = _minute;
		return true;
	}

	public String getHour() {
		return hour;
	}

	public void setHour(String hour) {
		this.hour = hour;
	}

	public String getMinute() {
		return minute;
	}

	public void setMinute(String minute) {
		this.minute = minute;
	}
}
