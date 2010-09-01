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

import org.miradi.dialogs.planning.RowColumnProvider;
import org.miradi.dialogs.planning.WorkPlanCategoryTreeRowColumnProvider;
import org.miradi.dialogs.planning.upperPanel.PlanningTreeTable;
import org.miradi.dialogs.planning.upperPanel.PlanningTreeTableModel;
import org.miradi.dialogs.planning.upperPanel.PlanningTreeTablePanel;
import org.miradi.main.CommandExecutedEvent;
import org.miradi.main.MainWindow;
import org.miradi.objects.ExpenseAssignment;
import org.miradi.objects.ResourceAssignment;
import org.miradi.objects.ViewData;
import org.miradi.questions.WorkPlanCategoryTypesQuestion;
import org.miradi.utils.CodeList;

public class WorkPlanCategoryTreeTablePanel extends PlanningTreeTablePanel
{
	protected WorkPlanCategoryTreeTablePanel(MainWindow mainWindowToUse,
			PlanningTreeTable treeToUse, 
			PlanningTreeTableModel modelToUse,
			Class[] buttonClasses,
			RowColumnProvider rowColumnProvider
	) throws Exception
	{
		super(mainWindowToUse, treeToUse, modelToUse, buttonClasses, rowColumnProvider);
	}
	
	public static PlanningTreeTablePanel createPlanningTreeTablePanel(MainWindow mainWindowToUse, PlanningTreeTableModel model, WorkPlanCategoryTreeRowColumnProvider  rowColumnProvider, Class[] buttonActions) throws Exception
	{
		PlanningTreeTable treeTable = new PlanningTreeTable(mainWindowToUse, model);

		return new WorkPlanCategoryTreeTablePanel(mainWindowToUse, treeTable, model, buttonActions, rowColumnProvider);
	}
	
	private WorkPlanCategoryTreeRowColumnProvider  getCategoryTreeRowColumnProvider()
	{
		return (WorkPlanCategoryTreeRowColumnProvider) getRowColumnProvider();
	}
	
	@Override
	protected boolean doesCommandForceRebuild(CommandExecutedEvent event) throws Exception
	{
		if (super.doesCommandForceRebuild(event))
			return true;
		
		if (event.isSetDataCommandWithThisTypeAndTag(ViewData.getObjectType(), ViewData.TAG_BUDGET_ROLLUP_REPORT_TYPES))
			return true;
		
		if (wasBudgetTypeCreatedOrDeleted(event))
			return true;
			
		return wereAssignmentsChanged(event);
	}

	private boolean wasBudgetTypeCreatedOrDeleted(CommandExecutedEvent event) throws Exception
	{
		CodeList possibleLevelTypes = getCategoryTreeRowColumnProvider().getLevelTypeCodes();
		for (int index = 0; index < possibleLevelTypes.size(); ++index)
		{
			String levelTypeAsString = possibleLevelTypes.get(index);
			if (levelTypeAsString.equals(WorkPlanCategoryTypesQuestion.UNSPECIFIED_CODE))
				continue;
			
			int levelType = Integer.parseInt(levelTypeAsString); 
			if (wasTypeCreatedOrDeleted(event, levelType))
				return true;
		}
		
		return false;
	}
	
	private boolean wereAssignmentsChanged(CommandExecutedEvent event)
	{
		if (wasResourceAssignmentBudgetRelatedTagChange(event))
			return true;
		
		return wasExpenseAssignmentBudgetRelatedTagChange(event);
	}

	private boolean wasResourceAssignmentBudgetRelatedTagChange(CommandExecutedEvent event)
	{
		String[] resourceAssignmentBudgetTags = new String[]{
				ResourceAssignment.TAG_RESOURCE_ID,
				ResourceAssignment.TAG_ACCOUNTING_CODE_ID,
				ResourceAssignment.TAG_FUNDING_SOURCE_ID,
				ResourceAssignment.TAG_CATEGORY_ONE_REF,
				ResourceAssignment.TAG_CATEGORY_TWO_REF,
		};
		
		return wasAssignmentBudgetRelatedChange(event, ResourceAssignment.getObjectType(), resourceAssignmentBudgetTags);
	}

	private boolean wasExpenseAssignmentBudgetRelatedTagChange(CommandExecutedEvent event)
	{
		String[] expenseAssignmentBudgetTags = new String[]{
				ExpenseAssignment.TAG_ACCOUNTING_CODE_REF,
				ExpenseAssignment.TAG_FUNDING_SOURCE_REF,
				ExpenseAssignment.TAG_CATEGORY_ONE_REF,
				ExpenseAssignment.TAG_CATEGORY_TWO_REF,
		};
		
		return wasAssignmentBudgetRelatedChange(event, ExpenseAssignment.getObjectType(), expenseAssignmentBudgetTags);
	}

	private boolean wasAssignmentBudgetRelatedChange(CommandExecutedEvent event, int objectType, String[] expenseAssignmentBudgetTags)
	{
		for (int index = 0; index < expenseAssignmentBudgetTags.length; ++index)
		{
			if (event.isSetDataCommandWithThisTypeAndTag(objectType, expenseAssignmentBudgetTags[index]))
				return true;
		}
		
		return false;
	}
}
