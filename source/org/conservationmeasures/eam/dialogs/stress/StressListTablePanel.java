/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.stress;

import org.conservationmeasures.eam.actions.ActionCloneStress;
import org.conservationmeasures.eam.actions.ActionCreateStress;
import org.conservationmeasures.eam.actions.ActionCreateStressFromKea;
import org.conservationmeasures.eam.actions.ActionDeleteStress;
import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.dialogs.base.ObjectListTablePanel;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.project.Project;

public class StressListTablePanel extends ObjectListTablePanel
{
	public StressListTablePanel(Project projectToUse, Actions actions, ORef nodeRef)
	{
		super(projectToUse, new StressListTableModel(projectToUse, nodeRef), 
				actions, 
				buttonActionClasses);
	}
	
	static Class[] buttonActionClasses = new Class[] {
		ActionCreateStress.class,
		ActionCloneStress.class,
		ActionCreateStressFromKea.class,
		ActionDeleteStress.class,
	};
}
