/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.resource;

import org.miradi.actions.ActionCreateResource;
import org.miradi.actions.ActionDeleteResource;
import org.miradi.actions.Actions;
import org.miradi.dialogs.base.ObjectTablePanelWithCreateAndDelete;
import org.miradi.project.Project;

public class ResourcePoolTablePanel extends ObjectTablePanelWithCreateAndDelete
{
	public ResourcePoolTablePanel(Project project, Actions actions)
	{
		super(project, new ResourcePoolTable(new ResourcePoolTableModel(project)), 
				actions,
				buttons);
	}
	
	static Class[] buttons = new Class[] {
		ActionCreateResource.class,
		ActionDeleteResource.class
	};
}
