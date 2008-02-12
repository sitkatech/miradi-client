/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.views.planning;

import org.miradi.actions.Actions;
import org.miradi.actions.views.ActionViewPlanning;
import org.miradi.main.EAMToolBar;

public class PlanningToolBar extends EAMToolBar
{
	public PlanningToolBar(Actions actions)
	{
		super(actions, ActionViewPlanning.class);
	}
}
