/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objects;

import org.conservationmeasures.eam.main.EAMTestCase;
import org.conservationmeasures.eam.objecthelpers.ObjectType;

public class TestObjectGetTypesThatCanReferToUs extends EAMTestCase
{
	public TestObjectGetTypesThatCanReferToUs(String name)
	{
		super(name);
	}

	public void testGetTypesThatCanReferToUs()
	{
		assertContains(ObjectType.ASSIGNMENT, BaseObject.getTypesThatCanReferToUs(ObjectType.PROJECT_RESOURCE));
		assertContains(ObjectType.ASSIGNMENT, BaseObject.getTypesThatCanReferToUs(ObjectType.ACCOUNTING_CODE));
		assertContains(ObjectType.ASSIGNMENT, BaseObject.getTypesThatCanReferToUs(ObjectType.FUNDING_SOURCE));
		
		assertContains(ObjectType.DIAGRAM_FACTOR, BaseObject.getTypesThatCanReferToUs(ObjectType.CAUSE));
		assertContains(ObjectType.DIAGRAM_FACTOR, BaseObject.getTypesThatCanReferToUs(ObjectType.STRATEGY));
		assertContains(ObjectType.DIAGRAM_FACTOR, BaseObject.getTypesThatCanReferToUs(ObjectType.TARGET));
		assertContains(ObjectType.DIAGRAM_FACTOR, BaseObject.getTypesThatCanReferToUs(ObjectType.INTERMEDIATE_RESULT));
		assertContains(ObjectType.DIAGRAM_FACTOR, BaseObject.getTypesThatCanReferToUs(ObjectType.THREAT_REDUCTION_RESULT));

		assertContains(ObjectType.FACTOR_LINK, BaseObject.getTypesThatCanReferToUs(ObjectType.CAUSE));
		assertContains(ObjectType.FACTOR_LINK, BaseObject.getTypesThatCanReferToUs(ObjectType.STRATEGY));
		assertContains(ObjectType.FACTOR_LINK, BaseObject.getTypesThatCanReferToUs(ObjectType.TARGET));
	
		
		assertContains(ObjectType.DIAGRAM_LINK, BaseObject.getTypesThatCanReferToUs(ObjectType.FACTOR_LINK));
		assertContains(ObjectType.DIAGRAM_LINK, BaseObject.getTypesThatCanReferToUs(ObjectType.FACTOR_LINK));

		assertContains(ObjectType.VIEW_DATA, BaseObject.getTypesThatCanReferToUs(ObjectType.CAUSE));
		assertContains(ObjectType.VIEW_DATA, BaseObject.getTypesThatCanReferToUs(ObjectType.STRATEGY));
		assertContains(ObjectType.VIEW_DATA, BaseObject.getTypesThatCanReferToUs(ObjectType.TARGET));
		
		assertContains(ObjectType.INDICATOR, BaseObject.getTypesThatCanReferToUs(ObjectType.MEASUREMENT));
	}
}
