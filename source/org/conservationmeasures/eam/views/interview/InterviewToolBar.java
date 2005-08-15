/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.interview;

import javax.swing.JToolBar;

import org.conservationmeasures.eam.actions.ActionViewInterview;
import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.main.ViewSwitcher;

public class InterviewToolBar extends JToolBar
{
	public InterviewToolBar(Actions actions)
	{
		setFloatable(false);

		add(ViewSwitcher.create(actions, ActionViewInterview.class));
	}

}
