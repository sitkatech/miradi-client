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
package org.miradi.dialogs.planning.propertiesPanel;

import java.awt.Color;

import org.miradi.commands.CommandSetObjectData;
import org.miradi.dialogs.tablerenderers.RowColumnBaseObjectProvider;
import org.miradi.main.AppPreferences;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.DateUnit;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefSet;
import org.miradi.objecthelpers.TimePeriodCosts;
import org.miradi.objects.Assignment;
import org.miradi.objects.BaseObject;
import org.miradi.objects.FundingSource;
import org.miradi.project.Project;
import org.miradi.questions.CustomPlanningColumnsQuestion;
import org.miradi.utils.OptionalDouble;

public class BudgetDetailsTableModel extends AssignmentDateUnitsTableModel
{
	public BudgetDetailsTableModel(Project projectToUse, RowColumnBaseObjectProvider providerToUse, String treeModelIdentifierAsTagToUse) throws Exception
	{
		super(projectToUse, providerToUse, treeModelIdentifierAsTagToUse);
	}
	
	@Override
	public boolean isCurrencyColumn(int column)
	{
		return true;
	}
	
	@Override
	public Color getCellBackgroundColor(int column)
	{
		DateUnit dateUnit = getDateUnit(column);
		return AppPreferences.getBudgetDetailsBackgroundColor(dateUnit);
	}
	
	@Override
	public String getColumnGroupCode(int modelColumn)
	{
		return CustomPlanningColumnsQuestion.META_BUDGET_DETAIL_COLUMN_CODE;
	}
	
	@Override
	protected OptionalDouble getOptionalDoubleData(BaseObject baseObject, DateUnit dateUnit) throws Exception
	{
		TimePeriodCosts timePeriodCosts = getProjectTotalTimePeriodCostFor(dateUnit);
		if (FundingSource.is(baseObject))
			timePeriodCosts.filterFundingSourcesExpenses(new ORefSet(baseObject));
		
		return calculateValue(timePeriodCosts);
	}
	
	@Override
	protected OptionalDouble calculateValue(TimePeriodCosts timePeriodCosts)
	{
		return timePeriodCosts.calculateTotalCost(getProject());
	}
	
	@Override
	public String getUniqueTableModelIdentifier()
	{
		return getTreeModelIdentifierAsTag() + "." + UNIQUE_TABLE_MODEL_IDENTIFIER;
	}
	
	protected boolean isAssignmentForModel(Assignment assignment)
	{
		return false;
	}

	@Override
	protected boolean isEditableModel()
	{
		return false;
	}
	
	@Override
	protected String getAssignmentsTag()
	{
		 throw new RuntimeException(getErrorMessage());
	}
	
	@Override
	protected int getAssignmentType()
	{
		throw new RuntimeException(getErrorMessage());
	}
	
	@Override
	protected CommandSetObjectData createAppendAssignmentCommand(BaseObject baseObjectForRowColumn, ORef assignmentRef)	throws Exception
	{
		throw new RuntimeException(getErrorMessage());
	}

	private String getErrorMessage()
	{
		return EAM.text("This model is not for a single assignment and has no assignment information");
	}
	
	private static final String UNIQUE_TABLE_MODEL_IDENTIFIER = "BudgetDetailsTableModel";
}
