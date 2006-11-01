/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram;


import org.conservationmeasures.eam.commands.CommandCreateObject;
import org.conservationmeasures.eam.commands.CommandDiagramAddLinkage;
import org.conservationmeasures.eam.commands.CommandDiagramAddNode;
import org.conservationmeasures.eam.commands.CommandDoNothing;
import org.conservationmeasures.eam.diagram.nodes.DiagramNode;
import org.conservationmeasures.eam.diagram.nodetypes.NodeType;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.ModelNodeId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.CreateModelNodeParameter;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
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

		fromId = createModelAndDiagramNodeWithCommands(DiagramNode.TYPE_FACTOR);
		toId = createModelAndDiagramNodeWithCommands(DiagramNode.TYPE_INTERVENTION);
		CommandDiagramAddLinkage link = new CommandDiagramAddLinkage(fromId, toId);
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
		project.undo();
		assertFalse("didn't undo?", model.hasLinkage(model.getNodeById(fromId), model.getNodeById(toId)));
	}
	
	public void testMultipleUndo() throws Exception
	{
		DiagramModel model = project.getDiagramModel();
		
		project.undo();
		assertFalse("didn't undo?", model.hasLinkage(model.getNodeById(fromId), model.getNodeById(toId)));
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
		
		ModelNodeId modelNodeId = project.createNode(DiagramNode.TYPE_FACTOR);
		CommandDiagramAddNode insert = new CommandDiagramAddNode(modelNodeId);
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
	
	private void verifyNodePresent(BaseId cellId) throws Exception
	{
		DiagramModel model = project.getDiagramModel();
		assertNotNull("Node not present?", model.getNodeById(cellId));
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
	
	private ModelNodeId createModelAndDiagramNodeWithCommands(NodeType type) throws Exception
	{
		CreateModelNodeParameter extraInfo = new CreateModelNodeParameter(DiagramNode.TYPE_FACTOR);
		CommandCreateObject createModelNodeCommand = new CommandCreateObject(ObjectType.MODEL_NODE, extraInfo);
		project.executeCommand(createModelNodeCommand);
		ModelNodeId modelNodeId = (ModelNodeId)createModelNodeCommand.getCreatedId();
		CommandDiagramAddNode addToDiagramCommand = new CommandDiagramAddNode(modelNodeId);
		project.executeCommand(addToDiagramCommand);
		return modelNodeId;
		
	}
	
	ProjectForTesting project;
	ModelNodeId fromId;
	ModelNodeId toId;
	BaseId linkId;
}
