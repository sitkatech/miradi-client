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
import org.conservationmeasures.eam.diagram.cells.DiagramFactor;
import org.conservationmeasures.eam.diagram.cells.LinkCell;
import org.conservationmeasures.eam.diagram.factortypes.FactorType;
import org.conservationmeasures.eam.diagram.factortypes.FactorTypeCause;
import org.conservationmeasures.eam.diagram.factortypes.FactorTypeStrategy;
import org.conservationmeasures.eam.diagram.factortypes.FactorTypeTarget;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.exceptions.NothingToRedoException;
import org.conservationmeasures.eam.exceptions.NothingToUndoException;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.DiagramFactorId;
import org.conservationmeasures.eam.ids.DiagramFactorLinkId;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.ids.FactorLinkId;
import org.conservationmeasures.eam.main.CommandExecutedEvent;
import org.conservationmeasures.eam.main.CommandExecutedListener;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.EAMTestCase;
import org.conservationmeasures.eam.objecthelpers.CreateFactorLinkParameter;
import org.conservationmeasures.eam.objecthelpers.CreateTaskParameter;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.DiagramFactorLink;
import org.conservationmeasures.eam.objects.EAMBaseObject;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.objects.RatingCriterion;
import org.conservationmeasures.eam.objects.ValueOption;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.project.ProjectForTesting;
import org.conservationmeasures.eam.project.ThreatRatingFramework;
import org.conservationmeasures.eam.views.diagram.InsertFactorLinkDoer;
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
		consumeNodeIdZero();
		super.setUp();
	}

	private void consumeNodeIdZero() throws Exception
	{
		project.createNode(new FactorTypeTarget());
	}
	
	public void tearDown() throws Exception
	{
		super.tearDown();
		project.close();
	}
	
	public void testCommandSetObjectData_ValueOption() throws Exception
	{
		int type = ObjectType.VALUE_OPTION;
		BaseId createdId = project.createObject(type);
		ValueOption option = project.getThreatRatingFramework().getValueOption(createdId);
		Color originalColor = option.getColor();
		
		Color newColor = Color.MAGENTA;
		String field = ValueOption.TAG_COLOR;
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
	
	public void testCommandSetObjectData_RatingCriterion() throws Exception
	{
		int type = ObjectType.RATING_CRITERION;
		BaseId createdId = project.createObject(type);
		RatingCriterion criterion = project.getThreatRatingFramework().getCriterion(createdId);
		String originalLabel = criterion.getLabel();
		
		String field = RatingCriterion.TAG_LABEL;
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
		int type = ObjectType.VALUE_OPTION;
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
		int type = ObjectType.RATING_CRITERION;
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
		int type = ObjectType.RATING_CRITERION;
		CommandCreateObject cmd = new CommandCreateObject(type);
		assertEquals("wrong type?", type, cmd.getObjectType());
		assertEquals("created id already set?", BaseId.INVALID, cmd.getCreatedId());

		int oldCount = framework.getCriteria().length;
		project.executeCommand(cmd);
		assertEquals("added to framework?", oldCount, framework.getCriteria().length);
		RatingCriterion criterion = framework.getCriterion(cmd.getCreatedId());
		assertEquals("wrong default label?", EAMBaseObject.DEFAULT_LABEL, criterion.getLabel());
		
		assertNotNull("didn't create?", framework.getCriterion(cmd.getCreatedId()));
		
		cmd.undo(project);
		assertEquals("didn't undo?", oldCount, framework.getCriteria().length);
		
		verifyUndoTwiceThrows(cmd);

	}
	
	public void testCommandCreateObject_ThreatRatingValueOption() throws Exception
	{
		ThreatRatingFramework framework = project.getThreatRatingFramework();
		int type = ObjectType.VALUE_OPTION;
		CommandCreateObject cmd = new CommandCreateObject(type);
		assertEquals("wrong type?", type, cmd.getObjectType());
		assertEquals("created id already set?", BaseId.INVALID, cmd.getCreatedId());

		int oldCount = framework.getValueOptions().length;
		project.executeCommand(cmd);
		ValueOption option = framework.getValueOption(cmd.getCreatedId());
		assertEquals("wrong default label?", "", option.getLabel());
		assertEquals("wrong default numeric?", 0, option.getNumericValue());
		assertEquals("wrong default color?", Color.BLACK, option.getColor());
		
		cmd.undo(project);
		assertEquals("didn't undo?", oldCount, framework.getValueOptions().length);
		
		verifyUndoTwiceThrows(cmd);

	}
	
	public void testCommandDiagramMove() throws Exception
	{
		Point moveTo = new Point(25, -68);
		DiagramFactorId[] ids = {insertTarget(), insertContributingFactor(), insertContributingFactor(), insertIntervention()};
		CommandDiagramMove cmd = new CommandDiagramMove(moveTo.x, moveTo.y, ids);
		project.executeCommand(cmd);
		
		for(int i=0; i < ids.length; ++i)
		{
			DiagramFactor node = project.getDiagramModel().getDiagramFactorById(ids[i]);
			assertEquals("didn't set location?", moveTo, node.getLocation());
		}

		Point zeroZero = new Point(0, 0);
		cmd.undo(project);
		for(int i=0; i < ids.length; ++i)
		{
			DiagramFactor node = project.getDiagramModel().getDiagramFactorById(ids[i]);
			assertEquals("didn't restore original location?", zeroZero, node.getLocation());
		}
	}
	
	public void testCommandSetThreatRating() throws Exception
	{
		FactorId threatId = new FactorId(100);
		FactorId targetId = new FactorId(101);
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
		DiagramFactorId id = insertTarget();
		Dimension defaultSize = new Dimension(120, 60);
		DiagramFactor node = project.getDiagramModel().getDiagramFactorById(id);
		Dimension originalSize = node.getSize();
		assertEquals(defaultSize, originalSize);
		
		Dimension newSize = new Dimension(88, 22);
		
		CommandSetFactorSize cmd = new CommandSetFactorSize(id, newSize, originalSize);
		project.executeCommand(cmd);
		assertEquals("didn't memorize old size?", originalSize, cmd.getPreviousSize());
		assertEquals("didn't change to new size?", newSize, node.getSize());

		cmd.undo(project);
		assertEquals("didn't undo?", originalSize, project.getDiagramModel().getDiagramFactorById(id).getSize());
		
		verifyUndoTwiceThrows(cmd);
	}
	

	public void testCommandAddTarget() throws Exception
	{
		verifyDiagramAddNode(new FactorTypeTarget());
	}

	public void testCommandInsertFactor() throws Exception
	{
		verifyDiagramAddNode(new FactorTypeCause());
	}

	public void testCommandInsertIntervention() throws Exception
	{
		verifyDiagramAddNode(new FactorTypeStrategy());
	}

	private void verifyDiagramAddNode(FactorType type) throws Exception, CommandFailedException
	{
		FactorId modelNodeId = project.createNode(type);
		CommandDiagramAddFactor add = new CommandDiagramAddFactor(new DiagramFactorId(BaseId.INVALID.asInt()), modelNodeId);
		project.executeCommand(add);

		DiagramFactorId insertedId = add.getInsertedId();
		DiagramFactor node = project.getDiagramModel().getDiagramFactorById(insertedId);
		assertEquals("type not right?", type, node.getFactorType());
		assertNotEquals("already have an id?", BaseId.INVALID, node.getDiagramFactorId());

		verifyUndoDiagramAddNode(add);
	}

	private void verifyUndoDiagramAddNode(CommandDiagramAddFactor cmd) throws CommandFailedException
	{
		DiagramFactorId insertedId = cmd.getInsertedId();
		cmd.undo(project);
		try
		{
			EAM.setLogToString();
			project.getDiagramModel().getDiagramFactorById(insertedId);
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

	public void testCommandDiagramAddLinkage() throws Exception
	{
		DiagramModel model = project.getDiagramModel();
		FactorType type = Factor.TYPE_CAUSE;

		DiagramFactorId from = insertNode(type);
		DiagramFactorId to = insertTarget();
		FactorId fromId = model.getDiagramFactorById(from).getWrappedId();
		FactorId toId = model.getDiagramFactorById(to).getWrappedId();
		
		CreateFactorLinkParameter extraInfo = new CreateFactorLinkParameter(fromId, toId);
		CommandCreateObject createModelLinkage = new CommandCreateObject(ObjectType.FACTOR_LINK, extraInfo);
		project.executeCommand(createModelLinkage);
		
		FactorLinkId modelLinkageId = (FactorLinkId)createModelLinkage.getCreatedId();
		CommandDiagramAddFactorLink addLinkageCommand = new CommandDiagramAddFactorLink(modelLinkageId);
		project.executeCommand(addLinkageCommand);
		
		DiagramFactorLink inserted = model.getDiagramFactorLinkbyWrappedId(modelLinkageId);
		LinkCell cell = model.findLinkCell(inserted);
		DiagramFactor fromNode = cell.getFrom();
		assertEquals("wrong source?", from, fromNode.getDiagramFactorId());
		DiagramFactor toNode = cell.getTo();
		assertEquals("wrong dest?", to, toNode.getDiagramFactorId());

		assertTrue("linkage not created?", project.getDiagramModel().areLinked(fromNode, toNode));
		addLinkageCommand.undo(project);
		createModelLinkage.undo(project);
		assertFalse("didn't remove linkage?", project.getDiagramModel().areLinked(fromNode, toNode));
		assertNull("didn't delete linkage from pool?", project.getFactorLinkPool().find(modelLinkageId));
		
		verifyUndoTwiceThrows(addLinkageCommand);
		verifyUndoTwiceThrows(createModelLinkage);
	}
	
	public void testDeleteLinkage() throws Exception
	{
		DiagramModel model = project.getDiagramModel();

		DiagramFactorId from = insertIntervention();
		DiagramFactorId to = insertContributingFactor();
		DiagramFactor fromNode = model.getDiagramFactorById(from);
		DiagramFactor toNode = model.getDiagramFactorById(to);

		CommandDiagramAddFactorLink addLinkageCommand = InsertFactorLinkDoer.createModelLinkageAndAddToDiagramUsingCommands(project, fromNode.getWrappedId(), toNode.getWrappedId());
		DiagramFactorLinkId linkageId = addLinkageCommand.getDiagramFactorLinkId();
	
		CommandDiagramRemoveFactorLink cmd = new CommandDiagramRemoveFactorLink(linkageId);
		assertEquals("model id not invalid?", BaseId.INVALID, cmd.getFactorLinkId());
		project.executeCommand(cmd);
		assertEquals("model id not set?", addLinkageCommand.getFactorLinkId(), cmd.getFactorLinkId());

		assertFalse("linkage not deleted?", model.areLinked(fromNode, toNode));
		cmd.undo(project);
		assertTrue("didn't restore link?", model.areLinked(fromNode, toNode));
	}

	public void testDeleteNode() throws Exception
	{
		DiagramFactorId id = insertTarget();
		FactorId modelNodeId = project.getDiagramModel().getDiagramFactorById(id).getWrappedId();
		
		CommandDiagramRemoveFactor cmd = new CommandDiagramRemoveFactor(id);
		assertEquals("modelNodeId not invalid?", BaseId.INVALID, cmd.getFactorId());
		project.executeCommand(cmd);
		
		assertEquals("modelNodeId not set by execute?", modelNodeId, cmd.getFactorId());
		
		cmd.undo(project);
		assertEquals("didn't undo delete?", Factor.TYPE_TARGET, project.getDiagramModel().getDiagramFactorById(id).getFactorType());

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
		DiagramFactorId insertedId = insertTarget();
		project.undo();
		project.redo();
		
		DiagramFactor inserted = project.getDiagramModel().getDiagramFactorById(insertedId);
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
		CommandCreateObject cmd = new CommandCreateObject(ObjectType.TASK, getTaskExtraInfo());
		project.executeCommand(cmd);
		project.undo();
		assertEquals("didn't undo one command?", 1, undoListener.undoneCommands.size());
		assertEquals("didn't fire proper undo?", cmd.toString(), undoListener.undoneCommands.get(0).toString());
		project.removeCommandExecutedListener(undoListener);
	}
	
	private DiagramFactorId insertTarget() throws Exception
	{
		FactorType type = Factor.TYPE_TARGET;
		return insertNode(type);
	}
	
	private DiagramFactorId insertContributingFactor() throws Exception
	{
		FactorType type = Factor.TYPE_CAUSE;
		return insertNode(type);
	}

	private DiagramFactorId insertIntervention() throws Exception
	{
		FactorType type = Factor.TYPE_INTERVENTION;
		return insertNode(type);
	}

	private DiagramFactorId insertNode(FactorType type) throws Exception
	{
		FactorId modelNodeId = project.createNode(type);
		CommandDiagramAddFactor add = new CommandDiagramAddFactor(new DiagramFactorId(BaseId.INVALID.asInt()), modelNodeId);
		project.executeCommand(add);
		return add.getInsertedId();
	}
	
	private CreateTaskParameter getTaskExtraInfo()
	{
		ORef parentRef = new ORef(ObjectType.FACTOR, new BaseId(45));
		CreateTaskParameter extraInfo = new CreateTaskParameter(parentRef);
		return extraInfo;
	}
	
	ProjectForTesting project;
}
