/* 
Copyright 2005-2010, Foundations of Success, Bethesda, Maryland 
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

package org.miradi.views.workplan;

import java.awt.BorderLayout;

import org.miradi.commands.CommandSetObjectData;
import org.miradi.dialogs.planning.RowColumnProvider;
import org.miradi.dialogs.planning.upperPanel.PlanningTreeTable;
import org.miradi.dialogs.planning.upperPanel.PlanningTreeTableModel;
import org.miradi.dialogs.planning.upperPanel.PlanningTreeTablePanel;
import org.miradi.main.CommandExecutedEvent;
import org.miradi.main.MainWindow;
import org.miradi.objects.Assignment;
import org.miradi.objects.ExpenseAssignment;
import org.miradi.objects.ResourceAssignment;
import org.miradi.objects.ViewData;

public class RollupReportsTreeTablePanel extends PlanningTreeTablePanel
{
	protected RollupReportsTreeTablePanel(MainWindow mainWindowToUse,
			PlanningTreeTable treeToUse, 
			PlanningTreeTableModel modelToUse,
			Class[] buttonClasses,
			RowColumnProvider rowColumnProvider
	) throws Exception
	{
		super(mainWindowToUse, treeToUse, modelToUse, buttonClasses, rowColumnProvider);
		
		rollupReportsEditorComponent = new BudgetRollupChoiceEditorPanel(getProject());
		add(rollupReportsEditorComponent, BorderLayout.BEFORE_FIRST_LINE);
	}
	
	@Override
	public void dispose()
	{
		rollupReportsEditorComponent.dispose();
		
		super.dispose();
	}

	public static PlanningTreeTablePanel createPlanningTreeTablePanel(MainWindow mainWindowToUse, PlanningTreeTableModel model, RowColumnProvider rowColumnProvider) throws Exception
	{
		PlanningTreeTable treeTable = new PlanningTreeTable(mainWindowToUse, model);

		return new RollupReportsTreeTablePanel(mainWindowToUse, treeTable, model, getButtonActions(), rowColumnProvider);
	}
	
	@Override
	protected boolean doesCommandForceRebuild(CommandExecutedEvent event)
	{
		if (super.doesCommandForceRebuild(event))
			return true;
		
		if (event.isSetDataCommandWithThisTypeAndTag(ViewData.getObjectType(), ViewData.TAG_BUDGET_ROLLUP_REPORT_TYPES))
			return true;
			
		return wereAssignmentsChanged(event);
	}
	
	private boolean wereAssignmentsChanged(CommandExecutedEvent event)
	{
		if (event.isSetDataCommandWithThisTypeAndTag(ExpenseAssignment.getObjectType(), ExpenseAssignment.TAG_ACCOUNTING_CODE_REF))
			return true;
		
		if (event.isSetDataCommandWithThisTypeAndTag(ExpenseAssignment.getObjectType(), ExpenseAssignment.TAG_FUNDING_SOURCE_REF))
			return true;
		
		if (event.isSetDataCommandWithThisTypeAndTag(ResourceAssignment.getObjectType(), ResourceAssignment.TAG_FUNDING_SOURCE_ID))
			return true;
		
		if (event.isSetDataCommandWithThisTypeAndTag(ResourceAssignment.getObjectType(), ResourceAssignment.TAG_ACCOUNTING_CODE_ID))
			return true;
		
		if (event.isSetDataCommandWithThisTypeAndTag(ResourceAssignment.getObjectType(), ResourceAssignment.TAG_RESOURCE_ID))
			return true;
		
		if (event.isSetDataCommand())
			return isAssignmentBudgetCategoryCommand(event.getSetCommand());
		
		return false;
	}

	private boolean isAssignmentBudgetCategoryCommand(CommandSetObjectData setCommand)
	{
		if (!Assignment.isAssignment(setCommand.getObjectType()))
			return false;
		
		if (setCommand.isJustTagInAnyType(Assignment.TAG_CATEGORY_ONE_REF))
			return true;
		
		return setCommand.isJustTagInAnyType(Assignment.TAG_CATEGORY_TWO_REF);
	}

	private static Class[] getButtonActions()
	{
		return new Class[] {
		};
	}
	
	private BudgetRollupChoiceEditorPanel rollupReportsEditorComponent;
}
