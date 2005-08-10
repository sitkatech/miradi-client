/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram.nodes;


import org.conservationmeasures.eam.commands.CommandDoNothing;
import org.conservationmeasures.eam.commands.CommandInsertNode;
import org.conservationmeasures.eam.commands.CommandLinkNodes;
import org.conservationmeasures.eam.commands.CommandRedo;
import org.conservationmeasures.eam.commands.CommandUndo;
import org.conservationmeasures.eam.diagram.DiagramModel;
import org.conservationmeasures.eam.diagram.nodes.Node;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.BaseProject;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.testall.EAMTestCase;

public class TestUndoAndRedo extends EAMTestCase
{
	public TestUndoAndRedo(String name)
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
		assertTrue("no linkage?", model.hasLinkage(model.getNodebyId(fromId), model.getNodebyId(toId)));
		CommandUndo undo = new CommandUndo();
		project.executeCommand(undo);
		assertFalse("didn't undo?", model.hasLinkage(model.getNodebyId(fromId), model.getNodebyId(toId)));
	}
	
	public void testMultipleUndo() throws Exception
	{
		DiagramModel model = project.getDiagramModel();
		
		CommandUndo undo = new CommandUndo();
		project.executeCommand(undo);
		assertFalse("didn't undo?", model.hasLinkage(model.getNodebyId(fromId), model.getNodebyId(toId)));
		verifyNotPresent(linkId);
		
		project.executeCommand(undo);
		verifyNotPresent(toId);

		project.executeCommand(undo);
		verifyNotPresent(fromId);

		try
		{
			EAM.setLogToString();
			project.executeCommand(undo);
			fail("Should have thrown");
		}
		catch(CommandFailedException ignoreExpected)
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

		project.executeCommand(undo);
		verifyNotPresent(linkId);
	
	}
	
	public void testGetIndexToUndoAndRedo() throws Exception
	{
		CommandDoNothing nop = new CommandDoNothing();
		CommandUndo undo = new CommandUndo();
		CommandRedo redo = new CommandRedo();
		BaseProject p = new BaseProject();
		
		assertEquals("already an undoable?", -1, p.getIndexToUndo());
		assertEquals("already a redoable?", -1, p.getIndexToRedo());
		p.executeCommand(nop);
		assertEquals("can't undo first?", 0, p.getIndexToUndo());
		assertEquals("redo before first undo?", -1, p.getIndexToRedo());
		p.executeCommand(nop);
		assertEquals("can't undo second?", 1, p.getIndexToUndo());
		assertEquals("redo when still no undo?", -1, p.getIndexToRedo());
		p.executeCommand(undo);
		assertEquals("can't undo twice?", 0, p.getIndexToUndo());
		assertEquals("can't redo first undo?", 1, p.getIndexToRedo());
		p.executeCommand(nop);
		assertEquals("can't undo latest?", 3, p.getIndexToUndo());
		assertEquals("redo when undo not last?", -1, p.getIndexToRedo());
		p.executeCommand(undo);
		assertEquals("can't undo earlier command?", 0, p.getIndexToUndo());
		assertEquals("can't redo very latest?", 3, p.getIndexToRedo());
		p.executeCommand(undo);
		assertEquals("can undo beyond first?", -1, p.getIndexToUndo());
		assertEquals("can't redo after two undos?", 0, p.getIndexToRedo());
		
		p.executeCommand(redo);
		assertEquals("can't undo after redo?", 0, p.getIndexToUndo());
		assertEquals("can't redo after redo?", 3, p.getIndexToRedo());
		
	}
	
	public void testUndoAndRedoAreStored()
	{
		
	}
	
	private void verifyNotPresent(int cellId)
	{
		DiagramModel model = project.getDiagramModel();
		
		EAM.setLogToString();
		try
		{
			model.getCellbyId(cellId);
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
