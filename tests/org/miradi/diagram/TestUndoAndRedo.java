/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.miradi.diagram;


import org.miradi.commands.CommandCreateObject;
import org.miradi.commands.CommandJump;
import org.miradi.commands.CommandSetObjectData;
import org.miradi.diagram.DiagramModel;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.ids.DiagramFactorId;
import org.miradi.ids.DiagramFactorLinkId;
import org.miradi.ids.FactorId;
import org.miradi.ids.FactorLinkId;
import org.miradi.main.EAM;
import org.miradi.main.EAMTestCase;
import org.miradi.objecthelpers.CreateDiagramFactorParameter;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.DiagramFactor;
import org.miradi.objects.DiagramLink;
import org.miradi.objects.DiagramObject;
import org.miradi.project.Project;
import org.miradi.project.ProjectForTesting;
import org.miradi.views.diagram.LinkCreator;

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

		DiagramFactor fromDiagramFactor = createModelAndDiagramNodeWithCommands(ObjectType.CAUSE); 
		fromId = fromDiagramFactor.getWrappedId();
		
		DiagramFactor toDiagramFactor = createModelAndDiagramNodeWithCommands(ObjectType.STRATEGY);
		toId = toDiagramFactor.getWrappedId();
		LinkCreator linkCreator = new LinkCreator(project);
		FactorLinkId factorLinkId= linkCreator.createFactorLinkAndAddToDiagramUsingCommands(project.getDiagramModel(), fromDiagramFactor, toDiagramFactor);
		DiagramLink diagramLink = project.getDiagramModel().getDiagramFactorLinkbyWrappedId(factorLinkId);
		linkId = diagramLink.getDiagramLinkageId();
	}
	
	public void tearDown() throws Exception
	{
		project.close();
		project = null;
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
		
		ORef factorRef = project.createFactorAndReturnRef(ObjectType.CAUSE);
		CreateDiagramFactorParameter extraDiagramFactorInfo = new CreateDiagramFactorParameter(factorRef);
		CommandCreateObject createDiagramFactorCommand = new CommandCreateObject(ObjectType.DIAGRAM_FACTOR, extraDiagramFactorInfo);
		project.executeCommand(createDiagramFactorCommand);
		
		DiagramFactorId diagramFactorId = (DiagramFactorId) createDiagramFactorCommand.getCreatedId();
		
		DiagramObject diagramObject = project.getDiagramObject();
		CommandSetObjectData addDiagramFactor = CommandSetObjectData.createAppendIdCommand(diagramObject, DiagramObject.TAG_DIAGRAM_FACTOR_IDS, diagramFactorId);
		project.executeCommand(addDiagramFactor);
		verifyNodePresent(diagramFactorId);
		project.undo();
		project.undo();
		
		verifyNodeNotPresent(diagramFactorId);
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
	
	private DiagramFactor createModelAndDiagramNodeWithCommands(int type) throws Exception
	{
		CommandCreateObject createModelNodeCommand = new CommandCreateObject(ObjectType.CAUSE);
		project.executeCommand(createModelNodeCommand);
		
		ORef factorRef = createModelNodeCommand.getObjectRef();
		CreateDiagramFactorParameter extraDiagramFactorInfo = new CreateDiagramFactorParameter(factorRef);
		CommandCreateObject createDiagramFactorCommand = new CommandCreateObject(ObjectType.DIAGRAM_FACTOR, extraDiagramFactorInfo);
		project.executeCommand(createDiagramFactorCommand);
		
		DiagramFactorId diagramFactorId = (DiagramFactorId) createDiagramFactorCommand.getCreatedId();
		DiagramFactor diagramFactor = (DiagramFactor) project.findObject(new ORef(ObjectType.DIAGRAM_FACTOR, diagramFactorId));
		
		DiagramObject diagramObject = project.getDiagramObject();
		CommandSetObjectData addDiagramFactor = CommandSetObjectData.createAppendIdCommand(diagramObject, DiagramObject.TAG_DIAGRAM_FACTOR_IDS, diagramFactorId);
		project.executeCommand(addDiagramFactor);
		
		return diagramFactor;
	}
	
	ProjectForTesting project;
	FactorId fromId;
	FactorId toId;
	DiagramFactorLinkId linkId;
}
