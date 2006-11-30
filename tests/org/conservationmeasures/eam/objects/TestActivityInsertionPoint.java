/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objects;

import javax.swing.tree.TreePath;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAMTestCase;
import org.conservationmeasures.eam.objecthelpers.ActivityInsertionPoint;
import org.conservationmeasures.eam.objecthelpers.CreateTaskParameter;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.views.workplan.WorkPlanStrategyTask;

public class TestActivityInsertionPoint extends EAMTestCase
{
	public TestActivityInsertionPoint(String name)
	{
		super(name);
	}

	public void testValidInsertionPoint() throws Exception 
	{
		ORef parentRef = new ORef(ObjectType.FACTOR, new BaseId(45));
		CreateTaskParameter extraInfo = new CreateTaskParameter(parentRef);
		Task rootTask = new Task(new BaseId(23), extraInfo);
		Task parentTask = new Task(new BaseId(58), extraInfo);
		WorkPlanStrategyTask root = new WorkPlanStrategyTask(null, rootTask);
		WorkPlanStrategyTask parent = new WorkPlanStrategyTask(null, parentTask);
		TreePath path = new TreePath(new Object[] {root, parent});
		int index = 9;
		ActivityInsertionPoint aip = new ActivityInsertionPoint(path, index);
		assertEquals("wrong id?", parentTask.getId(), aip.getProposedParentORef().getObjectId());
		assertEquals("wrong index?", index, aip.getIndex());
	}
}
