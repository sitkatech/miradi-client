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

package org.miradi.dialogs.planning.propertiesPanel;

import org.miradi.dialogs.planning.TableWithExpandableColumnsInterface;
import org.miradi.dialogs.planning.upperPanel.PlanningViewMainTableModel;
import org.miradi.project.Project;
import org.miradi.questions.WorkPlanColumnConfigurationQuestion;
import org.miradi.views.summary.SummaryPlanningWorkPlanSubPanel;

public class WorkPlanAboveBudgetColumnsBar extends AboveBudgetColumnsBar
{
	public WorkPlanAboveBudgetColumnsBar(Project projectToUse, TableWithExpandableColumnsInterface tableToSitAbove)
	{
		super(projectToUse, tableToSitAbove);
	}

	@Override
	protected String getWorkUnitsAboveColumnLabelLocal()
	{
		String columnName = getChoiceLabel(WorkPlanColumnConfigurationQuestion.META_RESOURCE_ASSIGNMENT_COLUMN_CODE);
		if (doesColumnHeaderNeedAsterisk())
			columnName += PlanningViewMainTableModel.HAS_DATA_OUTSIDE_OF_PROJECT_DATE_ASTERISK;

		return columnName;
	}

	@Override
	protected String getExpensesAboveColumnLabelLocal()
	{
		String columnName = getChoiceLabel(WorkPlanColumnConfigurationQuestion.META_EXPENSE_ASSIGNMENT_COLUMN_CODE);
		if (doesColumnHeaderNeedAsterisk())
			columnName += PlanningViewMainTableModel.HAS_DATA_OUTSIDE_OF_PROJECT_DATE_ASTERISK;

		return columnName;
	}

	private boolean doesColumnHeaderNeedAsterisk()
	{
		return SummaryPlanningWorkPlanSubPanel.hasAssignedDataOutsideOfProjectDateRange(getProject());
	}

}
