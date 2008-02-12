/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.planning.legend;

import org.miradi.dialogs.planning.RowColumnProvider;
import org.miradi.project.Project;
import org.miradi.views.planning.PlanningView;

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
