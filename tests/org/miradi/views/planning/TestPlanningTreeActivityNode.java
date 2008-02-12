/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.views.planning;

import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.Task;

public class TestPlanningTreeActivityNode extends TestPlanningTree
{
	public TestPlanningTreeActivityNode(String name)
	{
		super(name);
	}
	
	public void testPlanningTreeActivityNode() throws Exception
	{
		ORefList taskRefs = getTask().getSubtasks();
		assertEquals("wrong subtask count?", 1, taskRefs.size());
		assertEquals("wrong type returned?", Task.getObjectType(), taskRefs.get(0).getObjectType());
	}
}
