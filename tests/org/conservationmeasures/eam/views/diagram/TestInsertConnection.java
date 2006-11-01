/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.diagram;

import org.conservationmeasures.eam.commands.CommandDiagramRemoveNode;
import org.conservationmeasures.eam.diagram.DiagramModel;
import org.conservationmeasures.eam.diagram.nodes.DiagramNode;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.ModelNodeId;
import org.conservationmeasures.eam.objecthelpers.CreateModelLinkageParameter;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.project.ProjectForTesting;
import org.conservationmeasures.eam.testall.EAMTestCase;

public class TestInsertConnection extends EAMTestCase
{
	public TestInsertConnection(String name)
	{
		super(name);
	}

	public void setUp() throws Exception
	{
		super.setUp();
		project = new ProjectForTesting(getName());
		doer = new InsertConnection();
		doer.setProject(project);
	}
	
	public void tearDown() throws Exception
	{
		project.close();
		super.tearDown();
	}
	
	public void testwouldCreateLinkageLoop() throws Exception
	{
		ModelNodeId node1 = CommandDiagramRemoveNode.createNode(project, DiagramNode.TYPE_TARGET, BaseId.INVALID);
		ModelNodeId node2 = CommandDiagramRemoveNode.createNode(project, DiagramNode.TYPE_TARGET, BaseId.INVALID);
		ModelNodeId node3 = CommandDiagramRemoveNode.createNode(project, DiagramNode.TYPE_TARGET, BaseId.INVALID);
		
		CreateModelLinkageParameter parameter12 = new CreateModelLinkageParameter(node1, node2);
		project.createObject(ObjectType.MODEL_LINKAGE, BaseId.INVALID, parameter12);
		
		CreateModelLinkageParameter parameter23 = new CreateModelLinkageParameter(node2, node3);
		project.createObject(ObjectType.MODEL_LINKAGE, BaseId.INVALID, parameter23);
		
		DiagramModel model = project.getDiagramModel();
		assertTrue("Didnt catch loop?", doer.wouldCreateLinkageLoop(model, node3, node1));
		assertFalse("Prevented legal Link?", doer.wouldCreateLinkageLoop(model, node1, node3));
		assertTrue("Didnt catch link to itself?", doer.wouldCreateLinkageLoop(model, node1, node1));

	}
	
	public void testIsAvailable() throws Exception
	{
		try
		{
			assertFalse("Enabled when no nodes in the system?", doer.isAvailable());
			CommandDiagramRemoveNode.createNode(project, DiagramNode.TYPE_TARGET, BaseId.INVALID);
			assertFalse("Enabled when only 1 node?", doer.isAvailable());
			CommandDiagramRemoveNode.createNode(project, DiagramNode.TYPE_FACTOR, BaseId.INVALID);
			assertTrue("not enabled when 2 nodes?", doer.isAvailable());
		}
		finally
		{
			project.close();
		}
		assertFalse("enabled when no project open?", doer.isAvailable());
		
	}

	private ProjectForTesting project;
	private InsertConnection doer;

}
