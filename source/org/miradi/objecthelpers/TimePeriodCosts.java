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
import java.util.HashSet;
import java.util.Set;

import org.miradi.main.EAM;
import org.miradi.objects.FundingSource;
import org.miradi.objects.ProjectResource;
import org.miradi.project.Project;
import org.miradi.utils.OptionalDouble;

public class TimePeriodCosts
{
	public TimePeriodCosts()
	{
		totalExpenses = new OptionalDouble();
		totalWorkUnits = new OptionalDouble();
		
	    fundingSourceExpenseMap = new HashMap<ORef, OptionalDouble>();
		accountingCodeExpenseMap = new HashMap<ORef, OptionalDouble>();
		
		resourceWorkUnitMap = new HashMap<ORef, OptionalDouble>();
		fundingSourceWorkUnitMap = new HashMap<ORef, OptionalDouble>();
		accountingCodeWorkUnitMap = new HashMap<ORef, OptionalDouble>();
	}
	
	public TimePeriodCosts(TimePeriodCosts timePeriodCostsToUse)
	{
		this();
		add(timePeriodCostsToUse);
	}
	
	public TimePeriodCosts(ORef fundingSourceRef, OptionalDouble expenseToUse)
	{
		this();
		
		ensureFundingSource(fundingSourceRef);
		
		addExpensesToTotal(expenseToUse);
		addRefToMap(fundingSourceExpenseMap, fundingSourceRef, expenseToUse);
	}
	
	public TimePeriodCosts(ORef resourceRef, ORef fundingSourceRef,	OptionalDouble workUnits)
	{
		this();
		
		ensureCorrectRefTypes(resourceRef, fundingSourceRef);		
		
		addWorkUnitsToTotal(workUnits);
		addRefToMap(resourceWorkUnitMap, resourceRef, workUnits);
		addRefToMap(fundingSourceWorkUnitMap, fundingSourceRef, workUnits);
	}

	public void add(TimePeriodCosts timePeriodCosts)
	{
		addExpensesToTotal(timePeriodCosts);
		addMap(fundingSourceExpenseMap, timePeriodCosts.fundingSourceExpenseMap);
		
		addWorkUnitsToTotal(timePeriodCosts);
		addMap(resourceWorkUnitMap, timePeriodCosts.resourceWorkUnitMap);
		addMap(fundingSourceWorkUnitMap, timePeriodCosts.fundingSourceWorkUnitMap);
	}
	
	private void addMap(HashMap<ORef, OptionalDouble> mapToUpdate, HashMap<ORef, OptionalDouble> mapToAdd)
	{
		Set<ORef> refKeysToAdd = mapToAdd.keySet();
		for(ORef refToAdd : refKeysToAdd)
		{
			OptionalDouble workUnitsToUse = mapToAdd.get(refToAdd);
			addRefToMap(mapToUpdate, refToAdd, workUnitsToUse);			
		}
	}

	private void addRefToMap(HashMap<ORef, OptionalDouble> mapToUpdate, ORef refToAdd, OptionalDouble workUnitsToAdd)
	{
		OptionalDouble thisWorkUnits = getSafeValue(mapToUpdate, refToAdd);
		workUnitsToAdd = thisWorkUnits.add(workUnitsToAdd);

		mapToUpdate.put(refToAdd, workUnitsToAdd);
	}
	
	private void addWorkUnitsToTotal(TimePeriodCosts timePeriodCosts)
	{
		addWorkUnitsToTotal(timePeriodCosts.getTotalWorkUnits());
	}
	
	private void addWorkUnitsToTotal(OptionalDouble totalWorkUnitsToAdd)
	{
		totalWorkUnits = totalWorkUnits.add(totalWorkUnitsToAdd);	
	}
	
	public OptionalDouble getTotalWorkUnits()
	{
		return totalWorkUnits;
	}
	
	private void addExpensesToTotal(TimePeriodCosts timePeriodCostsToUse)
	{
		addExpensesToTotal(timePeriodCostsToUse.getExpense());
	}

	private void addExpensesToTotal(OptionalDouble expense)
	{
		totalExpenses = totalExpenses.add(expense);
	}
	
	public OptionalDouble getExpense()
	{
		return totalExpenses;
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
		Set<ORef> projectResourcRefs = resourceWorkUnitMap.keySet();
		for(ORef projectResourceRef : projectResourcRefs)
		{
			OptionalDouble costPerUnit = getCostPerUnit(projectToUse, projectResourceRef);
			OptionalDouble units = resourceWorkUnitMap.get(projectResourceRef);
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
	
	public OptionalDouble getResourceWorkUnits(ORef resourceRef)
	{
		return getSafeValue(resourceWorkUnitMap, resourceRef);
	}
	
	public OptionalDouble getFundingSourceWorkUnits(ORef fundingSourceRef)
	{
		return getSafeValue(fundingSourceWorkUnitMap, fundingSourceRef);
	}
	
	public OptionalDouble getFundingSourceExpenses(ORef fundingSourceRef)
	{
		return getSafeValue(fundingSourceExpenseMap, fundingSourceRef);
	}
	
	private OptionalDouble getSafeValue(HashMap<ORef, OptionalDouble> mapToExtractFrom, ORef refToExtract)
	{
		if (!mapToExtractFrom.containsKey(refToExtract))
			return new OptionalDouble();
		
		return mapToExtractFrom.get(refToExtract);
	}
	
	protected void mergeAllTimePeriodCosts(TimePeriodCosts timePeriodCostsToMergeAdd)
	{
		mergeAllExpenseMapsInPlace(timePeriodCostsToMergeAdd);
		mergeAllWorkUnitMapsInPlace(timePeriodCostsToMergeAdd);
	}

	private void mergeAllExpenseMapsInPlace(TimePeriodCosts timePeriodCostsToMergeAdd)
	{
		addExpensesToTotal(timePeriodCostsToMergeAdd);
		mergeMapInPlace(fundingSourceExpenseMap, timePeriodCostsToMergeAdd.fundingSourceExpenseMap);
	}
	
	public void mergeAllWorkUnitMapsInPlace(TimePeriodCosts timePeriodCostsToMerge)
	{
		addWorkUnitsToTotal(timePeriodCostsToMerge);
		mergeMapInPlace(resourceWorkUnitMap, timePeriodCostsToMerge.resourceWorkUnitMap);
		mergeMapInPlace(fundingSourceWorkUnitMap, timePeriodCostsToMerge.fundingSourceWorkUnitMap);
	}
	
	private void mergeMapInPlace(HashMap<ORef, OptionalDouble> mapToUpdate, HashMap<ORef, OptionalDouble> mapToMergeFrom)
	{
		Set<ORef> keysToMerge = mapToMergeFrom.keySet();
		for(ORef refToMerge : keysToMerge)
		{
			OptionalDouble valueToAdd = mapToMergeFrom.get(refToMerge);			
			addRefToMap(mapToUpdate, refToMerge, valueToAdd);
		}
	}
	
	protected void mergeNonConflicting(TimePeriodCosts snapShotTimePeriodCosts, TimePeriodCosts timePeriodCostsToMerge) throws Exception
	{
		if (!snapShotTimePeriodCosts.hasExpenseData())
			mergeAllExpenseMapsInPlace(timePeriodCostsToMerge);
		
		if (!snapShotTimePeriodCosts.hasTotalWorkUnitsData())
			mergeAllWorkUnitMapsInPlace(timePeriodCostsToMerge);
	}
	
	public void filterProjectResources(ORefSet projectResourceRefsToRetain)
	{
		if (projectResourceRefsToRetain.size() == 0)
			return;
		
		Set<ORef> refsToBeRemoved = getResourceRefSet();
		refsToBeRemoved.removeAll(projectResourceRefsToRetain);
		for(ORef projectResourceRefToRemove : refsToBeRemoved)
		{
			resourceWorkUnitMap.remove(projectResourceRefToRemove);
		}
		
		updateTotalWorkUnits();
	}
	
	private void updateTotalWorkUnits()
	{
		totalWorkUnits = new OptionalDouble();
		Set<ORef> resourceRefs = resourceWorkUnitMap.keySet();
		for(ORef  resourceRef: resourceRefs)
		{
			totalWorkUnits = totalWorkUnits.add(resourceWorkUnitMap.get(resourceRef));
		}
	}
	
	@Override
	public boolean equals(Object rawOther)
	{
		if (! (rawOther instanceof TimePeriodCosts))
			return false;
		
		TimePeriodCosts other = (TimePeriodCosts) rawOther;
		if (!other.getExpense().equals(getExpense()))
			return false;
		
		if (!other.getTotalWorkUnits().equals(getTotalWorkUnits()))
			return false;
		
		if (areNotEqual(other.fundingSourceExpenseMap, fundingSourceExpenseMap))
			return false;
		
		if (areNotEqual(other.accountingCodeExpenseMap, accountingCodeExpenseMap))
			return false;
		
		if (areNotEqual(other.fundingSourceWorkUnitMap, fundingSourceWorkUnitMap))
			return false;
		
		if (areNotEqual(other.accountingCodeWorkUnitMap, accountingCodeWorkUnitMap))
			return false;
		
		return other.resourceWorkUnitMap.equals(resourceWorkUnitMap);
	}
	
	private boolean areNotEqual(HashMap<ORef, OptionalDouble> map1, HashMap<ORef, OptionalDouble> map2)
	{
		return !map1.equals(map2);
	}

	@Override
	public int hashCode()
	{
		return totalExpenses.hashCode() + totalWorkUnits.hashCode();
	}
	
	@Override
	public String toString()
	{
		String asString = "";
		if (totalExpenses.hasValue())
			asString = "expense = " + totalExpenses.getValue() + "\n";
		
		Set<ORef> refs = resourceWorkUnitMap.keySet();
		for(ORef ref : refs)
		{
			asString += "resourceRef = " + ref + " units = " + resourceWorkUnitMap.get(ref) + "\n";
		}
		
		asString += "\nTotalWorkUnits = " + getTotalWorkUnits() + "\n\n";
		
		return asString;
	}
	
	public Set<ORef> getResourceRefSet()
	{
		return new HashSet(resourceWorkUnitMap.keySet());
	}
	
	public Set<ORef> getFundingSourceWorkUnitsRefSet()
	{
		return new HashSet(fundingSourceWorkUnitMap.keySet());
	}
	
	public Set<ORef> getFundingSourceExpensesRefSet()
	{
		return new HashSet(fundingSourceExpenseMap.keySet());
	}
	
	private boolean hasExpenseData()
	{
		return getExpense().hasValue();
	}

	private boolean hasTotalWorkUnitsData()
	{
		return getTotalWorkUnits().hasValue();
	}
	
	private void ensureCorrectRefTypes(ORef resourceRef, ORef fundingSourceRef)
	{
		if (resourceRef.isValid() && !ProjectResource.is(resourceRef))
			throw new RuntimeException(getWrongRefErrorMessage(resourceRef, "ProjectResource Ref"));
		
		ensureFundingSource(fundingSourceRef);
	}

	private void ensureFundingSource(ORef fundingSourceRef)
	{
		if (fundingSourceRef.isValid() && !FundingSource.is(fundingSourceRef))
			throw new RuntimeException(getWrongRefErrorMessage(fundingSourceRef, "FundingSource Ref"));
	}
	
	private String getWrongRefErrorMessage(ORef ref, String substituionText)
	{
		return EAM.substitute(EAM.text("Was expecting a %s, instead got:\n" + ref.toString()), substituionText);
	}
	
	private OptionalDouble totalExpenses;
	private OptionalDouble totalWorkUnits;
	
	private HashMap<ORef, OptionalDouble> fundingSourceExpenseMap;
	private HashMap<ORef, OptionalDouble> accountingCodeExpenseMap;
	
	private HashMap<ORef, OptionalDouble> resourceWorkUnitMap;
	private HashMap<ORef, OptionalDouble> fundingSourceWorkUnitMap;
	private HashMap<ORef, OptionalDouble> accountingCodeWorkUnitMap;
}
