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
import org.miradi.objects.Factor;
import org.miradi.objects.Indicator;
import org.miradi.objects.ResultsChainDiagram;
import org.miradi.objects.Strategy;

public class ProjectTotalCalculator
{
	public ProjectTotalCalculator(Project projectToUse)
	{
		project = projectToUse;
	}
	
	public TimePeriodCostsMap calculateProjectTotals() throws Exception
	{
		Set allIndicators = getResultsChainIndicators();
		Set nonDraftStrategiesInResultsChains = getAllResultsChainNonDraftStrategies();
		
		TimePeriodCostsMap totalTimePeriodCostsMap = new TimePeriodCostsMap();
		totalTimePeriodCostsMap.mergeAll(getTotalTimePeriodCostsMap(allIndicators));
		totalTimePeriodCostsMap.mergeAll(getTotalTimePeriodCostsMap(nonDraftStrategiesInResultsChains));
		
		return totalTimePeriodCostsMap;
	}

	private HashSet<Indicator> getResultsChainIndicators()
	{
		HashSet<Indicator> resultsChainIndicators = new HashSet();
		ORefList resultsChainRefs = getProject().getResultsChainDiagramPool().getRefList();
		for (int index = 0; index < resultsChainRefs.size(); ++index)
		{
			ResultsChainDiagram resultsChain = ResultsChainDiagram.find(getProject(), resultsChainRefs.get(index));
			Factor[] allResultsChainFactors = resultsChain.getAllWrappedFactors();
			resultsChainIndicators.addAll(getAllIndicators(allResultsChainFactors));
		}
		
		return resultsChainIndicators;
	}
	
	private Set<Indicator> getAllIndicators(Factor[] allResultsChainFactors)
	{
		ORefSet indicatorRefs = new ORefSet();
		for (int index = 0; index < allResultsChainFactors.length; ++index)
		{
			indicatorRefs.addAllRefs(allResultsChainFactors[index].getIndicatorRefs());
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

	private Set<Strategy> getAllResultsChainNonDraftStrategies()
	{
		Set<Strategy> nonDraftStrategies = new HashSet();
		ORefList resultsChainRefs = getProject().getResultsChainDiagramPool().getRefList();
		for (int index = 0; index < resultsChainRefs.size(); ++index)
		{
			ResultsChainDiagram resultsChain = ResultsChainDiagram.find(getProject(), resultsChainRefs.get(index));
			nonDraftStrategies.addAll(getNonDraftStrategies(resultsChain));
		}
		
		return nonDraftStrategies; 
	}

	private Set<Strategy> getNonDraftStrategies(ResultsChainDiagram resultsChain)
	{
		Set<Strategy> nonDraftStrategies = new HashSet();
		Set<Factor> strategies = resultsChain.getFactors(Strategy.getObjectType());
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
