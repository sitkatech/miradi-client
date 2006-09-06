/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objects;

import javax.swing.tree.TreePath;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.objecthelpers.ActivityInsertionPoint;
import org.conservationmeasures.eam.testall.EAMTestCase;
import org.conservationmeasures.eam.views.strategicplan.StratPlanActivity;

public class TestActivityInsertionPoint extends EAMTestCase
{
	public TestActivityInsertionPoint(String name)
	{
		super(name);
	}

	public void testValidInsertionPoint()
	{
		Task rootTask = new Task(new BaseId(23));
		Task parentTask = new Task(new BaseId(58));
		StratPlanActivity root = new StratPlanActivity(null, rootTask);
		StratPlanActivity parent = new StratPlanActivity(null, parentTask);
		TreePath path = new TreePath(new Object[] {root, parent});
		int index = 9;
		ActivityInsertionPoint aip = new ActivityInsertionPoint(path, index);
		assertEquals("wrong id?", parentTask.getId(), aip.getInterventionId());
		assertEquals("wrong index?", index, aip.getIndex());
	}
}
