package com.jan.data.storage;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.UnmarshalException;
import javax.xml.bind.Unmarshaller;

import com.jan.data.Settings;
import com.jan.data.WorkingDay;
import com.jan.data.WorkingDayStore;
import com.jan.data.time.TimeFactory;
import com.jan.ui.SettingsView;

public class XMLStorage {

	private final String ROOT_DIR = "data";
	private final String DATA_FILE = ROOT_DIR + "/workingdays.xml";
	private final String SETTINGS_FILE = ROOT_DIR + "/settings.xml";

	public XMLStorage() {
		// create the xml file
		try {
			File rootDir = new File(ROOT_DIR);
			rootDir.mkdirs();

			File dataFile = new File(DATA_FILE);
			File settingsFile = new File(SETTINGS_FILE);

			// Create new data file
			if (!dataFile.exists()) {
				dataFile.createNewFile();
			}

			// Create new Settingsfile
			if (!settingsFile.exists()) {
				settingsFile.createNewFile();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Add a new {@link WorkingDay} to the xml file. First the WorkingDay gets
	 * added to the {@link WorkingDayStore} and this object gets saved.
	 * 
	 * @param date
	 * @param workingTimes
	 * @param notice
	 */
	public void addNewDay(Date date, List<String> workingTimes, String notice) {
		List<WorkingDay> workingDayList = getExistingWorkingDays();
		WorkingDayStore workingDayStore = new WorkingDayStore();
		
		// Create a new WorkingDay object
		WorkingDay workingDay = getWorkingDay(date, workingDayList);
		if (workingDay == null) {
			workingDay = new WorkingDay();
			workingDayList.add(workingDay);
		}
		workingDay.setDate(date);
		workingDay.setWorkingTimes(workingTimes);
		workingDay.setNotice(notice);

		// Add workingday to the store
		workingDayStore.setWorkingDays(workingDayList);

		// Save to XML File
		try {
			JAXBContext context = JAXBContext.newInstance(WorkingDayStore.class);
			Marshaller marshaller = context.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

			// write
			marshaller.marshal(workingDayStore, new File(DATA_FILE));
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Get a List with all existing workingsdays in the xml file.
	 * 
	 * @return a list with all {@link WorkingDay}'s
	 */
	public List<WorkingDay> getExistingWorkingDays() {
		try {
			JAXBContext context = JAXBContext.newInstance(WorkingDayStore.class);
			Unmarshaller unmarshaller = context.createUnmarshaller();

			WorkingDayStore workingDayStore = null;
			try {
				workingDayStore = (WorkingDayStore) unmarshaller.unmarshal(new File(DATA_FILE));
			} catch (UnmarshalException e) {
				return new ArrayList<WorkingDay>();
			}

			if (workingDayStore == null || workingDayStore.getWorkingDays().isEmpty()) {
				return new ArrayList<WorkingDay>();
			}
			return workingDayStore.getWorkingDays();
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return new ArrayList<WorkingDay>();
	}

	/**
	 * Get a exactly workingday from the function
	 * {@link XMLStorage#getExistingWorkingDays()}
	 * 
	 * @param date
	 * @return
	 */
	public WorkingDay getWorkingDay(Date date, List<WorkingDay> workingDayList) {
		SimpleDateFormat dateFormat = new TimeFactory().getDateFormat();
		for (WorkingDay workingDay : workingDayList) {
			// Check if there is already an existing day for this date
			if (dateFormat.format(workingDay.getDate()).equalsIgnoreCase(dateFormat.format(date))) {
				return workingDay;
			}
		}
		return null;
	}

	/**
	 * Generate a {@link Settings} object out of a XML File. The settings can be
	 * edit in the {@link SettingsView}
	 * 
	 * @return
	 */
	public Settings getSettings() {
		try {
			JAXBContext context = JAXBContext.newInstance(Settings.class);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			return (Settings) unmarshaller.unmarshal(new File(SETTINGS_FILE));
		} catch (JAXBException e) {
		}
		return null;
	}

	public void saveSettings(boolean enabled, Date startDate, Date endDate, int hours) {
		Settings settings = new Settings();
		settings.setEnabled(enabled);
		settings.setStartDate(startDate);
		settings.setEndDate(endDate);
		settings.setHours(hours);

		// Save to XML File
		try {
			JAXBContext context = JAXBContext.newInstance(Settings.class);
			Marshaller marshaller = context.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

			// write
			marshaller.marshal(settings, new File(SETTINGS_FILE));
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}
}