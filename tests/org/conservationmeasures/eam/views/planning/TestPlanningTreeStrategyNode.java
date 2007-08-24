/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.planning;

import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objects.Strategy;
import org.conservationmeasures.eam.objects.Task;

public class TestPlanningTreeStrategyNode extends TestPlanningTree
{
	public TestPlanningTreeStrategyNode(String name)
	{
		super(name);
	}
	
	public void testPlanningTreeStrategyNode() throws Exception
	{
		String relatedActivities = getStrategy().getPseudoData(Strategy.PSEUDO_TAG_RELATED_ACTIVITY_OREF_LIST);
		ORefList activityRefs = new ORefList(relatedActivities);
		assertEquals("wrong activity count?", 1, activityRefs.size());
		assertTrue("wrong type returned?", isActivity(activityRefs.get(0)));
	}

	private boolean isActivity(ORef ref)
	{
		Task task = (Task) project.findObject(ref);
		return task.isActivity();
	}
}
