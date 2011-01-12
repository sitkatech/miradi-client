/* 
Copyright 2005-2011, Foundations of Success, Bethesda, Maryland 
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

package org.miradi.views.planning.doers;

import org.miradi.commands.CommandBeginTransaction;
import org.miradi.commands.CommandEndTransaction;
import org.miradi.commands.CommandSetObjectData;
import org.miradi.dialogs.planning.PlanningTreeManagementPanel;
import org.miradi.dialogs.planning.RowColumnProvider;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.BaseObject;
import org.miradi.objects.ViewData;
import org.miradi.utils.CodeList;
import org.miradi.views.ObjectsDoer;
import org.miradi.views.TabbedView;
import org.miradi.views.planning.ConfigurablePlanningTreeManagementPanel;
import org.miradi.views.planning.PlanningView;

public class CreateCustomFromCurrentTreeTableConfigurationDoer extends ObjectsDoer
{
	@Override
	public boolean isAvailable()
	{
		return super.isAvailable();
	}
	
	@Override
	public void doIt() throws Exception
	{
		if (!isAvailable())
			return;

		getProject().executeCommand(new CommandBeginTransaction());
		try
		{
			PlanningTreeManagementPanel tab = (PlanningTreeManagementPanel) getView().getCurrentTabPanel();
			RowColumnProvider provider = tab.getPlanningTreeTablePanel().getRowColumnProvider();
			CodeList columnCodes = provider.getColumnListToShow();
			CodeList rowCodes = provider.getRowListToShow();
			
			switchToCustomTab();
			CreatePlanningViewPrefilledConfigurationDoer.createPlanningViewConfiguration(getProject(), rowCodes, columnCodes);
			PlanningCustomizeDialogPopupDoer.showCustomizeDialog(getMainWindow());
		}
		finally
		{
			getProject().executeCommand(new CommandEndTransaction());
		}
	}

	private void switchToCustomTab() throws Exception
	{
		TabbedView view = (TabbedView) getView();	
		String customTabName = ConfigurablePlanningTreeManagementPanel.class.getSimpleName();
		int customTabIndex = view.getTabIndex(customTabName);
		getProject().executeCommand(createTabChangeCommand(customTabIndex));
	}

	private CommandSetObjectData createTabChangeCommand(int newTab) throws Exception
	{
		return new CommandSetObjectData(ObjectType.VIEW_DATA, getViewData().getId(), ViewData.TAG_CURRENT_TAB, Integer.toString(newTab));
	}

	private BaseObject getViewData() throws Exception
	{
		return getProject().getViewData(PlanningView.getViewName());
	}
}
