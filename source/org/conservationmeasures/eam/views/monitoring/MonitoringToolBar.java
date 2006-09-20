/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.monitoring;

import javax.swing.JComponent;

import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.actions.views.ActionViewMonitoring;
import org.conservationmeasures.eam.main.EAMToolBar;

public class MonitoringToolBar extends EAMToolBar
{
	public MonitoringToolBar(Actions actions)
	{
		super(actions, ActionViewMonitoring.class, getExtraButtons(actions));
	}

	static JComponent[][] getExtraButtons(Actions actions)
	{
		return new JComponent[0][0];
	}
	
}
