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

import java.util.Set;
import java.util.Vector;

import org.miradi.main.EAM;
import org.miradi.objects.AccountingCode;
import org.miradi.objects.FundingSource;
import org.miradi.objects.ProjectResource;
import org.miradi.project.Project;
import org.miradi.utils.OptionalDouble;

public class TimePeriodCosts
{
	public TimePeriodCosts()
	{
		workUnitCategorizedQuantities = new Vector();
		expensesCategorizedQuantities = new Vector();

		totalExpenses = new OptionalDouble();
		totalWorkUnits = new OptionalDouble();
	}
	
	public TimePeriodCosts(TimePeriodCosts timePeriodCostsToUse)
	{
		this();
		
		add(timePeriodCostsToUse);
	}
	
	public TimePeriodCosts(ORef fundingSourceRef, ORef accountingCodeRef, OptionalDouble expenseToUse)
	{
		this();
		
		fundingSourceRef.ensureValidType(FundingSource.getObjectType());
		accountingCodeRef.ensureValidType(AccountingCode.getObjectType());
		
		addExpensesToTotal(expenseToUse);
		addToCategorizedQuantities(expensesCategorizedQuantities, new CategorizedQuantity(ORef.INVALID, fundingSourceRef, accountingCodeRef, expenseToUse));
	}
	
	public TimePeriodCosts(ORef resourceRef, ORef fundingSourceRef,	ORef accountingCodeRef, OptionalDouble workUnits)
	{
		this();
		
		resourceRef.ensureValidType(ProjectResource.getObjectType());
		fundingSourceRef.ensureValidType(FundingSource.getObjectType());
		accountingCodeRef.ensureValidType(AccountingCode.getObjectType());
		
		addWorkUnitsToTotal(workUnits);
		addToCategorizedQuantities(workUnitCategorizedQuantities, new CategorizedQuantity(resourceRef, fundingSourceRef, accountingCodeRef, workUnits));
	}

	public void add(TimePeriodCosts timePeriodCosts)
	{
		addExpensesToTotal(timePeriodCosts);
		addCategorizedQuantity(expensesCategorizedQuantities, timePeriodCosts.expensesCategorizedQuantities);
		
		addWorkUnitsToTotal(timePeriodCosts);
		addCategorizedQuantity(workUnitCategorizedQuantities, timePeriodCosts.workUnitCategorizedQuantities);
	}
	
	private void addCategorizedQuantity(Vector<CategorizedQuantity> packToUpdate, Vector<CategorizedQuantity> packsToAdd)
	{
		if (packToUpdate == packsToAdd)
			throw new RuntimeException(EAM.text("Cannot add a vector to itself."));
		
		for(CategorizedQuantity thisDataPack : packsToAdd)
		{
			addToCategorizedQuantities(packToUpdate, thisDataPack);
		}
	}
	
	private void addToCategorizedQuantities(Vector<CategorizedQuantity> dataPacksToUpdate, CategorizedQuantity dataPackToAdd)
	{
		dataPacksToUpdate.add(dataPackToAdd);
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
		addExpensesToTotal(timePeriodCostsToUse.getTotalExpense());
	}

	private void addExpensesToTotal(OptionalDouble expense)
	{
		totalExpenses = totalExpenses.add(expense);
	}
	
	public OptionalDouble getTotalExpense()
	{
		return totalExpenses;
	}
	
	public OptionalDouble calculateTotalCost(Project projectToUse)
	{
		final OptionalDouble expenseToAdd = getTotalExpense();
		final OptionalDouble totalResourceCost = calculateResourcesTotalCost(projectToUse);
		
		return totalResourceCost.add(expenseToAdd);
	}
	
	private OptionalDouble calculateResourcesTotalCost(Project projectToUse)
	{
		OptionalDouble resourcesTotalCost = new OptionalDouble();
		Vector<CategorizedQuantity> dataPacks = workUnitCategorizedQuantities;
		for(CategorizedQuantity thisDataPack : dataPacks)
		{
			OptionalDouble costPerUnit = getCostPerUnit(projectToUse, thisDataPack.getResourceRef());
			OptionalDouble workUnits = thisDataPack.getQuantity();
			OptionalDouble multiplyValue = workUnits.multiply(costPerUnit);
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
	
	public OptionalDouble getWorkUnitsForRef(ORef ref)
	{
		return getRolledUpQuantityForRef(workUnitCategorizedQuantities, ref);
	}
	
	public OptionalDouble getFundingSourceExpenses(ORef fundingSourceRef)
	{
		return getRolledUpQuantityForRef(expensesCategorizedQuantities, fundingSourceRef);
	}
	
	private OptionalDouble getRolledUpQuantityForRef(Vector<CategorizedQuantity> dataPacksToSearch, ORef refToFindBy)
	{
		OptionalDouble totalQuantityForRef = new OptionalDouble();
		for(CategorizedQuantity thisDataPack : dataPacksToSearch)
		{
			if (thisDataPack.containsRef(refToFindBy))
				totalQuantityForRef = totalQuantityForRef.add(thisDataPack.getQuantity());
		}
		
		return totalQuantityForRef;
	}
	
	protected void mergeAllTimePeriodCosts(TimePeriodCosts timePeriodCostsToMergeAdd)
	{
		mergeAllExpensePacksInPlace(timePeriodCostsToMergeAdd);
		mergeAllWorkUnitDataPackInPlace(timePeriodCostsToMergeAdd);
	}

	private void mergeAllExpensePacksInPlace(TimePeriodCosts timePeriodCostsToMergeAdd)
	{
		addExpensesToTotal(timePeriodCostsToMergeAdd);
		
		mergeDataPackSetInPlace(expensesCategorizedQuantities, timePeriodCostsToMergeAdd.expensesCategorizedQuantities);
	}
	
	public void mergeAllWorkUnitDataPackInPlace(TimePeriodCosts timePeriodCostsToMerge)
	{
		addWorkUnitsToTotal(timePeriodCostsToMerge);
		
		mergeDataPackSetInPlace(workUnitCategorizedQuantities, timePeriodCostsToMerge.workUnitCategorizedQuantities);
	}
	
	private void mergeDataPackSetInPlace(Vector<CategorizedQuantity> dataPackToUpdate, Vector<CategorizedQuantity> dataPackToMergeFrom)
	{
		for(CategorizedQuantity thisDataPack : dataPackToMergeFrom)
		{
			addToCategorizedQuantities(dataPackToUpdate, thisDataPack);
		}
	}
	
	protected void mergeNonConflicting(TimePeriodCosts snapShotTimePeriodCosts, TimePeriodCosts timePeriodCostsToMerge) throws Exception
	{
		if (!snapShotTimePeriodCosts.hasExpenseData())
			mergeAllExpensePacksInPlace(timePeriodCostsToMerge);
		
		if (!snapShotTimePeriodCosts.hasTotalWorkUnitsData())
			mergeAllWorkUnitDataPackInPlace(timePeriodCostsToMerge);
	}
	
	public void retainWorkUnitDataRelatedToAnyOf(ORefSet refsToRetain)
	{
		filterByUnionOf(workUnitCategorizedQuantities, refsToRetain);
		updateTotalWorkUnits();
	}
	
	public void retainExpenseDataRelatedToAnyOf(ORefSet refsToRetain)
	{
		filterByUnionOf(expensesCategorizedQuantities, refsToRetain);
		updateTotalExpenses();
	}
	
	private void filterByUnionOf(Vector<CategorizedQuantity> dataPacks, ORefSet refsToRetain)
	{
		if (refsToRetain.size() == 0)
			return;
		
		if (refsToRetain.contains(ORef.INVALID))
			EAM.logError("WARNING: Filtering on invalid ref with no type");
		
		Vector<CategorizedQuantity> dataPacksToRetain = new Vector();
		for(CategorizedQuantity dataPackToFilter : dataPacks)
		{
			if (dataPackToFilter.containsAtleastOne(refsToRetain))
				dataPacksToRetain.add(dataPackToFilter);
		}
		
		dataPacks.retainAll(dataPacksToRetain);
	}
	
	private void updateTotalExpenses()
	{
		totalExpenses = getTotal(expensesCategorizedQuantities);		
	}

	private void updateTotalWorkUnits()
	{
		totalWorkUnits = getTotal(workUnitCategorizedQuantities);
	}
	
	private OptionalDouble getTotal(Vector<CategorizedQuantity> dataPacks)
	{
		OptionalDouble totals = new OptionalDouble();
		for(CategorizedQuantity dataPack: dataPacks)
		{
			totals = totals.add(dataPack.getQuantity());
		}
		
		return totals;
	}
	
	public void divideBy(OptionalDouble divideByValue)
	{
		divideByDataPacks(workUnitCategorizedQuantities, divideByValue);
		updateTotalWorkUnits();
		
		divideByDataPacks(expensesCategorizedQuantities, divideByValue);
		updateTotalExpenses();
	}
	
	private void divideByDataPacks(Vector<CategorizedQuantity> dataPacksToDivide, OptionalDouble divideByValue)
	{
		for(CategorizedQuantity dataPack : dataPacksToDivide)
		{
			dataPack.divideBy(divideByValue);
		}
	}
	
	@Override
	public boolean equals(Object rawOther)
	{
		if (! (rawOther instanceof TimePeriodCosts))
			return false;
		
		TimePeriodCosts other = (TimePeriodCosts) rawOther;
		if (!other.getTotalExpense().equals(getTotalExpense()))
			return false;
		
		if (!other.getTotalWorkUnits().equals(getTotalWorkUnits()))
			return false;
		
		if (!other.workUnitCategorizedQuantities.equals(workUnitCategorizedQuantities))
			return false;
		
		return other.expensesCategorizedQuantities.equals(expensesCategorizedQuantities);
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
		asString = "TotalExpenses = " + getTotalExpense() + "\n";		
		asString += "TotalWorkUnits = " + getTotalWorkUnits() + "\n";
		
		return asString;
	}

	public Set<ORef> getWorkUnitsRefSetForType(int objectType)
	{
		return extractRefs(workUnitCategorizedQuantities, objectType);
	}
	
	public Set<ORef> getExpenseRefSetForType(int objectType)
	{
		return extractRefs(expensesCategorizedQuantities, objectType);
	}
	
	private ORefSet extractRefs(Vector<CategorizedQuantity> dataPacksToUse, int type)
	{
		ORefSet extractedRefs = new ORefSet();
		for(CategorizedQuantity dataPack : dataPacksToUse)
		{
			ORefSet containingRefs = dataPack.getContainingRefs();
			ORefSet filteredRefs = containingRefs.getFilteredBy(type);
			extractedRefs.addAll(filteredRefs);
		}
		
		return extractedRefs;
	}
	
	private boolean hasExpenseData()
	{
		return getTotalExpense().hasValue();
	}

	private boolean hasTotalWorkUnitsData()
	{
		return getTotalWorkUnits().hasValue();
	}
	
	private OptionalDouble totalExpenses;
	private OptionalDouble totalWorkUnits;
	
	private Vector<CategorizedQuantity> workUnitCategorizedQuantities;
	private Vector<CategorizedQuantity> expensesCategorizedQuantities;
}
