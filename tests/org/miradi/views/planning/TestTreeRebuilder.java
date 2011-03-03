/* 
Copyright 2005-2011, Foundations of Success, Bethesda, Maryland 
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

import org.miradi.main.TestCaseWithProject;

public class TestTreeRebuilder extends TestCaseWithProject
{
	public TestTreeRebuilder(String name)
	{
		super(name);
	}
	
//FIXME urgent - The below commented code is from test classes.  They were testing old nodes.  Need
// to convert the test to test new TreeRebuilder. 
	
//	public void testPlanningTreeStrategyNode() throws Exception
//	{
//		ORefList activityRefs = getStrategy().getActivityRefs();
//		assertEquals("wrong activity count?", 1, activityRefs.size());
//		assertTrue("wrong type returned?", isActivity(activityRefs.get(0)));
//	}
//	
//	private boolean isActivity(ORef ref)
//	{
//		Task task = (Task) project.findObject(ref);
//		return task.isActivity();
//	}
	
	
//	public void testPlanningTreeActivityNode() throws Exception
//	{
//		ORefList taskRefs = getTask().getSubTaskRefs();
//		assertEquals("wrong subtask count?", 1, taskRefs.size());
//		assertEquals("wrong type returned?", Task.getObjectType(), taskRefs.get(0).getObjectType());
//	}
//
//	public void testSubtasks() throws Exception
//	{
//		AbstractProjectNode root = createCompleteTree();
//		ORefSet refsInTree = root.getAllRefsInTree();
//		assertTrue("Didn't add subtask to tree?", refsInTree.contains(getSubtask().getRef()));
//	}
//
//	public void testMerging() throws Exception
//	{
//		CodeList rowCodes = new CodeList();
//		rowCodes.add(Task.ACTIVITY_NAME);
//		HiddenConfigurableProjectRootNode root = new HiddenConfigurableProjectRootNode(project, rowCodes);
//		assertEquals(1, root.getChildCount());
//
//		final TreeTableNode firstChild = root.getChild(0);
//		assertEquals(Task.getObjectType(), firstChild.getType());
//		assertEquals(2, firstChild.getProportionShares());
//		assertFalse("Full proportion task is allocated?", firstChild.areBudgetValuesAllocated());
//
//	}
	
	
//	public void testPlanningTreeIndicatorNode() throws Exception
//	{
//		String relatedMethods = getIndicator().getPseudoData(Indicator.PSEUDO_TAG_RELATED_METHOD_OREF_LIST);
//		ORefList methodRefs = new ORefList(relatedMethods);
//		assertEquals("wrong method count?", 1, methodRefs.size());
//		assertTrue("wrong type returned?", isMethod(methodRefs.get(0)));
//	}
//	
//	private boolean isMethod(ORef ref)
//	{
//		Task task = (Task) project.findObject(ref);
//		return task.isMethod();
//	}

	
	
	
	


}
