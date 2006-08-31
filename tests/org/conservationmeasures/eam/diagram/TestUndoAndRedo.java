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
		
		assertFalse("already an undoable?", p.canUndo());
		assertFalse("already a redoable?", p.canRedo());
		p.executeCommand(nop);
		assertTrue("can't undo first?", p.canUndo());
		assertFalse("redo before first undo?", p.canRedo());
		p.executeCommand(nop);
		assertTrue("can't undo second?", p.canUndo());
		assertFalse("redo when still no undo?", p.canRedo());
		p.executeCommand(undo);
		assertTrue("can't undo twice?", p.canUndo());
		assertTrue("can't redo first undo?", p.canRedo());
		p.executeCommand(nop);
		assertTrue("can't undo latest?", p.canUndo());
		assertFalse("can redo when undo not last?", p.canRedo());
		p.executeCommand(undo);
		assertTrue("can't undo earlier command?", p.canUndo());
		assertTrue("can't redo very latest?", p.canRedo());
		p.executeCommand(undo);
		assertFalse("can undo beyond first?", p.canUndo());
		assertTrue("can't redo after two undos?", p.canRedo());
		
		p.executeCommand(redo);
		assertTrue("can't undo after redo?", p.canUndo());
		assertTrue("can't redo after redo?", p.canRedo());
		
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
