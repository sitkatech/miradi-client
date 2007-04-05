/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs;

import org.conservationmeasures.eam.actions.ActionCreateIndicator;
import org.conservationmeasures.eam.actions.ActionDeleteIndicator;
import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.actions.MainWindowAction;
import org.conservationmeasures.eam.actions.ObjectsAction;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.project.Project;

public class IndicatorListTablePanel extends ObjectListTablePanel
{
	public IndicatorListTablePanel(Project projectToUse, Actions actions, ORef ref)
	{
		super(projectToUse, ObjectType.INDICATOR, 
				new IndicatorListTableModel(projectToUse, ref), 
				(MainWindowAction)actions.get(ActionCreateIndicator.class), 
				(ObjectsAction)actions.get(ActionDeleteIndicator.class));
	}

}
