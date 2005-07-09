/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram;

import org.conservationmeasures.eam.commands.CommandLinkNodes;
import org.conservationmeasures.eam.commands.CommandInsertGoal;
import org.conservationmeasures.eam.commands.CommandInsertThreat;
import org.conservationmeasures.eam.diagram.nodes.Node;
import org.conservationmeasures.eam.main.Project;
import org.conservationmeasures.eam.testall.EAMTestCase;

public class TestCommandLinkNodes extends EAMTestCase
{
	public TestCommandLinkNodes(String name)
	{
		super(name);
	}

	public void testInsertConnection()
	{
		Project project = new Project();
		Node threat = (Node)new CommandInsertThreat().execute(project);
		Node goal = (Node)new CommandInsertGoal().execute(project);
		
		DiagramModel model = project.getDiagramModel();
		int threatId = model.getNodeId(threat);
		int goalId = model.getNodeId(goal);
		CommandLinkNodes command = new CommandLinkNodes(threatId, goalId);
		command.execute(project);

		assertEquals("didn't add to threat?", 1, threat.getLinkageCount());
		assertEquals("didn't add to goal?", 1, goal.getLinkageCount());
		fail("Need to figure out how to extract a linkage from a model");
	}
}
