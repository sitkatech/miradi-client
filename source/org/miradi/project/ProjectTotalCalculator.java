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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.miradi.main.CommandExecutedEvent;
import org.miradi.main.CommandExecutedListener;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ORefSet;
import org.miradi.objecthelpers.TimePeriodCostsMap;
import org.miradi.objects.BaseObject;
import org.miradi.objects.DiagramObject;
import org.miradi.objects.Factor;
import org.miradi.objects.Indicator;
import org.miradi.questions.WorkPlanVisibleRowsQuestion;

public class ProjectTotalCalculator implements CommandExecutedListener
{
	public ProjectTotalCalculator(Project projectToUse)
	{
		project = projectToUse;
		modeToTimePeriodPlannedCostsMapMap = new HashMap<String, TimePeriodCostsMap>();
		modeToTimePeriodAssignedCostsMapMap = new HashMap<String, TimePeriodCostsMap>();
	}
	
	public void enable()
	{
		getProject().addCommandExecutedListener(this);
	}
	
	public void disable()
	{
		getProject().removeCommandExecutedListener(this);
	}
	
	public void commandExecuted(CommandExecutedEvent event)
	{
		// TODO: Only clear the cache when it actually needs it
		clear();
	}
	
	public void clear()
	{
		modeToTimePeriodAssignedCostsMapMap.clear();
		modeToTimePeriodPlannedCostsMapMap.clear();
	}

	public TimePeriodCostsMap calculateProjectPlannedTotals(String mode) throws Exception
	{
		if(!modeToTimePeriodPlannedCostsMapMap.containsKey(mode))
			modeToTimePeriodPlannedCostsMapMap.put(mode, computeTotalTimePeriodPlannedCostsMap(mode));
		
		return modeToTimePeriodPlannedCostsMapMap.get(mode);
	}

	public TimePeriodCostsMap calculateProjectAssignedTotals() throws Exception
	{
		return calculateProjectAssignedTotals(WorkPlanVisibleRowsQuestion.SHOW_ALL_ROWS_CODE);
	}

	public TimePeriodCostsMap calculateProjectAssignedTotals(String mode) throws Exception
	{
		if(!modeToTimePeriodAssignedCostsMapMap.containsKey(mode))
			modeToTimePeriodAssignedCostsMapMap.put(mode, computeTotalTimePeriodAssignedCostsMap(mode));

		return modeToTimePeriodAssignedCostsMapMap.get(mode);
	}

	public TimePeriodCostsMap calculateDiagramObjectPlannedTotals(DiagramObject baseObject, String mode) throws Exception
	{
		if (shouldOnlyIncludeMonitoringData(mode))
			return getTotalTimePeriodPlannedCostsMap(getIncludedDiagramIndicators(baseObject.getRef()));

		if (shouldOnlyIncludeActionsData(mode))
			return getTotalTimePeriodPlannedCostsMap(getIncludedNonDraftStrategies(baseObject.getRef()));

		TimePeriodCostsMap merged = new TimePeriodCostsMap();
		merged.mergeAll(getTotalTimePeriodPlannedCostsMap(getIncludedDiagramIndicators(baseObject.getRef())));
		merged.mergeAll(getTotalTimePeriodPlannedCostsMap(getIncludedNonDraftStrategies(baseObject.getRef())));

		return merged;
	}

	public TimePeriodCostsMap calculateDiagramObjectAssignedTotals(DiagramObject baseObject, String mode) throws Exception
	{
		if (shouldOnlyIncludeMonitoringData(mode))
			return getTotalTimePeriodAssignedCostsMap(getIncludedDiagramIndicators(baseObject.getRef()));

		if (shouldOnlyIncludeActionsData(mode))
			return getTotalTimePeriodAssignedCostsMap(getIncludedNonDraftStrategies(baseObject.getRef()));

		TimePeriodCostsMap merged = new TimePeriodCostsMap();
		merged.mergeAll(getTotalTimePeriodAssignedCostsMap(getIncludedDiagramIndicators(baseObject.getRef())));
		merged.mergeAll(getTotalTimePeriodAssignedCostsMap(getIncludedNonDraftStrategies(baseObject.getRef())));

		return merged;
	}

	private TimePeriodCostsMap computeTotalTimePeriodPlannedCostsMap(String mode)	throws Exception
	{
		if (shouldOnlyIncludeMonitoringData(mode))
			return getTotalTimePeriodPlannedCostsMap(getIncludedDiagramIndicators());
		
		if (shouldOnlyIncludeActionsData(mode))
			return getTotalTimePeriodPlannedCostsMap(getIncludedNonDraftStrategies());
		
		TimePeriodCostsMap merged = new TimePeriodCostsMap();
		merged.mergeAll(calculateProjectPlannedTotals(WorkPlanVisibleRowsQuestion.SHOW_ACTION_RELATED_ROWS_CODE));
		merged.mergeAll(calculateProjectPlannedTotals(WorkPlanVisibleRowsQuestion.SHOW_MONITORING_RELATED_ROWS_CODE));
		
		return merged;
	}

	private TimePeriodCostsMap computeTotalTimePeriodAssignedCostsMap(String mode)	throws Exception
	{
		if (shouldOnlyIncludeMonitoringData(mode))
			return getTotalTimePeriodAssignedCostsMap(getIncludedDiagramIndicators());

		if (shouldOnlyIncludeActionsData(mode))
			return getTotalTimePeriodAssignedCostsMap(getIncludedNonDraftStrategies());

		TimePeriodCostsMap merged = new TimePeriodCostsMap();
		merged.mergeAll(calculateProjectAssignedTotals(WorkPlanVisibleRowsQuestion.SHOW_ACTION_RELATED_ROWS_CODE));
		merged.mergeAll(calculateProjectAssignedTotals(WorkPlanVisibleRowsQuestion.SHOW_MONITORING_RELATED_ROWS_CODE));

		return merged;
	}

	private boolean shouldOnlyIncludeActionsData(String mode)
	{
		return  mode.equals(WorkPlanVisibleRowsQuestion.SHOW_ACTION_RELATED_ROWS_CODE);
	}

	private boolean shouldOnlyIncludeMonitoringData(String mode)
	{
		return  mode.equals(WorkPlanVisibleRowsQuestion.SHOW_MONITORING_RELATED_ROWS_CODE);
	}

	private HashSet<BaseObject> getIncludedDiagramIndicators() throws Exception
	{
		HashSet<BaseObject> indicators = new HashSet<BaseObject>();
		ORefList diagramRefsToExtractIndicatorsFrom = getIncludedDiagramRefs();
		for (int index = 0; index < diagramRefsToExtractIndicatorsFrom.size(); ++index)
		{
			ORef diagramObjectRef = diagramRefsToExtractIndicatorsFrom.get(index);
			indicators.addAll(getIncludedDiagramIndicators(diagramObjectRef));
		}
		
		return indicators;
	}
	
	private HashSet<BaseObject> getIncludedDiagramIndicators(ORef diagramObjectRef) throws Exception
	{
		HashSet<BaseObject> indicators = new HashSet<BaseObject>();
		DiagramObject diagramObject = DiagramObject.findDiagramObject(getProject(), diagramObjectRef);
		Factor[] allDiagramFactors = diagramObject.getAllWrappedFactorsExcludingDraftStrategies();
		indicators.addAll(getAllIndicators(allDiagramFactors));
		return indicators;
	}

	private Set<Indicator> getAllIndicators(Factor[] allDiagramFactors)
	{
		ORefSet indicatorRefs = new ORefSet();
		for (int index = 0; index < allDiagramFactors.length; ++index)
		{
			indicatorRefs.addAll(allDiagramFactors[index].getDirectOrIndirectIndicatorRefSet());
		}
		
		HashSet<Indicator> indicators = new HashSet<Indicator>();
		for(ORef indicatorRef : indicatorRefs)
		{
			Indicator indicator = Indicator.find(getProject(), indicatorRef);
			indicators.add(indicator);
		}
		
		return indicators;
	}

	private TimePeriodCostsMap getTotalTimePeriodPlannedCostsMap(Set<BaseObject> baseObjects) throws Exception
	{
		TimePeriodCostsMap totalTimePeriodCostsMap = new TimePeriodCostsMap();
		for(BaseObject baseObject : baseObjects)
		{
			TimePeriodCostsMap indicatorTimePeriodCostsMap = baseObject.getTotalTimePeriodCostsMapForPlans();
			totalTimePeriodCostsMap.mergeAll(indicatorTimePeriodCostsMap);
		}
		
		return totalTimePeriodCostsMap;
	}

	private TimePeriodCostsMap getTotalTimePeriodAssignedCostsMap(Set<BaseObject> baseObjects) throws Exception
	{
		TimePeriodCostsMap totalTimePeriodCostsMap = new TimePeriodCostsMap();
		for(BaseObject baseObject : baseObjects)
		{
			TimePeriodCostsMap indicatorTimePeriodCostsMap = baseObject.getTotalTimePeriodCostsMapForAssignments();
			totalTimePeriodCostsMap.mergeAll(indicatorTimePeriodCostsMap);
		}

		return totalTimePeriodCostsMap;
	}

	private Set<BaseObject> getIncludedNonDraftStrategies() throws Exception
	{
		Set<BaseObject> nonDraftStrategies = new HashSet<BaseObject>();
		ORefList includedDiagramObjectRefs = getIncludedDiagramRefs();
		for (ORef diagramObjectRef : includedDiagramObjectRefs)
		{
			nonDraftStrategies.addAll(getIncludedNonDraftStrategies(diagramObjectRef));
		}
		
		return nonDraftStrategies; 
	}

	private Set<BaseObject> getIncludedNonDraftStrategies(ORef diagramObjectRef) throws Exception
	{
		Set<BaseObject> nonDraftStrategies = new HashSet<BaseObject>();
		DiagramObject diagramObject = DiagramObject.findDiagramObject(getProject(), diagramObjectRef);
		nonDraftStrategies.addAll(diagramObject.getNonDraftStrategies());
		return nonDraftStrategies;
	}

	private ORefList getIncludedDiagramRefs() throws Exception
	{
		ORefList diagramRefsToExtractIndicatorsFrom = new ORefList();
		if (getProject().getMetadata().shouldIncludeConceptualModelPage())
			diagramRefsToExtractIndicatorsFrom.addAll(getProject().getConceptualModelDiagramPool().getRefList());
		
		if (getProject().getMetadata().shouldIncludeResultsChain())
			diagramRefsToExtractIndicatorsFrom.addAll(getProject().getResultsChainDiagramPool().getRefList());
		
		return diagramRefsToExtractIndicatorsFrom;
	}

	private Project getProject()
	{
		return project;
	}
	
	private Project project;
	private HashMap<String, TimePeriodCostsMap> modeToTimePeriodAssignedCostsMapMap;
	private HashMap<String, TimePeriodCostsMap> modeToTimePeriodPlannedCostsMapMap;
}
