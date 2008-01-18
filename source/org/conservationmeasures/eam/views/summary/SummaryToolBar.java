/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
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
