/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.diagram;

import org.conservationmeasures.eam.commands.CommandInsertNode;
import org.conservationmeasures.eam.diagram.nodes.DiagramNode;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.project.ProjectForTesting;
import org.conservationmeasures.eam.testall.EAMTestCase;

public class TestInsertConnection extends EAMTestCase
{
	public TestInsertConnection(String name)
	{
		super(name);
	}
	
	public void testIsAvailable() throws Exception
	{
		ProjectForTesting project = new ProjectForTesting(getName());
		InsertConnection doer = new InsertConnection();
		doer.setProject(project);
		try
		{
			assertFalse("Enabled when no nodes in the system?", doer.isAvailable());
			CommandInsertNode.createNode(project, DiagramNode.TYPE_TARGET, BaseId.INVALID);
			assertFalse("Enabled when only 1 node?", doer.isAvailable());
			CommandInsertNode.createNode(project, DiagramNode.TYPE_FACTOR, BaseId.INVALID);
			assertTrue("not enabled when 2 nodes?", doer.isAvailable());
		}
		finally
		{
			project.close();
		}
		assertFalse("enabled when no project open?", doer.isAvailable());
		
	}

}
