/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.commands;

import java.awt.Dimension;
import java.awt.Point;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.Arrays;
import java.util.Vector;

import org.conservationmeasures.eam.annotations.Goal;
import org.conservationmeasures.eam.annotations.Goals;
import org.conservationmeasures.eam.annotations.IndicatorId;
import org.conservationmeasures.eam.annotations.Objective;
import org.conservationmeasures.eam.annotations.Objectives;
import org.conservationmeasures.eam.diagram.DiagramModel;
import org.conservationmeasures.eam.diagram.ProjectScopeBox;
import org.conservationmeasures.eam.diagram.nodes.DiagramLinkage;
import org.conservationmeasures.eam.diagram.nodes.DiagramNode;
import org.conservationmeasures.eam.diagram.nodetypes.NodeType;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.exceptions.NothingToRedoException;
import org.conservationmeasures.eam.exceptions.NothingToUndoException;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.ThreatPriority;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.project.ProjectForTesting;
import org.conservationmeasures.eam.testall.EAMTestCase;
import org.conservationmeasures.eam.views.interview.InterviewView;

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

	public void testCommandSetNodePriority() throws Exception
	{
		int targetId = insertTarget();
		DiagramNode node = project.getDiagramModel().getNodeById(targetId);
		assertEquals("New target should have a null priority level", ThreatPriority.PRIORITY_NOT_USED, node.getThreatPriority().getValue());
		
		int interventionId = insertIntervention();
		node = project.getDiagramModel().getNodeById(interventionId);
		assertEquals("New intervention should have a null priority level", ThreatPriority.PRIORITY_NOT_USED, node.getThreatPriority().getValue());
		
		int indirectId = insertIndirectFactor();
		node = project.getDiagramModel().getNodeById(indirectId);
		assertEquals("New indirect factor should have a priority level as None", ThreatPriority.PRIORITY_NONE, node.getThreatPriority().getValue());

		int id = insertDirectThreat();
		node = project.getDiagramModel().getNodeById(id);
		ThreatPriority originalPriority = node.getThreatPriority();
		assertEquals("New node should have priority level as None", ThreatPriority.PRIORITY_NONE, node.getThreatPriority().getValue());

		ThreatPriority newPriorityLow = ThreatPriority.createPriorityLow();
		CommandSetNodePriority cmd = new CommandSetNodePriority(id, newPriorityLow);
		project.executeCommand(cmd);
		assertEquals("didn't memorize old priority?", originalPriority, cmd.getPreviousPriority());
		assertEquals( newPriorityLow, node.getThreatPriority());

		CommandSetNodePriority loaded = (CommandSetNodePriority)saveAndReload(cmd);
		assertEquals("didn't restore id?", id, loaded.getId());
		assertEquals("didn't restore new priority?", newPriorityLow, loaded.getCurrentPriority());
		assertEquals("didn't restore previous priority?", originalPriority, loaded.getPreviousPriority());
		
		cmd.undo(project);
		assertEquals("didn't undo?", ThreatPriority.createPriorityNone().getValue(), project.getDiagramModel().getNodeById(id).getThreatPriority().getValue());
		
		verifyUndoTwiceThrows(cmd);
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
		assertFalse("New target should not have an indicator", node.getIndicator().hasIndicator());
		IndicatorId originalIndicator = node.getIndicator();
		
		IndicatorId indicator1 = new IndicatorId(1);
		CommandSetIndicator cmd = new CommandSetIndicator(id, indicator1);
		project.executeCommand(cmd);
		assertEquals("didn't memorize old indicator?", originalIndicator, cmd.getPreviousIndicator());
		assertEquals( indicator1, node.getIndicator());

		CommandSetIndicator loaded = (CommandSetIndicator)saveAndReload(cmd);
		assertEquals("didn't restore id?", id, loaded.getId());
		assertEquals("didn't restore new indicator?", indicator1, loaded.getCurrentIndicator());
		assertEquals("didn't restore previous indicator?", originalIndicator, loaded.getPreviousIndicator());
		
		cmd.undo(project);
		assertEquals("didn't undo?", originalIndicator, project.getDiagramModel().getNodeById(id).getIndicator());
		
		verifyUndoTwiceThrows(cmd);
	}

	public void testCommandSetNodeObjectives() throws Exception
	{
		int id = insertTarget();
		DiagramNode node = project.getDiagramModel().getNodeById(id);
		Objectives testObjectives = node.getObjectives();
		assertEquals("Targets can't have Objectives", 0, testObjectives.size());
		
		id = insertDirectThreat();
		node = project.getDiagramModel().getNodeById(id);
		Objectives originalObjectives = node.getObjectives();
		assertFalse("New target should not have an objective", node.getObjectives().hasAnnotation());

		String objectiveValue1 = "obj1";
		Objective objective1 = new Objective(objectiveValue1);
		String objectiveValue2 = "obj2";
		Objective objective2 = new Objective(objectiveValue2);

		Vector vectorOfObjectives = new Vector();
		vectorOfObjectives.add(objective1);
		vectorOfObjectives.add(objective2);
		Objectives objectives = new Objectives();
		objectives.setObjectives(vectorOfObjectives);
		
		CommandSetNodeObjectives cmd = new CommandSetNodeObjectives(id, objectives);
		project.executeCommand(cmd);
		assertEquals("didn't memorize old objective?", originalObjectives, cmd.getPreviousObjectives());
		assertEquals( 2, node.getObjectives().size());
		assertEquals( objectives, node.getObjectives());
		assertEquals( objectiveValue1, node.getObjectives().get(0).getAnnotation());

		CommandSetNodeObjectives loaded = (CommandSetNodeObjectives)saveAndReload(cmd);
		assertEquals("didn't restore id?", id, loaded.getId());
		assertEquals( 2, loaded.getCurrentObjectives().size());
		assertEquals("didn't restore new objective?", objectives, loaded.getCurrentObjectives());
		assertEquals( objective1, loaded.getCurrentObjectives().get(0));
		assertEquals( objective2, loaded.getCurrentObjectives().get(1));
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
		Goals originalGoals = node.getGoals();

		String goalValue1 = "goal 1";
		Goal goal1 = new Goal(goalValue1);
		String goalValue2 = "goal 2";
		Goal goal2 = new Goal(goalValue2);

		Vector vectorOfGoals = new Vector();
		vectorOfGoals.add(goal1);
		vectorOfGoals.add(goal2);
		Goals goals = new Goals();
		goals.setGoals(vectorOfGoals);
		
		CommandSetTargetGoal cmd = new CommandSetTargetGoal(id, goals);
		project.executeCommand(cmd);
		assertEquals("didn't memorize old goal?", originalGoals, cmd.getPreviousGoals());
		assertEquals( 2, node.getGoals().size());
		assertEquals( goals, node.getGoals());
		assertEquals( goalValue1, node.getGoals().get(0).getAnnotation());

		CommandSetTargetGoal loaded = (CommandSetTargetGoal)saveAndReload(cmd);
		assertEquals("didn't restore id?", id, loaded.getId());
		assertEquals( 2, loaded.getCurrentGoals().size());
		assertEquals("didn't restore new goals?", goals, loaded.getCurrentGoals());
		assertEquals( goal1, loaded.getCurrentGoals().get(0));
		assertEquals( goal2, loaded.getCurrentGoals().get(1));
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

		CommandSwitchView toInterview = new CommandSwitchView(InterviewView.getViewName());
		project.executeCommand(toInterview);
		assertEquals("didn't switch?", toInterview.getDestinationView(), project.getCurrentView());
		assertEquals("didn't set from?", originalViewName, toInterview.getPreviousView());
		
		CommandSwitchView loaded = (CommandSwitchView)saveAndReload(toInterview);
		assertNotNull("didn't reload?", loaded);
		assertEquals("wrong to?", toInterview.getDestinationView(), loaded.getDestinationView());
		assertEquals("wrong from?", toInterview.getPreviousView(), loaded.getPreviousView());
		
		project.undo();
		assertEquals("didn't switch back?", originalViewName, project.getCurrentView());
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
