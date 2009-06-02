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

import org.miradi.utils.DateRange;
import org.miradi.utils.OptionalDouble;

public class TimePeriodCostsMap
{
	public TimePeriodCostsMap()
	{
		data = new HashMap<DateUnit, TimePeriodCosts>();
	}
	
	public void add(DateUnit dateUnit, TimePeriodCosts timePeriodCosts)
	{
		data.put(dateUnit, timePeriodCosts);
	}
	
	public TimePeriodCosts getTimePeriodCostsForSpecificDateUnit(DateUnit dateUnitToUse)
	{
		return data.get(dateUnitToUse);
	}
	
	public TimePeriodCosts getTotalCost(DateUnit dateUnitToUse) throws Exception
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
	
	public boolean containsSpecificDateUnit(DateUnit dateUnitToUse)
	{
		return data.containsKey(dateUnitToUse);
	}
	
	public void mergeAdd(TimePeriodCostsMap timePeriodCostsMapToMerge)
	{
		Set<DateUnit> keys = timePeriodCostsMapToMerge.data.keySet();
		for(DateUnit dateUnitKey : keys)
		{
			TimePeriodCosts timePeriodCosts = timePeriodCostsMapToMerge.data.get(dateUnitKey);
			mergeAddTimePeriodCosts(dateUnitKey, timePeriodCosts);
		}
	}
		
	public void mergeOverlay(TimePeriodCostsMap timePeriodCostsMap) throws Exception
	{
		Set<DateUnit> keys = timePeriodCostsMap.getDateUnitTimePeriodCostsMap().keySet();
		for(DateUnit dateUnit : keys)
		{
			removeEntriesForLargerDateUnitsThatContainThisOne(dateUnit);
			if(hasAnyEntriesWithin(dateUnit))
				continue;
			
			TimePeriodCosts timePeriodCosts = timePeriodCostsMap.getTimePeriodCostsForSpecificDateUnit(dateUnit);
			mergeOverlayTimePeriodCosts(dateUnit, timePeriodCosts);
		}	
	}
	
	private void removeEntriesForLargerDateUnitsThatContainThisOne(DateUnit dateUnit) throws Exception
	{
		if(dateUnit.isBlank())
			return;
		
		DateUnit larger = dateUnit.getSuperDateUnit();
		DateUnit dateUnitToRemove = new DateUnit();
		if(!larger.isBlank())
			dateUnitToRemove = larger;
		data.remove(dateUnitToRemove);
		removeEntriesForLargerDateUnitsThatContainThisOne(larger);
	}
	
	private boolean hasAnyEntriesWithin(DateUnit largerDateUnit) throws Exception
	{
		Set<DateUnit> dateUnits = data.keySet();
		for(DateUnit thisDateUnit : dateUnits)
		{
			if(largerDateUnit.equals(thisDateUnit))
				continue;
			
			if (largerDateUnit.asDateRange().contains(thisDateUnit.asDateRange()))
				return true;
		}
		
		return false;
	}
	
	private void mergeAddTimePeriodCosts(DateUnit dateUnit, TimePeriodCosts timePeriodCostsToMerge)
	{
		TimePeriodCosts existing = getTimePeriodCostsForSpecificDateUnit(dateUnit);
		if(existing != null)
			existing.mergeAddTimePeriodCosts(timePeriodCostsToMerge);
		else
			add(dateUnit, timePeriodCostsToMerge);
	}
	
	private void mergeOverlayTimePeriodCosts(DateUnit dateUnit, TimePeriodCosts timePeriodCosts)
	{
		TimePeriodCosts existing = getTimePeriodCostsForSpecificDateUnit(dateUnit);
		if(existing != null)
		{
			existing.setExpense(existing.getExpense().add(timePeriodCosts.getExpense()));
			HashMap<ORef, OptionalDouble> thisResourceUnitsMap = existing.getResourceUnitsMap();
			thisResourceUnitsMap.putAll(timePeriodCosts.getResourceUnitsMap());
			existing.setResourceUnitsMap(thisResourceUnitsMap);
		}
		else
		{
			add(dateUnit, timePeriodCosts);
		}
	}
	
	public DateRange getRolledUpDateRange(DateRange projectDateRange) throws Exception
	{
		DateRange combinedDateRange = null;
		Set<DateUnit> keys = data.keySet();
		for(DateUnit dateUnit : keys)
		{
			combinedDateRange = DateRange.combine(combinedDateRange, convertToDateRange(dateUnit, projectDateRange));
		}
		
		return combinedDateRange;
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

	private DateRange convertToDateRange(DateUnit dateUnit, DateRange projectDateRange) throws Exception
	{
		if (dateUnit.isBlank())
			return projectDateRange;
		
		return dateUnit.asDateRange();
	} 
		
	public boolean isEmpty()
	{
		return data.isEmpty();
	}
	
	public Object size()
	{
		return data.size();
	}

	private HashMap<DateUnit, TimePeriodCosts> getDateUnitTimePeriodCostsMap()
	{
		return new HashMap(data);
	}

	private HashMap<DateUnit, TimePeriodCosts> data;
}
