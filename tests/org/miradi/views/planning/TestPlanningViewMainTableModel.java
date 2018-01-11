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

package org.miradi.views.planning;

import org.miradi.main.TestCaseWithProject;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Strategy;
import org.miradi.objects.Task;

public class TestPlanningViewMainTableModel extends TestCaseWithProject
{
	public TestPlanningViewMainTableModel(String name)
	{
		super(name);
	}
	
	public void testCanOwnAssignments()
	{
		for (int type = ObjectType.FIRST_OBJECT_TYPE; type < ObjectType.OBJECT_TYPE_COUNT; ++type)
		{
			if (Strategy.is(type) || Task.is(type))
				assertTrue("type " + type + "cannot refer to assignments?", canOwnAssignments(type));
			else
				assertFalse("Type" + type + " can refer to assignments?", canOwnAssignments(type));
		}
	}
	
	private boolean canOwnAssignments(int objectType)
	{
		return BaseObject.canOwnPlanningObjects(ORef.createInvalidWithType(objectType));
	}
}
