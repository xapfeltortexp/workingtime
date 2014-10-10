package com.jan.ui.custom;

import com.jan.util.Styles;
import com.vaadin.addon.touchkit.ui.VerticalComponentGroup;

@SuppressWarnings("serial")
public abstract class CustomVerticalComponentGroup extends VerticalComponentGroup {
	
	public CustomVerticalComponentGroup() {
		this.addStyleName(Styles.GROUP_STYLE);
		this.buildComponentGroup();
	}
	
	public CustomVerticalComponentGroup(String headline) {
		super(headline);
		this.addStyleName(Styles.GROUP_STYLE);
		this.buildComponentGroup();
	}
	
	public abstract void buildComponentGroup();
}
