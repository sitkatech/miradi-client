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
	public TimePeriodCosts(OptionalDouble expenseToUse)
	{
		this();
		setExpense(expenseToUse);
	}
	
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
			addResource(resourceUnitsMapToAdd, resourceRefToAdd);			
		}
	}

	private void addResource(HashMap<ORef, OptionalDouble> resourceUnitsMapToAdd, ORef resourceRefToAdd)
	{
		final OptionalDouble unitsToUse = resourceUnitsMapToAdd.get(resourceRefToAdd);
		if (resourceUnitsMap.containsKey(resourceRefToAdd))
		{
			OptionalDouble thisUnit = resourceUnitsMap.get(resourceRefToAdd);
			OptionalDouble newUnit = thisUnit.add(unitsToUse);
			resourceUnitsMap.put(resourceRefToAdd, newUnit);
		}
		else
		{
			addResourceCost(resourceRefToAdd, unitsToUse);
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
	
	public void addResourceCost(ORef resourceRef, OptionalDouble unitsToUse)
	{
		resourceUnitsMap.put(resourceRef, unitsToUse);
	}
	
	public OptionalDouble calculateTotal(Project projectToUse)
	{
		return calculateProjectResources(projectToUse).add(getExpense());
	}
	
	public OptionalDouble calculateProjectResources(Project projectToUse)
	{
		OptionalDouble projectResourceTotalUnits = new OptionalDouble();
		Set<ORef> projectResourcRefs = resourceUnitsMap.keySet();
		for(ORef projectResourceRef : projectResourcRefs)
		{
			ProjectResource projectResource = ProjectResource.find(projectToUse, projectResourceRef);
			Double costPerUnit = projectResource.getCostPerUnit();
			OptionalDouble units = resourceUnitsMap.get(projectResourceRef);
			OptionalDouble multiplyValue = units.multiplyValue(costPerUnit);
			projectResourceTotalUnits = projectResourceTotalUnits.add(multiplyValue);
		}
		
		return projectResourceTotalUnits;
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
