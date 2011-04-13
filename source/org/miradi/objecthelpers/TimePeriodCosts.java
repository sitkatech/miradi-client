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
import org.miradi.objects.BaseObject;
import org.miradi.objects.ProjectResource;
import org.miradi.project.Project;
import org.miradi.utils.OptionalDouble;

public class TimePeriodCosts
{
	public TimePeriodCosts()
	{
		workUnitCategorizedQuantities = new Vector<CategorizedQuantity>();
		expensesCategorizedQuantities = new Vector<CategorizedQuantity>();

		totalExpenses = new OptionalDouble();
		totalWorkUnits = new OptionalDouble();
	}
	
	public TimePeriodCosts(TimePeriodCosts timePeriodCostsToUse)
	{
		this();
		
		add(timePeriodCostsToUse);
	}
	
	public TimePeriodCosts(ORef fundingSourceRef, ORef accountingCodeRef, ORef categoryOneRef, ORef categoryTwoRef, OptionalDouble expenseToUse)
	{
		this();
		
		addExpensesToTotal(expenseToUse);
		addToCategorizedQuantities(expensesCategorizedQuantities, new CategorizedQuantity(ORef.INVALID, fundingSourceRef, accountingCodeRef, categoryOneRef, categoryTwoRef, expenseToUse));
	}
	
	public TimePeriodCosts(ORef resourceRef, ORef fundingSourceRef,	ORef accountingCodeRef, ORef categoryOneRef, ORef categoryTwoRef, OptionalDouble workUnits)
	{
		this();
		
		addWorkUnitsToTotal(workUnits);
		addToCategorizedQuantities(workUnitCategorizedQuantities, new CategorizedQuantity(resourceRef, fundingSourceRef, accountingCodeRef, categoryOneRef, categoryTwoRef, workUnits));
	}

	public void add(TimePeriodCosts timePeriodCosts)
	{
		addExpensesToTotal(timePeriodCosts);
		addCategorizedQuantity(expensesCategorizedQuantities, timePeriodCosts.expensesCategorizedQuantities);
		
		addWorkUnitsToTotal(timePeriodCosts);
		addCategorizedQuantity(workUnitCategorizedQuantities, timePeriodCosts.workUnitCategorizedQuantities);
	}
	
	private void addCategorizedQuantity(Vector<CategorizedQuantity> categorizedQuantityToUpdate, Vector<CategorizedQuantity> categorizedQuantitiesToAdd)
	{
		if (categorizedQuantityToUpdate == categorizedQuantitiesToAdd)
			throw new RuntimeException(EAM.text("Cannot add a vector to itself."));
		
		for(CategorizedQuantity thisCategorizedQuantity : categorizedQuantitiesToAdd)
		{
			addToCategorizedQuantities(categorizedQuantityToUpdate, thisCategorizedQuantity);
		}
	}
	
	private void addToCategorizedQuantities(Vector<CategorizedQuantity> categorizedQuantitiesToUpdate, CategorizedQuantity categorizedQuantityToAdd)
	{
		categorizedQuantitiesToUpdate.add(categorizedQuantityToAdd);
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
	
	public OptionalDouble calculateTotalCost(Project projectToUse) throws Exception
	{
		final OptionalDouble expenseToAdd = getTotalExpense();
		final OptionalDouble totalResourceCost = calculateResourcesTotalCost(projectToUse);
		
		return totalResourceCost.add(expenseToAdd);
	}
	
	private OptionalDouble calculateResourcesTotalCost(Project projectToUse) throws Exception
	{
		OptionalDouble resourcesTotalCost = new OptionalDouble();
		Vector<CategorizedQuantity> categorizedQuantities = workUnitCategorizedQuantities;
		for(CategorizedQuantity thisCategorizedQuantity : categorizedQuantities)
		{
			OptionalDouble costPerUnit = getCostPerUnit(projectToUse, thisCategorizedQuantity.getResourceRef());
			OptionalDouble workUnits = thisCategorizedQuantity.getQuantity();
			OptionalDouble multiplyValue = workUnits.multiply(costPerUnit);
			resourcesTotalCost = resourcesTotalCost.add(multiplyValue);
		}
		
		return resourcesTotalCost;
	}

	private OptionalDouble getCostPerUnit(Project projectToUse,	ORef projectResourceRef) throws Exception
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
	
	private OptionalDouble getRolledUpQuantityForRef(Vector<CategorizedQuantity> categorizedQuantitiesToSearch, ORef refToFindBy)
	{
		OptionalDouble totalQuantityForRef = new OptionalDouble();
		for(CategorizedQuantity thisCategorizedQuantity : categorizedQuantitiesToSearch)
		{
			if (thisCategorizedQuantity.containsRef(refToFindBy))
				totalQuantityForRef = totalQuantityForRef.add(thisCategorizedQuantity.getQuantity());
		}
		
		return totalQuantityForRef;
	}
	
	protected void mergeAllTimePeriodCosts(TimePeriodCosts timePeriodCostsToMergeAdd)
	{
		mergeAllExpenseCategorizedQuantityInPlace(timePeriodCostsToMergeAdd);
		mergeAllWorkUnitCategorizedQuantityInPlace(timePeriodCostsToMergeAdd);
	}

	private void mergeAllExpenseCategorizedQuantityInPlace(TimePeriodCosts timePeriodCostsToMergeAdd)
	{
		addExpensesToTotal(timePeriodCostsToMergeAdd);
		
		mergeCategorizedQuantitySetInPlace(expensesCategorizedQuantities, timePeriodCostsToMergeAdd.expensesCategorizedQuantities);
	}
	
	public void mergeAllWorkUnitCategorizedQuantityInPlace(TimePeriodCosts timePeriodCostsToMerge)
	{
		addWorkUnitsToTotal(timePeriodCostsToMerge);
		
		mergeCategorizedQuantitySetInPlace(workUnitCategorizedQuantities, timePeriodCostsToMerge.workUnitCategorizedQuantities);
	}
	
	private void mergeCategorizedQuantitySetInPlace(Vector<CategorizedQuantity> categorizedQuantityToUpdate, Vector<CategorizedQuantity> categorizedQuantityToMergeFrom)
	{
		for(CategorizedQuantity thisCategorizedQuantity : categorizedQuantityToMergeFrom)
		{
			addToCategorizedQuantities(categorizedQuantityToUpdate, thisCategorizedQuantity);
		}
	}
	
	protected void mergeNonConflicting(TimePeriodCosts snapShotTimePeriodCosts, TimePeriodCosts timePeriodCostsToMerge) throws Exception
	{
		if (!snapShotTimePeriodCosts.hasExpenseData())
			mergeAllExpenseCategorizedQuantityInPlace(timePeriodCostsToMerge);
		
		if (!snapShotTimePeriodCosts.hasTotalWorkUnitsData())
			mergeAllWorkUnitCategorizedQuantityInPlace(timePeriodCostsToMerge);
	}
	
	public void retainWorkUnitDataRelatedToAnyOf(BaseObject baseObject)
	{
		retainWorkUnitDataRelatedToAnyOf(new ORefSet(baseObject));
	}
	
	public void retainWorkUnitDataRelatedToAnyOf(ORefSet refsToRetain)
	{
		filterByUnionOf(workUnitCategorizedQuantities, refsToRetain);
		updateTotalWorkUnits();
	}
	
	public void retainExpenseDataRelatedToAnyOf(BaseObject baseObject)
	{
		retainExpenseDataRelatedToAnyOf(new ORefSet(baseObject));
	}
	
	public void retainExpenseDataRelatedToAnyOf(ORefSet refsToRetain)
	{
		filterByUnionOf(expensesCategorizedQuantities, refsToRetain);
		updateTotalExpenses();
	}
	
	public void retainExpenseDataRelatedToAllOf(ORefSet refsToRetain)
	{
		filterByIntersectionOf(expensesCategorizedQuantities, refsToRetain);
		updateTotalExpenses();
	}
	
	public void retainWorkUnitDataRelatedToAllOf(ORefSet refsToRetain)
	{
		filterByIntersectionOf(workUnitCategorizedQuantities, refsToRetain);
		updateTotalWorkUnits();
	}
	
	private void filterByUnionOf(Vector<CategorizedQuantity> categorizedQuantities, ORefSet refsToRetain)
	{
		if (refsToRetain.size() == 0)
			return;
		
		if (refsToRetain.contains(ORef.INVALID))
			EAM.logError("WARNING: Filtering on invalid ref with no type");
		
		
		Vector<CategorizedQuantity> categorizedQuantitiesToRetain = new Vector<CategorizedQuantity>();
		for(CategorizedQuantity categorizedQuantityToFilter : categorizedQuantities)
		{
			if (categorizedQuantityToFilter.containsAtleastOne(refsToRetain))
				categorizedQuantitiesToRetain.add(categorizedQuantityToFilter);
		}
		
		categorizedQuantities.retainAll(categorizedQuantitiesToRetain);
	}
	
	private void filterByIntersectionOf(Vector<CategorizedQuantity> categorizedQuantities, ORefSet refsToRetain)
	{
		if (refsToRetain.size() == 0)
			return;
		
		if (refsToRetain.contains(ORef.INVALID))
			EAM.logError("WARNING: Filtering on invalid ref with no type");
		
		Vector<CategorizedQuantity> categorizedQuantitiesToRetain = new Vector<CategorizedQuantity>();
		for(CategorizedQuantity categorizedQuantityToFilter : categorizedQuantities)
		{
			if (categorizedQuantityToFilter.containsAll(refsToRetain))
				categorizedQuantitiesToRetain.add(categorizedQuantityToFilter);
		}
		
		categorizedQuantities.retainAll(categorizedQuantitiesToRetain);
	}
	
	private void updateTotalExpenses()
	{
		totalExpenses = getTotal(expensesCategorizedQuantities);		
	}

	private void updateTotalWorkUnits()
	{
		totalWorkUnits = getTotal(workUnitCategorizedQuantities);
	}
	
	private OptionalDouble getTotal(Vector<CategorizedQuantity> categorizedQuantities)
	{
		OptionalDouble totals = new OptionalDouble();
		for(CategorizedQuantity categorizedQuantity: categorizedQuantities)
		{
			totals = totals.add(categorizedQuantity.getQuantity());
		}
		
		return totals;
	}
	
	public void divideBy(OptionalDouble divideByValue)
	{
		divideByCategorizedQuantities(workUnitCategorizedQuantities, divideByValue);
		updateTotalWorkUnits();
		
		divideByCategorizedQuantities(expensesCategorizedQuantities, divideByValue);
		updateTotalExpenses();
	}
	
	private void divideByCategorizedQuantities(Vector<CategorizedQuantity> categorizedQuantitiesToDivide, OptionalDouble divideByValue)
	{
		for(CategorizedQuantity categorizedQuantity : categorizedQuantitiesToDivide)
		{
			categorizedQuantity.divideBy(divideByValue);
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
	
	private ORefSet extractRefs(Vector<CategorizedQuantity> categorizedQuantitiesToUse, int type)
	{
		ORefSet extractedRefs = new ORefSet();
		for(CategorizedQuantity categorizedQuantity : categorizedQuantitiesToUse)
		{
			ORefSet containingRefs = categorizedQuantity.getContainingRefs();
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
	
	public Vector<CategorizedQuantity> getWorkUnitCategorizedQuantities()
	{
		return new Vector<CategorizedQuantity>(workUnitCategorizedQuantities);
	}
	
	public Vector<CategorizedQuantity> getExpensesCategorizedQuantities()
	{
		return new Vector<CategorizedQuantity>(expensesCategorizedQuantities);
	}
	
	private OptionalDouble totalExpenses;
	private OptionalDouble totalWorkUnits;
	
	private Vector<CategorizedQuantity> workUnitCategorizedQuantities;
	private Vector<CategorizedQuantity> expensesCategorizedQuantities;
}
