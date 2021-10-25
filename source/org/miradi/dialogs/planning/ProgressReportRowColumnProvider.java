/* 
Copyright 2005-2021, Foundations of Success, Bethesda, Maryland
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
package org.miradi.dialogs.planning;

import org.miradi.dialogs.planning.treenodes.AbstractPlanningTreeNode;
import org.miradi.objects.BaseObject;
import org.miradi.objects.PlanningTreeRowColumnProvider;
import org.miradi.objects.Task;
import org.miradi.questions.WorkPlanVisibleRowsQuestion;
import org.miradi.schemas.IndicatorSchema;
import org.miradi.schemas.IntermediateResultSchema;
import org.miradi.schemas.ObjectiveSchema;
import org.miradi.schemas.TaskSchema;
import org.miradi.utils.CodeList;

public class ProgressReportRowColumnProvider implements RowColumnProvider, PlanningTreeRowColumnProvider
{
	public CodeList getRowCodesToShow() throws Exception
	{
		return new CodeList(new String[] {
				IntermediateResultSchema.OBJECT_NAME,
				ObjectiveSchema.OBJECT_NAME,
				IndicatorSchema.OBJECT_NAME,
				TaskSchema.ACTIVITY_NAME,				
		});
	}

	public CodeList getColumnCodesToShow() throws Exception
	{
		return new CodeList(new String[]{
				BaseObject.PSEUDO_TAG_LATEST_PROGRESS_REPORT_CODE,
				BaseObject.PSEUDO_TAG_LATEST_PROGRESS_REPORT_DETAILS,
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
	
	public boolean doObjectivesContainStrategies() throws Exception
	{
		return true;
	}
	
	public boolean shouldPutTargetsAtTopLevelOfTree() throws Exception
	{
		return false;
	}

	public boolean shouldIncludeActivities()
	{
		return true;
	}

	public boolean shouldIncludeMonitoringActivities()
	{
		return true;
	}

	public String getRowTypeCodeForTask(Task task)
	{
		return task.getTypeName();
	}

	public boolean shouldBeVisible(AbstractPlanningTreeNode child) throws Exception
	{
		return getRowCodesToShow().contains(child.getObjectTypeName());
	}

	public String getWorkPlanBudgetMode() throws Exception
	{
		return WorkPlanVisibleRowsQuestion.SHOW_ALL_ROWS_CODE;
	}

	public String getDiagramFilter() throws Exception
	{
		return "";
	}
}
