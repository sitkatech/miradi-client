/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.objective;

import org.miradi.actions.ActionCloneObjective;
import org.miradi.actions.ActionCreateObjective;
import org.miradi.actions.ActionDeleteObjective;
import org.miradi.actions.Actions;
import org.miradi.dialogs.base.ObjectListTablePanel;
import org.miradi.objecthelpers.ORef;
import org.miradi.project.Project;

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
