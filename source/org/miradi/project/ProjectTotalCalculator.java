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
import org.miradi.objects.Strategy;
import org.miradi.questions.WorkPlanVisibleRowsQuestion;

public class ProjectTotalCalculator implements CommandExecutedListener
{
	public ProjectTotalCalculator(Project projectToUse)
	{
		project = projectToUse;
		modeToTimePeriodCostsMapMap = new HashMap<String, TimePeriodCostsMap>();
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
		modeToTimePeriodCostsMapMap.clear();
	}
	
	public TimePeriodCostsMap calculateProjectTotals(String mode) throws Exception
	{
		if(!modeToTimePeriodCostsMapMap.keySet().contains(mode))
			modeToTimePeriodCostsMapMap = computeTotalTimePeriodCostsMap(mode);
		
		return modeToTimePeriodCostsMapMap.get(mode);
	}

	public TimePeriodCostsMap calculateProjectTotals() throws Exception
	{
		return calculateProjectTotals(WorkPlanVisibleRowsQuestion.SHOW_ALL_ROWS_CODE);
	}

	private HashMap<String, TimePeriodCostsMap> computeTotalTimePeriodCostsMap(String mode)	throws Exception
	{
		Set<BaseObject> allIndicators = getIncludedDiagramIndicators();
		Set<BaseObject> nonDraftStrategies = getIncludedNonDraftStrategies();
		HashMap<String, TimePeriodCostsMap> map = new HashMap<String, TimePeriodCostsMap>();
		if (shouldIncludeIndicators(mode))
		{
			map.put(WorkPlanVisibleRowsQuestion.SHOW_MONITORING_RELATED_ROWS_CODE, getTotalTimePeriodCostsMap(allIndicators));
		}
		
		if (shouldIncludeNonDraftStrategies(mode))
		{
			map.put(WorkPlanVisibleRowsQuestion.SHOW_ACTION_RELATED_ROWS_CODE, getTotalTimePeriodCostsMap(nonDraftStrategies));
		}
		
		if (mode.equals(WorkPlanVisibleRowsQuestion.SHOW_ALL_ROWS_CODE))
		{
			TimePeriodCostsMap totalTimePeriodCostsMap = new TimePeriodCostsMap();
			Set<String> modesAsKeys = map.keySet();
			for (String budgetModeAsKey : modesAsKeys)
			{
				totalTimePeriodCostsMap.mergeAll(map.get(budgetModeAsKey));
			}
						
			map.put(mode, totalTimePeriodCostsMap);
		}
		
		return map;
	}

	private boolean shouldIncludeNonDraftStrategies(String mode)
	{
		if (mode.equals(WorkPlanVisibleRowsQuestion.SHOW_ALL_ROWS_CODE))
			return true;
		
		return  mode.equals(WorkPlanVisibleRowsQuestion.SHOW_ACTION_RELATED_ROWS_CODE);
	}

	private boolean shouldIncludeIndicators(String mode)
	{
		if (mode.equals(WorkPlanVisibleRowsQuestion.SHOW_ALL_ROWS_CODE))
			return true;
		
		return  mode.equals(WorkPlanVisibleRowsQuestion.SHOW_MONITORING_RELATED_ROWS_CODE);
	}

	private HashSet<BaseObject> getIncludedDiagramIndicators() throws Exception
	{
		HashSet<BaseObject> indicators = new HashSet<BaseObject>();
		ORefList diagramRefsToExtractIndicatorsFrom = getIncludedDiagramRefs();
		for (int index = 0; index < diagramRefsToExtractIndicatorsFrom.size(); ++index)
		{
			DiagramObject diagramObject = DiagramObject.findDiagramObject(getProject(), diagramRefsToExtractIndicatorsFrom.get(index));
			Factor[] allDiagramFactors = diagramObject.getAllWrappedFactors();
			indicators.addAll(getAllIndicators(allDiagramFactors));
		}
		
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

	private TimePeriodCostsMap getTotalTimePeriodCostsMap(Set<BaseObject> baseObjects) throws Exception
	{
		TimePeriodCostsMap totalTimePeriodCostsMap = new TimePeriodCostsMap();
		for(BaseObject baseObject : baseObjects)
		{
			TimePeriodCostsMap indicatorTimePeriodCostsMap = baseObject.getTotalTimePeriodCostsMap();
			totalTimePeriodCostsMap.mergeAll(indicatorTimePeriodCostsMap);
		}
		
		return totalTimePeriodCostsMap;
	}

	private Set<BaseObject> getIncludedNonDraftStrategies() throws Exception
	{
		Set<BaseObject> nonDraftStrategies = new HashSet<BaseObject>();
		ORefList diagramRefsToExtractIndicatorsFrom = getIncludedDiagramRefs();
		for (int index = 0; index < diagramRefsToExtractIndicatorsFrom.size(); ++index)
		{
			DiagramObject diagramObject = DiagramObject.findDiagramObject(getProject(), diagramRefsToExtractIndicatorsFrom.get(index));
			nonDraftStrategies.addAll(getNonDraftStrategies(diagramObject));
		}
		
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

	private Set<Strategy> getNonDraftStrategies(DiagramObject diagramObject)
	{
		Set<Strategy> nonDraftStrategies = new HashSet<Strategy>();
		Set<Factor> strategies = diagramObject.getFactorsOfType(Strategy.getObjectType());
		for(Factor factor : strategies)
		{
			Strategy strategy = (Strategy) factor;
			if (strategy.isStatusReal())
				nonDraftStrategies.add(strategy);
		}
		
		return nonDraftStrategies;
	}
	
	private Project getProject()
	{
		return project;
	}
	
	private Project project;
	private HashMap<String, TimePeriodCostsMap> modeToTimePeriodCostsMapMap;
}
