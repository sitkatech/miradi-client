package org.conservationmeasures.eam.vies.umbrella;
import java.util.Vector;

import org.conservationmeasures.eam.commands.CommandBeginTransaction;
import org.conservationmeasures.eam.commands.CommandEndTransaction;
import org.conservationmeasures.eam.commands.CommandInsertNode;
import org.conservationmeasures.eam.commands.CommandSetNodeText;
import org.conservationmeasures.eam.diagram.nodes.Node;
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
		int insertedId = insertTarget(project);
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
		Node node = (Node)inserted.get(0);
		assertTrue(project.getDiagramModel().isNodeInProject(node));
		assertEquals("Incorrect label?", target1Text, node.getText());
		
		undo.doIt();
		assertEquals("Should have 0 nodes again.", 0, project.getDiagramModel().getNodeCount());
	}

	private int insertTarget(Project project) throws CommandFailedException 
	{
		CommandInsertNode insert = new CommandInsertNode( Node.TYPE_TARGET);
		project.executeCommand(insert);
		int insertedId = insert.getId();
		return insertedId;
	}

}
