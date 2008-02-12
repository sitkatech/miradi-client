/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.organization;

import org.miradi.actions.ActionCreateOrganization;
import org.miradi.actions.ActionDeleteOrganization;
import org.miradi.actions.Actions;
import org.miradi.dialogs.base.ObjectTablePanelWithCreateAndDelete;
import org.miradi.project.Project;

public class OrganizationPoolTablePanel extends ObjectTablePanelWithCreateAndDelete
{
	public OrganizationPoolTablePanel(Project project, Actions actions)
	{
		super(project, new OrganizationPoolTable(new OrganizationPoolTableModel(project)), actions, buttons);
	}
	
	static Class[] buttons = new Class[] {
		ActionCreateOrganization.class,
		ActionDeleteOrganization.class,
	};
}
