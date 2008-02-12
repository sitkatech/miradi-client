/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.views.planning;

import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.Task;

public class TestPlanningTreeStrategyNode extends TestPlanningTree
{
	public TestPlanningTreeStrategyNode(String name)
	{
		super(name);
	}
	
	public void testPlanningTreeStrategyNode() throws Exception
	{
		ORefList activityRefs = getStrategy().getActivities();
		assertEquals("wrong activity count?", 1, activityRefs.size());
		assertTrue("wrong type returned?", isActivity(activityRefs.get(0)));
	}

	private boolean isActivity(ORef ref)
	{
		Task task = (Task) project.findObject(ref);
		return task.isActivity();
	}
}
