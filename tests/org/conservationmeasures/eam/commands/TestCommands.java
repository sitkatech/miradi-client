/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.commands;

import java.awt.Point;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.Arrays;

import org.conservationmeasures.eam.diagram.DiagramModel;
import org.conservationmeasures.eam.diagram.nodes.Linkage;
import org.conservationmeasures.eam.diagram.nodes.Node;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.exceptions.NothingToRedoException;
import org.conservationmeasures.eam.exceptions.NothingToUndoException;
import org.conservationmeasures.eam.main.BaseProject;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.testall.EAMTestCase;
import org.conservationmeasures.eam.views.NoProjectView;
import org.conservationmeasures.eam.views.diagram.DiagramView;

public class TestCommands extends EAMTestCase
{
	public TestCommands(String name)
	{
		super(name);
	}
	
	public void setUp() throws Exception
	{
		project = new BaseProject();
		Command consumeCellIdZero = new CommandInsertNode(Node.TYPE_TARGET);
		project.executeCommand(consumeCellIdZero);
		super.setUp();
	}
	
	public void tearDown() throws Exception
	{
		super.tearDown();
	}
	
	public void testCommandSetData() throws Exception
	{
		String fieldName = "fieldname";
		String fieldData = "new data";
		String oldData = "old data";
		project.setDataValue(fieldName, oldData);
		CommandSetData cmd = new CommandSetData(fieldName, fieldData);
		assertEquals("wrong field name?", fieldName, cmd.getFieldName());
		assertEquals("wrong field data?", fieldData, cmd.getFieldData());
		
		project.executeCommand(cmd);
		assertEquals("didn't set data?", fieldData, project.getDataValue(fieldName));

		CommandSetData loaded = (CommandSetData)saveAndReload(cmd);
		assertEquals("didn't load field name?", cmd.getFieldName(), loaded.getFieldName());
		assertEquals("didn't load field data?", cmd.getFieldData(), loaded.getFieldData());
		assertEquals("didn't load old field data?", cmd.getOldFieldData(), loaded.getOldFieldData());

		cmd.undo(project);
		assertEquals("didn't restore data?", oldData, project.getDataValue(fieldName));
	}
	
	public void testCommandInterviewSetStep() throws Exception
	{
		String stepName = project.getCurrentInterviewStepName();
		assertEquals("project not starting at welcome?", "welcome", stepName);

		String destinationStepName = "P1aT2S1";
		CommandInterviewSetStep cmd = new CommandInterviewSetStep(destinationStepName);
		assertEquals("wrong destination?", destinationStepName, cmd.getToStep());
		
		project.executeCommand(cmd);
		assertEquals("didn't set step?", destinationStepName, project.getCurrentInterviewStepName());

		CommandInterviewSetStep loaded = (CommandInterviewSetStep)saveAndReload(cmd);
		assertEquals("didn't load step name?", cmd.getToStep(), loaded.getToStep());
		
		cmd.undo(project);
		assertEquals("didn't move back to previous step?", stepName, project.getCurrentInterviewStepName());
	}
	
	public void testCommandDiagramMove() throws Exception
	{
		Point moveTo = new Point(25, -68);
		int[] ids = {insertTarget(), insertThreat(), insertThreat(), insertIntervention()};
		CommandDiagramMove cmd = new CommandDiagramMove(moveTo.x, moveTo.y, ids);
		project.executeCommand(cmd);
		
		for(int i=0; i < ids.length; ++i)
		{
			Node node = project.getDiagramModel().getNodeById(ids[i]);
			assertEquals("didn't set location?", moveTo, node.getLocation());
		}

		CommandDiagramMove loaded = (CommandDiagramMove)saveAndReload(cmd);
		assertEquals("didn't restore deltaX?", cmd.getDeltaX(), loaded.getDeltaX());
		assertEquals("didn't restore deltaY?", cmd.getDeltaY(), loaded.getDeltaY());
		assertTrue("didn't restore ids?", Arrays.equals(ids, loaded.getIds()));
		
		Point zeroZero = new Point(0, 0);
		cmd.undo(project);
		for(int i=0; i < ids.length; ++i)
		{
			Node node = project.getDiagramModel().getNodeById(ids[i]);
			assertEquals("didn't restore original location?", zeroZero, node.getLocation());
		}
	}
	
	public void testCommandSetNodeText() throws Exception
	{
		int id = insertTarget();
		
		String originalText = "original text";
		CommandSetNodeText starter = new CommandSetNodeText(id, originalText);
		project.executeCommand(starter);
		assertEquals("wasn't blank to start?", "", starter.getPreviousText());
		
		String newText = "much better text!";
		CommandSetNodeText cmd = new CommandSetNodeText(id, newText);
		project.executeCommand(cmd);
		assertEquals("didn't memorize old text?", originalText, cmd.getPreviousText());

		CommandSetNodeText loaded = (CommandSetNodeText)saveAndReload(cmd);
		assertEquals("didn't restore id?", id, loaded.getId());
		assertEquals("didn't restore new text?", newText, loaded.getNewText());
		assertEquals("didn't restore previous text?", originalText, loaded.getPreviousText());
		
		cmd.undo(project);
		assertEquals("didn't undo?", originalText, project.getDiagramModel().getNodeById(id).getText());
		
		verifyUndoTwiceThrows(cmd);
	}

	public void testCommandInsertTarget() throws Exception
	{
		CommandInsertNode cmd = new CommandInsertNode(Node.TYPE_TARGET);
		assertEquals("type not right?", Node.TYPE_TARGET, cmd.getNodeType());
		assertEquals("already have an id?", -1, cmd.getId());
		project.executeCommand(cmd);
		int insertedId = cmd.getId();
		
		Node inserted = project.getDiagramModel().getNodeById(insertedId);
		assertTrue("didn't insert a target?", inserted.isTarget());

		CommandInsertNode loaded = (CommandInsertNode)saveAndReload(cmd);
		assertNotNull(loaded);
		assertEquals("didn't load type?", cmd.getNodeType(), loaded.getNodeType());
		assertEquals("didn't load id?", cmd.getId(), loaded.getId());
		
		verifyUndoInsertNode(cmd);
	}

	public void testCommandInsertThreat() throws Exception
	{
		CommandInsertNode cmd = new CommandInsertNode(Node.TYPE_THREAT);
		assertEquals("already have an id?", -1, cmd.getId());
		
		project.executeCommand(cmd);
		int insertedId = cmd.getId();
		Node inserted = project.getDiagramModel().getNodeById(insertedId);
		assertTrue("didn't insert a threat?", inserted.isThreat());

		CommandInsertNode loaded = (CommandInsertNode)saveAndReload(cmd);
		assertNotNull(loaded);
		assertEquals("didn't load id?", cmd.getId(), loaded.getId());
		
		verifyUndoInsertNode(cmd);
	}

	public void testCommandInsertIntervention() throws Exception
	{
		CommandInsertNode cmd = new CommandInsertNode(Node.TYPE_INTERVENTION);
		assertEquals("already have an id?", -1, cmd.getId());
		
		project.executeCommand(cmd);
		int insertedId = cmd.getId();
		Node inserted = project.getDiagramModel().getNodeById(insertedId);
		assertTrue("didn't insert an intervention?", inserted.isIntervention());

		CommandInsertNode loaded = (CommandInsertNode)saveAndReload(cmd);
		assertNotNull(loaded);
		assertEquals("didn't load id?", cmd.getId(), loaded.getId());
		
		verifyUndoInsertNode(cmd);
	}

	private void verifyUndoInsertNode(CommandInsertNode cmd) throws CommandFailedException
	{
		int insertedId = cmd.getId();
		cmd.undo(project);
		try
		{
			EAM.setLogToString();
			project.getDiagramModel().getNodeById(insertedId);
			fail("Should have thrown because node didn't exist");
		}
		catch(Exception ignoreExpected)
		{
		}

		verifyUndoTwiceThrows(cmd);
	}

	private void verifyUndoTwiceThrows(Command cmd)
	{
		try
		{
			EAM.setLogToString();
			cmd.undo(project);
			fail("Should have thrown because can't undotwice");
		}
		catch(CommandFailedException ignoreExpected)
		{
		}
	}

	public void testCommandInsertLinkage() throws Exception
	{
		DiagramModel model = project.getDiagramModel();

		int from = insertThreat();
		int to = insertTarget();
		CommandLinkNodes cmd = new CommandLinkNodes(from, to);
		project.executeCommand(cmd);
		int linkageId = cmd.getLinkageId();

		Linkage inserted = model.getLinkageById(linkageId);
		Node fromNode = inserted.getFromNode();
		assertEquals("wrong source?", from, fromNode.getId());
		Node toNode = inserted.getToNode();
		assertEquals("wrong dest?", to, toNode.getId());

		CommandLinkNodes loaded = (CommandLinkNodes)saveAndReload(cmd);
		assertEquals("didn't restore from?", from, loaded.getFromId());
		assertEquals("didn't restore to?", to, loaded.getToId());
		assertEquals("didn't restore linkage?", linkageId, loaded.getLinkageId());
		
		assertTrue("linkage not created?", project.getDiagramModel().hasLinkage(fromNode, toNode));
		cmd.undo(project);
		assertFalse("didn't remove linkage?", project.getDiagramModel().hasLinkage(fromNode, toNode));
		
		verifyUndoTwiceThrows(cmd);
	}
	
	public void testDeleteLinkage() throws Exception
	{
		DiagramModel model = project.getDiagramModel();

		int from = insertIntervention();
		int to = insertThreat();
		Node fromNode = model.getNodeById(from);
		Node toNode = model.getNodeById(to);

		CommandLinkNodes link = new CommandLinkNodes(from, to);
		project.executeCommand(link);
		int linkageId = link.getLinkageId();
	
		CommandDeleteLinkage cmd = new CommandDeleteLinkage(linkageId);
		project.executeCommand(cmd);
		assertEquals("didn't set from?", from, cmd.getWasFromId());
		assertEquals("didn't set to?", to, cmd.getWasToId());

		CommandDeleteLinkage loaded = (CommandDeleteLinkage)saveAndReload(cmd);
		assertEquals("didn't restore id?", linkageId, loaded.getId());
		assertEquals("didn't restore wasFrom?", from, loaded.getWasFromId());
		assertEquals("didn't restore wasTo?", to, loaded.getWasToId());
		
		assertFalse("linkage not deleted?", model.hasLinkage(fromNode, toNode));
		cmd.undo(project);
		assertTrue("didn't restore linkage?", model.hasLinkage(fromNode, toNode));
	}

	public void testDeleteNode() throws Exception
	{
		int id = insertTarget();
		CommandDeleteNode cmd = new CommandDeleteNode(id);
		assertEquals("type not defaulting properly?", Node.TYPE_INVALID, cmd.getNodeType());
		project.executeCommand(cmd);
		
		assertEquals("type not set by execute?", Node.TYPE_TARGET, cmd.getNodeType());
		
		CommandDeleteNode loaded = (CommandDeleteNode)saveAndReload(cmd);
		assertEquals("didn't restore id?", id, loaded.getId());
		assertEquals("didn't restore type?", cmd.getNodeType(), loaded.getNodeType());
		
		cmd.undo(project);
		assertEquals("didn't undo delete?", Node.TYPE_TARGET, project.getDiagramModel().getNodeById(id).getNodeType());

		verifyUndoTwiceThrows(cmd);
	}
	
	public void testUndo() throws Exception
	{
		CommandUndo cmd = new CommandUndo();
		assertTrue(cmd.isUndo());
		assertFalse(cmd.isRedo());
		int insertedId = insertTarget();
		project.executeCommand(cmd);
		try
		{
			EAM.setLogToString();
			project.getDiagramModel().getNodeById(insertedId);
			fail("Undo didn't work?");
		}
		catch(Exception ignoreExpected)
		{
		}
		EAM.setLogToConsole();
		
		CommandUndo loaded = (CommandUndo)saveAndReload(cmd);
		assertNotNull("didn't reload?", loaded);
	}
	
	public void testBeginTransaction() throws Exception
	{
		CommandBeginTransaction cmd = new CommandBeginTransaction();
		assertTrue(cmd.isBeginTransaction());
		assertFalse(cmd.isEndTransaction());
		project.executeCommand(cmd);
		assertTrue(project.getCommandToUndo().isBeginTransaction());
		assertFalse(project.getCommandToUndo().isEndTransaction());
		project.executeCommand(new CommandUndo());
		assertTrue(project.getCommandToRedo().isBeginTransaction());
		assertFalse(project.getCommandToRedo().isEndTransaction());

		EAM.setLogToConsole();
		CommandBeginTransaction loaded = (CommandBeginTransaction)saveAndReload(cmd);
		assertNotNull("didn't reload?", loaded);
	}

	public void testEndTransaction() throws Exception
	{
		CommandEndTransaction cmd = new CommandEndTransaction();
		assertTrue(cmd.isEndTransaction());
		assertFalse(cmd.isBeginTransaction());
		project.executeCommand(cmd);
		assertFalse(project.getCommandToUndo().isBeginTransaction());
		assertTrue(project.getCommandToUndo().isEndTransaction());
		project.executeCommand(new CommandUndo());
		assertFalse(project.getCommandToRedo().isBeginTransaction());
		assertTrue(project.getCommandToRedo().isEndTransaction());

		EAM.setLogToConsole();
		CommandEndTransaction loaded = (CommandEndTransaction)saveAndReload(cmd);
		assertNotNull("didn't reload?", loaded);
	}

	public void testUndoWhenNothingToUndo() throws Exception
	{
		BaseProject emptyProject = new BaseProject();
		CommandUndo undo = new CommandUndo();
		try
		{
			EAM.setLogToString();
			emptyProject.executeCommand(undo);
			fail("Should have thrown");
		}
		catch(NothingToUndoException ignoreExpected)
		{
		}
		EAM.setLogToConsole();
	}

	public void testRedo() throws Exception
	{
		int insertedId = insertTarget();
		CommandUndo undo = new CommandUndo();
		project.executeCommand(undo);
		CommandRedo redo = new CommandRedo();
		project.executeCommand(redo);
		
		Node inserted = project.getDiagramModel().getNodeById(insertedId);
		assertTrue("wrong node?", inserted.isTarget());
		
		CommandRedo loaded = (CommandRedo)saveAndReload(redo);
		assertNotNull("didn't reload?", loaded);
	}
	
	public void testRedoWhenNothingToRedo() throws Exception
	{
		BaseProject emptyProject = new BaseProject();
		CommandRedo redo = new CommandRedo();
		try
		{
			EAM.setLogToString();
			emptyProject.executeCommand(redo);
			fail("Should have thrown");
		}
		catch(NothingToRedoException ignoreExpected)
		{
		}
		EAM.setLogToConsole();
	}
	
	public void testCommandSwitchView() throws Exception
	{
		CommandSwitchView toDiagram = new CommandSwitchView(DiagramView.getViewName());
		project.executeCommand(toDiagram);
		assertEquals("didn't switch?", toDiagram.getDestinationView(), project.getCurrentView());
		assertEquals("didn't set from?", NoProjectView.getViewName(), toDiagram.getPreviousView());
		
		CommandSwitchView loaded = (CommandSwitchView)saveAndReload(toDiagram);
		assertNotNull("didn't reload?", loaded);
		assertEquals("wrong to?", toDiagram.getDestinationView(), loaded.getDestinationView());
		assertEquals("wrong from?", toDiagram.getPreviousView(), loaded.getPreviousView());
		
		project.undo();
		assertEquals("didn't switch back?", NoProjectView.getViewName(), project.getCurrentView());
	}

	private int insertTarget() throws Exception
	{
		int type = Node.TYPE_TARGET;
		return insertNode(type);
	}
	
	private int insertThreat() throws Exception
	{
		int type = Node.TYPE_THREAT;
		return insertNode(type);
	}

	private int insertIntervention() throws Exception
	{
		int type = Node.TYPE_INTERVENTION;
		return insertNode(type);
	}

	private int insertNode(int type) throws CommandFailedException
	{
		CommandInsertNode insert = new CommandInsertNode(type);
		project.executeCommand(insert);
		int id = insert.getId();
		return id;
	}
	
	private Command saveAndReload(Command cmd) throws Exception
	{
		ByteArrayOutputStream dest = new ByteArrayOutputStream();
		cmd.writeTo(new DataOutputStream(dest));
		byte[] result = dest.toByteArray();
		DataInputStream dataIn = new DataInputStream(new ByteArrayInputStream(result));
		return Command.readFrom(dataIn);
	}
	
	BaseProject project;
}
