package com.jan.data.storage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import com.jan.data.Settings;
import com.jan.data.WorkingDay;
import com.jan.data.WorkingDayStore;
import com.jan.ui.FilterView;

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
	public void saveDays(List<WorkingDay> workingDays) {
		WorkingDayStore workingDayStore = new WorkingDayStore();
		workingDayStore.setWorkingDays(workingDays);

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
	public List<WorkingDay> loadWorkingDays() {
		try {
			JAXBContext context = JAXBContext.newInstance(WorkingDayStore.class);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			WorkingDayStore workingDayStore = (WorkingDayStore) unmarshaller.unmarshal(new File(DATA_FILE));
			
			if (workingDayStore == null || workingDayStore.getWorkingDays() == null || workingDayStore.getWorkingDays().isEmpty()) {
				return new ArrayList<WorkingDay>();
			}

			return workingDayStore.getWorkingDays();
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return new ArrayList<WorkingDay>();
	}

	/**
	 * Generate a {@link Settings} object out of a XML File. The settings can be
	 * edit in the {@link FilterView}
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