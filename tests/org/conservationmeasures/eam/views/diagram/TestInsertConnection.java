/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.diagram;

import java.io.IOException;

import org.conservationmeasures.eam.diagram.nodes.Node;
import org.conservationmeasures.eam.main.ProjectForTesting;
import org.conservationmeasures.eam.testall.EAMTestCase;

public class TestInsertConnection extends EAMTestCase
{
	public TestInsertConnection(String name)
	{
		super(name);
	}
	
	public void testIsAvailable() throws Exception
	{
		OpenableProject project = new OpenableProject();
		InsertConnection doer = new InsertConnection();
		doer.setProject(project);

		assertFalse("enabled when no project open?", doer.isAvailable());
		project.setIsOpen(true);
		assertFalse("Enabled when no nodes in the system?", doer.isAvailable());
		project.insertNodeAtId(Node.TYPE_TARGET, Node.INVALID_ID);
		assertFalse("Enabled when only 1 node?", doer.isAvailable());
		project.insertNodeAtId(Node.TYPE_FACTOR, Node.INVALID_ID);
		assertTrue("not enabled when 2 nodes?", doer.isAvailable());
		
	}

}

class OpenableProject extends ProjectForTesting
{
	public OpenableProject() throws IOException
	{
		super();
	}

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
