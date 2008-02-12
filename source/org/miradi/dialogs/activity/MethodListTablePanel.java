/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.activity;

import org.miradi.actions.ActionCreateMethod;
import org.miradi.actions.ActionDeleteMethod;
import org.miradi.actions.ActionShareMethod;
import org.miradi.actions.Actions;
import org.miradi.dialogs.base.ObjectListTablePanelWithParent;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.project.Project;

public class MethodListTablePanel extends ObjectListTablePanelWithParent
{
	public MethodListTablePanel(Project projectToUse, Actions actions, ORef nodeRef)
	{
		super(projectToUse, ObjectType.TASK, new MethodListTableModel(projectToUse, nodeRef),	actions, buttonActionClasses);
	}
	
	static Class[] buttonActionClasses = new Class[] {
		ActionCreateMethod.class,
		ActionDeleteMethod.class,
		ActionShareMethod.class,
	};

}
