/* 
Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
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
