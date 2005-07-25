/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram;

import org.conservationmeasures.eam.commands.CommandDeleteLinkage;
import org.conservationmeasures.eam.commands.CommandDeleteNode;
import org.conservationmeasures.eam.commands.CommandFailedException;
import org.conservationmeasures.eam.commands.CommandInsertNode;
import org.conservationmeasures.eam.commands.CommandLinkNodes;
import org.conservationmeasures.eam.diagram.nodes.Node;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.Project;
import org.conservationmeasures.eam.testall.EAMTestCase;

public class TestDelete extends EAMTestCase
{
	public TestDelete(String name)
	{
		super(name);
	}
	
	public void testBasics() throws Exception
	{
		Project project = new Project();
		DiagramModel model = project.getDiagramModel();
		
		CommandInsertNode insertIntervention = new CommandInsertNode(Node.TYPE_INTERVENTION);
		CommandInsertNode insertThreat = new CommandInsertNode(Node.TYPE_THREAT);
		insertIntervention.execute(project);
		Node intervention = model.getNodeById(insertIntervention.getId());
		insertThreat.execute(project);
		Node threat = model.getNodeById(insertThreat.getId());
		int interventionId = model.getNodeId(intervention);
		int threatId = model.getNodeId(threat);
		CommandLinkNodes link = new CommandLinkNodes(interventionId, threatId);
		link.execute(project);
		int linkageId = link.getLinkageId();
		
		assertTrue("linkage not found?", model.hasLinkage(intervention, threat));
		
		CommandDeleteLinkage delete = new CommandDeleteLinkage(linkageId);
		delete.execute(project);
		assertFalse("linkage not deleted?", model.hasLinkage(intervention, threat));
		
		EAM.setLogToString();
		try
		{
			delete.execute(project);
			fail("should have thrown for deleting non-existant linkage");
		}
		catch(CommandFailedException ignoreExpected)
		{
		}
		EAM.setLogToConsole();
		
		CommandDeleteNode deleteNode = new CommandDeleteNode(threatId);
		deleteNode.execute(project);
		assertEquals("node not deleted?", -1, model.getNodeId(threat));

		EAM.setLogToString();
		try
		{
			deleteNode.execute(project);
			fail("should have thrown for deleting non-existant linkage");
		}
		catch(CommandFailedException ignoreExpected)
		{
		}
		EAM.setLogToConsole();
	}
}
