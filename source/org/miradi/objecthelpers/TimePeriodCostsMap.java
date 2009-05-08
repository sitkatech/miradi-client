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
		if (data.containsKey(dateUnitToUse))
			return data.get(dateUnitToUse);
		
		return new TimePeriodCosts();
	}
	
	public void mergeAdd(TimePeriodCostsMap timePeriodCostsMapToMerge, DateUnit dateUnit)
	{
		Set<DateUnit> keys = timePeriodCostsMapToMerge.data.keySet();
		for(DateUnit dateUnitKey : keys)
		{
			TimePeriodCosts timePeriodCosts = timePeriodCostsMapToMerge.data.get(dateUnitKey);
			addUnits(timePeriodCosts, dateUnit);
		}
	}
	
	private void addUnits(TimePeriodCosts timePeriodCosts, DateUnit dateUnit)
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

	private HashMap<DateUnit, TimePeriodCosts> data;
}
