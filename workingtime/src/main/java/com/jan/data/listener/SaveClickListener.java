package com.jan.data.listener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jan.MyTouchKitUI;
import com.jan.data.time.NotValidTimeException;
import com.jan.data.time.TimeFactory;
import com.jan.data.time.TimeValidator;
import com.vaadin.server.UserError;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;

public class SaveClickListener {
	
	private Date date;
	private String notice;

	private List<TextField> workingTimeFields;
	private String timePattern = "((\\d{1}|\\d{2}):\\d{2})\\s*-\\s*(\\d{2}:\\d{2})";

	public SaveClickListener(Date date, List<TextField> workingTimeFields, String notice) {
		this.date = date;
		this.workingTimeFields = workingTimeFields;
		this.notice = notice;
	}

	public boolean fireEvent() {
		List<String> workingTimes = new ArrayList<String>();
		boolean allEmptry = true;
		for (TextField field : workingTimeFields) {
			final String value = field.getValue();
			if (value.isEmpty()) {
				continue;
			}
			allEmptry = false;

			// The workingtime have to matches the pattern
			Matcher timeMatcher = Pattern.compile(timePattern).matcher(value);
			if (!timeMatcher.matches()) {
				field.setComponentError(new UserError("Falscher Input"));
				return false;
			}

			// Split the both times into twice
			String timeOne = timeMatcher.group(1);
			String timeTwo = timeMatcher.group(3);

			TimeValidator validatorOne = new TimeValidator(timeOne);
			TimeValidator validatorTwo = new TimeValidator(timeTwo);
			try {
				validatorOne.isValid();
				validatorTwo.isValid();
			} catch (NotValidTimeException e) {
				e.printStackTrace();
				field.setComponentError(new UserError(e.getMessage()));
				return false;
			}
			
			TimeFactory timeFactory = new TimeFactory();

			field.setComponentError(null);
			
			long timeOneMillis = timeFactory.toCurrentTimeMillis(timeFactory.toDate(date, validatorOne.getHour(), validatorOne.getMinute()));
			long timeTwoMillis = timeFactory.toCurrentTimeMillis(timeFactory.toDate(date, validatorTwo.getHour(), validatorTwo.getMinute()));
			workingTimes.add(timeOneMillis + " - " + timeTwoMillis);
		}
		
		// All fields are emptry
		if(allEmptry) {
			Notification.show("Alle Arbeitsstunden sind leer.");
			return false;
		}
		
		// Save to XML
		MyTouchKitUI.getManager().addWorkingDay(date, workingTimes, notice);
		MyTouchKitUI.getManager().getXmlStorage().saveDays(MyTouchKitUI.getManager().getWorkingDays());
		return true;
	}
}
