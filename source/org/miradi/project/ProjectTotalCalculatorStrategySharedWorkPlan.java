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
package org.miradi.project;

import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Strategy;
import org.miradi.questions.SharedWorkPlanVisibleRowsQuestion;

import java.util.HashSet;

public class ProjectTotalCalculatorStrategySharedWorkPlan extends ProjectTotalCalculatorStrategy
{
	public ProjectTotalCalculatorStrategySharedWorkPlan(String getWorkPlanBudgetModeToUse)
	{
		super(getWorkPlanBudgetModeToUse);
	}

	public ORefList getChildTaskRefs(BaseObject baseObject)
	{
		if (Strategy.is(baseObject))
		{
			Strategy strategy = (Strategy) baseObject;

			if (getWorkPlanBudgetMode().equals(SharedWorkPlanVisibleRowsQuestion.SHOW_ALL_ROWS_CODE))
				return strategy.getChildTaskRefs();

			if (getWorkPlanBudgetMode().equals(SharedWorkPlanVisibleRowsQuestion.SHOW_MONITORING_RELATED_ROWS_CODE))
				return strategy.getMonitoringActivityRefs();

			if (getWorkPlanBudgetMode().equals(SharedWorkPlanVisibleRowsQuestion.SHOW_ACTION_RELATED_ROWS_CODE))
				return ORefList.subtract(strategy.getActivityRefs(), strategy.getMonitoringActivityRefs());
		}

		return new ORefList();
	}

	public boolean shouldOnlyIncludeActionsData()
	{
		return getWorkPlanBudgetMode().equals(SharedWorkPlanVisibleRowsQuestion.SHOW_ACTION_RELATED_ROWS_CODE);
	}

	public boolean shouldOnlyIncludeMonitoringData()
	{
		return getWorkPlanBudgetMode().equals(SharedWorkPlanVisibleRowsQuestion.SHOW_MONITORING_RELATED_ROWS_CODE);
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
}
