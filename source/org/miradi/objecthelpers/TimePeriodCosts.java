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
		expense = new OptionalDouble();
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
	
	public TimePeriodCosts(OptionalDouble expenseToUse)
	{
		this();
		
		setExpense(expenseToUse);
	}
	
	public TimePeriodCosts(ORef resourceRef, ORef fundingSourceRef,	OptionalDouble workUnits)
	{
		this();
		
		ensureCorrectRefTypes(resourceRef, fundingSourceRef);		
		addRefToMap(resourceWorkUnitMap, resourceRef, workUnits);
		addRefToMap(fundingSourceWorkUnitMap, fundingSourceRef, workUnits);
	}

	private void ensureCorrectRefTypes(ORef resourceRef, ORef fundingSourceRef)
	{
		if (resourceRef.isValid() && !ProjectResource.is(resourceRef))
			throw new RuntimeException(getWrongRefErrorMessage(resourceRef, "ProjectResource Ref"));
		
		if (fundingSourceRef.isValid() && !FundingSource.is(fundingSourceRef))
			throw new RuntimeException(getWrongRefErrorMessage(fundingSourceRef, "FundingSource Ref"));
	}
	
	private String getWrongRefErrorMessage(ORef ref, String substituionText)
	{
		return EAM.substitute(EAM.text("Was expecting a %s, instead got:\n" + ref.toString()), substituionText);
	}
	
	public void add(TimePeriodCosts timePeriodCosts)
	{
		addExpenses(timePeriodCosts);
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
		if (mapToUpdate.containsKey(refToAdd))
		{
			OptionalDouble thisWorkUnits = mapToUpdate.get(refToAdd);
			workUnitsToAdd = thisWorkUnits.add(workUnitsToAdd);
		}
		
		putRef(mapToUpdate, refToAdd, workUnitsToAdd);
	}
	
	private void putRef(HashMap<ORef, OptionalDouble> mapToUpdate, ORef refToAdd, OptionalDouble workUnitsToAdd)
	{
		if (ProjectResource.is(refToAdd))
			updateTotalWorkUnits(refToAdd, workUnitsToAdd);
		
		mapToUpdate.put(refToAdd, workUnitsToAdd);
	}

	private void updateTotalWorkUnits(ORef resourceRefToAdd, OptionalDouble workUnitsToAdd)
	{
		if (resourceWorkUnitMap.containsKey(resourceRefToAdd))
			removeResource(resourceRefToAdd);

		totalWorkUnits = totalWorkUnits.add(workUnitsToAdd);		
	}

	public void removeResource(ORef resourceRefToRemove)
	{
		OptionalDouble workUnitToRemove = resourceWorkUnitMap.get(resourceRefToRemove);
		resourceWorkUnitMap.remove(resourceRefToRemove);
		totalWorkUnits = totalWorkUnits.subtract(workUnitToRemove);		
	}
	
	private void addExpenses(TimePeriodCosts timePeriodCostsToUse)
	{
		expense = expense.add(timePeriodCostsToUse.getExpense());
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
	
	private void setExpenseValueFrom(TimePeriodCosts timePeriodCosts) throws Exception
	{
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
	
	public OptionalDouble getTotalWorkUnits()
	{
		return totalWorkUnits;
	}

	@Override
	public boolean equals(Object rawOther)
	{
		if (! (rawOther instanceof TimePeriodCosts))
			return false;
		
		TimePeriodCosts other = (TimePeriodCosts) rawOther;
		if (!other.getExpense().equals(getExpense()))
			return false;
		
		if (!other.fundingSourceWorkUnitMap.equals(fundingSourceWorkUnitMap))
			return false;
		
		return other.resourceWorkUnitMap.equals(resourceWorkUnitMap);
	}
	
	@Override
	public int hashCode()
	{
		return expense.hashCode() + resourceWorkUnitMap.hashCode() + fundingSourceWorkUnitMap.hashCode();
	}
	
	public OptionalDouble getResourceWorkUnits(ORef resourceRef)
	{
		return getWorkUnits(resourceWorkUnitMap, resourceRef);
	}
	
	public OptionalDouble getFundingSourceWorkUnits(ORef fundingSourceRef)
	{
		return getWorkUnits(fundingSourceWorkUnitMap, fundingSourceRef);
	}
	
	private OptionalDouble getWorkUnits(HashMap<ORef, OptionalDouble> mapToExtractFrom, ORef refToExtract)
	{
		if (!mapToExtractFrom.containsKey(refToExtract))
			return new OptionalDouble();
		
		return mapToExtractFrom.get(refToExtract);
	}
	
	public OptionalDouble getExpense()
	{
		return expense;
	}
	
	public Set<ORef> getResourceRefSet()
	{
		return new HashSet(resourceWorkUnitMap.keySet());
	}
	
	public Set<ORef> getFundingSourceRefSet()
	{
		return new HashSet(fundingSourceWorkUnitMap.keySet());
	}
	
	public void mergeAllTimePeriodCosts(TimePeriodCosts timePeriodCostsToMergeAdd)
	{
		addExpenses(timePeriodCostsToMergeAdd);
		mergeAllWorkUnitMapsInPlace(timePeriodCostsToMergeAdd);
	}
	
	public void mergeAllWorkUnitMapsInPlace(TimePeriodCosts timePeriodCostsToMerge)
	{
		mergeWorkUnitMapInPlace(resourceWorkUnitMap, timePeriodCostsToMerge.resourceWorkUnitMap);
		mergeWorkUnitMapInPlace(fundingSourceWorkUnitMap, timePeriodCostsToMerge.fundingSourceWorkUnitMap);
	}
	
	private void mergeWorkUnitMapInPlace(HashMap<ORef, OptionalDouble> mapToUpdate, HashMap<ORef, OptionalDouble> mapToMergeFrom)
	{
		Set<ORef> keysToMerge = mapToMergeFrom.keySet();
		for(ORef refToMerge : keysToMerge)
		{
			OptionalDouble thisWorkUnits = mapToMergeFrom.get(refToMerge);
			thisWorkUnits = thisWorkUnits.add(getWorkUnits(mapToUpdate, refToMerge));
			
			putRef(mapToUpdate, refToMerge, thisWorkUnits);
		}
	}
	
	@Override
	public String toString()
	{
		String asString = "";
		if (expense.hasValue())
			asString = "expense = " + expense.getValue() + "\n";
		
		Set<ORef> refs = resourceWorkUnitMap.keySet();
		for(ORef ref : refs)
		{
			asString += "resourceRef = " + ref + " units = " + resourceWorkUnitMap.get(ref) + "\n";
		}
		
		asString += "\nTotalWorkUnits = " + getTotalWorkUnits() + "\n\n";
		
		return asString;
	}
	
	private boolean hasExpenseData()
	{
		return getExpense().hasValue();
	}

	private boolean hasTotalWorkUnitsData()
	{
		return getTotalWorkUnits().hasValue();
	}

	protected void mergeNonConflicting(TimePeriodCosts snapShotTimePeriodCosts, TimePeriodCosts timePeriodCostsToMerge) throws Exception
	{
		if (!snapShotTimePeriodCosts.hasExpenseData())
			setExpenseValueFrom(timePeriodCostsToMerge);
		
		if (!snapShotTimePeriodCosts.hasTotalWorkUnitsData())
			mergeAllWorkUnitMapsInPlace(timePeriodCostsToMerge);
	}

	private OptionalDouble expense;
	private OptionalDouble totalWorkUnits;
	
	HashMap<ORef, OptionalDouble> fundingSourceExpenseMap;
	HashMap<ORef, OptionalDouble> accountingCodeExpenseMap;
	
	private HashMap<ORef, OptionalDouble> resourceWorkUnitMap;
	HashMap<ORef, OptionalDouble> fundingSourceWorkUnitMap;
	HashMap<ORef, OptionalDouble> accountingCodeWorkUnitMap;
	
}
