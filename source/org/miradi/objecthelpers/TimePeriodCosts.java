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

import org.miradi.main.EAM;
import org.miradi.objects.ProjectResource;
import org.miradi.project.Project;
import org.miradi.utils.OptionalDouble;

public class TimePeriodCosts
{
	public TimePeriodCosts()
	{
		expense = new OptionalDouble();
		
	    fundingSourceExpenseMap = new HashMap<ORef, OptionalDouble>();
		accountingCodeExpenseMap = new HashMap<ORef, OptionalDouble>();
		
		resourceUnitsMap = new HashMap<ORef, OptionalDouble>();
		fundingSourceWorkUnitMap = new HashMap<ORef, OptionalDouble>();
		accountingCodeWorkUnitMap = new HashMap<ORef, OptionalDouble>();
		
		updateWorkUnits();
	}
	
	public TimePeriodCosts(TimePeriodCosts timePeriodCostsToUse)
	{
		this();
		add(timePeriodCostsToUse);
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
			putResource(resourceRefToAdd, newUnit);
		}
		else
		{
			putResource(resourceRefToAdd, unitsToUse);
		}
	}
	
	private void putResource(ORef resourceRefToAdd,	OptionalDouble unitsToUse)
	{
		resourceUnitsMap.put(resourceRefToAdd, unitsToUse);
		updateWorkUnits();
	}
	
	public void removeResource(ORef resourceRefToRemove)
	{
		resourceUnitsMap.remove(resourceRefToRemove);
		updateWorkUnits();
	}
	
	private void updateWorkUnits()
	{
		workUnits = new OptionalDouble();
		Set<ORef> projectResourcRefs = resourceUnitsMap.keySet();
		for(ORef projectResourceRef : projectResourcRefs)
		{
			OptionalDouble units = resourceUnitsMap.get(projectResourceRef);
			workUnits = workUnits.add(units);
		}
	}
	
	private void addExpenses(OptionalDouble expenseToAdd)
	{
		expense = expense.add(expenseToAdd);
	}
	
	public void filterProjectResources(ORefSet projectResourceRefsToRetain)
	{
		if (projectResourceRefsToRetain.size() == 0)
			return;
		
		Set<ORef> refsToBeRemoved = getResourceRefSet();
		refsToBeRemoved.removeAll(projectResourceRefsToRetain);
		for(ORef projectResourceRefToRemove : refsToBeRemoved)
		{
			removeResource(projectResourceRefToRemove);
		}
	}
	
	public void setExpense(OptionalDouble expenseToUse)
	{
		expense = expenseToUse;
	}
	
	public void replaceEmptyExpenseValue(TimePeriodCosts timePeriodCosts)
	{
		if (getExpense().hasValue())
			throw new RuntimeException((EAM.text("Existing expense was not empty. value=" + expense.getValue())));
		
		setExpense(timePeriodCosts.getExpense());
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
			OptionalDouble costPerUnit = getCostPerUnit(projectToUse, projectResourceRef);
			OptionalDouble units = resourceUnitsMap.get(projectResourceRef);
			OptionalDouble multiplyValue = units.multiply(costPerUnit);
			resourcesTotalCost = resourcesTotalCost.add(multiplyValue);
		}
		
		return resourcesTotalCost;
	}

	private OptionalDouble getCostPerUnit(Project projectToUse,	ORef projectResourceRef)
	{
		if (projectResourceRef.isInvalid())
			return new OptionalDouble(0.0);
		
		ProjectResource projectResource = ProjectResource.find(projectToUse, projectResourceRef);
		return new OptionalDouble(projectResource.getCostPerUnit());
	}
	
	public OptionalDouble getResourcesTotalUnits()
	{
		return workUnits;
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
	
	private HashMap<ORef, OptionalDouble> getResourceUnitsMap()
	{
		return new HashMap(resourceUnitsMap);
	}
	
	public Set<ORef> getResourceRefSet()
	{
		return getResourceUnitsMap().keySet();
	}
	
	public void mergeAllTimePeriodCosts(TimePeriodCosts timePeriodCostsToMergeAdd)
	{
		addExpenses(timePeriodCostsToMergeAdd.getExpense());
		mergeAllProjectResourcesInPlace(timePeriodCostsToMergeAdd);
	}
	
	public void mergeAllProjectResourcesInPlace(TimePeriodCosts timePeriodCostsToMergeAdd)
	{
		Set<ORef> keysToMerge = timePeriodCostsToMergeAdd.getResourceRefSet();
		for(ORef refToMerge : keysToMerge)
		{
			OptionalDouble thisWorkUnits = timePeriodCostsToMergeAdd.getUnits(refToMerge);
			thisWorkUnits = thisWorkUnits.add(getUnits(refToMerge));
			
			putResource(refToMerge, thisWorkUnits);
		}
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
	
	public void removeProjectResources(ORef projectResourceRefToRetain)
	{
		ORefSet projectResourcesToRemove = new ORefSet();
		Set<ORef> projectResourceRefs = getResourceRefSet();
		for(ORef projectResourceRef : projectResourceRefs)
		{
			if (!projectResourceRef.equals(projectResourceRefToRetain))
				projectResourcesToRemove.add(projectResourceRef);
		}
		
		for(ORef projectResourceRefsToRemove : projectResourcesToRemove)
		{
			removeResource(projectResourceRefsToRemove);
		}
	}

	private OptionalDouble expense;
	private OptionalDouble workUnits;
	
	HashMap<ORef, OptionalDouble> fundingSourceExpenseMap;
	HashMap<ORef, OptionalDouble> accountingCodeExpenseMap;
	
	private HashMap<ORef, OptionalDouble> resourceUnitsMap;
	HashMap<ORef, OptionalDouble> fundingSourceWorkUnitMap;
	HashMap<ORef, OptionalDouble> accountingCodeWorkUnitMap;
	
}
