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
import org.miradi.objects.DiagramObject;

import java.util.HashSet;

abstract public class ProjectTotalCalculatorStrategy
{
	public ProjectTotalCalculatorStrategy(String workPlanBudgetModeToUse)
	{
		workPlanBudgetMode = workPlanBudgetModeToUse;
	}

	public abstract ORefList getChildTaskRefs(BaseObject baseObject);

	public abstract boolean shouldOnlyIncludeActionsData();

	public abstract boolean shouldOnlyIncludeMonitoringData();

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

	public abstract HashSet<BaseObject> getMonitoringData(Project project) throws Exception;

	public abstract HashSet<BaseObject> getMonitoringData(Project project, ORef diagramObjectRef) throws Exception;

	public abstract HashSet<BaseObject> getAllData(Project project) throws Exception;

	public abstract HashSet<BaseObject> getAllData(Project project, ORef diagramObjectRef) throws Exception;

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
