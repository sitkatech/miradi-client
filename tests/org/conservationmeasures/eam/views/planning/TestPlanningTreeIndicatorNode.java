/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.planning;

import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.objects.Task;

public class TestPlanningTreeIndicatorNode extends TestPlanningTree
{
	public TestPlanningTreeIndicatorNode(String name)
	{
		super(name);
	}
	
	public void testPlanningTreeIndicatorNode() throws Exception
	{
		String relatedMethods = getIndicator().getPseudoData(Indicator.PSEUDO_TAG_RELATED_METHOD_OREF_LIST);
		ORefList methodRefs = new ORefList(relatedMethods);
		assertEquals("wrong method count?", 1, methodRefs.size());
		assertTrue("wrong type returned?", isMethod(methodRefs.get(0)));
	}

	private boolean isMethod(ORef ref)
	{
		Task task = (Task) project.findObject(ref);
		return task.isMethod();
	}
}
