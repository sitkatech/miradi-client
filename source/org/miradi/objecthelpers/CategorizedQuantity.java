/* 
Copyright 2005-2018, Foundations of Success, Bethesda, Maryland
on behalf of the Conservation Measures Partnership ("CMP").
Material developed between 2005-2013 is jointly copyright by Beneficent Technology, Inc. ("The Benetech Initiative"), Palo Alto, California.

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

import org.miradi.schemas.AccountingCodeSchema;
import org.miradi.schemas.BudgetCategoryOneSchema;
import org.miradi.schemas.BudgetCategoryTwoSchema;
import org.miradi.schemas.FundingSourceSchema;
import org.miradi.schemas.ProjectResourceSchema;
import org.miradi.utils.OptionalDouble;

public class CategorizedQuantity 
{
	public CategorizedQuantity(ORef resourceRefToUse, ORef fundingSourceRefToUse, ORef accountingCodeRefToUse, ORef categoryOneRefToUse, ORef categoryTwoRefToUse, OptionalDouble quantityToUse)
	{
		resourceRef = ensureRefHasCorrectType(resourceRefToUse, ProjectResourceSchema.getObjectType());
		fundingSourceRef = ensureRefHasCorrectType(fundingSourceRefToUse, FundingSourceSchema.getObjectType());
		accountingCodeRef = ensureRefHasCorrectType(accountingCodeRefToUse, AccountingCodeSchema.getObjectType());
		categoryOneRef = ensureRefHasCorrectType(categoryOneRefToUse, BudgetCategoryOneSchema.getObjectType());
		categoryTwoRef = ensureRefHasCorrectType(categoryTwoRefToUse, BudgetCategoryTwoSchema.getObjectType());
		
		quantity = quantityToUse;
	}

	private ORef ensureRefHasCorrectType(ORef categoryRefToUse,	final int objectType)
	{
		if (categoryRefToUse.isInvalid())
			categoryRefToUse = ORef.createInvalidWithType(objectType);
			
		categoryRefToUse.ensureExactType(objectType);
		
		return categoryRefToUse;
	}
	
	public ORefSet getContainingRefs()
	{
		ORefSet allContainingRefs = new ORefSet();
		allContainingRefs.add(getResourceRef());
		allContainingRefs.add(fundingSourceRef);
		allContainingRefs.add(accountingCodeRef);
		allContainingRefs.add(categoryOneRef);
		allContainingRefs.add(categoryTwoRef);
		
		return allContainingRefs;
	}
	
	public boolean containsAtLeastOne(ORefSet refsToRetain)
	{
		ORefSet containingRefs = getContainingRefs();
		containingRefs.retainAll(refsToRetain);
		
		return containingRefs.size() > 0;
	}
	
	public boolean containsAll(ORefSet refsToRetain)
	{
		ORefSet containingRefs = getContainingRefs();
		
		return containingRefs.containsAll(refsToRetain);
	}
	
	public boolean containsRef(ORef refToMatch)
	{
		if (resourceRef.equals(refToMatch))
			return true;
		
		if (accountingCodeRef.equals(refToMatch))
			return true;
		
		if (fundingSourceRef.equals(refToMatch))
			return true;
		
		if (categoryOneRef.equals(refToMatch))
			return true;
		
		return categoryTwoRef.equals(refToMatch);
	}
	
	public void divideBy(OptionalDouble divideBy)
	{
		quantity = quantity.divideBy(divideBy);
	}
	
	public ORef getResourceRef()
	{
		return resourceRef;
	}
	
	public OptionalDouble getQuantity()
	{
		return quantity;
	}
	
	@Override
	public int hashCode()
	{
		return fundingSourceRef.hashCode() + resourceRef.hashCode() + accountingCodeRef.hashCode() + categoryOneRef.hashCode() + categoryTwoRef.hashCode();
	}
	
	@Override
	public boolean equals(Object rawOther)
	{
		if (!(rawOther instanceof CategorizedQuantity))
			return false;
		
		CategorizedQuantity other = (CategorizedQuantity) rawOther;
		if (!fundingSourceRef.equals(other.fundingSourceRef))
			return false;
		
		if (!resourceRef.equals(other.resourceRef))
			return false;
		
		if (!accountingCodeRef.equals(other.accountingCodeRef))
			return false;
		
		if (!categoryOneRef.equals(other.categoryOneRef))
			return false;
		
		if (!categoryTwoRef.equals(other.categoryTwoRef))
			return false;
		
		return quantity.equals(other.quantity);
	}
	
	@Override
	public String toString()
	{
		return "resourceRef=" + resourceRef + " fundingSourceRef=" + fundingSourceRef + " accountingCodeRef=" + accountingCodeRef + " quantity=" + quantity;
	}
	
	public ORef getFundingSourceRef()
	{
		return fundingSourceRef;
	}
	
	public ORef getAccountingCodeRef()
	{
		return accountingCodeRef;
	}
	
	public ORef getCategoryOneRef()
	{
		return categoryOneRef;
	}
	
	public ORef getCategoryTwoRef()
	{
		return categoryTwoRef;
	}
	
	private ORef resourceRef;
	private ORef fundingSourceRef;
	private ORef accountingCodeRef;
	private ORef categoryOneRef;
	private ORef categoryTwoRef;
	private OptionalDouble quantity;
}