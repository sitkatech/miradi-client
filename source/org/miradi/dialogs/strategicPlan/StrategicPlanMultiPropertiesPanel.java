/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
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

package org.miradi.dialogs.strategicPlan;

import org.miradi.dialogs.diagram.AbstractStrategyPropertiesPanel;
import org.miradi.dialogs.planning.propertiesPanel.PlanningTreeMultiPropertiesPanel;
import org.miradi.dialogs.planning.propertiesPanel.PlanningViewTaskPropertiesPanel;
import org.miradi.dialogs.viability.AbstractIndicatorPropertiesPanel;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORef;
import org.miradi.views.umbrella.ObjectPicker;

public class StrategicPlanMultiPropertiesPanel extends	PlanningTreeMultiPropertiesPanel
{
	public StrategicPlanMultiPropertiesPanel(MainWindow mainWindowToUse, ORef orefToUse, ObjectPicker objectPickerToUse) throws Exception
	{
		super(mainWindowToUse, orefToUse, objectPickerToUse);
	}
	
	@Override
	protected AbstractIndicatorPropertiesPanel createIndicatorPropertiesPanel() throws Exception
	{
		return new IndicatorPropertiesPanelWithoutBudgetPanels(getProject());
	}
	
	@Override
	protected AbstractStrategyPropertiesPanel createStrategyPropertiesPanel(ObjectPicker objectPickerToUse) throws Exception
	{
		return new StrategicPlanStrategyPropertiesPanel(getProject(), objectPickerToUse);
	}
	
	@Override
	protected PlanningViewTaskPropertiesPanel createTaskPropertiesPanel(ObjectPicker objectPickerToUse) throws Exception
	{
		return new TaskPropertiesPanelWithoutBudgetPanels(getMainWindow());
	}
}
