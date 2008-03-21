/* 
Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
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
