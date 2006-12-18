/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram;


import org.conservationmeasures.eam.commands.CommandCreateObject;
import org.conservationmeasures.eam.commands.CommandDiagramAddFactorLink;
import org.conservationmeasures.eam.commands.CommandDiagramAddFactor;
import org.conservationmeasures.eam.commands.CommandDoNothing;
import org.conservationmeasures.eam.diagram.factortypes.FactorType;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.DiagramFactorLinkId;
import org.conservationmeasures.eam.ids.DiagramFactorId;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.EAMTestCase;
import org.conservationmeasures.eam.objecthelpers.CreateFactorParameter;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.project.ProjectForTesting;
import org.conservationmeasures.eam.views.diagram.InsertFactorLinkDoer;

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

		fromId = createModelAndDiagramNodeWithCommands(Factor.TYPE_CAUSE);
		toId = createModelAndDiagramNodeWithCommands(Factor.TYPE_STRATEGY);
		CommandDiagramAddFactorLink addLinkageCommand = InsertFactorLinkDoer.createModelLinkageAndAddToDiagramUsingCommands(project, fromId, toId);
		linkId = addLinkageCommand.getDiagramFactorLinkId();
	}
	
	public void tearDown() throws Exception
	{
		project.close();
		super.tearDown();
	}

	public void testSingleUndo() throws Exception
	{
		DiagramModel model = project.getDiagramModel();
		assertTrue("no link?", model.areLinked(model.getDiagramFactorByWrappedId(fromId), model.getDiagramFactorByWrappedId(toId)));
		// undo add linkage to diagram
		project.undo();
		// undo create model linkage
		project.undo();
		assertFalse("didn't undo?", model.areLinked(model.getDiagramFactorByWrappedId(fromId), model.getDiagramFactorByWrappedId(toId)));
	}
	
	public void testMultipleUndo() throws Exception
	{
		DiagramModel model = project.getDiagramModel();
		
		// undo add linkage to diagram
		project.undo();
		// undo create model linkage
		project.undo();
		assertFalse("didn't undo?", model.areLinked(model.getDiagramFactorByWrappedId(fromId), model.getDiagramFactorByWrappedId(toId)));
		verifyLinkageNotPresent(linkId);

		// undo diagram node add
		project.undo();
		// undo model node create
		project.undo();
		verifyNodeNotPresent(toId);

		// undo diagram node add
		project.undo();
		// undo model node create
		project.undo();
		verifyNodeNotPresent(fromId);

		try
		{
			EAM.setLogToString();
			project.undo();
			fail("Should have thrown");
		}
		catch(CommandFailedException ignoreExpected)
		{
		}
		EAM.setLogToConsole();
	}
	
	public void testUndoThenCommandThenUndo() throws Exception
	{
		project.undo();
		
		FactorId modelNodeId = project.createNode(Factor.TYPE_CAUSE);
		CommandDiagramAddFactor insert = new CommandDiagramAddFactor(new DiagramFactorId(BaseId.INVALID.asInt()), modelNodeId);
		project.executeCommand(insert);
		verifyNodePresent(insert.getInsertedId());
		project.undo();
		verifyNodeNotPresent(insert.getInsertedId());

		project.undo();
		verifyLinkageNotPresent(linkId);
	
	}
	
	public void testGetIndexToUndoAndRedo() throws Exception
	{
		CommandDoNothing nop = new CommandDoNothing();
		Project p = new ProjectForTesting(getName());
		
		assertFalse("already an undoable?", p.canUndo());
		assertFalse("already a redoable?", p.canRedo());
		p.executeCommand(nop);
		assertTrue("can't undo first?", p.canUndo());
		assertFalse("redo before first undo?", p.canRedo());
		p.executeCommand(nop);
		assertTrue("can't undo second?", p.canUndo());
		assertFalse("redo when still no undo?", p.canRedo());
		p.undo();
		assertTrue("can't undo twice?", p.canUndo());
		assertTrue("can't redo first undo?", p.canRedo());
		p.executeCommand(nop);
		assertTrue("can't undo latest?", p.canUndo());
		assertFalse("can redo when undo not last?", p.canRedo());
		p.undo();
		assertTrue("can't undo earlier command?", p.canUndo());
		assertTrue("can't redo very latest?", p.canRedo());
		p.undo();
		assertFalse("can undo beyond first?", p.canUndo());
		assertTrue("can't redo after two undos?", p.canRedo());
		
		p.redo();
		assertTrue("can't undo after redo?", p.canUndo());
		assertTrue("can't redo after redo?", p.canRedo());
		
		p.close();
		
	}
	
	public void testUndoAndRedoAreStored()
	{
		
	}
	
	private void verifyNodePresent(DiagramFactorId cellId) throws Exception
	{
		DiagramModel model = project.getDiagramModel();
		assertNotNull("Node not present?", model.getDiagramFactorById(cellId));
	}
	
	private void verifyNodeNotPresent(DiagramFactorId cellId)
	{
		DiagramModel model = project.getDiagramModel();
		
		EAM.setLogToString();
		try
		{
			model.getDiagramFactorById(cellId);
			fail("Cell should be gone: " + cellId);
		}
		catch(Exception ignoreExpected)
		{
		}
		EAM.setLogToConsole();
	}
	
	private void verifyNodeNotPresent(FactorId cellId)
	{
		DiagramModel model = project.getDiagramModel();
		
		EAM.setLogToString();
		assertNull("Found the deleted factor?", model.getDiagramFactorByWrappedId(cellId));
		EAM.setLogToConsole();
	}
	
	private void verifyLinkageNotPresent(DiagramFactorLinkId cellId)
	{
		DiagramModel model = project.getDiagramModel();
		
		EAM.setLogToString();
		try
		{
			model.getDiagramFactorLinkById(cellId);
			fail("Cell should be gone: " + cellId);
		}
		catch(Exception ignoreExpected)
		{
		}
		EAM.setLogToConsole();
	}
	
	private FactorId createModelAndDiagramNodeWithCommands(FactorType type) throws Exception
	{
		CreateFactorParameter extraInfo = new CreateFactorParameter(Factor.TYPE_CAUSE);
		CommandCreateObject createModelNodeCommand = new CommandCreateObject(ObjectType.FACTOR, extraInfo);
		project.executeCommand(createModelNodeCommand);
		FactorId modelNodeId = (FactorId)createModelNodeCommand.getCreatedId();
		CommandDiagramAddFactor addToDiagramCommand = new CommandDiagramAddFactor(new DiagramFactorId(BaseId.INVALID.asInt()), modelNodeId);
		project.executeCommand(addToDiagramCommand);
		return modelNodeId;
		
	}
	
	ProjectForTesting project;
	FactorId fromId;
	FactorId toId;
	DiagramFactorLinkId linkId;
}
