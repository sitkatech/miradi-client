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

package org.miradi.dialogs.strategicPlan;

import org.miradi.dialogs.diagram.StrategyPropertiesPanel;
import org.miradi.dialogs.planning.propertiesPanel.PlanningTreeMultiPropertiesPanel;
import org.miradi.dialogs.planning.propertiesPanel.PlanningViewTaskPropertiesPanel;
import org.miradi.dialogs.task.WorkPlanActivityPropertiesPanel;
import org.miradi.dialogs.task.WorkPlanActivityPropertiesPanelWithoutBudgetPanels;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORef;

public class ActionPlanMultiPropertiesPanel extends	PlanningTreeMultiPropertiesPanel
{
	public ActionPlanMultiPropertiesPanel(MainWindow mainWindowToUse, ORef orefToUse) throws Exception
	{
		super(mainWindowToUse, orefToUse);
	}

	@Override
	protected WorkPlanActivityPropertiesPanel createActivityPropertiesPanel() throws Exception
	{
		return new WorkPlanActivityPropertiesPanelWithoutBudgetPanels(getMainWindow());
	}

	@Override
	protected IndicatorPropertiesPanel createIndicatorPropertiesPanel() throws Exception
	{
		return new IndicatorPropertiesPanel(getProject(), getMainWindow());
	}
	
	@Override
	protected StrategyPropertiesPanel createStrategyPropertiesPanel() throws Exception
	{
		return new StrategyPropertiesPanel(getMainWindow());
	}
	
	@Override
	protected PlanningViewTaskPropertiesPanel createTaskPropertiesPanel() throws Exception
	{
		return new TaskPropertiesPanelWithoutBudgetPanels(getMainWindow());
	}
}
