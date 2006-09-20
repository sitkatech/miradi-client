/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.interview;

import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.actions.views.ActionViewInterview;
import org.conservationmeasures.eam.main.EAMToolBar;

public class InterviewToolBar extends EAMToolBar
{
	public InterviewToolBar(Actions actions)
	{
		super(actions, ActionViewInterview.class);
	}

}
