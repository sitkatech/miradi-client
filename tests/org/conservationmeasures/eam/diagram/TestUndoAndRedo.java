/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram;


import org.conservationmeasures.eam.commands.CommandCreateObject;
import org.conservationmeasures.eam.commands.CommandDiagramAddFactor;
import org.conservationmeasures.eam.commands.CommandDiagramAddFactorLink;
import org.conservationmeasures.eam.commands.CommandJump;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.ids.DiagramFactorId;
import org.conservationmeasures.eam.ids.DiagramFactorLinkId;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.EAMTestCase;
import org.conservationmeasures.eam.objecthelpers.CreateDiagramFactorParameter;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
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

		fromId = createModelAndDiagramNodeWithCommands(ObjectType.CAUSE);
		toId = createModelAndDiagramNodeWithCommands(ObjectType.STRATEGY);
		CommandDiagramAddFactorLink addLinkageCommand = InsertFactorLinkDoer.createModelLinkageAndAddToDiagramUsingCommands(project.getDiagramModel(), fromId, toId);
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
		assertTrue("no link?", model.areLinked(model.getDiagramFactorIdFromWrappedId(fromId), model.getDiagramFactorIdFromWrappedId(toId)));
		
		// undo add linkage to diagram
		project.undo();

		// undo create model linkage
		project.undo();
		
		assertFalse("didn't undo?", model.areLinked(model.getDiagramFactorIdFromWrappedId(fromId), model.getDiagramFactorIdFromWrappedId(toId)));
	}
	
	public void testMultipleUndo() throws Exception
	{
		DiagramModel model = project.getDiagramModel();
		
		//undo create diagram lingate
		project.undo();
		
		// undo add linkage to diagram
		project.undo();
		
		// undo create model linkage
		project.undo();
		
		assertFalse("didn't undo?", model.areLinked(model.getDiagramFactorIdFromWrappedId(fromId), model.getDiagramFactorIdFromWrappedId(toId)));
		verifyLinkageNotPresent(linkId);

		// undo diagram node add
		project.undo();
		
		// undo diagram node create
		project.undo();
		
		// undo model node create
		project.undo();
		
		verifyNodeNotPresent(toId);

		// undo diagram node add
		project.undo();
		
		// undo diagram node create
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
		
		FactorId factorId = project.createFactor(ObjectType.CAUSE);
		CreateDiagramFactorParameter extraDiagramFactorInfo = new CreateDiagramFactorParameter(factorId);
		CommandCreateObject createDiagramFactorCommand = new CommandCreateObject(ObjectType.DIAGRAM_FACTOR, extraDiagramFactorInfo);
		project.executeCommand(createDiagramFactorCommand);
		
		DiagramFactorId diagramFactorId = (DiagramFactorId) createDiagramFactorCommand.getCreatedId();
		CommandDiagramAddFactor insert = new CommandDiagramAddFactor(diagramFactorId);
		project.executeCommand(insert);
		
		verifyNodePresent(insert.getInsertedId());
		project.undo();
		project.undo();
		
		verifyNodeNotPresent(insert.getInsertedId());
		project.undo();
		
		verifyLinkageNotPresent(linkId);
	
	}
	
	public void testGetIndexToUndoAndRedo() throws Exception
	{
		CommandJump nop = new CommandJump(4);
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
		assertNotNull("Node not present?", model.getFactorCellById(cellId));
	}
	
	private void verifyNodeNotPresent(DiagramFactorId cellId)
	{
		DiagramModel model = project.getDiagramModel();
		
		EAM.setLogToString();
		try
		{
			model.getFactorCellById(cellId);
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
		assertNull("Found the deleted factor?", model.getFactorCellByWrappedId(cellId));
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
	
	private FactorId createModelAndDiagramNodeWithCommands(int type) throws Exception
	{
		CommandCreateObject createModelNodeCommand = new CommandCreateObject(ObjectType.CAUSE);
		project.executeCommand(createModelNodeCommand);
		
		FactorId factorId = (FactorId) createModelNodeCommand.getCreatedId();
		CreateDiagramFactorParameter extraDiagramFactorInfo = new CreateDiagramFactorParameter(factorId);
		CommandCreateObject createDiagramFactorCommand = new CommandCreateObject(ObjectType.DIAGRAM_FACTOR, extraDiagramFactorInfo);
		project.executeCommand(createDiagramFactorCommand);
		
		DiagramFactorId diagramFactorId = (DiagramFactorId) createDiagramFactorCommand.getCreatedId();
		CommandDiagramAddFactor addToDiagramCommand = new CommandDiagramAddFactor(diagramFactorId);
		project.executeCommand(addToDiagramCommand);
		
		return factorId;
		
	}
	
	ProjectForTesting project;
	FactorId fromId;
	FactorId toId;
	DiagramFactorLinkId linkId;
}
