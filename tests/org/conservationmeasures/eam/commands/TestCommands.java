/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.commands;

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
import org.conservationmeasures.eam.objecthelpers.CreateDiagramFactorLinkParameter;
import org.conservationmeasures.eam.objecthelpers.CreateFactorLinkParameter;
import org.conservationmeasures.eam.objecthelpers.CreateTaskParameter;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.DiagramFactorLink;
import org.conservationmeasures.eam.objects.EAMBaseObject;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.objects.RatingCriterion;
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
	
	public void testCommandSetObjectData_RatingCriterion() throws Exception
	{
		int type = ObjectType.RATING_CRITERION;
		BaseId createdId = project.createObject(type);
		RatingCriterion criterion = project.getThreatRatingFramework().getCriterion(createdId);
		
		String field = RatingCriterion.TAG_LABEL;
		String value = "Blah";
		CommandSetObjectData cmd = new CommandSetObjectData(type, createdId, field, value);
		assertEquals("wrong type?", type, cmd.getObjectType());
		assertEquals("wrong id?", createdId, cmd.getObjectId());
		assertEquals("wrong field?", field, cmd.getFieldTag());
		assertEquals("wrong value?", value, cmd.getDataValue());
		
		project.executeCommand(cmd);
		assertEquals("didn't set value?", value, criterion.getLabel());
		
		CommandSetObjectData badId = new CommandSetObjectData(type, new BaseId(-99), field, value);
		try
		{
			ignoreLogs();
			project.executeCommand(badId);
			fail("Should have thrown for bad id");
		}
		catch (CommandFailedException ignoreExpected)
		{
		}
		finally 
		{
			logToConsole();
		}
		
		CommandSetObjectData badField = new CommandSetObjectData(type, createdId, "bogus", value);
		try
		{
			ignoreLogs();
			project.executeCommand(badField);
			fail("Should have thrown for bad field tag");
		}
		catch (CommandFailedException ignoreExpected)
		{
		}
		finally
		{
			logToConsole();
		}
		
	}

	private void logToConsole()
	{
		EAM.setLogToConsole();
	}

	private void ignoreLogs()
	{
		EAM.setLogToString();
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
		
		project.undo();
		assertNotNull("Didn't undelete?", project.getThreatRatingFramework().getValueOption(createdId));
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
		
		project.undo();
		assertNotNull("Didn't undelete?", project.getThreatRatingFramework().getCriterion(createdId));
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
		
		project.undo();
		assertEquals("didn't undo?", oldCount, framework.getCriteria().length);
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
		project.undo();
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
		
		project.undo();
		assertEquals("Didn't undo?", defaultValueId, framework.getBundle(threatId, targetId).getValueId(criterionId));
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

		project.undo();
		assertEquals("didn't undo?", originalSize, project.getDiagramModel().getDiagramFactorById(id).getSize());
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
		project.undo();
		try
		{
			EAM.setLogToString();
			project.getDiagramModel().getDiagramFactorById(insertedId);
			fail("Should have thrown because node didn't exist");
		}
		catch(Exception ignoreExpected)
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
		DiagramFactorId fromDiagramFactorId = project.getDiagramModel().getDiagramFactorByWrappedId(fromId).getDiagramFactorId();
		DiagramFactorId toDiagramFactorId = project.getDiagramModel().getDiagramFactorByWrappedId(toId).getDiagramFactorId();
		CreateDiagramFactorLinkParameter diagramLinkExtraInfo = new CreateDiagramFactorLinkParameter(modelLinkageId, fromDiagramFactorId, toDiagramFactorId);
		
		CommandCreateObject createDiagramLinkCommand =  new CommandCreateObject(ObjectType.DIAGRAM_LINK, diagramLinkExtraInfo);
    	project.executeCommand(createDiagramLinkCommand);
		
		CommandDiagramAddFactorLink addLinkageCommand = new CommandDiagramAddFactorLink(modelLinkageId);
		project.executeCommand(addLinkageCommand);
		
		DiagramFactorLink inserted = model.getDiagramFactorLinkbyWrappedId(modelLinkageId);
		LinkCell cell = model.findLinkCell(inserted);
		DiagramFactor fromNode = cell.getFrom();
		assertEquals("wrong source?", from, fromNode.getDiagramFactorId());
		DiagramFactor toNode = cell.getTo();
		assertEquals("wrong dest?", to, toNode.getDiagramFactorId());

		assertTrue("linkage not created?", project.getDiagramModel().areLinked(fromNode, toNode));
		project.undo();
		
		project.undo();
		assertFalse("didn't remove linkage?", project.getDiagramModel().areLinked(fromNode, toNode));
		
		project.undo();
		assertNull("didn't delete linkage from pool?", project.getFactorLinkPool().find(modelLinkageId));
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
		project.undo();
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
		
		project.undo();
		assertEquals("didn't undo delete?", Factor.TYPE_TARGET, project.getDiagramModel().getDiagramFactorById(id).getFactorType());
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
			undoneCommands.add(event.getCommand());
			
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
		assertEquals("didn't undo one command?", 2, undoListener.undoneCommands.size());
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
		FactorType type = Factor.TYPE_STRATEGY;
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
