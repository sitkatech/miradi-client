/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram;

import org.conservationmeasures.eam.commands.CommandFailedException;
import org.conservationmeasures.eam.commands.CommandInsertNode;
import org.conservationmeasures.eam.commands.CommandLinkNodes;
import org.conservationmeasures.eam.commands.CommandUndo;
import org.conservationmeasures.eam.diagram.nodes.Node;
import org.conservationmeasures.eam.main.BaseProject;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.testall.EAMTestCase;

public class TestUndo extends EAMTestCase
{
	public TestUndo(String name)
	{
		super(name);
	}

	public void testBasics() throws Exception
	{
		BaseProject project = new BaseProject();
		DiagramModel model = project.getDiagramModel();
		
		CommandInsertNode insertThreat = new CommandInsertNode(Node.TYPE_THREAT);
		project.executeCommand(insertThreat);
		CommandInsertNode insertIntervention = new CommandInsertNode(Node.TYPE_INTERVENTION);
		project.executeCommand(insertIntervention);
		int fromId = insertThreat.getId();
		int toId = insertIntervention.getId();
		CommandLinkNodes link = new CommandLinkNodes(fromId, toId);
		project.executeCommand(link);
		assertTrue("no linkage?", model.hasLinkage(model.getNodeById(fromId), model.getNodeById(toId)));
		CommandUndo undo = new CommandUndo();
		project.executeCommand(undo);
		assertFalse("didn't undo?", model.hasLinkage(model.getNodeById(fromId), model.getNodeById(toId)));
		try
		{
			EAM.setLogToString();
			project.executeCommand(undo);
		}
		catch(CommandFailedException ignoreExpected)
		{
		}
		EAM.setLogToConsole();
		
	}
}
