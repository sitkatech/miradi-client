/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.views.planning;

import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.Goal;
import org.miradi.objects.ProjectMetadata;

public class TestPlanningTreeMetadataGoals extends TestPlanningTree
{
	public TestPlanningTreeMetadataGoals(String name)
	{
		super(name);
	}
	
	public void testPlanningTreeMetadataGoalNode() throws Exception
	{
		String goalRefsAsString = getProjectMetadata().getPseudoData(ProjectMetadata.PSEUDO_TAG_RELATED_GOAL_REFS);
		ORefList goalRefs = new ORefList(goalRefsAsString);
		assertEquals("wrong goal count?", 1, goalRefs.size());
		assertEquals("wrong type returned?", Goal.getObjectType(), goalRefs.get(0).getObjectType());
	}

}
