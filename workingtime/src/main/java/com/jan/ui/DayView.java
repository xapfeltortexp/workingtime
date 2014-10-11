package com.jan.ui;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import com.jan.MyTouchKitUI;
import com.jan.data.WorkingDay;
import com.jan.data.listener.DayValueChangeListener;
import com.jan.data.listener.SaveClickListener;
import com.jan.data.time.TimeFactory;
import com.jan.ui.custom.CustomVerticalComponentGroup;
import com.vaadin.addon.touchkit.ui.DatePicker;
import com.vaadin.addon.touchkit.ui.NavigationView;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;

@SuppressWarnings("serial") public class DayView extends NavigationView {

	private CssLayout contentLayout;

	private CustomVerticalComponentGroup inputComponentGroup;

	private DatePicker datePicker;
	
	private TextField workingTimeField;
	private List<TextField> workingTimeFields;
	
	private TextArea noticeArea;
	private Button cancelButton;
	private Button saveButton;

	public DayView() {
		setCaption("Tag bearbeiten");

		contentLayout = new CssLayout();
		workingTimeFields = new LinkedList<TextField>();

		// build the input fields
		buildInputFields();

		// build the notice field
		buildNoticeField();

		// Create the top buttons
		buildTopButtons();

		// set the content
		setContent(contentLayout);

		// Check for existing data and fill the fields
		checkExistingData();
	}

	/**
	 * Add to the contentLayout a VerticalComponentGroup. In that Group I add a
	 * DatePicker (Locale in Germany) to define the day and a TextField to say
	 * how long someone worked at this day. If you chance something in the
	 * TextField, a new TextField below get created to insert more workingtimes
	 */
	private final void buildInputFields() {
		contentLayout.addComponent(new CustomVerticalComponentGroup("Eingabefelder") {

			@Override
			public void buildComponentGroup() {
				inputComponentGroup = this;

				datePicker = new DatePicker("Datum");
				datePicker.setValue(new Date());
				datePicker.addValueChangeListener(new ValueChangeListener() {

					@Override
					public void valueChange(ValueChangeEvent event) {
						checkExistingData();
					}
				});

				// The textfield for the working time - pattern XXH XXM
				workingTimeField = new TextField("Arbeiszeit");
				workingTimeField.setInputPrompt("00:00 - 00:00");
				workingTimeFields.add(workingTimeField);

				// Add a valuechangelistener
				workingTimeField.addValueChangeListener(new DayValueChangeListener(workingTimeFields, workingTimeField, this));

				addComponent(datePicker);
				addComponent(workingTimeField);
			}
		});
	}

	/**
	 * Add the the contentLayout a VerticalComponentGroup. In This group I add a
	 * TextArea. In this area you can add some notice of a day.
	 */
	private final void buildNoticeField() {
		contentLayout.addComponent(new CustomVerticalComponentGroup("Notiz") {

			@Override
			public void buildComponentGroup() {
				noticeArea = new TextArea();
				noticeArea.setInputPrompt("Gebe hier deine Notiz ein...");
				addComponent(noticeArea);
			}
		});
	}

	/**
	 * Create to Buttons on the top. The left button is to cancel the action and
	 * the right buton is to save the action
	 */
	private final void buildTopButtons() {
		cancelButton = new Button("Verwerfen");
		cancelButton.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				getNavigationManager().navigateTo(new MenuView());
			}
		});
		setLeftComponent(cancelButton);

		// Create the savebutton
		saveButton = new Button("Speichern");
		saveButton.setClickShortcut(KeyCode.ENTER);
		saveButton.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				boolean success = new SaveClickListener(datePicker.getValue(), workingTimeFields, noticeArea.getValue()).fireEvent();
				if (success) {
					getNavigationManager().navigateTo(new MenuView());
				}
			}
		});

		// Set Button as right component
		setRightComponent(saveButton);
	}

	private void checkExistingData() {
		WorkingDay workingDay = MyTouchKitUI.getManager().getWorkingDay(datePicker.getValue());
		if (workingDay == null) {
			resetFields();
			return;
		}
		datePicker.setValue(workingDay.getDate());

		// remove all workingTimeFields
		for (TextField textField : workingTimeFields) {
			inputComponentGroup.removeComponent(textField);
		}
		workingTimeFields.clear();

		for (String workingTimes : workingDay.getWorkingTimes()) {
			// convert millis to readable time
			String[] splittedTime = workingTimes.split(" - ");
			TimeFactory timeFactory = new TimeFactory();

			long millisOne = Long.valueOf(splittedTime[0]);
			long millisTwo = Long.valueOf(splittedTime[1]);
			workingTimes = timeFactory.toReadableTime(millisOne) + " - " + timeFactory.toReadableTime(millisTwo);

			// Create new fields for the other workingtimes
			TextField workingTimeField = new TextField("Arbeitszeit");
			workingTimeField.setValue(workingTimes);
			workingTimeField.setInputPrompt("00:00 - 00:00");
			workingTimeField.addValueChangeListener(new DayValueChangeListener(workingTimeFields, workingTimeField, inputComponentGroup));
			workingTimeFields.add(workingTimeField);
			inputComponentGroup.addComponent(workingTimeField);
		}
	}

	private void resetFields() {
		noticeArea.setValue("");
		for(TextField textField : workingTimeFields) {
			inputComponentGroup.removeComponent(textField);
		}
		workingTimeFields.clear();
		
		// The textfield for the working time - pattern XXH XXM
		workingTimeField = new TextField("Arbeiszeit");
		workingTimeField.setInputPrompt("00:00 - 00:00");
		workingTimeFields.add(workingTimeField);

		// Add a valuechangelistener
		workingTimeField.addValueChangeListener(new DayValueChangeListener(workingTimeFields, workingTimeField, inputComponentGroup));
		inputComponentGroup.addComponent(workingTimeField);
	}
}
