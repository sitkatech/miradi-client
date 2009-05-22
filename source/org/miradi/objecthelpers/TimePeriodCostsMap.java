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

import org.miradi.utils.OptionalDouble;

public class TimePeriodCostsMap
{
	public TimePeriodCostsMap()
	{
		data = new HashMap<DateUnit, TimePeriodCosts>();
	}
	
	public TimePeriodCostsMap(DateUnit dateUnit, TimePeriodCosts timePeriodCosts)
	{
		this();
		add(dateUnit, timePeriodCosts);
	}
	
	public void add(DateUnit dateUnit, TimePeriodCosts timePeriodCosts)
	{
		data.put(dateUnit, timePeriodCosts);
	}
	
	public TimePeriodCosts getTimePeriodCostsForSpecificDateUnit(DateUnit dateUnitToUse)
	{
		return data.get(dateUnitToUse);
	}
	
	public boolean containsSpecificDateUnit(DateUnit dateUnitToUse)
	{
		return data.containsKey(dateUnitToUse);
	}
	
	public void mergeAdd(TimePeriodCostsMap timePeriodCostsMapToMerge, DateUnit dateUnit)
	{
		Set<DateUnit> keys = timePeriodCostsMapToMerge.data.keySet();
		for(DateUnit dateUnitKey : keys)
		{
			TimePeriodCosts timePeriodCosts = timePeriodCostsMapToMerge.data.get(dateUnitKey);
			mergeAddTimePeriodCosts(dateUnit, timePeriodCosts);
		}
	}
		
	public void mergeOverlay(TimePeriodCostsMap timePeriodCostsMap,	DateUnit projectDateUnit) throws Exception
	{
		Set<DateUnit> keys = timePeriodCostsMap.getDateUnitTimePeriodCostsMap().keySet();
		for(DateUnit dateUnit : keys)
		{
			removeEntriesForLargerDateUnitsThatContainThisOne(dateUnit, projectDateUnit);
			if(hasAnyEntriesWithin(dateUnit))
				continue;
			
			TimePeriodCosts timePeriodCosts = timePeriodCostsMap.getTimePeriodCostsForSpecificDateUnit(dateUnit);
			mergeOverlayTimePeriodCosts(dateUnit, timePeriodCosts);
		}	
	}
	
	private void removeEntriesForLargerDateUnitsThatContainThisOne(DateUnit dateUnit, DateUnit projectDateUnit) throws Exception
	{
		if(dateUnit.isBlank())
			return;
		
		DateUnit larger = dateUnit.getSuperDateUnit();
		DateUnit dateUnitToRemove = projectDateUnit;
		if(!larger.isBlank())
			dateUnitToRemove = larger;
		data.remove(dateUnitToRemove);
		removeEntriesForLargerDateUnitsThatContainThisOne(larger, projectDateUnit);
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
	
	public void mergeAddTimePeriodCosts(DateUnit dateUnit, TimePeriodCosts timePeriodCosts)
	{
		TimePeriodCosts existing = getTimePeriodCostsForSpecificDateUnit(dateUnit);
		if(existing != null)
		{
			existing.setExpense(existing.getExpense().add(timePeriodCosts.getExpense()));
			HashMap<ORef, OptionalDouble> merged = mergeAddProjectResources(existing.getResourceUnitsMap(), timePeriodCosts.getResourceUnitsMap());
			existing.setResourceUnitsMap(merged);
		}
		else
		{
			add(dateUnit, timePeriodCosts);
		}
	}

	private HashMap mergeAddProjectResources(HashMap<ORef, OptionalDouble> existingResourceUnitsMap, HashMap<ORef, OptionalDouble> resourceUnitsMapToMerge)
	{
		HashMap<ORef, OptionalDouble> mergedResourceWorkUnits = new HashMap();
		Set<ORef> keys = resourceUnitsMapToMerge.keySet();
		for(ORef projectResourceRef : keys)
		{
			OptionalDouble workUnitsForResource = resourceUnitsMapToMerge.get(projectResourceRef);
			if (existingResourceUnitsMap.containsKey(projectResourceRef))
				workUnitsForResource = workUnitsForResource.add(existingResourceUnitsMap.get(projectResourceRef));
			
			mergedResourceWorkUnits.put(projectResourceRef, workUnitsForResource);
		}
		
		return mergedResourceWorkUnits;
	}
	
	public void mergeOverlayTimePeriodCosts(DateUnit dateUnit, TimePeriodCosts timePeriodCosts)
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
