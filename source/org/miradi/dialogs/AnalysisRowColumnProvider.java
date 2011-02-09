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

package org.miradi.dialogs;

import org.miradi.dialogs.planning.AbstractBudgetCategoryRowColumnProvider;
import org.miradi.dialogs.planning.WorkPlanCategoryTreeRowColumnProvider;
import org.miradi.main.EAM;
import org.miradi.objects.AccountingCode;
import org.miradi.objects.BudgetCategoryOne;
import org.miradi.objects.BudgetCategoryTwo;
import org.miradi.objects.FundingSource;
import org.miradi.objects.ProjectResource;
import org.miradi.project.Project;
import org.miradi.questions.WorkPlanColumnConfigurationQuestion;
import org.miradi.utils.CodeList;
import org.miradi.views.workplan.WorkPlanView;

public class AnalysisRowColumnProvider extends AbstractBudgetCategoryRowColumnProvider implements WorkPlanCategoryTreeRowColumnProvider
{ 
	public AnalysisRowColumnProvider(Project projectToUse)
	{
		super(projectToUse);
	}
	
	@Override
	public CodeList getColumnCodesToShow() throws Exception
	{
		CodeList columnCodes = new CodeList();		
		columnCodes.add(WorkPlanColumnConfigurationQuestion.META_ANALYSIS_WORK_UNITS_COLUMN_CODE);
		columnCodes.add(WorkPlanColumnConfigurationQuestion.META_ANALYSIS_EXPENSES_CODE);
		columnCodes.add(WorkPlanColumnConfigurationQuestion.META_ANALYSIS_BUDGET_DETAILS_COLUMN_CODE);
		
		return columnCodes;
	}

	@Override
	public CodeList getRowCodesToShow() throws Exception
	{
		return new CodeList(new String[] {
				ProjectResource.OBJECT_NAME,
				AccountingCode.OBJECT_NAME,
				FundingSource.OBJECT_NAME,
				BudgetCategoryOne.OBJECT_NAME,
				BudgetCategoryTwo.OBJECT_NAME,
		});
	}
	
	@Override
	public boolean shouldIncludeResultsChain() throws Exception
	{
		return true;
	}

	@Override
	public boolean shouldIncludeConceptualModelPage() throws Exception
	{
		return true;
	}

	@Override
	public boolean shouldIncludeEmptyRows()
	{
		return false;
	}
	
	@Override
	public boolean doObjectivesContainStrategies() throws Exception
	{
		return true;
	}
	
	@Override
	public CodeList getLevelTypeCodes()
	{
		//FIXME medium - remove try catch and refactor to make method throw
		try
		{
			return getProject().getViewData(WorkPlanView.getViewName()).getBudgetRollupReportLevelTypes();
		}
		catch (Exception e)
		{
			EAM.logException(e);
			return new CodeList();
		}
	}
	
	@Override
	public int getObjectType()
	{
		throw new RuntimeException("getObjectType() is not implemented.");
	}

	@Override
	protected String getObjectTypeName()
	{
		throw new RuntimeException("getObjectName() is not implemented.");
	}
}
