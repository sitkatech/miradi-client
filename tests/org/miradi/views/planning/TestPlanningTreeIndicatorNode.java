/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.views.planning;

import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.Indicator;
import org.miradi.objects.Task;

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
