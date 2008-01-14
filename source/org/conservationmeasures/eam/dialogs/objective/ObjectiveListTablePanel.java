/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.objective;

import org.conservationmeasures.eam.actions.ActionCloneObjective;
import org.conservationmeasures.eam.actions.ActionCreateObjective;
import org.conservationmeasures.eam.actions.ActionDeleteObjective;
import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.dialogs.base.ObjectListTablePanel;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.project.Project;

public class ObjectiveListTablePanel extends ObjectListTablePanel
{
	public ObjectiveListTablePanel(Project projectToUse, Actions actions, ORef nodeRef)
	{
		super(projectToUse, new ObjectiveListTableModel(projectToUse, nodeRef), 
				actions, 
				buttonActionClasses);
	}
	
	static Class[] buttonActionClasses = new Class[] {
		ActionCreateObjective.class,
		ActionDeleteObjective.class,
		ActionCloneObjective.class
	};

}
