package org.conservationmeasures.eam.views.umbrella;
import java.util.Vector;

import org.conservationmeasures.eam.commands.CommandBeginTransaction;
import org.conservationmeasures.eam.commands.CommandEndTransaction;
import org.conservationmeasures.eam.commands.CommandInsertNode;
import org.conservationmeasures.eam.commands.CommandSetIndicator;
import org.conservationmeasures.eam.commands.CommandSetNodeObjective;
import org.conservationmeasures.eam.commands.CommandSetNodePriority;
import org.conservationmeasures.eam.commands.CommandSetNodeText;
import org.conservationmeasures.eam.diagram.nodes.DiagramNode;
import org.conservationmeasures.eam.diagram.nodes.Indicator;
import org.conservationmeasures.eam.diagram.nodes.Objective;
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
	
	public void testBasics() throws Exception
	{
		Project project = new ProjectForTesting(getName());

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
		Project project = new ProjectForTesting(getName());

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
		Project project = new ProjectForTesting(getName());

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
		Project project = new ProjectForTesting(getName());

		Objective target1Objective = new Objective("test");
		int insertedId = insertDirectThreat(project);

		project.executeCommand(new CommandBeginTransaction());
		project.executeCommand(new CommandSetNodeObjective(insertedId, target1Objective));
		project.executeCommand(new CommandEndTransaction());

		assertEquals(target1Objective, project.getDiagramModel().getNodeById(insertedId).getObjective());

		Undo undo = new Undo();
		undo.setProject(project);
		undo.doIt();
		assertEquals(new Objective(), project.getDiagramModel().getNodeById(insertedId).getObjective());

		Redo redo = new Redo();
		redo.setProject(project);
		redo.doIt();
		assertEquals(target1Objective, project.getDiagramModel().getNodeById(insertedId).getObjective());

		undo.doIt();
		assertEquals("Should have no objective again", new Objective(), project.getDiagramModel().getNodeById(insertedId).getObjective());
	}

	private int insertDirectThreat(Project project) throws CommandFailedException 
	{
		CommandInsertNode insert = new CommandInsertNode( DiagramNode.TYPE_DIRECT_THREAT);
		project.executeCommand(insert);
		int insertedId = insert.getId();
		return insertedId;
	}

}
