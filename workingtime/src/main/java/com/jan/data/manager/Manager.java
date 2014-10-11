package com.jan.data.manager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.jan.data.WorkingDay;
import com.jan.data.storage.XMLStorage;
import com.jan.data.time.TimeFactory;

public class Manager {

	private List<WorkingDay> workingDays;
	private XMLStorage xmlStorage;
	
	public Manager() {
		this.xmlStorage = new XMLStorage();
		this.workingDays = xmlStorage.loadWorkingDays();
	}
	
	public List<WorkingDay> getWorkingDays() {
		return workingDays;
	}

	public XMLStorage getXmlStorage() {
		return xmlStorage;
	}
	
	public void addWorkingDay(Date date, List<String> workingTimes, String notice) {
			// Create a new WorkingDay object
			WorkingDay workingDay = getWorkingDay(date);
			if (workingDay == null) {
				workingDay = new WorkingDay();
				workingDays.add(workingDay);
			}
			workingDay.setDate(date);
			workingDay.setWorkingTimes(workingTimes);	
	}
	
	public WorkingDay getWorkingDay(Date date) {
		SimpleDateFormat dateFormat = new TimeFactory().getDateFormat();
		for (WorkingDay workingDay : workingDays) {
			// Check if there is already an existing day for this date
			if (dateFormat.format(workingDay.getDate()).equalsIgnoreCase(dateFormat.format(date))) {
				return workingDay;
			}
		}
		return null;
	}
}
