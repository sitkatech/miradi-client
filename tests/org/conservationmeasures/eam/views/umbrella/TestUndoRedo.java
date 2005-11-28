package org.conservationmeasures.eam.views.umbrella;
import java.awt.Dimension;
import java.util.Vector;

import org.conservationmeasures.eam.commands.CommandBeginTransaction;
import org.conservationmeasures.eam.commands.CommandEndTransaction;
import org.conservationmeasures.eam.commands.CommandInsertNode;
import org.conservationmeasures.eam.commands.CommandSetIndicator;
import org.conservationmeasures.eam.commands.CommandSetNodeObjectives;
import org.conservationmeasures.eam.commands.CommandSetNodePriority;
import org.conservationmeasures.eam.commands.CommandSetNodeSize;
import org.conservationmeasures.eam.commands.CommandSetNodeText;
import org.conservationmeasures.eam.commands.CommandSetTargetGoal;
import org.conservationmeasures.eam.diagram.nodes.DiagramNode;
import org.conservationmeasures.eam.diagram.nodes.Goal;
import org.conservationmeasures.eam.diagram.nodes.Goals;
import org.conservationmeasures.eam.diagram.nodes.Indicator;
import org.conservationmeasures.eam.diagram.nodes.Objective;
import org.conservationmeasures.eam.diagram.nodes.Objectives;
import org.conservationmeasures.eam.diagram.nodes.ThreatPriority;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.project.ProjectForTesting;
import org.conservationmeasures.eam.testall.EAMTestCase;
import org.conservationmeasures.eam.views.umbrella.Redo;
import org.conservationmeasures.eam.views.umbrella.Undo;

public class TestUndoRedo extends EAMTestCase 
{

	public TestUndoRedo(String name) 
	{
		super(name);
	}
	
	public void setUp() throws Exception
	{
		project = new ProjectForTesting(getName());
		super.setUp();
	}
	
	public void tearDown() throws Exception
	{
		super.tearDown();
		project.close();
	}
	
	public void testBasics() throws Exception
	{
		String target1Text = "Target 1 Text";
		project.executeCommand(new CommandBeginTransaction());
		int insertedId = insertDirectThreat(project);
		project.executeCommand(new CommandSetNodeText(insertedId, target1Text));
		project.executeCommand(new CommandEndTransaction());
		assertEquals("Should have 1 node now.", 1, project.getDiagramModel().getNodeCount());
		
		project.getDiagramModel().getNodeById(insertedId);
		Undo undo = new Undo();
		undo.setProject(project);
		undo.doIt();
		assertEquals("Should have 0 nodes now.", 0, project.getDiagramModel().getNodeCount());

		Redo redo = new Redo();
		redo.setProject(project);
		redo.doIt();

		Vector inserted = project.getDiagramModel().getAllNodes();
		
		assertEquals("Should have 1 node again after redo.", 1, project.getDiagramModel().getNodeCount());
		assertEquals("wrong number of nodes after redo?", 1, inserted.size());
		DiagramNode node = (DiagramNode)inserted.get(0);
		assertTrue(project.getDiagramModel().isNodeInProject(node));
		assertEquals("Incorrect label?", target1Text, node.getText());
		
		undo.doIt();
		assertEquals("Should have 0 nodes again.", 0, project.getDiagramModel().getNodeCount());
	}

	public void testUndoRedoPriority() throws Exception
	{
		ThreatPriority target1Priority = ThreatPriority.createPriorityVeryHigh();
		int insertedId = insertDirectThreat(project);

		project.executeCommand(new CommandBeginTransaction());
		project.executeCommand(new CommandSetNodePriority(insertedId, target1Priority));
		project.executeCommand(new CommandEndTransaction());

		assertEquals("Should have 1 node now.", 1, project.getDiagramModel().getNodeCount());
		assertEquals(target1Priority, project.getDiagramModel().getNodeById(insertedId).getThreatPriority());

		Undo undo = new Undo();
		undo.setProject(project);
		undo.doIt();
		assertEquals("Should have still 1 nodes now.", 1, project.getDiagramModel().getNodeCount());
		assertEquals(ThreatPriority.createPriorityNone().getValue(), project.getDiagramModel().getNodeById(insertedId).getThreatPriority().getValue());

		Redo redo = new Redo();
		redo.setProject(project);
		redo.doIt();
		assertEquals(target1Priority, project.getDiagramModel().getNodeById(insertedId).getThreatPriority());

		undo.doIt();
		assertEquals("Should have no priority again", ThreatPriority.createPriorityNone().getValue(), project.getDiagramModel().getNodeById(insertedId).getThreatPriority().getValue());
	}
	
	public void testUndoRedoIndication() throws Exception
	{
		Indicator target1Indicator = new Indicator(2);
		int insertedId = insertDirectThreat(project);

		project.executeCommand(new CommandBeginTransaction());
		project.executeCommand(new CommandSetIndicator(insertedId, target1Indicator));
		project.executeCommand(new CommandEndTransaction());

		assertEquals(target1Indicator, project.getDiagramModel().getNodeById(insertedId).getIndicator());

		Undo undo = new Undo();
		undo.setProject(project);
		undo.doIt();
		assertEquals(new Indicator(), project.getDiagramModel().getNodeById(insertedId).getIndicator());

		Redo redo = new Redo();
		redo.setProject(project);
		redo.doIt();
		assertEquals(target1Indicator, project.getDiagramModel().getNodeById(insertedId).getIndicator());

		undo.doIt();
		assertEquals("Should have no indicator again", new Indicator(), project.getDiagramModel().getNodeById(insertedId).getIndicator());
	}
	
	public void testUndoRedoObjective() throws Exception
	{
		Objective target1Objective = new Objective("test");
		
		Objectives target1Objectives = new Objectives();
		target1Objectives.setObjectives(target1Objective);
		
		int insertedId = insertDirectThreat(project);

		project.executeCommand(new CommandBeginTransaction());
		project.executeCommand(new CommandSetNodeObjectives(insertedId, target1Objectives));
		project.executeCommand(new CommandEndTransaction());

		assertTrue(project.getDiagramModel().getNodeById(insertedId).getObjectives().hasAnnotation());
		assertEquals(1, project.getDiagramModel().getNodeById(insertedId).getObjectives().size());
		assertEquals(target1Objective, project.getDiagramModel().getNodeById(insertedId).getObjectives().get(0));

		Undo undo = new Undo();
		undo.setProject(project);
		undo.doIt();
		assertFalse(project.getDiagramModel().getNodeById(insertedId).getObjectives().hasAnnotation());
		assertEquals("Should now have (No) Ojectives", 0, project.getDiagramModel().getNodeById(insertedId).getObjectives().size());

		Redo redo = new Redo();
		redo.setProject(project);
		redo.doIt();
		assertTrue(project.getDiagramModel().getNodeById(insertedId).getObjectives().hasAnnotation());
		assertEquals(1, project.getDiagramModel().getNodeById(insertedId).getObjectives().size());
		assertEquals(target1Objective, project.getDiagramModel().getNodeById(insertedId).getObjectives().get(0));

		undo.doIt();
		assertFalse(project.getDiagramModel().getNodeById(insertedId).getObjectives().hasAnnotation());
		assertEquals(0, project.getDiagramModel().getNodeById(insertedId).getObjectives().size());
	}

	public void testUndoRedoGoals() throws Exception
	{
		Goal target1Goal = new Goal("test");
		
		Goals target1Goals = new Goals();
		target1Goals.setGoals(target1Goal);
		
		int insertedId = insertTarget(project);

		project.executeCommand(new CommandBeginTransaction());
		project.executeCommand(new CommandSetTargetGoal(insertedId, target1Goals));
		project.executeCommand(new CommandEndTransaction());

		assertTrue(project.getDiagramModel().getNodeById(insertedId).getGoals().hasAnnotation());
		assertEquals(1, project.getDiagramModel().getNodeById(insertedId).getGoals().size());
		assertEquals(target1Goal, project.getDiagramModel().getNodeById(insertedId).getGoals().get(0));

		Undo undo = new Undo();
		undo.setProject(project);
		undo.doIt();
		assertFalse(project.getDiagramModel().getNodeById(insertedId).getGoals().hasAnnotation());
		assertEquals("Should now have (No) Goals", 0, project.getDiagramModel().getNodeById(insertedId).getGoals().size());

		Redo redo = new Redo();
		redo.setProject(project);
		redo.doIt();
		assertTrue(project.getDiagramModel().getNodeById(insertedId).getGoals().hasAnnotation());
		assertEquals(1, project.getDiagramModel().getNodeById(insertedId).getGoals().size());
		assertEquals(target1Goal, project.getDiagramModel().getNodeById(insertedId).getGoals().get(0));

		undo.doIt();
		assertFalse(project.getDiagramModel().getNodeById(insertedId).getGoals().hasAnnotation());
		assertEquals(0, project.getDiagramModel().getNodeById(insertedId).getGoals().size());
	}
	
	public void testUndoRedoNodeSize() throws Exception
	{
		int insertedId = insertDirectThreat(project);
		DiagramNode node = project.getDiagramModel().getNodeById(insertedId);
		Dimension originalSize = node.getSize();
		assertEquals(originalSize, node.getSize());

		Dimension newSize1 = new Dimension(5,10);
		project.executeCommand(new CommandBeginTransaction());
		project.executeCommand(new CommandSetNodeSize(insertedId, newSize1, originalSize));
		project.executeCommand(new CommandEndTransaction());

		assertEquals(newSize1, node.getSize());

		Dimension newSize2 = new Dimension(20,30);
		project.executeCommand(new CommandBeginTransaction());
		project.executeCommand(new CommandSetNodeSize(insertedId, newSize2, newSize1));
		project.executeCommand(new CommandEndTransaction());
		assertEquals(newSize2, node.getSize());

		Undo undo = new Undo();
		undo.setProject(project);
		undo.doIt();
		assertEquals(newSize1, node.getSize());

		undo = new Undo();
		undo.setProject(project);
		undo.doIt();
		assertEquals(originalSize, node.getSize());

		Redo redo = new Redo();
		redo.setProject(project);
		redo.doIt();
		assertEquals(newSize1, node.getSize());

		redo = new Redo();
		redo.setProject(project);
		redo.doIt();
		assertEquals(newSize2, node.getSize());
	}
	

	private int insertDirectThreat(Project p) throws CommandFailedException 
	{
		CommandInsertNode insert = new CommandInsertNode( DiagramNode.TYPE_DIRECT_THREAT);
		p.executeCommand(insert);
		int insertedId = insert.getId();
		return insertedId;
	}

	private int insertTarget(Project p) throws CommandFailedException 
	{
		CommandInsertNode insert = new CommandInsertNode( DiagramNode.TYPE_TARGET);
		p.executeCommand(insert);
		int insertedId = insert.getId();
		return insertedId;
	}

	ProjectForTesting project;
}
