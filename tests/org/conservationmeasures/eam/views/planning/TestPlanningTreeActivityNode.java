/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.planning;

import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objects.Task;

public class TestPlanningTreeActivityNode extends TestPlanningTree
{
	public TestPlanningTreeActivityNode(String name)
	{
		super(name);
	}
	
	public void testPlanningTreeActivityNode() throws Exception
	{
		String relatedTasks = getTask().getPseudoData(Task.PSEUDO_TAG_RELATED_SUBTASKS_OREF_LIST);
		ORefList taskRefs = new ORefList(relatedTasks);
		assertEquals("wrong objective count?", 1, taskRefs.size());
		assertEquals("wrong type returned?", Task.getObjectType(), taskRefs.get(0).getObjectType());
	}
}
