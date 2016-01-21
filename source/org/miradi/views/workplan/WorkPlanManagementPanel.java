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

import org.miradi.dialogs.planning.propertiesPanel.PlanningTreeMultiPropertiesPanel;
import org.miradi.dialogs.planning.upperPanel.PlanningTreeTablePanel;
import org.miradi.dialogs.planning.upperPanel.WorkPlanTreeTablePanel;
import org.miradi.icons.IconManager;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.PlanningTreeRowColumnProvider;

import javax.swing.*;

class WorkPlanManagementPanel extends AbstractWorkPlanManagementPanel
{
	public WorkPlanManagementPanel(MainWindow mainWindowToUse, PlanningTreeTablePanel planningTreeTablePanel, PlanningTreeMultiPropertiesPanel planningTreePropertiesPanel)	throws Exception
	{
		super(mainWindowToUse, planningTreeTablePanel, planningTreePropertiesPanel);
	}
	
	@Override
	public String getPanelDescription()
	{
		return EAM.text("Tab|Work Plan");
	}

	@Override
	public Icon getIcon()
	{
		return IconManager.getWorkPlanIcon();
	}
	
	@Override
	protected PlanningTreeTablePanel createPlanningTreeTablePanel(String uniqueTreeTableModelIdentifier, PlanningTreeRowColumnProvider rowColumnProvider) throws Exception
	{
		return WorkPlanTreeTablePanel.createPlanningTreeTablePanel(getMainWindow());
	}
	
	public static WorkPlanManagementPanel createWorkPlanPanel(MainWindow mainWindowToUse) throws Exception
	{
		PlanningTreeTablePanel workPlanTreeTablePanel = WorkPlanTreeTablePanel.createPlanningTreeTablePanel(mainWindowToUse);
		PlanningTreeMultiPropertiesPanel workPlanPropertiesPanel = new PlanningTreeMultiPropertiesPanel(mainWindowToUse, ORef.INVALID);

		return new WorkPlanManagementPanel(mainWindowToUse, workPlanTreeTablePanel, workPlanPropertiesPanel);
	}

}