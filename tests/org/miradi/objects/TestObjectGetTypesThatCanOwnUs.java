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
package org.miradi.objects;

import org.miradi.main.EAMTestCase;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.BaseObject;

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

		assertContains(ObjectType.TARGET, BaseObject.getTypesThatCanOwnUs(ObjectType.INDICATOR));
		assertContains(ObjectType.TARGET, BaseObject.getTypesThatCanOwnUs(ObjectType.GOAL));
		assertContains(ObjectType.TARGET, BaseObject.getTypesThatCanOwnUs(ObjectType.KEY_ECOLOGICAL_ATTRIBUTE));

		assertContains(ObjectType.KEY_ECOLOGICAL_ATTRIBUTE, BaseObject.getTypesThatCanOwnUs(ObjectType.INDICATOR));
		
		assertContains(ObjectType.TASK, BaseObject.getTypesThatCanOwnUs(ObjectType.TASK));
		assertContains(ObjectType.TASK, BaseObject.getTypesThatCanOwnUs(ObjectType.RESOURCE_ASSIGNMENT));
				
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
