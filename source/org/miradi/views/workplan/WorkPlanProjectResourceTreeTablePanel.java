/* 
Copyright 2005-2022, Foundations of Success, Bethesda, Maryland
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

import org.miradi.dialogs.planning.WorkPlanCategoryTreeRowColumnProvider;
import org.miradi.dialogs.planning.upperPanel.PlanningTreeTable;
import org.miradi.dialogs.planning.upperPanel.PlanningTreeTableModel;
import org.miradi.dialogs.planning.upperPanel.PlanningTreeTablePanel;
import org.miradi.main.CommandExecutedEvent;
import org.miradi.main.MainWindow;
import org.miradi.objects.PlanningTreeRowColumnProvider;
import org.miradi.objects.TableSettings;
import org.miradi.schemas.TableSettingsSchema;

import javax.swing.*;

public class WorkPlanProjectResourceTreeTablePanel extends WorkPlanCategoryTreeTablePanel
{
	protected WorkPlanProjectResourceTreeTablePanel(MainWindow mainWindowToUse,
													PlanningTreeTable treeToUse,
													PlanningTreeTableModel modelToUse,
													Class[] buttonClasses,
													PlanningTreeRowColumnProvider rowColumnProvider
	) throws Exception
	{
		super(mainWindowToUse, treeToUse, modelToUse, buttonClasses, rowColumnProvider);
	}
	
	public static PlanningTreeTablePanel createPlanningTreeTablePanel(MainWindow mainWindowToUse, PlanningTreeTableModel model, WorkPlanCategoryTreeRowColumnProvider  rowColumnProvider, Class[] buttonActions) throws Exception
	{
		PlanningTreeTable treeTable = new PlanningTreeTable(mainWindowToUse, model);

		return new WorkPlanProjectResourceTreeTablePanel(mainWindowToUse, treeTable, model, buttonActions, rowColumnProvider);
	}
	
	@Override
	protected void addFilterPanel(JPanel filterPanel) throws Exception
	{
		TableSettings tableSettings = TableSettings.findOrCreate(getProject(), getTreeTableModel().getUniqueTreeTableModelIdentifier());
		WorkPlanProjectResourceConfigurationPanel diagramFilterPanel = new WorkPlanProjectResourceConfigurationPanel(getProject(), tableSettings);
		filterPanel.add(diagramFilterPanel);
	}

	@Override
	protected boolean doesCommandForceRebuild(CommandExecutedEvent event) throws Exception
	{
		if (super.doesCommandForceRebuild(event))
			return true;
		
		if (event.isSetDataCommandWithThisTypeAndTag(TableSettingsSchema.getObjectType(), TableSettings.TAG_WORK_PLAN_PROJECT_RESOURCE_CONFIGURATION))
			return true;

		return false;
	}
}
