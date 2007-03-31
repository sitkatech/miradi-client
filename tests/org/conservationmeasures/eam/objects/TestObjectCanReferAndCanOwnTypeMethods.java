/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objects;

import java.util.Arrays;

import org.conservationmeasures.eam.main.EAMTestCase;
import org.conservationmeasures.eam.objecthelpers.ObjectType;

public class TestObjectCanReferAndCanOwnTypeMethods extends EAMTestCase
{
	public TestObjectCanReferAndCanOwnTypeMethods(String name)
	{
		super(name);
	}

	public void testCanXXXXThisType()
	{
		assertEquals(true, Assignment.canReferToThisType(ObjectType.PROJECT_RESOURCE));
		assertEquals(true, Assignment.canReferToThisType(ObjectType.ACCOUNTING_CODE));
		assertEquals(true, Assignment.canReferToThisType(ObjectType.FUNDING_SOURCE));
		assertEquals(true, Assignment.canReferToThisType(ObjectType.TASK));
		assertEquals(false, Assignment.canReferToThisType(ObjectType.FACTOR));
		assertEquals(false, Assignment.canOwnThisType(ObjectType.FACTOR));
		
		assertEquals(true, DiagramFactor.canReferToThisType(ObjectType.FACTOR));
		assertEquals(false, DiagramFactor.canReferToThisType(ObjectType.FACTOR_LINK));
		assertEquals(false, DiagramFactor.canOwnThisType(ObjectType.FACTOR));
		
		assertEquals(true, DiagramFactorLink.canReferToThisType(ObjectType.FACTOR_LINK));
		assertEquals(true, DiagramFactorLink.canReferToThisType(ObjectType.DIAGRAM_FACTOR));
		assertEquals(false, DiagramFactorLink.canReferToThisType(ObjectType.FACTOR));
		assertEquals(false, DiagramFactorLink.canOwnThisType(ObjectType.FACTOR));
		
		assertEquals(true, Factor.canOwnThisType(ObjectType.INDICATOR));
		assertEquals(true, Factor.canOwnThisType(ObjectType.GOAL));
		assertEquals(true, Factor.canOwnThisType(ObjectType.OBJECTIVE));
		assertEquals(true, Factor.canOwnThisType(ObjectType.TASK));
		assertEquals(true, Factor.canOwnThisType(ObjectType.KEY_ECOLOGICAL_ATTRIBUTE));
		assertEquals(false, Factor.canOwnThisType(ObjectType.FACTOR));
		
		assertEquals(true, FactorLink.canReferToThisType(ObjectType.FACTOR));
		assertEquals(false, FactorLink.canReferToThisType(ObjectType.FACTOR_LINK));
		assertEquals(false, FactorLink.canOwnThisType(ObjectType.FACTOR));
		
		assertEquals(true, Indicator.canOwnThisType(ObjectType.GOAL));
		assertEquals(true, Indicator.canOwnThisType(ObjectType.TASK));
		assertEquals(false, Indicator.canOwnThisType(ObjectType.FACTOR));
		assertEquals(false, Indicator.canReferToThisType(ObjectType.FACTOR));
		
		assertEquals(true, KeyEcologicalAttribute.canOwnThisType(ObjectType.INDICATOR));
		assertEquals(false, KeyEcologicalAttribute.canOwnThisType(ObjectType.FACTOR));
		assertEquals(false, KeyEcologicalAttribute.canReferToThisType(ObjectType.FACTOR));
		
		assertEquals(true, ProjectMetadata.canReferToThisType(ObjectType.PROJECT_RESOURCE));
		assertEquals(false, Indicator.canReferToThisType(ObjectType.FACTOR));
		assertEquals(false, ProjectMetadata.canOwnThisType(ObjectType.FACTOR));
		
		assertEquals(true, Task.canOwnThisType(ObjectType.TASK));
		assertEquals(true, Task.canOwnThisType(ObjectType.ASSIGNMENT));
		assertEquals(true, Task.canReferToThisType(ObjectType.TASK));
		assertEquals(false, Task.canOwnThisType(ObjectType.FACTOR));
		assertEquals(false, Task.canReferToThisType(ObjectType.FACTOR_LINK));
		
		assertEquals(true, ViewData.canReferToThisType(ObjectType.FACTOR));
		assertEquals(false, ViewData.canOwnThisType(ObjectType.FACTOR));
		
		//FIXME: convert above to look like this, and get rid of method below
		assertContains(ObjectType.VIEW_DATA, BaseObject.getTypesThatCanReferToUs(ObjectType.FACTOR));
	}
	
	
	public void testGetTypesThatCanXXXUs()
	{
		int[] referTypes = BaseObject.getTypesThatCanReferToUs(ObjectType.TASK);
		Arrays.sort(referTypes);
		assertEquals(false, Arrays.binarySearch(referTypes, ObjectType.ASSIGNMENT)<0);
		assertEquals(false, Arrays.binarySearch(referTypes, ObjectType.TASK)<0);
		assertEquals(true, Arrays.binarySearch(referTypes, ObjectType.GOAL)<0);
		assertEquals(true, Arrays.binarySearch(referTypes, ObjectType.INDICATOR)<0);
		
		int[] ownedTypes = BaseObject.getTypesThatCanOwnUs(ObjectType.TASK);
		Arrays.sort(ownedTypes);
		assertEquals(false, Arrays.binarySearch(ownedTypes, ObjectType.INDICATOR)<0);
		assertEquals(false, Arrays.binarySearch(referTypes, ObjectType.TASK)<0);
		assertEquals(true, Arrays.binarySearch(ownedTypes, ObjectType.GOAL)<0);
		assertEquals(true, Arrays.binarySearch(ownedTypes, ObjectType.ASSIGNMENT)<0);
	}
	
}
