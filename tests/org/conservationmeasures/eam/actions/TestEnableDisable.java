/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.actions;

import org.conservationmeasures.eam.diagram.nodes.Node;
import org.conservationmeasures.eam.main.BaseProject;
import org.conservationmeasures.eam.testall.EAMTestCase;

public class TestEnableDisable extends EAMTestCase
{
	public TestEnableDisable(String name)
	{
		super(name);
	}
	
	public void testInsertConnection()
	{
		OpenableProject project = new OpenableProject();
		ActionInsertConnection action = new ActionInsertConnection(project);

		assertFalse("enabled when no project open?", action.shouldBeEnabled());
		project.setIsOpen(true);
		assertFalse("Enabled when no nodes in the system?", action.shouldBeEnabled());
		project.insertNodeAtId(Node.TYPE_GOAL, Node.INVALID_ID);
		assertFalse("Enabled when only 1 node?", action.shouldBeEnabled());
		project.insertNodeAtId(Node.TYPE_THREAT, Node.INVALID_ID);
		assertTrue("not enabled when 2 nodes?", action.shouldBeEnabled());
		
	}

}

class OpenableProject extends BaseProject
{
	public boolean isOpen()
	{
		return isOpenFlag;
	}
	
	public void setIsOpen(boolean newValue)
	{
		isOpenFlag = newValue;
	}
	
	boolean isOpenFlag;
}
