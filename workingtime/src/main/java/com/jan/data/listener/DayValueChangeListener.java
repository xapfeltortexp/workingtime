package com.jan.data.listener;

import java.util.List;

import com.jan.ui.custom.CustomVerticalComponentGroup;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.TextField;

@SuppressWarnings("serial")
public class DayValueChangeListener implements ValueChangeListener {
	
	private List<TextField> textFields;
	private TextField textField;
	
	private CustomVerticalComponentGroup group;

	public DayValueChangeListener(List<TextField> textFields, TextField textField, CustomVerticalComponentGroup group) {
		this.textFields = textFields;
		this.textField = textField;
		this.group = group;
	}
	
	/**
	 * Adds a new {@link TextField}, when the last textfield gets edited.
	 * Get just added, when just exactly the last TextField gets edited.
	 */
	@Override
	public void valueChange(ValueChangeEvent event) {
		int size = textFields.size();
		// If the TextField is the LastTextField, than add a new TextField below
		if ((size - 1) == getCurrentPos()) {
			TextField workingTimeField = new TextField("Arbeitszeit");
			workingTimeField.setInputPrompt("00:00 - 00:00");
			workingTimeField.addValueChangeListener(new DayValueChangeListener(textFields, workingTimeField, group));
			textFields.add(workingTimeField);
			
			group.addComponent(workingTimeField);
		}
	}

	private int getCurrentPos() {
		return textFields.indexOf(textField);
	}
}
