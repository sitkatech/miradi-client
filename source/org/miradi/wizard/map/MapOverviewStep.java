/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 

package org.miradi.wizard.map;

import org.miradi.actions.views.ActionViewMap;
import org.miradi.views.map.MapView;
import org.miradi.wizard.SplitWizardStep;
import org.miradi.wizard.WizardPanel;

public class MapOverviewStep extends SplitWizardStep
{
	public MapOverviewStep(WizardPanel wizardToUse)
	{
		super(wizardToUse, MapView.getViewName());
	}

	public String getProcessStepTitle()
	{
		return "";
	}

	public Class getAssociatedActionClass()
	{
		return ActionViewMap.class;
	}
}
