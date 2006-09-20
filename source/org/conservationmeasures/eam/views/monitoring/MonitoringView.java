/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.monitoring;

import java.awt.BorderLayout;

import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.views.umbrella.UmbrellaView;

public class MonitoringView extends UmbrellaView
{
	public MonitoringView(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse);
		setToolBar(new MonitoringToolBar(mainWindowToUse.getActions()));
	}

	public String cardName() 
	{
		return getViewName();
	}
	
	static public String getViewName()
	{
		return "Monitoring ";
	}

	public void becomeActive() throws Exception
	{
		panel = new MonitoringPanel(getProject());
		removeAll();
		add(panel, BorderLayout.CENTER);
	}

	public void becomeInactive() throws Exception
	{
		panel = null;
		removeAll();
	}

	MonitoringPanel panel;
}
