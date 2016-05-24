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
import org.miradi.main.AppPreferences;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.DateUnit;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.project.Project;
import org.miradi.questions.WorkPlanColumnConfigurationQuestion;
import org.miradi.views.summary.SummaryPlanningWorkPlanSubPanel;

import java.awt.*;
import java.util.Vector;

public class WorkPlanAboveBudgetColumnsBar extends AboveBudgetColumnsBar
{
	public WorkPlanAboveBudgetColumnsBar(Project projectToUse, TableWithExpandableColumnsInterface tableToSitAbove)
	{
		super(projectToUse, tableToSitAbove);
	}

	@Override
	protected void paintComponent(Graphics g)
	{
		g.setColor(getBackground());
		g.fillRect(getX(), getY(), getWidth(), getHeight());
		DateUnit forever = new DateUnit();
		drawColumnGroupHeader(g, findColumnGroupBounds(createColumnGroup(WorkPlanColumnConfigurationQuestion.META_TIMEFRAME_TOTAL)), getTimeframeAboveColumnLabelLocal(), AppPreferences.RESOURCE_TABLE_BACKGROUND);
		drawColumnGroupHeader(g, findColumnGroupBounds(createColumnGroup(WorkPlanColumnConfigurationQuestion.META_ASSIGNED_WHEN_TOTAL)), getAssignedWhenAboveColumnLabelLocal(), AppPreferences.getWorkUnitsBackgroundColor());
		drawColumnGroupHeader(g, findColumnGroupBounds(createColumnGroup(WorkPlanColumnConfigurationQuestion.META_ASSIGNED_WHO_TOTAL)), getAssignedWhoAboveColumnLabelLocal(), AppPreferences.getWorkUnitsBackgroundColor());
		drawColumnGroupHeader(g, findColumnGroupBounds(WorkPlanColumnConfigurationQuestion.getAllPossibleWorkUnitsColumnGroups()), getWorkUnitsAboveColumnLabelLocal(), AppPreferences.getWorkUnitsBackgroundColor(forever));
		drawColumnGroupHeader(g, findColumnGroupBounds(WorkPlanColumnConfigurationQuestion.getAllPossibleExpensesColumnGroups()), getExpensesAboveColumnLabelLocal(), AppPreferences.getExpenseAmountBackgroundColor(forever));
		drawColumnGroupHeader(g, findColumnGroupBounds(WorkPlanColumnConfigurationQuestion.getAllPossibleBudgetTotalsColumnGroups()), getBudgetTotalsAboveColumnLabel(), AppPreferences.getBudgetDetailsBackgroundColor(forever));
	}

	private Vector<String> createColumnGroup(String column)
	{
		Vector<String> columnGroups = new Vector<String>();
		columnGroups.add(column);
		return columnGroups;
	}

	protected String getTimeframeAboveColumnLabelLocal()
	{
		return EAM.fieldLabel(ObjectType.FAKE, WorkPlanColumnConfigurationQuestion.META_TIMEFRAME_TOTAL_ALT_SHORT);
	}

	protected String getAssignedWhenAboveColumnLabelLocal()
	{
		String columnName = EAM.fieldLabel(ObjectType.FAKE, WorkPlanColumnConfigurationQuestion.META_ASSIGNED_WHEN_TOTAL_ALT_SHORT);
		if (doesColumnHeaderNeedAsterisk())
			columnName += HAS_DATA_OUTSIDE_OF_PROJECT_DATE_ASTERISK;

		return columnName;
	}

	protected String getAssignedWhoAboveColumnLabelLocal()
	{
		String columnName = EAM.fieldLabel(ObjectType.FAKE, WorkPlanColumnConfigurationQuestion.META_ASSIGNED_WHO_TOTAL_ALT_SHORT);
		if (doesColumnHeaderNeedAsterisk())
			columnName += HAS_DATA_OUTSIDE_OF_PROJECT_DATE_ASTERISK;

		return columnName;
	}

	@Override
	protected String getWorkUnitsAboveColumnLabelLocal()
	{
		String columnName = getChoiceLabel(WorkPlanColumnConfigurationQuestion.META_RESOURCE_ASSIGNMENT_COLUMN_CODE);
		if (doesColumnHeaderNeedAsterisk())
			columnName += HAS_DATA_OUTSIDE_OF_PROJECT_DATE_ASTERISK;

		return columnName;
	}

	@Override
	protected String getExpensesAboveColumnLabelLocal()
	{
		String columnName = getChoiceLabel(WorkPlanColumnConfigurationQuestion.META_EXPENSE_ASSIGNMENT_COLUMN_CODE);
		if (doesColumnHeaderNeedAsterisk())
			columnName += HAS_DATA_OUTSIDE_OF_PROJECT_DATE_ASTERISK;

		return columnName;
	}

	private boolean doesColumnHeaderNeedAsterisk()
	{
		return SummaryPlanningWorkPlanSubPanel.hasAssignedDataOutsideOfProjectDateRange(getProject());
	}

	private static final String HAS_DATA_OUTSIDE_OF_PROJECT_DATE_ASTERISK = "*";
}
