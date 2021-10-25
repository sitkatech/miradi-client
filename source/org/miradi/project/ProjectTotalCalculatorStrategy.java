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
package org.miradi.project;

import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.BaseObject;
import org.miradi.objects.DiagramObject;
import org.miradi.objects.Strategy;
import org.miradi.questions.WorkPlanVisibleRowsQuestion;

import java.util.HashSet;

public class ProjectTotalCalculatorStrategy
{
	public ProjectTotalCalculatorStrategy(String workPlanBudgetModeToUse)
	{
		workPlanBudgetMode = workPlanBudgetModeToUse;
	}

	public ORefList getChildTaskRefs(BaseObject baseObject)
	{
		if (Strategy.is(baseObject))
		{
			Strategy strategy = (Strategy) baseObject;

			if (getWorkPlanBudgetMode().equals(WorkPlanVisibleRowsQuestion.SHOW_ALL_ROWS_CODE))
				return strategy.getChildTaskRefs();

			if (getWorkPlanBudgetMode().equals(WorkPlanVisibleRowsQuestion.SHOW_MONITORING_RELATED_ROWS_CODE))
				return strategy.getMonitoringActivityRefs();

			if (getWorkPlanBudgetMode().equals(WorkPlanVisibleRowsQuestion.SHOW_ACTION_RELATED_ROWS_CODE))
				return ORefList.subtract(strategy.getActivityRefs(), strategy.getMonitoringActivityRefs());
		}

		return baseObject.getChildTaskRefs();
	}

	public boolean shouldOnlyIncludeActionsData()
	{
		return getWorkPlanBudgetMode().equals(WorkPlanVisibleRowsQuestion.SHOW_ACTION_RELATED_ROWS_CODE);
	}

	public boolean shouldOnlyIncludeMonitoringData()
	{
		return getWorkPlanBudgetMode().equals(WorkPlanVisibleRowsQuestion.SHOW_MONITORING_RELATED_ROWS_CODE);
	}

	public HashSet<BaseObject> getMonitoringData(Project project) throws Exception
	{
		return getActionsData(project);
	}

	public HashSet<BaseObject> getMonitoringData(Project project, ORef diagramObjectRef) throws Exception
	{
		return getActionsData(project, diagramObjectRef);
	}

	public HashSet<BaseObject> getAllData(Project project) throws Exception
	{
		return getActionsData(project);
	}

	public HashSet<BaseObject> getAllData(Project project, ORef diagramObjectRef) throws Exception
	{
		return getActionsData(project, diagramObjectRef);
	}

	public HashSet<BaseObject> getActionsData(Project project) throws Exception
	{
		HashSet<BaseObject> nonDraftStrategies = new HashSet<BaseObject>();
		ORefList includedDiagramObjectRefs = getIncludedDiagramRefs(project);
		for (ORef diagramObjectRef : includedDiagramObjectRefs)
		{
			nonDraftStrategies.addAll(getActionsData(project, diagramObjectRef));
		}

		return nonDraftStrategies;
	}

	public HashSet<BaseObject> getActionsData(Project project, ORef diagramObjectRef) throws Exception
	{
		return getNonDraftStrategies(project, diagramObjectRef);
	}

	protected ORefList getIncludedDiagramRefs(Project project) throws Exception
	{
		ORefList diagramRefs = new ORefList();
		if (project.getMetadata().shouldIncludeConceptualModelPage())
			diagramRefs.addAll(project.getConceptualModelDiagramPool().getRefList());

		if (project.getMetadata().shouldIncludeResultsChain())
			diagramRefs.addAll(project.getResultsChainDiagramPool().getRefList());

		return diagramRefs;
	}

	private HashSet<BaseObject> getNonDraftStrategies(Project project, ORef diagramObjectRef) throws Exception
	{
		HashSet<BaseObject> nonDraftStrategies = new HashSet<BaseObject>();
		DiagramObject diagramObject = DiagramObject.findDiagramObject(project, diagramObjectRef);
		nonDraftStrategies.addAll(diagramObject.getNonDraftStrategies());
		return nonDraftStrategies;
	}

	public void setWorkPlanBudgetMode(String workPlanBudgetModeToUse)
	{
		workPlanBudgetMode = workPlanBudgetModeToUse;
	}

	public String getWorkPlanBudgetMode()
	{
		return workPlanBudgetMode;
	}

	private String workPlanBudgetMode;
}
