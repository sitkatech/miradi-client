/* 
Copyright 2005-2010, Foundations of Success, Bethesda, Maryland 
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

import org.miradi.utils.OptionalDouble;

public class CategorizedQuantity 
{
	public CategorizedQuantity()
	{
		resourceRef = ORef.INVALID;
		fundingSourceRef = ORef.INVALID;
		accountingCodeRef = ORef.INVALID;
		quantity = new OptionalDouble();
	}
	
	public CategorizedQuantity(ORef resourceRefToUse, ORef fundingSourceRefToUse, ORef accountingCodeRefToUse, OptionalDouble quantityToUse)
	{
		resourceRef = resourceRefToUse;
		fundingSourceRef = fundingSourceRefToUse;
		accountingCodeRef = accountingCodeRefToUse;
		quantity = quantityToUse;
	}
	
	public ORefSet getContainingRefs()
	{
		ORefSet allContainingRefs = new ORefSet();
		allContainingRefs.add(getResourceRef());
		allContainingRefs.add(getFundingSourceRef());
		allContainingRefs.add(getAccountingCodeRef());
		
		return allContainingRefs;
	}
	
	public boolean containsAtleastOne(ORefSet refsToRetain)
	{
		ORefSet containingRefs = getContainingRefs();
		containingRefs.retainAll(refsToRetain);
		
		return containingRefs.size() > 0;
	}
	
	public boolean containsRef(ORef refToMatch)
	{
		if (resourceRef.equals(refToMatch))
			return true;
		
		if (accountingCodeRef.equals(refToMatch))
			return true;
		
		return fundingSourceRef.equals(refToMatch);
	}
	
	public void addQuantity(OptionalDouble quantityToAdd)
	{
		quantity = quantity.add(quantityToAdd);
	}
	
	public void divideBy(OptionalDouble divideBy)
	{
		quantity = quantity.divideBy(divideBy);
	}
	
	public ORef getResourceRef()
	{
		return resourceRef;
	}
	
	private ORef getFundingSourceRef()
	{
		return fundingSourceRef;
	}
	
	private ORef getAccountingCodeRef()
	{
		return accountingCodeRef;
	}
	
	public OptionalDouble getQuantity()
	{
		return quantity;
	}
	
	@Override
	public int hashCode()
	{
		return fundingSourceRef.hashCode() + resourceRef.hashCode() + accountingCodeRef.hashCode();
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
		
		return quantity.equals(other.quantity);
	}
	
	@Override
	public String toString()
	{
		return "rsourceRef=" + resourceRef + " fundingSourceRef=" + fundingSourceRef + " accountingCodeRef=" + accountingCodeRef + " quantiy=" + quantity; 
	}
	
	private ORef resourceRef;
	private ORef fundingSourceRef;
	private ORef accountingCodeRef;
	private OptionalDouble quantity;
}