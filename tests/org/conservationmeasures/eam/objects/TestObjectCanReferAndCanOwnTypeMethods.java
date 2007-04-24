/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objects;

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
		assertContains(ObjectType.ASSIGNMENT, BaseObject.getTypesThatCanReferToUs(ObjectType.PROJECT_RESOURCE));
		assertContains(ObjectType.ASSIGNMENT, BaseObject.getTypesThatCanReferToUs(ObjectType.ACCOUNTING_CODE));
		assertContains(ObjectType.ASSIGNMENT, BaseObject.getTypesThatCanReferToUs(ObjectType.FUNDING_SOURCE));
		assertContains(ObjectType.ASSIGNMENT, BaseObject.getTypesThatCanReferToUs(ObjectType.TASK));
		
		assertContains(ObjectType.DIAGRAM_FACTOR, BaseObject.getTypesThatCanReferToUs(ObjectType.CAUSE));
		assertContains(ObjectType.DIAGRAM_FACTOR, BaseObject.getTypesThatCanReferToUs(ObjectType.STRATEGY));
		assertContains(ObjectType.DIAGRAM_FACTOR, BaseObject.getTypesThatCanReferToUs(ObjectType.TARGET));

		assertContains(ObjectType.DIAGRAM_LINK, BaseObject.getTypesThatCanReferToUs(ObjectType.FACTOR_LINK));
		assertContains(ObjectType.DIAGRAM_LINK, BaseObject.getTypesThatCanReferToUs(ObjectType.FACTOR_LINK));

		assertContains(ObjectType.CAUSE, BaseObject.getTypesThatCanOwnUs(ObjectType.INDICATOR));
		assertContains(ObjectType.CAUSE, BaseObject.getTypesThatCanOwnUs(ObjectType.OBJECTIVE));

		assertContains(ObjectType.STRATEGY, BaseObject.getTypesThatCanOwnUs(ObjectType.INDICATOR));
		assertContains(ObjectType.STRATEGY, BaseObject.getTypesThatCanOwnUs(ObjectType.OBJECTIVE));
		assertContains(ObjectType.STRATEGY, BaseObject.getTypesThatCanOwnUs(ObjectType.TASK));

		assertContains(ObjectType.TARGET, BaseObject.getTypesThatCanOwnUs(ObjectType.INDICATOR));
		assertContains(ObjectType.TARGET, BaseObject.getTypesThatCanOwnUs(ObjectType.GOAL));
		assertContains(ObjectType.TARGET, BaseObject.getTypesThatCanOwnUs(ObjectType.KEY_ECOLOGICAL_ATTRIBUTE));

		assertContains(ObjectType.FACTOR_LINK, BaseObject.getTypesThatCanReferToUs(ObjectType.CAUSE));
		assertContains(ObjectType.FACTOR_LINK, BaseObject.getTypesThatCanReferToUs(ObjectType.STRATEGY));
		assertContains(ObjectType.FACTOR_LINK, BaseObject.getTypesThatCanReferToUs(ObjectType.TARGET));
		
		assertContains(ObjectType.INDICATOR, BaseObject.getTypesThatCanOwnUs(ObjectType.TASK));

		assertContains(ObjectType.KEY_ECOLOGICAL_ATTRIBUTE, BaseObject.getTypesThatCanOwnUs(ObjectType.INDICATOR));
		
		assertContains(ObjectType.TASK, BaseObject.getTypesThatCanOwnUs(ObjectType.TASK));
		assertContains(ObjectType.TASK, BaseObject.getTypesThatCanOwnUs(ObjectType.ASSIGNMENT));
		assertContains(ObjectType.TASK, BaseObject.getTypesThatCanReferToUs(ObjectType.TASK));

		assertContains(ObjectType.VIEW_DATA, BaseObject.getTypesThatCanReferToUs(ObjectType.CAUSE));
		assertContains(ObjectType.VIEW_DATA, BaseObject.getTypesThatCanReferToUs(ObjectType.STRATEGY));
		assertContains(ObjectType.VIEW_DATA, BaseObject.getTypesThatCanReferToUs(ObjectType.TARGET));
		
		assertContains(ObjectType.RESULTS_CHAIN_DIAGRAM, BaseObject.getTypesThatCanReferToUs(ObjectType.DIAGRAM_FACTOR));
		assertContains(ObjectType.RESULTS_CHAIN_DIAGRAM, BaseObject.getTypesThatCanReferToUs(ObjectType.DIAGRAM_LINK));
	}
}
