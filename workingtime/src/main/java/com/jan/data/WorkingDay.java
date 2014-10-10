package com.jan.data;

import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class WorkingDay {

	// The Date of the day you worked
	private Date date;

	// A List with all workingtimes in this day
	private List<String> workingTimes;

	private String notice;

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public List<String> getWorkingTimes() {
		return workingTimes;
	}

	public void setWorkingTimes(List<String> workingTimes) {
		this.workingTimes = workingTimes;
	}

	public String getNotice() {
		return notice;
	}

	public void setNotice(String notice) {
		this.notice = notice;
	}
}
