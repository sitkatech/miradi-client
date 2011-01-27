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

import org.miradi.dialogs.planning.WorkPlanCategoryTreeRowColumnProvider;
import org.miradi.objects.AccountingCode;
import org.miradi.objects.BudgetCategoryOne;
import org.miradi.objects.BudgetCategoryTwo;
import org.miradi.objects.FundingSource;
import org.miradi.objects.ProjectResource;
import org.miradi.project.Project;
import org.miradi.questions.WorkPlanColumnConfigurationQuestion;
import org.miradi.utils.CodeList;
import org.miradi.views.workplan.WorkPlanView;

public class AnalysisRowColumnProvider implements WorkPlanCategoryTreeRowColumnProvider
{ 
	public AnalysisRowColumnProvider(Project projectToUse)
	{
		project = projectToUse;
	}
	
	public CodeList getColumnCodesToShow()
	{
		CodeList columnCodes = new CodeList();		
		columnCodes.add(WorkPlanColumnConfigurationQuestion.META_ANALYSIS_WORK_UNITS_COLUMN_CODE);
		columnCodes.add(WorkPlanColumnConfigurationQuestion.META_ANALYSIS_EXPENSES_CODE);
		columnCodes.add(WorkPlanColumnConfigurationQuestion.META_ANALYSIS_BUDGET_DETAILS_COLUMN_CODE);
		
		return columnCodes;
	}

	public CodeList getRowCodesToShow()
	{
		return new CodeList(new String[] {
				ProjectResource.OBJECT_NAME,
				AccountingCode.OBJECT_NAME,
				FundingSource.OBJECT_NAME,
				BudgetCategoryOne.OBJECT_NAME,
				BudgetCategoryTwo.OBJECT_NAME,
		});
	}
	
	public boolean shouldIncludeResultsChain() throws Exception
	{
		return true;
	}

	public boolean shouldIncludeConceptualModelPage() throws Exception
	{
		return true;
	}

	public boolean shouldIncludeEmptyRows()
	{
		return false;
	}
	
	public CodeList getLevelTypeCodes() throws Exception
	{
		return getProject().getViewData(WorkPlanView.getViewName()).getBudgetRollupReportLevelTypes();
	}
	
	private Project getProject()
	{
		return project;
	}
	
	private Project project;
}
