/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.commands;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.Arrays;
import java.util.Vector;

import org.conservationmeasures.eam.annotations.Goal;
import org.conservationmeasures.eam.annotations.GoalIds;
import org.conservationmeasures.eam.annotations.IndicatorId;
import org.conservationmeasures.eam.annotations.Objective;
import org.conservationmeasures.eam.annotations.ObjectiveIds;
import org.conservationmeasures.eam.diagram.DiagramModel;
import org.conservationmeasures.eam.diagram.ProjectScopeBox;
import org.conservationmeasures.eam.diagram.nodes.DiagramLinkage;
import org.conservationmeasures.eam.diagram.nodes.DiagramNode;
import org.conservationmeasures.eam.diagram.nodetypes.NodeType;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.exceptions.NothingToRedoException;
import org.conservationmeasures.eam.exceptions.NothingToUndoException;
import org.conservationmeasures.eam.main.CommandExecutedEvent;
import org.conservationmeasures.eam.main.CommandExecutedListener;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.ObjectType;
import org.conservationmeasures.eam.objects.ThreatRatingCriterion;
import org.conservationmeasures.eam.objects.ThreatRatingValueOption;
import org.conservationmeasures.eam.project.IdAssigner;
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
		int createdId = project.createObject(type);
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
		
		CommandSetObjectData loaded = (CommandSetObjectData)saveAndReload(cmd);
		assertEquals("didn't load type?", cmd.getObjectType(), loaded.getObjectType());
		assertEquals("didn't load id?", cmd.getObjectId(), loaded.getObjectId());
		assertEquals("didn't load field?", cmd.getFieldTag(), loaded.getFieldTag());
		assertEquals("didn't load value?", cmd.getDataValue(), loaded.getDataValue());

		cmd.undo(project);
		assertEquals("didn't undo?", originalColor, option.getColor());
		
		verifyUndoTwiceThrows(cmd);
		
		
		CommandSetObjectData badId = new CommandSetObjectData(type, -99, field, value);
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
		int createdId = project.createObject(type);
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
		
		CommandSetObjectData loaded = (CommandSetObjectData)saveAndReload(cmd);
		assertEquals("didn't load type?", cmd.getObjectType(), loaded.getObjectType());
		assertEquals("didn't load id?", cmd.getObjectId(), loaded.getObjectId());
		assertEquals("didn't load field?", cmd.getFieldTag(), loaded.getFieldTag());
		assertEquals("didn't load value?", cmd.getDataValue(), loaded.getDataValue());

		cmd.undo(project);
		assertEquals("didn't undo?", originalLabel, criterion.getLabel());
		
		verifyUndoTwiceThrows(cmd);
		
		
		CommandSetObjectData badId = new CommandSetObjectData(type, -99, field, value);
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
		int createdId = project.createObject(type);
		
		CommandDeleteObject cmd = new CommandDeleteObject(type, createdId);
		assertEquals("wrong type?", type, cmd.getObjectType());
		assertEquals("wrong id?", createdId, cmd.getObjectId());
		
		project.executeCommand(cmd);
		try
		{
			project.getThreatRatingFramework().getValueOption(createdId);
			fail("Should have thrown getting deleted object");
		}
		catch (RuntimeException ignoreExpected)
		{
		}
		
		CommandDeleteObject loaded = (CommandDeleteObject)saveAndReload(cmd);
		assertEquals("didn't load type?", cmd.getObjectType(), loaded.getObjectType());
		assertEquals("didn't load id?", cmd.getObjectId(), loaded.getObjectId());
		
		cmd.undo(project);
		project.getThreatRatingFramework().getValueOption(createdId);
		
		verifyUndoTwiceThrows(cmd);
	}
	
	public void testCommandDeleteObject_ThreatRatingCriterion() throws Exception
	{
		int type = ObjectType.THREAT_RATING_CRITERION;
		int createdId = project.createObject(type);
		
		CommandDeleteObject cmd = new CommandDeleteObject(type, createdId);
		assertEquals("wrong type?", type, cmd.getObjectType());
		assertEquals("wrong id?", createdId, cmd.getObjectId());
		
		project.executeCommand(cmd);
		try
		{
			project.getThreatRatingFramework().getCriterion(createdId);
			fail("Should have thrown getting deleted object");
		}
		catch (RuntimeException ignoreExpected)
		{
		}
		
		CommandDeleteObject loaded = (CommandDeleteObject)saveAndReload(cmd);
		assertEquals("didn't load type?", cmd.getObjectType(), loaded.getObjectType());
		assertEquals("didn't load id?", cmd.getObjectId(), loaded.getObjectId());
		
		cmd.undo(project);
		project.getThreatRatingFramework().getCriterion(createdId);
		
		verifyUndoTwiceThrows(cmd);
	}
	
	public void testCommandCreateObject_ThreatRatingCriterion() throws Exception
	{
		ThreatRatingFramework framework = project.getThreatRatingFramework();
		int type = ObjectType.THREAT_RATING_CRITERION;
		CommandCreateObject cmd = new CommandCreateObject(type);
		assertEquals("wrong type?", type, cmd.getObjectType());
		assertEquals("created id already set?", IdAssigner.INVALID_ID, cmd.getCreatedId());

		int oldCount = framework.getCriteria().length;
		project.executeCommand(cmd);
		assertEquals("didn't add?", oldCount+1, framework.getCriteria().length);
		ThreatRatingCriterion criterion = framework.getCriterion(cmd.getCreatedId());
		assertEquals("wrong default label?", "Unknown", criterion.getLabel());
		
		ThreatRatingCriterion added = framework.getCriteria()[oldCount];
		assertEquals("didn't update created id?", added.getId(), cmd.getCreatedId());
		
		CommandCreateObject loaded = (CommandCreateObject)saveAndReload(cmd);
		assertEquals("didn't load type?", cmd.getObjectType(), loaded.getObjectType());
		assertEquals("didn't load id?", cmd.getCreatedId(), loaded.getCreatedId());
		
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
		assertEquals("created id already set?", IdAssigner.INVALID_ID, cmd.getCreatedId());

		int oldCount = framework.getValueOptions().length;
		project.executeCommand(cmd);
		assertEquals("didn't add?", oldCount+1, framework.getValueOptions().length);
		ThreatRatingValueOption option = framework.getValueOption(cmd.getCreatedId());
		assertEquals("wrong default label?", "Unknown", option.getLabel());
		assertEquals("wrong default numeric?", 0, option.getNumericValue());
		assertEquals("wrong default color?", Color.BLACK, option.getColor());
		
		ThreatRatingValueOption added = framework.getValueOptions()[oldCount];
		assertEquals("didn't update created id?", added.getId(), cmd.getCreatedId());
		
		CommandCreateObject loaded = (CommandCreateObject)saveAndReload(cmd);
		assertEquals("didn't load type?", cmd.getObjectType(), loaded.getObjectType());
		assertEquals("didn't load id?", cmd.getCreatedId(), loaded.getCreatedId());
		
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
		
		CommandSetProjectVision loaded = (CommandSetProjectVision)saveAndReload(cmd);
		assertEquals("Didn't load vision?", cmd.getVisionText(), loaded.getVisionText());
		assertEquals("Didn't load previous vision?", cmd.getPreviousVisionText(), loaded.getPreviousVisionText());
		
		cmd.undo(project);
		assertEquals("Didn't undo?", oldVision, scope.getVision());
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

		String destinationStepName = "1.0.1_0_A.2.a";
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
		int[] ids = {insertTarget(), insertIndirectFactor(), insertIndirectFactor(), insertIntervention()};
		CommandDiagramMove cmd = new CommandDiagramMove(moveTo.x, moveTo.y, ids);
		project.executeCommand(cmd);
		
		for(int i=0; i < ids.length; ++i)
		{
			DiagramNode node = project.getDiagramModel().getNodeById(ids[i]);
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
			DiagramNode node = project.getDiagramModel().getNodeById(ids[i]);
			assertEquals("didn't restore original location?", zeroZero, node.getLocation());
		}
	}
	
	public void testCommandSetThreatRating() throws Exception
	{
		int threatId = 100;
		int targetId = 101;
		int criterionId = 102;
		int valueId = 103;
		ThreatRatingFramework framework = project.getThreatRatingFramework();
		int defaultValueId = framework.getDefaultValueId();
		
		CommandSetThreatRating cmd = new CommandSetThreatRating(threatId, targetId, criterionId, valueId);
		project.executeCommand(cmd);
		assertEquals("Didn't memorize old value?", defaultValueId, cmd.getPreviousValueId());
		assertEquals("Didn't set new value?", valueId, framework.getBundle(threatId, targetId).getValueId(criterionId));
		
		CommandSetThreatRating loaded = (CommandSetThreatRating)saveAndReload(cmd);
		assertEquals("didn't restore threatid?", threatId, loaded.getThreatId());
		assertEquals("didn't restore targetId?", targetId, loaded.getTargetId());
		assertEquals("didn't restore criterionId?", criterionId, loaded.getCriterionId());
		assertEquals("didn't restore valueId?", valueId, loaded.getValueId());
		assertEquals("didn't restore previousValueId?", defaultValueId, loaded.getPreviousValueId());
		
		cmd.undo(project);
		assertEquals("Didn't undo?", defaultValueId, framework.getBundle(threatId, targetId).getValueId(criterionId));
		verifyUndoTwiceThrows(cmd);
	}
	
	public void testCommandSetNodeText() throws Exception
	{
		int id = insertTarget();
		
		String originalText = "original text";
		CommandSetNodeText starter = new CommandSetNodeText(id, originalText);
		project.executeCommand(starter);
		assertEquals("wasn't blank to start?", "", starter.getPreviousText());
		DiagramNode node = project.getDiagramModel().getNodeById(starter.getId());
		assertEquals("also set name?", "", node.getName());
		
		String newText = "much better text!";
		CommandSetNodeText cmd = new CommandSetNodeText(id, newText);
		project.executeCommand(cmd);
		assertEquals("didn't memorize old text?", originalText, cmd.getPreviousText());
		assertEquals("updated name?", "", node.getName());

		CommandSetNodeText loaded = (CommandSetNodeText)saveAndReload(cmd);
		assertEquals("didn't restore id?", id, loaded.getId());
		assertEquals("didn't restore new text?", newText, loaded.getNewText());
		assertEquals("didn't restore previous text?", originalText, loaded.getPreviousText());
		
		cmd.undo(project);
		assertEquals("didn't undo?", originalText, node.getText());
		verifyUndoTwiceThrows(cmd);
		
		starter.undo(project);
		assertEquals("didn't undo again?", "", node.getText());
		verifyUndoTwiceThrows(starter);
		
	}
	
	public void testCommandSetNodeName() throws Exception
	{
		int id = insertTarget();
		
		String originalName = "original text";
		CommandSetNodeName starter = new CommandSetNodeName(id, originalName);
		project.executeCommand(starter);
		assertEquals("wasn't blank to start?", "", starter.getPreviousName());
		DiagramNode node = project.getDiagramModel().getNodeById(starter.getId());
		assertEquals("also set text?", "", node.getText());
		
		String newName = "much better name!";
		CommandSetNodeName cmd = new CommandSetNodeName(id, newName);
		project.executeCommand(cmd);
		assertEquals("didn't memorize old name?", originalName, cmd.getPreviousName());
		assertEquals("updated text?", "", node.getText());

		CommandSetNodeName loaded = (CommandSetNodeName)saveAndReload(cmd);
		assertEquals("didn't restore id?", id, loaded.getId());
		assertEquals("didn't restore new name?", newName, loaded.getNewName());
		assertEquals("didn't restore previous name?", originalName, loaded.getPreviousName());
		
		cmd.undo(project);
		assertEquals("didn't undo?", originalName, node.getName());
		verifyUndoTwiceThrows(cmd);
		
		starter.undo(project);
		assertEquals("didn't undo again?", "", node.getName());
		verifyUndoTwiceThrows(starter);

	}

	public void testCommandSetNodeComment() throws Exception
	{
		int id = insertTarget();
		
		String originalComment = "original comment";
		CommandSetNodeComment starter = new CommandSetNodeComment(id, originalComment);
		project.executeCommand(starter);
		assertEquals("wasn't blank to start?", "", starter.getPreviousComment());
		DiagramNode node = project.getDiagramModel().getNodeById(starter.getId());
		
		String newComment = "much better comment!";
		CommandSetNodeComment cmd = new CommandSetNodeComment(id, newComment);
		project.executeCommand(cmd);
		assertEquals("didn't memorize old name?", originalComment, cmd.getPreviousComment());

		CommandSetNodeComment loaded = (CommandSetNodeComment)saveAndReload(cmd);
		assertEquals("didn't restore id?", id, loaded.getId());
		assertEquals("didn't restore new comment?", newComment, loaded.getNewComment());
		assertEquals("didn't restore previous comment?", originalComment, loaded.getPreviousComment());
		
		cmd.undo(project);
		assertEquals("didn't undo?", originalComment, node.getComment());
		verifyUndoTwiceThrows(cmd);
		
		starter.undo(project);
		assertEquals("didn't undo again?", "", node.getComment());
		verifyUndoTwiceThrows(starter);

	}

	public void testCommandSetNodePriority() throws Exception
	{
		int targetId = insertTarget();
		DiagramNode node = project.getDiagramModel().getNodeById(targetId);
		assertNull("New target should have a null priority level", node.getThreatRating());
		
		int interventionId = insertIntervention();
		node = project.getDiagramModel().getNodeById(interventionId);
		assertNull("New intervention should have a null priority level", node.getThreatRating());
		
		int indirectId = insertIndirectFactor();
		node = project.getDiagramModel().getNodeById(indirectId);
		assertNull("New indirect factor should have a priority level as None", node.getThreatRating());

		int id = insertDirectThreat();
		node = project.getDiagramModel().getNodeById(id);
		assertNull("New node should have priority level as None", node.getThreatRating());

		CommandSetNodePriority cmd = new CommandSetNodePriority(id, null);

		// Deprecated command--just make sure it doesn't crash
		project.executeCommand(cmd);
		cmd.undo(project);
	}

	public void testCommandFactorSetType() throws Exception
	{
		int id = insertDirectThreat();
		DiagramNode node = project.getDiagramModel().getNodeById(id);
		
		NodeType originalType = DiagramNode.TYPE_DIRECT_THREAT;
		NodeType switchedToType = DiagramNode.TYPE_INDIRECT_FACTOR;
		
		assertEquals("Should be a Direct Threat", originalType, node.getType());
		CommandSetFactorType cmd = new CommandSetFactorType(id, switchedToType);
		project.executeCommand(cmd);
		assertEquals("didn't memorize old objective?", originalType, cmd.getPreviousType());
		assertEquals("didn't set new type?", switchedToType, cmd.getCurrentType());

		CommandSetFactorType loaded = (CommandSetFactorType)saveAndReload(cmd);
		assertEquals("didn't restore id?", id, loaded.getId());
		assertEquals("loaded didn't have switched node type?", switchedToType, loaded.getCurrentType());
		assertEquals("didn't restore previous type?", originalType, loaded.getPreviousType());
		
		cmd.undo(project);
		assertEquals("didn't undo?", originalType, project.getDiagramModel().getNodeById(id).getType());
		
		verifyUndoTwiceThrows(cmd);
	}

	public void testCommandSetIndicator() throws Exception
	{
		int id = insertTarget();
		DiagramNode node = project.getDiagramModel().getNodeById(id);
		assertFalse("New target should not have an indicator", node.getIndicatorId().hasId());
		IndicatorId originalIndicator = node.getIndicatorId();
		
		IndicatorId indicator1 = new IndicatorId(1);
		CommandSetIndicator cmd = new CommandSetIndicator(id, indicator1);
		project.executeCommand(cmd);
		assertEquals("didn't memorize old indicator?", originalIndicator, cmd.getPreviousIndicator());
		assertEquals( indicator1, node.getIndicatorId());

		CommandSetIndicator loaded = (CommandSetIndicator)saveAndReload(cmd);
		assertEquals("didn't restore id?", id, loaded.getId());
		assertEquals("didn't restore new indicator?", indicator1, loaded.getCurrentIndicator());
		assertEquals("didn't restore previous indicator?", originalIndicator, loaded.getPreviousIndicator());
		
		cmd.undo(project);
		assertEquals("didn't undo?", originalIndicator, project.getDiagramModel().getNodeById(id).getIndicatorId());
		
		verifyUndoTwiceThrows(cmd);
	}

	public void testCommandSetNodeObjectives() throws Exception
	{
		int id = insertTarget();
		DiagramNode node = project.getDiagramModel().getNodeById(id);
		ObjectiveIds testObjectives = node.getObjectives();
		assertEquals("Targets can't have Objectives", 0, testObjectives.size());
		
		id = insertDirectThreat();
		node = project.getDiagramModel().getNodeById(id);
		ObjectiveIds originalObjectives = node.getObjectives();
		assertFalse("New target should not have an objective", node.getObjectives().hasAnnotation());
		assertEquals("size not zero?", 0, originalObjectives.size());

		int[] allObjectiveIds = project.getObjectivePool().getIds();
		Objective objective1 = project.getObjectivePool().find(allObjectiveIds[1]); 
		Objective objective2 = project.getObjectivePool().find(allObjectiveIds[3]);

		ObjectiveIds objectiveIds = new ObjectiveIds();
		objectiveIds.addId(objective1.getId());
		objectiveIds.addId(objective2.getId());
		
		CommandSetNodeObjectives cmd = new CommandSetNodeObjectives(id, objectiveIds);
		project.executeCommand(cmd);
		assertEquals("didn't memorize old objective?", originalObjectives, cmd.getPreviousObjectives());
		assertEquals( 2, node.getObjectives().size());
		assertEquals( objective1.getId(), node.getObjectives().getId(0));
		assertEquals( objective2.getId(), node.getObjectives().getId(1));

		CommandSetNodeObjectives loaded = (CommandSetNodeObjectives)saveAndReload(cmd);
		assertEquals("didn't restore id?", id, loaded.getId());
		assertEquals( 2, loaded.getCurrentObjectives().size());
		assertEquals("didn't restore new objective?", 2, loaded.getCurrentObjectives().size());
		assertEquals( objective1.getId(), loaded.getCurrentObjectives().getId(0));
		assertEquals( objective2.getId(), loaded.getCurrentObjectives().getId(1));
		assertEquals("didn't restore previous objective?", originalObjectives, loaded.getPreviousObjectives());
		
		cmd.undo(project);
		assertEquals("didn't undo?", originalObjectives, project.getDiagramModel().getNodeById(id).getObjectives());
		
		verifyUndoTwiceThrows(cmd);
	}

	public void testCommandSetGoals() throws Exception
	{
		int id = insertTarget();
		DiagramNode node = project.getDiagramModel().getNodeById(id);
		assertFalse("New target should not have a goal", node.getGoals().hasAnnotation());
		GoalIds originalGoals = node.getGoals();
		assertEquals("size not zero?", 0, originalGoals.size());

		int[] allGoalIds = project.getGoalPool().getIds();
		Goal goal1 = project.getGoalPool().find(allGoalIds[1]);
		Goal goal2 = project.getGoalPool().find(allGoalIds[2]);

		GoalIds goalIds = new GoalIds();
		goalIds.addId(goal1.getId());
		goalIds.addId(goal2.getId());
		
		CommandSetTargetGoal cmd = new CommandSetTargetGoal(id, goalIds);
		project.executeCommand(cmd);
		assertEquals("didn't memorize old goal?", originalGoals, cmd.getPreviousGoals());
		assertEquals( 2, node.getGoals().size());
		assertEquals( goal1.getId(), node.getGoals().getId(0));
		assertEquals( goal2.getId(), node.getGoals().getId(1));

		CommandSetTargetGoal loaded = (CommandSetTargetGoal)saveAndReload(cmd);
		assertEquals("didn't restore id?", id, loaded.getId());
		assertEquals( 2, loaded.getCurrentGoals().size());
		assertEquals("didn't restore new goals?", 2, loaded.getCurrentGoals().size());
		assertEquals( goal1.getId(), loaded.getCurrentGoals().getId(0));
		assertEquals( goal2.getId(), loaded.getCurrentGoals().getId(1));
		assertEquals("didn't restore previous objective?", originalGoals, loaded.getPreviousGoals());
		
		cmd.undo(project);
		assertEquals("didn't undo?", originalGoals, project.getDiagramModel().getNodeById(id).getGoals());
		
		verifyUndoTwiceThrows(cmd);
	}
	
	public void testCommandNodeResized() throws Exception
	{
		int id = insertTarget();
		Dimension defaultSize = new Dimension(120, 60);
		DiagramNode node = project.getDiagramModel().getNodeById(id);
		Dimension originalSize = node.getSize();
		assertEquals(defaultSize, originalSize);
		
		Dimension newSize = new Dimension(88, 22);
		
		CommandSetNodeSize cmd = new CommandSetNodeSize(id, newSize, originalSize);
		project.executeCommand(cmd);
		assertEquals("didn't memorize old size?", originalSize, cmd.getPreviousSize());
		assertEquals("didn't change to new size?", newSize, node.getSize());

		CommandSetNodeSize loaded = (CommandSetNodeSize)saveAndReload(cmd);
		assertEquals("didn't restore id?", id, loaded.getId());
		assertEquals( newSize, loaded.getCurrentSize());
		assertEquals("didn't restore previous size?", originalSize, loaded.getPreviousSize());
		
		cmd.undo(project);
		assertEquals("didn't undo?", originalSize, project.getDiagramModel().getNodeById(id).getSize());
		
		verifyUndoTwiceThrows(cmd);
	}
	

	public void testCommandInsertTarget() throws Exception
	{
		CommandInsertNode cmd = new CommandInsertNode(DiagramNode.TYPE_TARGET);
		assertEquals("type not right?", DiagramNode.TYPE_TARGET, cmd.getNodeType());
		assertEquals("already have an id?", -1, cmd.getId());
		project.executeCommand(cmd);
		int insertedId = cmd.getId();
		
		DiagramNode inserted = project.getDiagramModel().getNodeById(insertedId);
		assertTrue("didn't insert a target?", inserted.isTarget());

		CommandInsertNode loaded = (CommandInsertNode)saveAndReload(cmd);
		assertNotNull(loaded);
		assertEquals("didn't load type?", cmd.getNodeType(), loaded.getNodeType());
		assertEquals("didn't load id?", cmd.getId(), loaded.getId());
		
		verifyUndoInsertNode(cmd);
	}

	public void testCommandInsertIndirectFactor() throws Exception
	{
		CommandInsertNode cmd = new CommandInsertNode(DiagramNode.TYPE_INDIRECT_FACTOR);
		assertEquals("already have an id?", -1, cmd.getId());
		
		project.executeCommand(cmd);
		int insertedId = cmd.getId();
		DiagramNode inserted = project.getDiagramModel().getNodeById(insertedId);
		assertTrue("didn't insert an indirect factor?", inserted.isIndirectFactor());

		CommandInsertNode loaded = (CommandInsertNode)saveAndReload(cmd);
		assertNotNull(loaded);
		assertEquals("didn't load id?", cmd.getId(), loaded.getId());
		
		verifyUndoInsertNode(cmd);
	}

	public void testCommandInsertDirectThreat() throws Exception
	{
		CommandInsertNode cmd = new CommandInsertNode(DiagramNode.TYPE_DIRECT_THREAT);
		assertEquals("already have an id?", -1, cmd.getId());
		
		project.executeCommand(cmd);
		int insertedId = cmd.getId();
		DiagramNode inserted = project.getDiagramModel().getNodeById(insertedId);
		assertTrue("didn't insert a direct threat?", inserted.isDirectThreat());

		CommandInsertNode loaded = (CommandInsertNode)saveAndReload(cmd);
		assertNotNull(loaded);
		assertEquals("didn't load id?", cmd.getId(), loaded.getId());
		
		verifyUndoInsertNode(cmd);
	}

	public void testCommandInsertStress() throws Exception
	{
		CommandInsertNode cmd = new CommandInsertNode(DiagramNode.TYPE_STRESS);
		assertEquals("already have an id?", -1, cmd.getId());
		
		project.executeCommand(cmd);
		int insertedId = cmd.getId();
		DiagramNode inserted = project.getDiagramModel().getNodeById(insertedId);
		assertTrue("didn't insert a Stress?", inserted.isStress());

		CommandInsertNode loaded = (CommandInsertNode)saveAndReload(cmd);
		assertNotNull(loaded);
		assertEquals("didn't load id?", cmd.getId(), loaded.getId());
		
		verifyUndoInsertNode(cmd);
	}

	public void testCommandInsertIntervention() throws Exception
	{
		CommandInsertNode cmd = new CommandInsertNode(DiagramNode.TYPE_INTERVENTION);
		assertEquals("already have an id?", -1, cmd.getId());
		
		project.executeCommand(cmd);
		int insertedId = cmd.getId();
		DiagramNode inserted = project.getDiagramModel().getNodeById(insertedId);
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

		int from = insertIndirectFactor();
		int to = insertTarget();
		CommandLinkNodes cmd = new CommandLinkNodes(from, to);
		project.executeCommand(cmd);
		int linkageId = cmd.getLinkageId();

		DiagramLinkage inserted = model.getLinkageById(linkageId);
		DiagramNode fromNode = inserted.getFromNode();
		assertEquals("wrong source?", from, fromNode.getId());
		DiagramNode toNode = inserted.getToNode();
		assertEquals("wrong dest?", to, toNode.getId());

		CommandLinkNodes loaded = (CommandLinkNodes)saveAndReload(cmd);
		assertEquals("didn't restore from?", from, loaded.getFromId());
		assertEquals("didn't restore to?", to, loaded.getToId());
		assertEquals("didn't restore linkage?", linkageId, loaded.getLinkageId());
		
		assertTrue("linkage not created?", project.getDiagramModel().hasLinkage(fromNode, toNode));
		cmd.undo(project);
		assertFalse("didn't remove linkage?", project.getDiagramModel().hasLinkage(fromNode, toNode));
		assertNull("didn't delete linkage from pool?", project.getLinkagePool().find(linkageId));
		
		verifyUndoTwiceThrows(cmd);
	}
	
	public void testDeleteLinkage() throws Exception
	{
		DiagramModel model = project.getDiagramModel();

		int from = insertIntervention();
		int to = insertIndirectFactor();
		DiagramNode fromNode = model.getNodeById(from);
		DiagramNode toNode = model.getNodeById(to);

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
		assertEquals("type not defaulting properly?", DiagramNode.TYPE_INVALID, cmd.getNodeType());
		project.executeCommand(cmd);
		
		assertEquals("type not set by execute?", DiagramNode.TYPE_TARGET, cmd.getNodeType());
		
		CommandDeleteNode loaded = (CommandDeleteNode)saveAndReload(cmd);
		assertEquals("didn't restore id?", id, loaded.getId());
		assertEquals("didn't restore type?", cmd.getNodeType(), loaded.getNodeType());
		
		cmd.undo(project);
		assertEquals("didn't undo delete?", DiagramNode.TYPE_TARGET, project.getDiagramModel().getNodeById(id).getType());

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
		Project emptyProject = new ProjectForTesting(getName());
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
		emptyProject.close();
	}

	public void testRedo() throws Exception
	{
		int insertedId = insertTarget();
		CommandUndo undo = new CommandUndo();
		project.executeCommand(undo);
		CommandRedo redo = new CommandRedo();
		project.executeCommand(redo);
		
		DiagramNode inserted = project.getDiagramModel().getNodeById(insertedId);
		assertTrue("wrong node?", inserted.isTarget());
		
		CommandRedo loaded = (CommandRedo)saveAndReload(redo);
		assertNotNull("didn't reload?", loaded);
	}
	
	public void testRedoWhenNothingToRedo() throws Exception
	{
		Project emptyProject = new ProjectForTesting(getName());
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
		emptyProject.close();
	}
	
	public void testCommandSwitchView() throws Exception
	{
		String originalViewName = project.getCurrentView();

		CommandSwitchView toMap = new CommandSwitchView(MapView.getViewName());
		project.executeCommand(toMap);
		assertEquals("didn't switch?", toMap.getDestinationView(), project.getCurrentView());
		assertEquals("didn't set from?", originalViewName, toMap.getPreviousView());
		
		CommandSwitchView loaded = (CommandSwitchView)saveAndReload(toMap);
		assertNotNull("didn't reload?", loaded);
		assertEquals("wrong to?", toMap.getDestinationView(), loaded.getDestinationView());
		assertEquals("wrong from?", toMap.getPreviousView(), loaded.getPreviousView());
		
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
		project.executeCommand(new CommandUndo());
		assertEquals("didn't undo one command?", 1, undoListener.undoneCommands.size());
		assertEquals("didn't fire proper undo?", cmd.toString(), undoListener.undoneCommands.get(0).toString());
	}
	
	private int insertTarget() throws Exception
	{
		NodeType type = DiagramNode.TYPE_TARGET;
		return insertNode(type);
	}
	
	private int insertIndirectFactor() throws Exception
	{
		NodeType type = DiagramNode.TYPE_INDIRECT_FACTOR;
		return insertNode(type);
	}

	private int insertDirectThreat() throws Exception
	{
		NodeType type = DiagramNode.TYPE_DIRECT_THREAT;
		return insertNode(type);
	}

	private int insertIntervention() throws Exception
	{
		NodeType type = DiagramNode.TYPE_INTERVENTION;
		return insertNode(type);
	}

	private int insertNode(NodeType type) throws CommandFailedException
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
	
	Project project;
}
