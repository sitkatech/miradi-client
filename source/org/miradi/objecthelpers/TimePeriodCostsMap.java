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
package org.miradi.objecthelpers;

import java.util.HashMap;
import java.util.Set;

import org.miradi.project.Project;
import org.miradi.utils.DateRange;
import org.miradi.utils.OptionalDouble;

public class TimePeriodCostsMap
{
	public TimePeriodCostsMap()
	{
		data = new HashMap<DateUnit, TimePeriodCosts>();
	}
	
	public TimePeriodCostsMap(TimePeriodCostsMap timePeriodCostsMap)
	{
		this();
		mergeAll(timePeriodCostsMap);
	}
	
	public void add(DateUnit dateUnit, TimePeriodCosts timePeriodCosts)
	{
		data.put(dateUnit, new TimePeriodCosts(timePeriodCosts));
	}
	
	public TimePeriodCosts getTimePeriodCostsForSpecificDateUnit(DateUnit dateUnitToUse)
	{
		return data.get(dateUnitToUse);
	}
	
	private TimePeriodCosts getSafeTimePeriodCostsForSpecificDateUnit(DateUnit dateUnitToUse)
	{
		TimePeriodCosts timePeriodCosts = getTimePeriodCostsForSpecificDateUnit(dateUnitToUse);
		if(timePeriodCosts == null)
			return new TimePeriodCosts();
		
		return timePeriodCosts;
	}
	
	public OptionalDouble calculateTotalBudgetCost(Project project) throws Exception
	{
		final TimePeriodCosts totalTimePeriodCosts = calculateTimePeriodCosts(new DateUnit());
		return totalTimePeriodCosts.calculateTotalCost(project);
	}
	
	public TimePeriodCosts calculateTimePeriodCosts(DateUnit dateUnitToUse) throws Exception
	{
		TimePeriodCosts totalTimePeriodCosts = new TimePeriodCosts();
		Set<DateUnit> dateUnitKeys = data.keySet();
		for(DateUnit dateUnitKey : dateUnitKeys)
		{
			TimePeriodCosts timePeriodCosts = data.get(dateUnitKey);
			if (dateUnitToUse.contains(dateUnitKey))
				totalTimePeriodCosts.add(timePeriodCosts);
		}
		
		return totalTimePeriodCosts;
	}
	
	public void mergeAll(TimePeriodCostsMap timePeriodCostsMapToMerge)
	{
		Set<DateUnit> keysToMerge = timePeriodCostsMapToMerge.data.keySet();
		for(DateUnit dateUnitToMerge : keysToMerge)
		{
			TimePeriodCosts timePeriodCostsToMerge = timePeriodCostsMapToMerge.data.get(dateUnitToMerge);
			mergeAllTimePeriodCosts(dateUnitToMerge, timePeriodCostsToMerge);
		}
	}
	
	private void mergeAllTimePeriodCosts(DateUnit dateUnit, TimePeriodCosts timePeriodCostsToMerge)
	{
		TimePeriodCosts existing = getTimePeriodCostsForSpecificDateUnit(dateUnit);
		if(existing != null)
			existing.mergeAllTimePeriodCosts(timePeriodCostsToMerge);
		else
			add(dateUnit, timePeriodCostsToMerge);
	}
		
	public void mergeNonConflicting(TimePeriodCostsMap timePeriodCostsMapToMerge) throws Exception
	{
		TimePeriodCostsMap snapShot = new TimePeriodCostsMap(this);
		Set<DateUnit> keysToMerge = timePeriodCostsMapToMerge.getDateUnitTimePeriodCostsMap().keySet();
		for(DateUnit dateUnitToMerge : keysToMerge)
		{
			TimePeriodCosts timePeriodCostsToMerge = timePeriodCostsMapToMerge.getTimePeriodCostsForSpecificDateUnit(dateUnitToMerge);
			TimePeriodCosts snapShotTimePeriodCosts = snapShot.calculateTimePeriodCosts(dateUnitToMerge);
			TimePeriodCosts existing = getSafeTimePeriodCostsForSpecificDateUnit(dateUnitToMerge);
			existing.mergeNonConflicting(snapShotTimePeriodCosts, timePeriodCostsToMerge);
			
			add(dateUnitToMerge, existing);
		}
	}

	public DateRange getRolledUpDateRange(DateRange projectDateRange) throws Exception
	{
		return getRolledUpDateRange(projectDateRange, new ORefSet());
	}
	
	public DateRange getRolledUpDateRange(DateRange projectDateRange, ORefSet resourcesToFilterBy) throws Exception
	{
		DateRange combinedDateRange = null;
		Set<DateUnit> keys = data.keySet();
		for(DateUnit dateUnit : keys)
		{
			TimePeriodCosts timePeriodCosts = data.get(dateUnit);
			Set<ORef> resourceRefSet = timePeriodCosts.getResourceRefSet();
			ORefSet resourcesRefs = new ORefSet(resourceRefSet);
			if (resourcesToFilterBy.containsAny(resourcesRefs) || resourcesToFilterBy.isEmpty())
				combinedDateRange = DateRange.combine(combinedDateRange, convertToDateRange(dateUnit, projectDateRange));
		}
		
		return combinedDateRange;
	}
	
	private DateRange convertToDateRange(DateUnit dateUnit, DateRange projectDateRange) throws Exception
	{
		if (dateUnit.isProjectTotal())
			return projectDateRange;
		
		return dateUnit.asDateRange();
	} 
			
	public ORefSet getAllProjectResourceRefs()
	{
		ORefSet allProjectResourceRefs = new ORefSet();
		Set<DateUnit> keys = data.keySet();
		for(DateUnit dateUnit : keys)
		{
			final TimePeriodCosts timePeriodCosts = data.get(dateUnit);
			allProjectResourceRefs.addAll(timePeriodCosts.getResourceRefSet());
		}
		
		return allProjectResourceRefs;
	}
	
	public void divideBy(OptionalDouble divideByValue)
	{
		Set<DateUnit> keys = data.keySet();
		for(DateUnit dateUnit : keys)
		{
			TimePeriodCosts timePeriodCosts = data.get(dateUnit);
			timePeriodCosts.divideBy(divideByValue);
		}
	}

	public boolean isEmpty()
	{
		return data.isEmpty();
	}
	
	public int size()
	{
		return data.size();
	}

	private HashMap<DateUnit, TimePeriodCosts> getDateUnitTimePeriodCostsMap()
	{
		return new HashMap(data);
	}

	private HashMap<DateUnit, TimePeriodCosts> data;
}
