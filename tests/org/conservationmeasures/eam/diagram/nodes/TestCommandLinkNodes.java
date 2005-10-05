/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram.nodes;

import org.conservationmeasures.eam.commands.CommandInsertNode;
import org.conservationmeasures.eam.commands.CommandLinkNodes;
import org.conservationmeasures.eam.diagram.DiagramModel;
import org.conservationmeasures.eam.main.ProjectForTesting;
import org.conservationmeasures.eam.testall.EAMTestCase;

public class TestCommandLinkNodes extends EAMTestCase
{
	public TestCommandLinkNodes(String name)
	{
		super(name);
	}

	public void testInsertConnection() throws Exception
	{
		ProjectForTesting project = new ProjectForTesting(getName());
		DiagramModel model = project.getDiagramModel();

		CommandInsertNode insertFactor = new CommandInsertNode(Node.TYPE_FACTOR);
		insertFactor.execute(project);
		int factorId = insertFactor.getId();
		Node factor = model.getNodeById(factorId);
		CommandInsertNode insertTarget = new CommandInsertNode(Node.TYPE_TARGET);
		insertTarget.execute(project);
		int targetId = insertTarget.getId();
		Node target = model.getNodeById(targetId);
		
		CommandLinkNodes command = new CommandLinkNodes(factorId, targetId);
		command.execute(project);
		Linkage linkage = model.getLinkageById(command.getLinkageId());

		assertEquals("not from factor?", factor, linkage.getFromNode());
		assertEquals("not to target?", target, linkage.getToNode());
	}
}
