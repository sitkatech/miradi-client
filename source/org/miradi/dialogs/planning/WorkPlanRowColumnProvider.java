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
package org.miradi.dialogs.planning;

import org.miradi.dialogs.planning.upperPanel.WorkPlanTreeTablePanel;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.StringStringMap;
import org.miradi.objects.BaseObject;
import org.miradi.objects.ConceptualModelDiagram;
import org.miradi.objects.Indicator;
import org.miradi.objects.ResultsChainDiagram;
import org.miradi.objects.Strategy;
import org.miradi.objects.TableSettings;
import org.miradi.objects.Task;
import org.miradi.project.Project;
import org.miradi.questions.CustomPlanningColumnsQuestion;
import org.miradi.questions.WorkPlanVisibleRowsQuestion;
import org.miradi.utils.CodeList;

public class WorkPlanRowColumnProvider extends AbstractPlanningTreeRowColumnProvider
{
	public WorkPlanRowColumnProvider(Project projectToUse)
	{
		super(projectToUse);
	}
	
	public boolean shouldIncludeResultsChain()
	{
		return true;
	}

	public boolean shouldIncludeConceptualModelPage()
	{
		return true;
	}
	
	public CodeList getColumnCodesToShow() throws Exception
	{
		CodeList columnCodesToShow = new CodeList(new String[] {
				BaseObject.PSEUDO_TAG_LATEST_PROGRESS_REPORT_CODE,
				CustomPlanningColumnsQuestion.META_WHO_TOTAL,
				BaseObject.PSEUDO_TAG_WHEN_TOTAL,
				});
		
		columnCodesToShow.addAll(getBudgetColumnCodesFromTableSettingsMap());
		
		return columnCodesToShow;
	}
	
	protected CodeList getBudgetColumnCodesFromTableSettingsMap()
	{
		try
		{
			TableSettings tableSettings = getWorkPlanTableSettings();
			StringStringMap tableSettingsMap = tableSettings.getTableSettingsMap();
			String codeListAsString = tableSettingsMap.get(TableSettings.WORK_PLAN_BUDGET_COLUMNS_CODELIST_KEY);

			return new CodeList(codeListAsString);
		}
		catch (Exception e)
		{
			EAM.logException(e);
			return new CodeList();
		}
	}

	public CodeList getRowCodesToShow() throws Exception
	{
		
		TableSettings tableSettings = getWorkPlanTableSettings();
		String code = tableSettings.getData(TableSettings.TAG_WORK_PLAN_VISIBLE_NODES_CODE);
		
		return getRowCodes(code);
	}

	protected CodeList getRowCodes(String code)
	{
		if (code.equals(WorkPlanVisibleRowsQuestion.SHOW_ALL_ROWS_CODE))
			return createAllRowCodeList();
		
		if (code.equals(WorkPlanVisibleRowsQuestion.SHOW_ACTION_RELATED_ROWS_CODE))
			return createActionRelatedRowCodeList();
		
		if (code.equals(WorkPlanVisibleRowsQuestion.SHOW_MONITORING_RELATED_ROWS_CODE))
			return createMonitoringRelatedRowCodeList();
		
		EAM.logDebug("No proper code found, returning empty row codes for work plan tree table");
		return new CodeList();
	}

	private CodeList createMonitoringRelatedRowCodeList()
	{
		return new CodeList(new String[] {
				ConceptualModelDiagram.OBJECT_NAME,
				ResultsChainDiagram.OBJECT_NAME,
				Indicator.OBJECT_NAME,
				Task.METHOD_NAME,
				Task.OBJECT_NAME,
				});
	}

	private CodeList createActionRelatedRowCodeList()
	{
		return new CodeList(new String[] {
				ConceptualModelDiagram.OBJECT_NAME,
				ResultsChainDiagram.OBJECT_NAME,
				Strategy.OBJECT_NAME,
				Task.ACTIVITY_NAME,
				Task.OBJECT_NAME,
				});
	}

	protected CodeList createAllRowCodeList()
	{
		return new CodeList(new String[] {
				ConceptualModelDiagram.OBJECT_NAME,
				ResultsChainDiagram.OBJECT_NAME,
				Strategy.OBJECT_NAME,
				Task.ACTIVITY_NAME,
				Indicator.OBJECT_NAME,
				Task.METHOD_NAME,
				Task.OBJECT_NAME,
				});
	}
	
	private TableSettings getWorkPlanTableSettings() throws Exception
	{
		return TableSettings.findOrCreate(getProject(), WorkPlanTreeTablePanel.getTabSpecificModelIdentifier());
	}
}
