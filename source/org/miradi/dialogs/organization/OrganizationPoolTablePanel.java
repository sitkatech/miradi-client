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
import org.miradi.dialogs.fieldComponents.PanelTitleLabel;
import org.miradi.main.EAM;
import org.miradi.project.Project;
import org.miradi.resources.ResourcesHandler;

public class OrganizationPoolTablePanel extends ObjectTablePanelWithCreateAndDelete
{
	public OrganizationPoolTablePanel(Project project, Actions actions) throws Exception
	{
		super(project, new OrganizationPoolTable(new OrganizationPoolTableModel(project)), actions, buttons);
		
		String html = EAM.loadResourceFile(ResourcesHandler.class, "OtherOrgOverview.html");
		addAboveTable(new PanelTitleLabel(html));
	}
	
	static Class[] buttons = new Class[] {
		ActionCreateOrganization.class,
		ActionDeleteOrganization.class,
	};
}
