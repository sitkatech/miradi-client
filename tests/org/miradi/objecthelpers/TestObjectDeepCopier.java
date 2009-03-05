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
package org.miradi.objecthelpers;

import java.util.Vector;

import org.miradi.diagram.cells.EAMGraphCell;
import org.miradi.ids.BaseId;
import org.miradi.ids.IdList;
import org.miradi.main.EAMTestCase;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ObjectDeepCopier;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Strategy;
import org.miradi.objects.Task;
import org.miradi.project.ProjectForTesting;
import org.miradi.utils.EnhancedJsonObject;

public class TestObjectDeepCopier extends EAMTestCase
{
	public TestObjectDeepCopier(String name)
	{
		super(name);
	}
	
	public void setUp() throws Exception
	{
		super.setUp();
		project = new ProjectForTesting(getName());
	}

	public void tearDown() throws Exception
	{
		super.tearDown();
		project.close();
		project = null;
	}

	public void testDeepCopy() throws Exception
	{
		ORef activityRef = project.createObject(Task.getObjectType());
		Task activity = (Task) project.findObject(activityRef);
		IdList activityIds = new IdList(Task.getObjectType());
		activityIds.add(activityRef.getObjectId());
	
		BaseId taskId = project.createObjectAndReturnId(Task.getObjectType());
		activity.addSubtaskId(taskId);
		
		ORef strategyRef = project.createObject(ObjectType.STRATEGY);
		Strategy strategy = (Strategy) project.findObject(strategyRef);
		assertEquals("wrong initial number of objects to deep copy?", 0, strategy.getAllObjectsToDeepCopy(new EAMGraphCell[0]).size());
		
		strategy.addActivity(activityRef);
		assertEquals("wrong number of objects to deep copy?", 1, strategy.getAllObjectsToDeepCopy(new EAMGraphCell[0]).size());
		ObjectDeepCopier deepCopier = new ObjectDeepCopier(project);
		Vector deepCopiedNull = deepCopier.createDeepCopy(null);
		assertEquals("deep copied null?", 0, deepCopiedNull.size());
		
		Vector deepCopiedJsonStrings = deepCopier.createDeepCopy(strategy);		
		assertEquals("not all objects copied?", 3, deepCopiedJsonStrings.size());
		 
		IdList deepCopiedFactorIds = extractRefsFromStrings(deepCopiedJsonStrings);
		assertEquals("wrong ref count?", 3, deepCopiedFactorIds.size());
		assertTrue("does not contain strategy?", deepCopiedFactorIds.contains(strategyRef.getObjectId()));
	}
	
	private IdList extractRefsFromStrings(Vector jsonStrings) throws Exception
	{
		IdList idList = new IdList(0);
		for (int i = 0 ; i < jsonStrings.size(); ++i)
		{
			EnhancedJsonObject json = new EnhancedJsonObject(jsonStrings.get(i).toString());
			idList.add(json.getId(BaseObject.TAG_ID));
		}
		
		return idList;
	}
	
	ProjectForTesting project;
}
