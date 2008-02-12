/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.views.targetviability;

import org.miradi.actions.Actions;
import org.miradi.actions.views.ActionViewTargetViability;
import org.miradi.main.EAMToolBar;

public class TargetViabilityToolBar extends EAMToolBar
{
	public TargetViabilityToolBar(Actions actions)
	{
		super(actions, ActionViewTargetViability.class);
	}
}
