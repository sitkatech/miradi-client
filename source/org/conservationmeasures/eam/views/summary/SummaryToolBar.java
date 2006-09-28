/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.summary;

import javax.swing.JComponent;

import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.actions.views.ActionViewSummary;
import org.conservationmeasures.eam.main.EAMToolBar;

public class SummaryToolBar extends EAMToolBar
{

	public SummaryToolBar(Actions actions)
	{
		super(actions, ActionViewSummary.class, getExtraButtons(actions));
	}

	static JComponent[][] getExtraButtons(Actions actions)
	{
		return new JComponent[0][0];
	}
	
}
