/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram;


import org.conservationmeasures.eam.commands.CommandDoNothing;
import org.conservationmeasures.eam.commands.CommandInsertNode;
import org.conservationmeasures.eam.commands.CommandLinkNodes;
import org.conservationmeasures.eam.commands.CommandRedo;
import org.conservationmeasures.eam.commands.CommandUndo;
import org.conservationmeasures.eam.diagram.nodes.DiagramNode;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.project.ProjectForTesting;
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
		project = new ProjectForTesting(getName());
		
		CommandInsertNode insertFactor = new CommandInsertNode(DiagramNode.TYPE_INDIRECT_FACTOR);
		project.executeCommand(insertFactor);
		CommandInsertNode insertIntervention = new CommandInsertNode(DiagramNode.TYPE_INTERVENTION);
		project.executeCommand(insertIntervention);
		fromId = insertFactor.getId();
		toId = insertIntervention.getId();
		CommandLinkNodes link = new CommandLinkNodes(fromId, toId);
		project.executeCommand(link);
		linkId = link.getLinkageId();
	}
	
	public void tearDown() throws Exception
	{
		project.close();
		super.tearDown();
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
		verifyLinkageNotPresent(linkId);
		
		project.executeCommand(undo);
		verifyNodeNotPresent(toId);

		project.executeCommand(undo);
		verifyNodeNotPresent(fromId);

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
		
		CommandInsertNode insert = new CommandInsertNode(DiagramNode.TYPE_TARGET);
		project.executeCommand(insert);
		project.executeCommand(undo);
		verifyNodeNotPresent(insert.getId());

		project.executeCommand(undo);
		verifyLinkageNotPresent(linkId);
	
	}
	
	public void testGetIndexToUndoAndRedo() throws Exception
	{
		CommandDoNothing nop = new CommandDoNothing();
		CommandUndo undo = new CommandUndo();
		CommandRedo redo = new CommandRedo();
		Project p = new ProjectForTesting(getName());
		
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
		
		p.close();
		
	}
	
	public void testUndoAndRedoAreStored()
	{
		
	}
	
	private void verifyNodeNotPresent(BaseId cellId)
	{
		DiagramModel model = project.getDiagramModel();
		
		EAM.setLogToString();
		try
		{
			model.getNodeById(cellId);
			fail("Cell should be gone: " + cellId);
		}
		catch(Exception ignoreExpected)
		{
		}
		EAM.setLogToConsole();
	}
	
	private void verifyLinkageNotPresent(BaseId cellId)
	{
		DiagramModel model = project.getDiagramModel();
		
		EAM.setLogToString();
		try
		{
			model.getLinkageById(cellId);
			fail("Cell should be gone: " + cellId);
		}
		catch(Exception ignoreExpected)
		{
		}
		EAM.setLogToConsole();
	}
	
	ProjectForTesting project;
	BaseId fromId;
	BaseId toId;
	BaseId linkId;
}
