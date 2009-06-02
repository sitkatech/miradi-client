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

import org.miradi.objects.ProjectResource;
import org.miradi.project.Project;
import org.miradi.utils.OptionalDouble;

public class TimePeriodCosts
{
	public TimePeriodCosts()
	{
		expense = new OptionalDouble();
		resourceUnitsMap = new HashMap<ORef, OptionalDouble>();
	}
	
	public void add(TimePeriodCosts timePeriodCosts)
	{
		addExpenses(timePeriodCosts.getExpense());
		addResources(timePeriodCosts.getResourceUnitsMap());
	}

	private void addResources(HashMap<ORef, OptionalDouble> resourceUnitsMapToAdd)
	{
		Set<ORef> resourceRefKeysToAdd = resourceUnitsMapToAdd.keySet();
		for(ORef resourceRefToAdd : resourceRefKeysToAdd)
		{
			final OptionalDouble unitsToUse = resourceUnitsMapToAdd.get(resourceRefToAdd);
			addResource(resourceRefToAdd, unitsToUse);			
		}
	}

	public void addResource(ORef resourceRefToAdd,	final OptionalDouble unitsToUse)
	{
		if (resourceUnitsMap.containsKey(resourceRefToAdd))
		{
			OptionalDouble thisUnit = resourceUnitsMap.get(resourceRefToAdd);
			OptionalDouble newUnit = thisUnit.add(unitsToUse);
			resourceUnitsMap.put(resourceRefToAdd, newUnit);
		}
		else
		{
			resourceUnitsMap.put(resourceRefToAdd, unitsToUse);
		}
	}

	private void addExpenses(OptionalDouble expenseToAdd)
	{
		expense = expense.add(expenseToAdd);
	}
	
	public void setExpense(OptionalDouble expenseToUse)
	{
		expense = expenseToUse;
	}
	
	public OptionalDouble calculateTotalCost(Project projectToUse)
	{
		final OptionalDouble expenseToAdd = getExpense();
		final OptionalDouble totalResourceCost = calculateResourcesTotalCost(projectToUse);
		
		return totalResourceCost.add(expenseToAdd);
	}
	
	private OptionalDouble calculateResourcesTotalCost(Project projectToUse)
	{
		OptionalDouble resourcesTotalCost = new OptionalDouble();
		Set<ORef> projectResourcRefs = resourceUnitsMap.keySet();
		for(ORef projectResourceRef : projectResourcRefs)
		{
			ProjectResource projectResource = ProjectResource.find(projectToUse, projectResourceRef);
			Double costPerUnit = projectResource.getCostPerUnit();
			OptionalDouble units = resourceUnitsMap.get(projectResourceRef);
			OptionalDouble multiplyValue = units.multiplyValue(costPerUnit);
			resourcesTotalCost = resourcesTotalCost.add(multiplyValue);
		}
		
		return resourcesTotalCost;
	}
	
	public OptionalDouble calculateResourcesTotalUnits()
	{
		OptionalDouble resourcesTotalCost = new OptionalDouble();
		Set<ORef> projectResourcRefs = resourceUnitsMap.keySet();
		for(ORef projectResourceRef : projectResourcRefs)
		{
			OptionalDouble units = resourceUnitsMap.get(projectResourceRef);
			resourcesTotalCost = resourcesTotalCost.add(units);
		}
		
		return resourcesTotalCost;
	}
	
	@Override
	public boolean equals(Object rawOther)
	{
		if (! (rawOther instanceof TimePeriodCosts))
			return false;
		
		TimePeriodCosts other = (TimePeriodCosts) rawOther;
		if (!other.getExpense().equals(getExpense()))
			return false;
		
		return other.resourceUnitsMap.equals(resourceUnitsMap);
	}
	
	@Override
	public int hashCode()
	{
		return expense.hashCode() + resourceUnitsMap.hashCode();
	}
	
	public OptionalDouble getUnits(ORef resourceRef)
	{
		if (!resourceUnitsMap.containsKey(resourceRef))
			return new OptionalDouble();
		
		return resourceUnitsMap.get(resourceRef);
	}
	
	public OptionalDouble getExpense()
	{
		return expense;
	}
	
	public HashMap<ORef, OptionalDouble> getResourceUnitsMap()
	{
		return new HashMap(resourceUnitsMap);
	}
	
	public Set<ORef> getResourceRefSet()
	{
		return getResourceUnitsMap().keySet();
	}
	
	public void mergeAddTimePeriodCosts(TimePeriodCosts timePeriodCostsToMergeAdd)
	{
		addExpenses(timePeriodCostsToMergeAdd.getExpense());
		mergeAddProjectResourcesInPlace(timePeriodCostsToMergeAdd.getResourceUnitsMap());
	}
	
	private void mergeAddProjectResourcesInPlace(HashMap<ORef, OptionalDouble> resourceUnitsMapToMerge)
	{
		Set<ORef> keysToMerge = resourceUnitsMapToMerge.keySet();
		for(ORef refToMerge : keysToMerge)
		{
			OptionalDouble workUnits = resourceUnitsMapToMerge.get(refToMerge);
			workUnits = workUnits.add(getUnits(refToMerge));
			
			resourceUnitsMap.put(refToMerge, workUnits);
		}
	}

	public void setResourceUnitsMap(HashMap<ORef, OptionalDouble> resourceUnitsMapToUse)
	{
		resourceUnitsMap = resourceUnitsMapToUse;
	}
	
	@Override
	public String toString()
	{
		String asString = "";
		if (expense.hasValue())
			asString = "expense = " + expense.getValue() + "\n";
		
		Set<ORef> refs = resourceUnitsMap.keySet();
		for(ORef ref : refs)
		{
			asString += "resourceRef = " + ref + " units = " + resourceUnitsMap.get(ref) + "\n";
		}
		
		return asString;
	}
	
	private OptionalDouble expense;
	private HashMap<ORef, OptionalDouble> resourceUnitsMap;
}
