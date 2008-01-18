/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objects;

import org.conservationmeasures.eam.main.EAMTestCase;
import org.conservationmeasures.eam.objecthelpers.ObjectType;

public class TestObjectGetTypesThatCanOwnUs extends EAMTestCase
{
	public TestObjectGetTypesThatCanOwnUs(String name)
	{
		super(name);
	}
	
	public void testGetTypesThatCanOwnUs()
	{
		assertContains(ObjectType.CAUSE, BaseObject.getTypesThatCanOwnUs(ObjectType.INDICATOR));
		assertContains(ObjectType.CAUSE, BaseObject.getTypesThatCanOwnUs(ObjectType.OBJECTIVE));

		assertContains(ObjectType.STRATEGY, BaseObject.getTypesThatCanOwnUs(ObjectType.INDICATOR));
		assertContains(ObjectType.STRATEGY, BaseObject.getTypesThatCanOwnUs(ObjectType.OBJECTIVE));
		assertContains(ObjectType.STRATEGY, BaseObject.getTypesThatCanReferToUs(ObjectType.TASK));

		assertContains(ObjectType.TARGET, BaseObject.getTypesThatCanOwnUs(ObjectType.INDICATOR));
		assertContains(ObjectType.TARGET, BaseObject.getTypesThatCanOwnUs(ObjectType.GOAL));
		assertContains(ObjectType.TARGET, BaseObject.getTypesThatCanOwnUs(ObjectType.KEY_ECOLOGICAL_ATTRIBUTE));

		assertContains(ObjectType.INDICATOR, BaseObject.getTypesThatCanReferToUs(ObjectType.TASK));

		assertContains(ObjectType.KEY_ECOLOGICAL_ATTRIBUTE, BaseObject.getTypesThatCanOwnUs(ObjectType.INDICATOR));
		
		assertContains(ObjectType.TASK, BaseObject.getTypesThatCanOwnUs(ObjectType.TASK));
		assertContains(ObjectType.TASK, BaseObject.getTypesThatCanOwnUs(ObjectType.ASSIGNMENT));
				
		assertContains(ObjectType.INTERMEDIATE_RESULT, BaseObject.getTypesThatCanOwnUs(ObjectType.INDICATOR));
		assertContains(ObjectType.INTERMEDIATE_RESULT, BaseObject.getTypesThatCanOwnUs(ObjectType.OBJECTIVE));
		
		assertContains(ObjectType.THREAT_REDUCTION_RESULT, BaseObject.getTypesThatCanOwnUs(ObjectType.INDICATOR));
		assertContains(ObjectType.THREAT_REDUCTION_RESULT, BaseObject.getTypesThatCanOwnUs(ObjectType.OBJECTIVE));
	
		assertContains(ObjectType.RESULTS_CHAIN_DIAGRAM, BaseObject.getTypesThatCanOwnUs(ObjectType.DIAGRAM_FACTOR));
		assertContains(ObjectType.RESULTS_CHAIN_DIAGRAM, BaseObject.getTypesThatCanOwnUs(ObjectType.DIAGRAM_LINK));
		
		assertContains(ObjectType.CONCEPTUAL_MODEL_DIAGRAM, BaseObject.getTypesThatCanOwnUs(ObjectType.DIAGRAM_FACTOR));
		assertContains(ObjectType.CONCEPTUAL_MODEL_DIAGRAM, BaseObject.getTypesThatCanOwnUs(ObjectType.DIAGRAM_LINK));
	}

}
