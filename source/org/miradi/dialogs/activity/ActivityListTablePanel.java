/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.activity;

import org.miradi.actions.ActionCreateActivity;
import org.miradi.actions.ActionDeleteActivity;
import org.miradi.actions.ActionShareActivity;
import org.miradi.actions.Actions;
import org.miradi.dialogs.base.ObjectListTablePanelWithParent;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.project.Project;

public class ActivityListTablePanel extends ObjectListTablePanelWithParent
{
	public ActivityListTablePanel(Project projectToUse, Actions actions, ORef nodeRef)
	{
		super(projectToUse, ObjectType.TASK, new ActivityListTableModel(projectToUse, nodeRef),	actions, buttonActionClasses);
	}
	
	static Class[] buttonActionClasses = new Class[] {
		ActionCreateActivity.class,
		ActionDeleteActivity.class,
		ActionShareActivity.class,
	};
}
