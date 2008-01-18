/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.planning.legend;

import org.conservationmeasures.eam.dialogs.planning.RowColumnProvider;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.planning.PlanningView;

public class PlanningViewSingleLevelRadioButton extends PlanningViewRadioButton
{
	public PlanningViewSingleLevelRadioButton(Project projectToUse, RowColumnProvider rowColumnProvider)
	{
		super(projectToUse);
	}

	public String getPropertyName()
	{
		return PlanningView.SINGLE_LEVEL_RADIO_CHOICE;
	}
}
