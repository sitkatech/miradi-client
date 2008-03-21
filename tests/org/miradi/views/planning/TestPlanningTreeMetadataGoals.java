/* 
Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
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
