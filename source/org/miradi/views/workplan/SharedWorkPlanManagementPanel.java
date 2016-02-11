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
import org.miradi.dialogs.planning.propertiesPanel.SharedWorkPlanMultiPropertiesPanel;
import org.miradi.dialogs.planning.upperPanel.PlanningTreeTablePanel;
import org.miradi.dialogs.planning.upperPanel.SharedWorkPlanTreeTablePanel;
import org.miradi.icons.IconManager;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.TimePeriodCostsMapsCache;
import org.miradi.objects.PlanningTreeRowColumnProvider;
import org.miradi.project.ProjectTotalCalculatorStrategyDefault;
import org.miradi.project.ProjectTotalCalculatorStrategySharedWorkPlan;

import javax.swing.*;

public class SharedWorkPlanManagementPanel extends AbstractWorkPlanManagementPanel
{
	public SharedWorkPlanManagementPanel(MainWindow mainWindowToUse, PlanningTreeTablePanel planningTreeTablePanel, PlanningTreeMultiPropertiesPanel planningTreePropertiesPanel) throws Exception
	{
		super(mainWindowToUse, planningTreeTablePanel, planningTreePropertiesPanel);
	}
	
	@Override
	public String getPanelDescription()
	{
		return EAM.text("Tab|Shared Work Plan");
	}

	@Override
	public Icon getIcon()
	{
		return IconManager.getSharedWorkPlanIcon();
	}

	@Override
	public void becomeActive()
	{
		super.becomeActive();
		updateStatusBar();

		TimePeriodCostsMapsCache cache = getProject().getTimePeriodCostsMapsCache();
		String workPlanBudgetMode = cache.getWorkPlanBudgetMode();
		cache.setProjectTotalCalculatorStrategy(new ProjectTotalCalculatorStrategySharedWorkPlan(workPlanBudgetMode));
	}

	@Override
	public void becomeInactive()
	{
		clearStatusBar();
		super.becomeInactive();

		TimePeriodCostsMapsCache cache = getProject().getTimePeriodCostsMapsCache();
		String workPlanBudgetMode = cache.getWorkPlanBudgetMode();
		cache.setProjectTotalCalculatorStrategy(new ProjectTotalCalculatorStrategyDefault(workPlanBudgetMode));
	}

	@Override
	protected PlanningTreeTablePanel createPlanningTreeTablePanel(String uniqueTreeTableModelIdentifier, PlanningTreeRowColumnProvider rowColumnProvider) throws Exception
	{
		return SharedWorkPlanTreeTablePanel.createPlanningTreeTablePanel(getMainWindow());
	}

	@Override
	protected void updateStatusBar()
	{
		SharedWorkPlanTreeTablePanel workPlanTreeTablePanel = (SharedWorkPlanTreeTablePanel) getPlanningTreeTablePanel();
		workPlanTreeTablePanel.updateStatusBar();
	}

	public static SharedWorkPlanManagementPanel createWorkPlanPanel(MainWindow mainWindowToUse) throws Exception
	{
		PlanningTreeTablePanel workPlanTreeTablePanel = SharedWorkPlanTreeTablePanel.createPlanningTreeTablePanel(mainWindowToUse);
		PlanningTreeMultiPropertiesPanel workPlanPropertiesPanel = new SharedWorkPlanMultiPropertiesPanel(mainWindowToUse, ORef.INVALID);

		return new SharedWorkPlanManagementPanel(mainWindowToUse, workPlanTreeTablePanel, workPlanPropertiesPanel);
	}
}