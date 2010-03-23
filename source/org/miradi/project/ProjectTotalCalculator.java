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

import java.util.HashSet;
import java.util.Set;

import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ORefSet;
import org.miradi.objecthelpers.TimePeriodCostsMap;
import org.miradi.objects.BaseObject;
import org.miradi.objects.DiagramObject;
import org.miradi.objects.Factor;
import org.miradi.objects.Indicator;
import org.miradi.objects.Strategy;

public class ProjectTotalCalculator
{
	public ProjectTotalCalculator(Project projectToUse)
	{
		project = projectToUse;
	}
	
	public TimePeriodCostsMap calculateProjectTotals() throws Exception
	{
		Set allIndicators = getIncludedDiagramIndicators();
		Set nonDraftStrategies = getIncludedNonDraftStrategies();
		
		TimePeriodCostsMap totalTimePeriodCostsMap = new TimePeriodCostsMap();
		totalTimePeriodCostsMap.mergeAll(getTotalTimePeriodCostsMap(allIndicators));
		totalTimePeriodCostsMap.mergeAll(getTotalTimePeriodCostsMap(nonDraftStrategies));
		
		return totalTimePeriodCostsMap;
	}

	private HashSet<Indicator> getIncludedDiagramIndicators() throws Exception
	{
		HashSet<Indicator> indicators = new HashSet();
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
		
		HashSet<Indicator> indicators = new HashSet();
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

	private Set<Strategy> getIncludedNonDraftStrategies() throws Exception
	{
		Set<Strategy> nonDraftStrategies = new HashSet();
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
		Set<Strategy> nonDraftStrategies = new HashSet();
		Set<Factor> strategies = diagramObject.getFactors(Strategy.getObjectType());
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
}
