/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 

package org.conservationmeasures.eam.wizard.map;

import org.conservationmeasures.eam.actions.views.ActionViewMap;
import org.conservationmeasures.eam.views.map.MapView;
import org.conservationmeasures.eam.wizard.SplitWizardStep;
import org.conservationmeasures.eam.wizard.WizardPanel;

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
