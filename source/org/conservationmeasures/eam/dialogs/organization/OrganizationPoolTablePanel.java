/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.organization;

import org.conservationmeasures.eam.actions.ActionCreateOrganization;
import org.conservationmeasures.eam.actions.ActionDeleteOrganization;
import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.dialogs.base.ObjectTablePanelWithCreateAndDelete;
import org.conservationmeasures.eam.project.Project;

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
