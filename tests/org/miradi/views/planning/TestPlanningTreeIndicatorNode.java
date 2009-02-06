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
