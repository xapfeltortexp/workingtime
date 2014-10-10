package com.jan.ui;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.jan.MyTouchKitUI;
import com.jan.data.WorkingDay;
import com.jan.data.time.TimeFactory;
import com.jan.ui.custom.CustomVerticalComponentGroup;
import com.vaadin.addon.touchkit.ui.NavigationView;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;

@SuppressWarnings("serial")
public class WorkingDaysView extends NavigationView {
	
	private SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
	
	public WorkingDaysView() {
		setCaption("Arbeisstunden");
		
		buildComponentGroupList();
	}
	
	private void buildComponentGroupList() {
		CustomVerticalComponentGroup componentGroup = new CustomVerticalComponentGroup() {
			
			@Override
			public void buildComponentGroup() {
				for(WorkingDay workingDay : MyTouchKitUI.getManager().getWorkingDays()) {
					// Create a layout. Label gets added there
					CssLayout workingDayLayout = new CssLayout();
					
					workingDayLayout.addComponent(new Label("Datum: " + dateFormat.format(workingDay.getDate())));
					for(String hours :  workingDay.getWorkingTimes()) {
						String[] splittedHours = hours.split(" - ");
						
						// Convert the timestamp into readable times
						SimpleDateFormat timeFormat = new TimeFactory().getTimeFormat();
						String timeOne = timeFormat.format(new Date(Long.valueOf(splittedHours[0])));
						String timeTwo = timeFormat.format(new Date(Long.valueOf(splittedHours[1])));
						
						workingDayLayout.addComponent(new Label("Arbeitszeit: " + timeOne + " - " + timeTwo));
					}
					addComponent(workingDayLayout);
				}
			}
		};
		setContent(new CssLayout(componentGroup));
	}

}
