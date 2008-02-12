/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.subTarget;

import org.miradi.actions.ActionCreateSubTarget;
import org.miradi.actions.ActionDeleteSubTarget;
import org.miradi.actions.Actions;
import org.miradi.dialogs.base.ObjectListTablePanel;
import org.miradi.objecthelpers.ORef;
import org.miradi.project.Project;

public class SubTargetListTablePanel extends ObjectListTablePanel
{
	public SubTargetListTablePanel(Project projectToUse, Actions actions, ORef nodeRef)
	{
		super(projectToUse, new SubTargetListTableModel(projectToUse, nodeRef), actions, 
				buttonActionClasses);
	}
	
	static Class[] buttonActionClasses = new Class[] {
		ActionCreateSubTarget.class,
		ActionDeleteSubTarget.class,
	};
}
