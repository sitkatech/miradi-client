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
import org.miradi.objecthelpers.ORefSet;
import org.miradi.objects.BaseObject;
import org.miradi.objects.DiagramObject;
import org.miradi.objects.Factor;
import org.miradi.objects.Indicator;
import org.miradi.questions.WorkPlanVisibleRowsQuestion;

import java.util.HashSet;
import java.util.Set;

public class ProjectTotalCalculatorStrategyDefault extends ProjectTotalCalculatorStrategy
{
	public ProjectTotalCalculatorStrategyDefault(String workPlanBudgetModeToUse)
	{
		super(workPlanBudgetModeToUse);
	}

	public ORefList getChildTaskRefs(BaseObject baseObject)
	{
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
		HashSet<BaseObject> indicators = new HashSet<BaseObject>();
		ORefList diagramRefsToExtractIndicatorsFrom = getIncludedDiagramRefs(project);
		for (int index = 0; index < diagramRefsToExtractIndicatorsFrom.size(); ++index)
		{
			ORef diagramObjectRef = diagramRefsToExtractIndicatorsFrom.get(index);
			indicators.addAll(getIncludedDiagramIndicators(project, diagramObjectRef));
		}

		return indicators;
	}

	public HashSet<BaseObject> getMonitoringData(Project project, ORef diagramObjectRef) throws Exception
	{
		return getIncludedDiagramIndicators(project, diagramObjectRef);
	}

	public HashSet<BaseObject> getAllData(Project project) throws Exception
	{
		HashSet<BaseObject> allData = new HashSet<>();
		allData.addAll(getMonitoringData(project));
		allData.addAll(getActionsData(project));
		return allData;
	}

	public HashSet<BaseObject> getAllData(Project project, ORef diagramObjectRef) throws Exception
	{
		HashSet<BaseObject> allData = new HashSet<>();
		allData.addAll(getMonitoringData(project, diagramObjectRef));
		allData.addAll(getActionsData(project, diagramObjectRef));
		return allData;
	}

	private HashSet<BaseObject> getIncludedDiagramIndicators(Project project, ORef diagramObjectRef) throws Exception
	{
		HashSet<BaseObject> indicators = new HashSet<BaseObject>();
		DiagramObject diagramObject = DiagramObject.findDiagramObject(project, diagramObjectRef);
		Factor[] allDiagramFactors = diagramObject.getAllWrappedFactorsExcludingDraftStrategies();
		indicators.addAll(getAllIndicators(project, allDiagramFactors));
		return indicators;
	}

	private Set<Indicator> getAllIndicators(Project project, Factor[] allDiagramFactors)
	{
		ORefSet indicatorRefs = new ORefSet();
		for (int index = 0; index < allDiagramFactors.length; ++index)
		{
			indicatorRefs.addAll(allDiagramFactors[index].getDirectOrIndirectIndicatorRefSet());
		}

		HashSet<Indicator> indicators = new HashSet<Indicator>();
		for(ORef indicatorRef : indicatorRefs)
		{
			Indicator indicator = Indicator.find(project, indicatorRef);
			indicators.add(indicator);
		}

		return indicators;
	}
}
