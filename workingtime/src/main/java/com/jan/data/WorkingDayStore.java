package com.jan.data;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class WorkingDayStore {

	private List<WorkingDay> workingDays;

	public List<WorkingDay> getWorkingDays() {
		return workingDays;
	}

	/**
	 * Stors temporaly the days for the xml file
	 * @param workingDays
	 */
	@XmlElement(name = "day", required = true)
	public void setWorkingDays(List<WorkingDay> workingDays) {
		this.workingDays = workingDays;
	}
	
}
