/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram;

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
	
	public void setUp() throws Exception
	{
		super.setUp();
		project = new BaseProject();
		
		CommandInsertNode insertThreat = new CommandInsertNode(Node.TYPE_THREAT);
		project.executeCommand(insertThreat);
		CommandInsertNode insertIntervention = new CommandInsertNode(Node.TYPE_INTERVENTION);
		project.executeCommand(insertIntervention);
		fromId = insertThreat.getId();
		toId = insertIntervention.getId();
		CommandLinkNodes link = new CommandLinkNodes(fromId, toId);
		project.executeCommand(link);
		linkId = link.getLinkageId();
	}

	public void testSingleUndo() throws Exception
	{
		DiagramModel model = project.getDiagramModel();
		assertTrue("no linkage?", model.hasLinkage(model.getNodeById(fromId), model.getNodeById(toId)));
		CommandUndo undo = new CommandUndo();
		project.executeCommand(undo);
		assertFalse("didn't undo?", model.hasLinkage(model.getNodeById(fromId), model.getNodeById(toId)));
	}
	
	public void testMultipleUndo() throws Exception
	{
		DiagramModel model = project.getDiagramModel();
		
		CommandUndo undo = new CommandUndo();
		project.executeCommand(undo);
		assertFalse("didn't undo?", model.hasLinkage(model.getNodeById(fromId), model.getNodeById(toId)));
		verifyNotPresent(linkId);
		
		project.executeCommand(undo);
		verifyNotPresent(toId);

		project.executeCommand(undo);
		verifyNotPresent(fromId);

		EAM.setLogToString();
		try
		{
			project.executeCommand(undo);
			fail("can't undo beyond first command");
		}
		catch(Exception ignoreExpected)
		{
		}
		EAM.setLogToConsole();
	}
	
	public void testUndoActUndo() throws Exception
	{
		CommandUndo undo = new CommandUndo();
		project.executeCommand(undo);
		
		CommandInsertNode insert = new CommandInsertNode(Node.TYPE_GOAL);
		project.executeCommand(insert);
		project.executeCommand(undo);
		verifyNotPresent(insert.getId());
	}

	private void verifyNotPresent(int cellId)
	{
		DiagramModel model = project.getDiagramModel();
		
		EAM.setLogToString();
		try
		{
			model.getCellById(cellId);
			fail("Cell should be gone: " + cellId);
		}
		catch(Exception ignoreExpected)
		{
		}
		EAM.setLogToConsole();
	}
	
	BaseProject project;
	int fromId;
	int toId;
	int linkId;
}
