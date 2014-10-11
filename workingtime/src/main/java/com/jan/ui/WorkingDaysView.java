package com.jan.ui;

import java.util.Date;
import java.util.Iterator;

import com.jan.MyTouchKitUI;
import com.jan.data.WorkingDay;
import com.jan.data.time.TimeFactory;
import com.jan.ui.custom.CustomVerticalComponentGroup;
import com.vaadin.addon.touchkit.ui.NavigationView;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;

@SuppressWarnings("serial")
public class WorkingDaysView extends NavigationView {
	
	private CustomVerticalComponentGroup componentGroup;
	private TimeFactory timeFactory = new TimeFactory();
	
	public WorkingDaysView() {
		setCaption("Arbeisstunden");
		
		Button backButton = new Button("Zurück");
		backButton.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				getNavigationManager().navigateTo(new MenuView());
			}
		});
		setLeftComponent(backButton);
		
		buildComponentGroupList();
	}
	
	private void buildComponentGroupList() {
		componentGroup = new CustomVerticalComponentGroup() {
			
			@Override
			public void buildComponentGroup() {
				final Iterator<WorkingDay> iterator = MyTouchKitUI.getManager().getWorkingDays().iterator();
				while(iterator.hasNext()) {
					WorkingDay workingDay = iterator.next();
					
					// Create a layout. Label gets added there
					final HorizontalLayout workingDayLayout = new HorizontalLayout();
					Button deleteButton = new Button();
					deleteButton.setIcon(FontAwesome.TRASH_O);
					workingDayLayout.addComponent(deleteButton);
					deleteButton.addClickListener(new ClickListener() {
						
						@Override
						public void buttonClick(ClickEvent event) {
							HorizontalLayout tmpLayout = workingDayLayout;
							
							iterator.remove();
							MyTouchKitUI.getManager().getXmlStorage().saveDays(MyTouchKitUI.getManager().getWorkingDays());
							componentGroup.removeComponent(tmpLayout);
						}
					});
					
					CssLayout cssLayout = new CssLayout();
					cssLayout.addComponent(new Label("Datum: " + timeFactory.getDateFormat().format(workingDay.getDate())));
					for(String hours :  workingDay.getWorkingTimes()) {
						String[] splittedHours = hours.split(" - ");
						
						// Convert the timestamp into readable times
						String timeOne = timeFactory.getTimeFormat().format(new Date(Long.valueOf(splittedHours[0])));
						String timeTwo = timeFactory.getTimeFormat().format(new Date(Long.valueOf(splittedHours[1])));
						
						cssLayout.addComponent(new Label("Arbeitszeit: " + timeOne + " - " + timeTwo));
					}
					workingDayLayout.addComponent(cssLayout);
					addComponent(workingDayLayout);
				}
			}
		};
		setContent(componentGroup);
	}

}
