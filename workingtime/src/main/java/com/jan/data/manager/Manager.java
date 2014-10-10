package com.jan.data.manager;

import java.util.List;

import com.jan.data.WorkingDay;
import com.jan.data.storage.XMLStorage;

public class Manager {

	private XMLStorage xmlStorage;
	
	public Manager() {
		this.xmlStorage = new XMLStorage();
	}
	
	public List<WorkingDay> getWorkingDays() {
		return new XMLStorage().getExistingWorkingDays();
	}

	public XMLStorage getXmlStorage() {
		return xmlStorage;
	}

	public void setXmlStorage(XMLStorage xmlStorage) {
		this.xmlStorage = xmlStorage;
	}
}
