/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.organization;

import javax.swing.BorderFactory;

import org.miradi.actions.ActionCreateOrganization;
import org.miradi.actions.ActionDeleteOrganization;
import org.miradi.actions.Actions;
import org.miradi.dialogs.base.ObjectTablePanelWithCreateAndDelete;
import org.miradi.main.AppPreferences;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.resources.ResourcesHandler;
import org.miradi.utils.FlexibleWidthHtmlViewer;

public class OrganizationPoolTablePanel extends ObjectTablePanelWithCreateAndDelete
{
	public OrganizationPoolTablePanel(MainWindow mainWindowToUse, Actions actions) throws Exception
	{
		super(mainWindowToUse.getProject(), new OrganizationPoolTable(new OrganizationPoolTableModel(mainWindowToUse.getProject())), actions, buttons);
		
		String html = EAM.loadResourceFile(ResourcesHandler.class, "OtherOrgOverview.html");
		FlexibleWidthHtmlViewer htmlViewer = new FlexibleWidthHtmlViewer(mainWindowToUse, html);
		
		// NOTE: For some reason, without a border, the htmlViewer is not visible
		htmlViewer.setBorder(BorderFactory.createLineBorder(AppPreferences.getDataPanelBackgroundColor()));
		
		addAboveTable(htmlViewer);
	}
	
	static Class[] buttons = new Class[] {
		ActionCreateOrganization.class,
		ActionDeleteOrganization.class,
	};
}
