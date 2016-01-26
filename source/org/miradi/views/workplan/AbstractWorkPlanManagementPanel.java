/* 
Copyright 2005-2015, Foundations of Success, Bethesda, Maryland
on behalf of the Conservation Measures Partnership ("CMP").
Material developed between 2005-2013 is jointly copyright by Beneficent Technology, Inc. ("The Benetech Initiative"), Palo Alto, California.

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
package org.miradi.views.workplan;

import org.miradi.dialogs.planning.PlanningTreeManagementPanel;
import org.miradi.dialogs.planning.propertiesPanel.PlanningTreeMultiPropertiesPanel;
import org.miradi.dialogs.planning.upperPanel.PlanningTreeTablePanel;
import org.miradi.main.MainWindow;

class AbstractWorkPlanManagementPanel extends PlanningTreeManagementPanel
{
	public AbstractWorkPlanManagementPanel(MainWindow mainWindowToUse, PlanningTreeTablePanel planningTreeTablePanel, PlanningTreeMultiPropertiesPanel planningTreePropertiesPanel)	throws Exception
	{
		super(mainWindowToUse, planningTreeTablePanel, planningTreePropertiesPanel);
	}

	@Override
	public String getPanelDescription()
	{
		return "";
	}

	@Override
	public void becomeActive()
	{
		super.becomeActive();
		updateStatusBar();
	}
	
	@Override
	public void becomeInactive()
	{
		clearStatusBar();
		super.becomeInactive();
	}

	protected void updateStatusBar()
	{
		getMainWindow().updatePlanningDateRelatedStatus();
	}

	protected void clearStatusBar()
	{
		getMainWindow().clearStatusBar();
	}
}