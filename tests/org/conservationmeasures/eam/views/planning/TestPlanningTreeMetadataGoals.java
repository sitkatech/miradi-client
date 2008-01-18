/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.planning;

import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objects.Goal;
import org.conservationmeasures.eam.objects.ProjectMetadata;

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
