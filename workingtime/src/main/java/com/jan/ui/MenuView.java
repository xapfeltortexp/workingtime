package com.jan.ui;

import java.text.SimpleDateFormat;

import com.jan.MyTouchKitUI;
import com.jan.data.Settings;
import com.jan.data.time.TimeFactory;
import com.jan.ui.custom.CustomVerticalComponentGroup;
import com.vaadin.addon.touchkit.ui.NavigationButton;
import com.vaadin.addon.touchkit.ui.NavigationView;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;

@SuppressWarnings("serial")
public class MenuView extends NavigationView {

	private Layout contentLayout = new CssLayout();
	private TimeFactory timeFactory = new TimeFactory();

	public MenuView() {
		setCaption("Menu");

		// build the Information Layout
		contentLayout.addComponent(new CustomVerticalComponentGroup("Information") {

			@Override
			public void buildComponentGroup() {
				Settings settings = MyTouchKitUI.getManager().getXmlStorage().getSettings();

				Label informationLabel = new Label();
				informationLabel.setContentMode(ContentMode.HTML);

				if (settings == null || !settings.isEnabled()) {
					informationLabel.setValue("Du hast derzeit keinen Filter festgelegt.<br>Dies kannst du unter dem Reiter 'Filter' tun.");
				} else {
					SimpleDateFormat dateFormat = new TimeFactory().getDateFormat();
					
					boolean workedEnough = (settings.getHours() - timeFactory.getWorkedHoursUsingSettings()) <= 0;
					
					informationLabel.setValue("Du hast deinen Filter konfiguriert.<br>"
							+ "Vom <b>" + dateFormat.format(settings.getStartDate()) + "</b>"
							+ " bis zum <b>" + dateFormat.format(settings.getEndDate())
							+ "</b>" + " musst du <b>" + settings.getHours() + "</b> Stunden arbeiten.<br>"
							+ "Du hast bereits <b><span style=\"color: " + (workedEnough ? "green;\">" : "red;\">") + timeFactory.getWorkedHoursUsingSettings() + " Stunden</span></b> gearbeitet.");
				}

				addComponent(informationLabel);
			}
		});

		// build the Navigation layout
		contentLayout.addComponent(new CustomVerticalComponentGroup("Menu") {

			@Override
			public void buildComponentGroup() {
				DayView dayView = new DayView();
				NavigationButton editDayButton = new NavigationButton("Tag bearbeiten", dayView);
				
				WorkingDaysView workingDaysView = new WorkingDaysView();
				NavigationButton showAllDaysButton = new NavigationButton("Alle Tage anzeigen", workingDaysView);
				
				FilterView filterView = new FilterView();
				NavigationButton filterButton = new NavigationButton("Filter", filterView);

				// Add Buttons to the group
				addComponent(editDayButton);
				addComponent(showAllDaysButton);
				addComponent(filterButton);
			}
		});

		setLeftComponent(new Button(""));
		setRightComponent(new Button(""));
		setContent(contentLayout);
	}
}
