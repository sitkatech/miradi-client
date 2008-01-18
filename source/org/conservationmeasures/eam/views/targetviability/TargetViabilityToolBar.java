/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.targetviability;

import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.actions.views.ActionViewTargetViability;
import org.conservationmeasures.eam.main.EAMToolBar;

public class TargetViabilityToolBar extends EAMToolBar
{
	public TargetViabilityToolBar(Actions actions)
	{
		super(actions, ActionViewTargetViability.class);
	}
}
