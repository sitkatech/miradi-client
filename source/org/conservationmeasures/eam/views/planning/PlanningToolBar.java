/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.planning;

import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.actions.views.ActionViewPlanning;
import org.conservationmeasures.eam.main.EAMToolBar;

public class PlanningToolBar extends EAMToolBar
{
	public PlanningToolBar(Actions actions)
	{
		super(actions, ActionViewPlanning.class);
	}
}
