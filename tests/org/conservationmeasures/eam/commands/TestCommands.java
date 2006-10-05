/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.commands;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.util.Vector;

import org.conservationmeasures.eam.diagram.DiagramModel;
import org.conservationmeasures.eam.diagram.ProjectScopeBox;
import org.conservationmeasures.eam.diagram.nodes.DiagramLinkage;
import org.conservationmeasures.eam.diagram.nodes.DiagramNode;
import org.conservationmeasures.eam.diagram.nodetypes.NodeType;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.exceptions.NothingToRedoException;
import org.conservationmeasures.eam.exceptions.NothingToUndoException;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.ModelNodeId;
import org.conservationmeasures.eam.main.CommandExecutedEvent;
import org.conservationmeasures.eam.main.CommandExecutedListener;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.EAMBaseObject;
import org.conservationmeasures.eam.objects.ThreatRatingCriterion;
import org.conservationmeasures.eam.objects.ThreatRatingValueOption;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.project.ProjectForTesting;
import org.conservationmeasures.eam.project.ThreatRatingFramework;
import org.conservationmeasures.eam.testall.EAMTestCase;
import org.conservationmeasures.eam.views.map.MapView;

public class TestCommands extends EAMTestCase
{
	public TestCommands(String name)
	{
		super(name);
	}
	
	public void setUp() throws Exception
	{
		project = new ProjectForTesting(getName());
		Command consumeCellIdZero = new CommandInsertNode(DiagramNode.TYPE_TARGET);
		project.executeCommand(consumeCellIdZero);
		super.setUp();
	}
	
	public void tearDown() throws Exception
	{
		super.tearDown();
		project.close();
	}
	
	public void testCommandSetObjectData_ThreatRatingValueOption() throws Exception
	{
		int type = ObjectType.THREAT_RATING_VALUE_OPTION;
		BaseId createdId = project.createObject(type);
		ThreatRatingValueOption option = project.getThreatRatingFramework().getValueOption(createdId);
		Color originalColor = option.getColor();
		
		Color newColor = Color.MAGENTA;
		String field = ThreatRatingValueOption.TAG_COLOR;
		String value = Integer.toString(newColor.getRGB());
		CommandSetObjectData cmd = new CommandSetObjectData(type, createdId, field, value);
		assertEquals("wrong type?", type, cmd.getObjectType());
		assertEquals("wrong id?", createdId, cmd.getObjectId());
		assertEquals("wrong field?", field, cmd.getFieldTag());
		assertEquals("wrong value?", value, cmd.getDataValue());
		
		project.executeCommand(cmd);
		assertEquals("didn't set value?", newColor, option.getColor());
		
		cmd.undo(project);
		assertEquals("didn't undo?", originalColor, option.getColor());
		
		verifyUndoTwiceThrows(cmd);
		
		
		CommandSetObjectData badId = new CommandSetObjectData(type, new BaseId(-99), field, value);
		try
		{
			project.executeCommand(badId);
			fail("Should have thrown for bad id");
		}
		catch (CommandFailedException ignoreExpected)
		{
		}
		
		CommandSetObjectData badField = new CommandSetObjectData(type, createdId, "bogus", value);
		try
		{
			project.executeCommand(badField);
			fail("Should have thrown for bad field tag");
		}
		catch (CommandFailedException ignoreExpected)
		{
		}
		
	}
	
	public void testCommandSetObjectData_ThreatRatingCriterion() throws Exception
	{
		int type = ObjectType.THREAT_RATING_CRITERION;
		BaseId createdId = project.createObject(type);
		ThreatRatingCriterion criterion = project.getThreatRatingFramework().getCriterion(createdId);
		String originalLabel = criterion.getLabel();
		
		String field = ThreatRatingCriterion.TAG_LABEL;
		String value = "Blah";
		CommandSetObjectData cmd = new CommandSetObjectData(type, createdId, field, value);
		assertEquals("wrong type?", type, cmd.getObjectType());
		assertEquals("wrong id?", createdId, cmd.getObjectId());
		assertEquals("wrong field?", field, cmd.getFieldTag());
		assertEquals("wrong value?", value, cmd.getDataValue());
		
		project.executeCommand(cmd);
		assertEquals("didn't set value?", value, criterion.getLabel());
		
		cmd.undo(project);
		assertEquals("didn't undo?", originalLabel, criterion.getLabel());
		
		verifyUndoTwiceThrows(cmd);
		
		
		CommandSetObjectData badId = new CommandSetObjectData(type, new BaseId(-99), field, value);
		try
		{
			project.executeCommand(badId);
			fail("Should have thrown for bad id");
		}
		catch (CommandFailedException ignoreExpected)
		{
		}
		
		CommandSetObjectData badField = new CommandSetObjectData(type, createdId, "bogus", value);
		try
		{
			project.executeCommand(badField);
			fail("Should have thrown for bad field tag");
		}
		catch (CommandFailedException ignoreExpected)
		{
		}
		
	}
	
	public void testCommandDeleteObject_ThreatRatingValueOption() throws Exception
	{
		int type = ObjectType.THREAT_RATING_VALUE_OPTION;
		BaseId createdId = project.createObject(type);
		
		CommandDeleteObject cmd = new CommandDeleteObject(type, createdId);
		assertEquals("wrong type?", type, cmd.getObjectType());
		assertEquals("wrong id?", createdId, cmd.getObjectId());
		
		project.executeCommand(cmd);
		assertNull("Got deleted object?", project.getThreatRatingFramework().getValueOption(createdId));
		
		cmd.undo(project);
		assertNotNull("Didn't undelete?", project.getThreatRatingFramework().getValueOption(createdId));
		
		verifyUndoTwiceThrows(cmd);
	}
	
	public void testCommandDeleteObject_ThreatRatingCriterion() throws Exception
	{
		int type = ObjectType.THREAT_RATING_CRITERION;
		BaseId createdId = project.createObject(type);
		
		CommandDeleteObject cmd = new CommandDeleteObject(type, createdId);
		assertEquals("wrong type?", type, cmd.getObjectType());
		assertEquals("wrong id?", createdId, cmd.getObjectId());
		
		project.executeCommand(cmd);
		assertNull("Got deleted object?", project.getThreatRatingFramework().getCriterion(createdId));
		
		cmd.undo(project);
		assertNotNull("Didn't undelete?", project.getThreatRatingFramework().getCriterion(createdId));
		
		verifyUndoTwiceThrows(cmd);
	}
	
	public void testCommandCreateObject_ThreatRatingCriterion() throws Exception
	{
		ThreatRatingFramework framework = project.getThreatRatingFramework();
		int type = ObjectType.THREAT_RATING_CRITERION;
		CommandCreateObject cmd = new CommandCreateObject(type);
		assertEquals("wrong type?", type, cmd.getObjectType());
		assertEquals("created id already set?", BaseId.INVALID, cmd.getCreatedId());

		int oldCount = framework.getCriteria().length;
		project.executeCommand(cmd);
		assertEquals("didn't add?", oldCount+1, framework.getCriteria().length);
		ThreatRatingCriterion criterion = framework.getCriterion(cmd.getCreatedId());
		assertEquals("wrong default label?", EAMBaseObject.DEFAULT_LABEL, criterion.getLabel());
		
		assertNotNull("didn't create?", framework.getCriterion(cmd.getCreatedId()));
		
		cmd.undo(project);
		assertEquals("didn't undo?", oldCount, framework.getCriteria().length);
		
		verifyUndoTwiceThrows(cmd);

	}
	
	public void testCommandCreateObject_ThreatRatingValueOption() throws Exception
	{
		ThreatRatingFramework framework = project.getThreatRatingFramework();
		int type = ObjectType.THREAT_RATING_VALUE_OPTION;
		CommandCreateObject cmd = new CommandCreateObject(type);
		assertEquals("wrong type?", type, cmd.getObjectType());
		assertEquals("created id already set?", BaseId.INVALID, cmd.getCreatedId());

		int oldCount = framework.getValueOptions().length;
		project.executeCommand(cmd);
		assertEquals("didn't add?", oldCount+1, framework.getValueOptions().length);
		ThreatRatingValueOption option = framework.getValueOption(cmd.getCreatedId());
		assertEquals("wrong default label?", "", option.getLabel());
		assertEquals("wrong default numeric?", 0, option.getNumericValue());
		assertEquals("wrong default color?", Color.BLACK, option.getColor());
		
		assertNotNull("didn't create?", framework.getValueOption(cmd.getCreatedId()));
		
		cmd.undo(project);
		assertEquals("didn't undo?", oldCount, framework.getValueOptions().length);
		
		verifyUndoTwiceThrows(cmd);

	}
	
	public void testCommandSetProjectVision() throws Exception
	{
		String vision = "Save the world";
		String oldVision = "less interesting vision";
		ProjectScopeBox scope = project.getDiagramModel().getProjectScopeBox();
		scope.setVision(oldVision);
		
		CommandSetProjectVision cmd = new CommandSetProjectVision(vision);
		assertEquals("wrong vision?", vision, cmd.getVisionText());
		
		project.executeCommand(cmd);
		assertEquals("Didn't set?", vision, scope.getVision());
		assertEquals("wrong previous vision?", oldVision, cmd.getPreviousVisionText());
		
		cmd.undo(project);
		assertEquals("Didn't undo?", oldVision, scope.getVision());
	}
	
	public void testCommandDiagramMove() throws Exception
	{
		Point moveTo = new Point(25, -68);
		BaseId[] ids = {insertTarget(), insertIndirectFactor(), insertIndirectFactor(), insertIntervention()};
		CommandDiagramMove cmd = new CommandDiagramMove(moveTo.x, moveTo.y, ids);
		project.executeCommand(cmd);
		
		for(int i=0; i < ids.length; ++i)
		{
			DiagramNode node = project.getDiagramModel().getNodeById(ids[i]);
			assertEquals("didn't set location?", moveTo, node.getLocation());
		}

		Point zeroZero = new Point(0, 0);
		cmd.undo(project);
		for(int i=0; i < ids.length; ++i)
		{
			DiagramNode node = project.getDiagramModel().getNodeById(ids[i]);
			assertEquals("didn't restore original location?", zeroZero, node.getLocation());
		}
	}
	
	public void testCommandSetThreatRating() throws Exception
	{
		ModelNodeId threatId = new ModelNodeId(100);
		ModelNodeId targetId = new ModelNodeId(101);
		BaseId criterionId = new BaseId(102);
		BaseId valueId = new BaseId(103);
		ThreatRatingFramework framework = project.getThreatRatingFramework();
		BaseId defaultValueId = framework.getDefaultValueId();
		
		CommandSetThreatRating cmd = new CommandSetThreatRating(threatId, targetId, criterionId, valueId);
		project.executeCommand(cmd);
		assertEquals("Didn't memorize old value?", defaultValueId, cmd.getPreviousValueId());
		assertEquals("Didn't set new value?", valueId, framework.getBundle(threatId, targetId).getValueId(criterionId));
		
		cmd.undo(project);
		assertEquals("Didn't undo?", defaultValueId, framework.getBundle(threatId, targetId).getValueId(criterionId));
		verifyUndoTwiceThrows(cmd);
	}
	
	public void testCommandNodeResized() throws Exception
	{
		BaseId id = insertTarget();
		Dimension defaultSize = new Dimension(120, 60);
		DiagramNode node = project.getDiagramModel().getNodeById(id);
		Dimension originalSize = node.getSize();
		assertEquals(defaultSize, originalSize);
		
		Dimension newSize = new Dimension(88, 22);
		
		CommandSetNodeSize cmd = new CommandSetNodeSize(id, newSize, originalSize);
		project.executeCommand(cmd);
		assertEquals("didn't memorize old size?", originalSize, cmd.getPreviousSize());
		assertEquals("didn't change to new size?", newSize, node.getSize());

		cmd.undo(project);
		assertEquals("didn't undo?", originalSize, project.getDiagramModel().getNodeById(id).getSize());
		
		verifyUndoTwiceThrows(cmd);
	}
	

	public void testCommandInsertTarget() throws Exception
	{
		CommandInsertNode cmd = new CommandInsertNode(DiagramNode.TYPE_TARGET);
		assertEquals("type not right?", DiagramNode.TYPE_TARGET, cmd.getNodeType());
		assertEquals("already have an id?", BaseId.INVALID, cmd.getId());
		project.executeCommand(cmd);
		BaseId insertedId = cmd.getId();
		
		DiagramNode inserted = project.getDiagramModel().getNodeById(insertedId);
		assertTrue("didn't insert a target?", inserted.isTarget());

		verifyUndoInsertNode(cmd);
	}

	public void testCommandInsertFactor() throws Exception
	{
		CommandInsertNode cmd = new CommandInsertNode(DiagramNode.TYPE_FACTOR);
		assertEquals("already have an id?", BaseId.INVALID, cmd.getId());
		
		project.executeCommand(cmd);
		BaseId insertedId = cmd.getId();
		DiagramNode inserted = project.getDiagramModel().getNodeById(insertedId);
		assertTrue("didn't insert a factor?", inserted.isIndirectFactor());

		verifyUndoInsertNode(cmd);
	}

	public void testCommandInsertIntervention() throws Exception
	{
		CommandInsertNode cmd = new CommandInsertNode(DiagramNode.TYPE_INTERVENTION);
		assertEquals("already have an id?", BaseId.INVALID, cmd.getId());
		
		project.executeCommand(cmd);
		BaseId insertedId = cmd.getId();
		DiagramNode inserted = project.getDiagramModel().getNodeById(insertedId);
		assertTrue("didn't insert an intervention?", inserted.isIntervention());

		verifyUndoInsertNode(cmd);
	}

	private void verifyUndoInsertNode(CommandInsertNode cmd) throws CommandFailedException
	{
		BaseId insertedId = cmd.getId();
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

		ModelNodeId from = insertIndirectFactor();
		ModelNodeId to = insertTarget();
		CommandLinkNodes cmd = new CommandLinkNodes(from, to);
		project.executeCommand(cmd);
		BaseId linkageId = cmd.getLinkageId();

		DiagramLinkage inserted = model.getLinkageById(linkageId);
		DiagramNode fromNode = inserted.getFromNode();
		assertEquals("wrong source?", from, fromNode.getDiagramNodeId());
		DiagramNode toNode = inserted.getToNode();
		assertEquals("wrong dest?", to, toNode.getDiagramNodeId());

		assertTrue("linkage not created?", project.getDiagramModel().hasLinkage(fromNode, toNode));
		cmd.undo(project);
		assertFalse("didn't remove linkage?", project.getDiagramModel().hasLinkage(fromNode, toNode));
		assertNull("didn't delete linkage from pool?", project.getLinkagePool().find(linkageId));
		
		verifyUndoTwiceThrows(cmd);
	}
	
	public void testDeleteLinkage() throws Exception
	{
		DiagramModel model = project.getDiagramModel();

		ModelNodeId from = insertIntervention();
		ModelNodeId to = insertIndirectFactor();
		DiagramNode fromNode = model.getNodeById(from);
		DiagramNode toNode = model.getNodeById(to);

		CommandLinkNodes link = new CommandLinkNodes(from, to);
		project.executeCommand(link);
		BaseId linkageId = link.getLinkageId();
	
		CommandDeleteLinkage cmd = new CommandDeleteLinkage(linkageId);
		project.executeCommand(cmd);
		assertEquals("didn't set from?", from, cmd.getWasFromId());
		assertEquals("didn't set to?", to, cmd.getWasToId());

		assertFalse("linkage not deleted?", model.hasLinkage(fromNode, toNode));
		cmd.undo(project);
		assertTrue("didn't restore linkage?", model.hasLinkage(fromNode, toNode));
	}

	public void testDeleteNode() throws Exception
	{
		BaseId id = insertTarget();
		CommandDeleteNode cmd = new CommandDeleteNode(id);
		assertEquals("type not defaulting properly?", DiagramNode.TYPE_INVALID, cmd.getNodeType());
		project.executeCommand(cmd);
		
		assertEquals("type not set by execute?", DiagramNode.TYPE_TARGET, cmd.getNodeType());
		
		cmd.undo(project);
		assertEquals("didn't undo delete?", DiagramNode.TYPE_TARGET, project.getDiagramModel().getNodeById(id).getNodeType());

		verifyUndoTwiceThrows(cmd);
	}
	
	public void testBeginTransaction() throws Exception
	{
		CommandBeginTransaction cmd = new CommandBeginTransaction();
		assertTrue(cmd.isBeginTransaction());
		assertFalse(cmd.isEndTransaction());
		project.executeCommand(cmd);

		EAM.setLogToConsole();
	}

	public void testEndTransaction() throws Exception
	{
		CommandEndTransaction cmd = new CommandEndTransaction();
		assertTrue(cmd.isEndTransaction());
		assertFalse(cmd.isBeginTransaction());
		project.executeCommand(cmd);

		EAM.setLogToConsole();
	}

	public void testUndoWhenNothingToUndo() throws Exception
	{
		Project emptyProject = new ProjectForTesting(getName());
		try
		{
			EAM.setLogToString();
			emptyProject.undo();
			fail("Should have thrown");
		}
		catch(NothingToUndoException ignoreExpected)
		{
		}
		EAM.setLogToConsole();
		emptyProject.close();
	}

	public void testRedo() throws Exception
	{
		BaseId insertedId = insertTarget();
		project.undo();
		project.redo();
		
		DiagramNode inserted = project.getDiagramModel().getNodeById(insertedId);
		assertTrue("wrong node?", inserted.isTarget());
		
	}
	
	public void testRedoWhenNothingToRedo() throws Exception
	{
		Project emptyProject = new ProjectForTesting(getName());
		try
		{
			EAM.setLogToString();
			emptyProject.redo();
			fail("Should have thrown");
		}
		catch(NothingToRedoException ignoreExpected)
		{
		}
		EAM.setLogToConsole();
		emptyProject.close();
	}
	
	public void testCommandSwitchView() throws Exception
	{
		String originalViewName = project.getCurrentView();

		CommandSwitchView toMap = new CommandSwitchView(MapView.getViewName());
		project.executeCommand(toMap);
		assertEquals("didn't switch?", toMap.getDestinationView(), project.getCurrentView());
		assertEquals("didn't set from?", originalViewName, toMap.getPreviousView());
		
		project.undo();
		assertEquals("didn't switch back?", originalViewName, project.getCurrentView());
	}
	
	static class UndoListener implements CommandExecutedListener
	{
		public UndoListener()
		{
			undoneCommands = new Vector();
		}
		
		public void commandExecuted(CommandExecutedEvent event)
		{
		}

		public void commandUndone(CommandExecutedEvent event)
		{
			undoneCommands.add(event.getCommand());
		}
		
		public void commandFailed(Command command, CommandFailedException e)
		{
		}
		
		Vector undoneCommands;
	}
	
	public void testUndoFiresCommandUndone() throws Exception
	{
		UndoListener undoListener = new UndoListener();
		project.addCommandExecutedListener(undoListener);
		CommandCreateObject cmd = new CommandCreateObject(ObjectType.TASK);
		project.executeCommand(cmd);
		project.undo();
		assertEquals("didn't undo one command?", 1, undoListener.undoneCommands.size());
		assertEquals("didn't fire proper undo?", cmd.toString(), undoListener.undoneCommands.get(0).toString());
	}
	
	private ModelNodeId insertTarget() throws Exception
	{
		NodeType type = DiagramNode.TYPE_TARGET;
		return insertNode(type);
	}
	
	private ModelNodeId insertIndirectFactor() throws Exception
	{
		NodeType type = DiagramNode.TYPE_FACTOR;
		return insertNode(type);
	}

	private ModelNodeId insertIntervention() throws Exception
	{
		NodeType type = DiagramNode.TYPE_INTERVENTION;
		return insertNode(type);
	}

	private ModelNodeId insertNode(NodeType type) throws CommandFailedException
	{
		CommandInsertNode insert = new CommandInsertNode(type);
		project.executeCommand(insert);
		ModelNodeId id = insert.getId();
		return id;
	}
	
	Project project;
}
