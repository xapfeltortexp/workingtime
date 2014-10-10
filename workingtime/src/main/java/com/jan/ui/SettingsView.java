package com.jan.ui;

import java.util.Date;

import org.vaadin.risto.stepper.IntStepper;

import com.jan.MyTouchKitUI;
import com.jan.data.Settings;
import com.jan.data.storage.XMLStorage;
import com.jan.ui.custom.CustomVerticalComponentGroup;
import com.vaadin.addon.touchkit.ui.DatePicker;
import com.vaadin.addon.touchkit.ui.NavigationView;
import com.vaadin.addon.touchkit.ui.Switch;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CssLayout;

@SuppressWarnings("serial")
public class SettingsView extends NavigationView {

	private Switch enableSwitch;
	private DatePicker startDatePicker;
	private DatePicker endDatePicker;
	private IntStepper hoursStepper;

	private Button saveButton;

	private Settings settings;

	public SettingsView() {
		setCaption("Einstellungen");
		settings = new XMLStorage().getSettings();

		// build the componentgroup in the middle
		buildContentGroup();

		// Build the top Buttons
		buildTopButton();
	}

	private void buildContentGroup() {
		CustomVerticalComponentGroup componentGroup = new CustomVerticalComponentGroup() {

			@Override
			public void buildComponentGroup() {
				enableSwitch = new Switch("An / Aus");
				enableSwitch.addValueChangeListener(new ValueChangeListener() {

					@Override
					public void valueChange(ValueChangeEvent event) {
						boolean enabled = enableSwitch.getValue();
						startDatePicker.setEnabled(enabled);
						endDatePicker.setEnabled(enabled);
						hoursStepper.setEnabled(enabled);
					}
				});

				startDatePicker = new DatePicker("Start Datum");
				startDatePicker.setValue(new Date());
				endDatePicker = new DatePicker("End Datum");
				endDatePicker.setValue(new Date());

				hoursStepper = new IntStepper("Benötigte Arbeitszeit");
				hoursStepper.setMinValue(1);
				hoursStepper.setValue(1);

				// Update the fields
				if (settings != null) {
					updateFieldsBySettings();
				}

				addComponent(enableSwitch);
				addComponent(startDatePicker);
				addComponent(endDatePicker);
				addComponent(hoursStepper);
			}
		};
		setContent(new CssLayout(componentGroup));
	}

	private void buildTopButton() {
		saveButton = new Button("Speichern");
		saveButton.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				if (enableSwitch.getValue()) {
					// Check if all fields are filled
					if (startDatePicker.getValue() == null || endDatePicker.getValue() == null || hoursStepper.getValue() == null) {
						Notification.show("Alle Felder müssen ausgefüllt sein!");
						return;
					}

					// Check if the startDate is before endDate
					if (!startDatePicker.getValue().before(endDatePicker.getValue())) {
						Notification.show("Das Startdatum muss vor dem Enddatum liegen!");
						return;
					}
				}

				// Save settings
				MyTouchKitUI.getManager().getXmlStorage().saveSettings(enableSwitch.getValue(), startDatePicker.getValue(), endDatePicker.getValue(), hoursStepper.getValue());
				getNavigationManager().navigateTo(new MenuView());
			}
		});
		setRightComponent(saveButton);
	}

	private void updateFieldsBySettings() {
		boolean enabled = settings.isEnabled();

		// Set the values
		enableSwitch.setValue(enabled);
		startDatePicker.setValue(settings.getStartDate());
		endDatePicker.setValue(settings.getEndDate());
		hoursStepper.setValue(settings.getHours());

		// Disable or enable the fields
		startDatePicker.setEnabled(enabled);
		endDatePicker.setEnabled(enabled);
		hoursStepper.setEnabled(enabled);
	}
}
