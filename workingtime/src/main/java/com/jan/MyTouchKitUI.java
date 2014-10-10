package com.jan;

import com.jan.data.manager.Manager;
import com.jan.ui.MenuView;
import com.vaadin.addon.touchkit.annotations.CacheManifestEnabled;
import com.vaadin.addon.touchkit.annotations.OfflineModeEnabled;
import com.vaadin.addon.touchkit.ui.NavigationManager;
import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.UI;

@SuppressWarnings("serial")
@Widgetset("com.jan.gwt.MyWidgetSet")
@Theme("mytheme")
@CacheManifestEnabled
@OfflineModeEnabled
@PreserveOnRefresh
public class MyTouchKitUI extends UI {
	
	private final NavigationManager navigationManager = new NavigationManager();
	
	private final static Manager manager = new Manager();
	
    @Override
    protected void init(VaadinRequest request) {
    	// Set the MainView.class as CurrentComposite
    	navigationManager.setCurrentComponent(new MenuView());
    	
    	// Set the Navigation as the Main View
        setContent(navigationManager);
    }
    
    public static Manager getManager() {
    	return manager;
    }
}

