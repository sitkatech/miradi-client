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
import org.miradi.dialogs.planning.upperPanel.WorkPlanTreeTablePanel;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.CodeToCodeListMap;
import org.miradi.objects.TableSettings;
import org.miradi.project.Project;
import org.miradi.schemas.AccountingCodeSchema;
import org.miradi.schemas.BudgetCategoryOneSchema;
import org.miradi.schemas.BudgetCategoryTwoSchema;
import org.miradi.schemas.FundingSourceSchema;
import org.miradi.schemas.ProjectResourceSchema;
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
		return getBudgetColumnCodesFromTableSettingsMap();
	}

	@Override
	public CodeList getRowCodesToShow() throws Exception
	{
		return new CodeList(new String[] {
				ProjectResourceSchema.OBJECT_NAME,
				AccountingCodeSchema.OBJECT_NAME,
				FundingSourceSchema.OBJECT_NAME,
				BudgetCategoryOneSchema.OBJECT_NAME,
				BudgetCategoryTwoSchema.OBJECT_NAME,
		});
	}
	
	protected CodeList getBudgetColumnCodesFromTableSettingsMap()
	{
		try
		{
			TableSettings tableSettings = getWorkPlanTableSettings();
			CodeToCodeListMap tableSettingsMap = tableSettings.getTableSettingsMap();
			return tableSettingsMap.getCodeList(TableSettings.WORK_PLAN_BUDGET_COLUMNS_CODELIST_KEY);
		}
		catch (Exception e)
		{
			EAM.logException(e);
			return new CodeList();
		}
	}
	
	private TableSettings getWorkPlanTableSettings() throws Exception
	{
		return TableSettings.findOrCreate(getProject(), WorkPlanTreeTablePanel.getTabSpecificModelIdentifier());
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
		return true;
	}
	
	@Override
	public boolean doObjectivesContainStrategies() throws Exception
	{
		return true;
	}
	
	@Override
	public CodeList getLevelTypeCodes() throws Exception
	{
		return getProject().getViewData(WorkPlanView.getViewName()).getBudgetRollupReportLevelTypes();
	}
	
	@Override
	public int getObjectType()
	{
		throw new RuntimeException("getObjectType() is not implemented.");
	}

	@Override
	public String getObjectTypeName()
	{
		throw new RuntimeException("getObjectName() is not implemented.");
	}
}
