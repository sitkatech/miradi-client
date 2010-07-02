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

import org.miradi.main.TestCaseWithProject;
import org.miradi.utils.OptionalDouble;

public class TestCategorizedQuantity extends TestCaseWithProject
{
	public TestCategorizedQuantity(String name)
	{
		super(name);
	}

	public void testBasics() throws Exception
	{
		ORef resourceRef = getProject().createProjectResource().getRef();
		ORef fundingSourceRef = getProject().createFundingSource().getRef();
		ORef accountingCodeRef = getProject().createAccountingCode().getRef();
		ORef categoryOneRef = getProject().createCategoryOne().getRef();
		ORef categoryTwoRef = getProject().createCategoryTwo().getRef();
		CategorizedQuantity categorizedQuantity = new CategorizedQuantity(resourceRef, fundingSourceRef, accountingCodeRef, categoryOneRef, categoryTwoRef, new OptionalDouble(14.0));
		
		verifyContainsMethods(categorizedQuantity, resourceRef);
		verifyContainsMethods(categorizedQuantity, fundingSourceRef);
		verifyContainsMethods(categorizedQuantity, accountingCodeRef);
		verifyContainsMethods(categorizedQuantity, fundingSourceRef);
		verifyContainsMethods(categorizedQuantity, categoryOneRef);
		verifyContainsMethods(categorizedQuantity, categoryTwoRef);
	}

	private void verifyContainsMethods(CategorizedQuantity categorizedQuantity, ORef ref)
	{
		assertTrue("sould contain atleast one of ref?", categorizedQuantity.containsAtleastOne(new ORefSet(ref)));
		assertTrue("sould contain ref?", categorizedQuantity.containsRef(ref));
		assertTrue("should contain ref?", categorizedQuantity.getContainingRefs().contains(ref));
	}
	
	public void testDivideBy()
	{
		OptionalDouble quantity = new OptionalDouble(14.0);
		CategorizedQuantity categorizedQuantity = new CategorizedQuantity(ORef.INVALID, ORef.INVALID, ORef.INVALID, ORef.INVALID, ORef.INVALID, quantity);
		assertEquals("incorrect quantity?", quantity, categorizedQuantity.getQuantity());
		
		categorizedQuantity.divideBy(new OptionalDouble(2.0));
		assertEquals("incorrect divide by result?", new OptionalDouble(7.0), categorizedQuantity.getQuantity());
	}
	
	public void testEquals() throws Exception
	{
		CategorizedQuantity categorizedQuantity1 = createSampleCategorizedQuantity();
		CategorizedQuantity categorizedQuantity2 = createSampleCategorizedQuantity();
		assertTrue("should be equal to itself?", categorizedQuantity1.equals(categorizedQuantity1));
		assertFalse("categorized Quantity objects with different containing refs should not be the same", categorizedQuantity1.equals(categorizedQuantity2));
	}
	
	public void testContainsAll() throws Exception
	{
		ORef resourceRef = getProject().createProjectResource().getRef();
		ORef fundingSourceRef = getProject().createFundingSource().getRef();
		ORef accountingCodeRef = getProject().createAccountingCode().getRef();
		ORef categoryOneRef = getProject().createCategoryOne().getRef();
		ORef categoryTwoRef = getProject().createCategoryTwo().getRef();
		
		CategorizedQuantity categorizedQuantity = new CategorizedQuantity(resourceRef, fundingSourceRef, accountingCodeRef, categoryOneRef, categoryTwoRef, new OptionalDouble(14.0));
		
		ORefSet refs = new ORefSet();
		addAndVerifyTrueContainsAll(categorizedQuantity, refs, resourceRef);
		addAndVerifyTrueContainsAll(categorizedQuantity, refs, fundingSourceRef);
		addAndVerifyTrueContainsAll(categorizedQuantity, refs, accountingCodeRef);
		addAndVerifyTrueContainsAll(categorizedQuantity, refs, categoryOneRef);
		addAndVerifyTrueContainsAll(categorizedQuantity, refs, categoryTwoRef);
		
		ORef resourceRefNotContained = getProject().createProjectResource().getRef();
		refs.add(resourceRefNotContained);
		assertFalse("should not contain all?", categorizedQuantity.containsAll(refs));
	}

	private void addAndVerifyTrueContainsAll(CategorizedQuantity categorizedQuantity, ORefSet refs, ORef resourceRef)
	{
		refs.add(resourceRef);
		assertTrue("should contain all?", categorizedQuantity.containsAll(refs));
	}

	private CategorizedQuantity createSampleCategorizedQuantity() throws Exception
	{
		ORef resourceRef = getProject().createProjectResource().getRef();
		ORef fundingSourceRef = getProject().createFundingSource().getRef();
		ORef accountingCodeRef = getProject().createAccountingCode().getRef();
		ORef categoryOneRef = getProject().createCategoryOne().getRef();
		ORef categoryTwoRef = getProject().createCategoryTwo().getRef();
		
		return new CategorizedQuantity(resourceRef, fundingSourceRef, accountingCodeRef, categoryOneRef, categoryTwoRef, new OptionalDouble(14.0));
	}
}
