package org.conservationmeasures.eam.views.umbrella;
import java.awt.Dimension;
import java.util.Vector;

import org.conservationmeasures.eam.commands.CommandBeginTransaction;
import org.conservationmeasures.eam.commands.CommandEndTransaction;
import org.conservationmeasures.eam.commands.CommandInsertNode;
import org.conservationmeasures.eam.commands.CommandSetNodeSize;
import org.conservationmeasures.eam.diagram.nodes.DiagramNode;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.ModelNodeId;
import org.conservationmeasures.eam.project.NodeCommandHelper;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.project.ProjectForTesting;
import org.conservationmeasures.eam.testall.EAMTestCase;

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
		ModelNodeId insertedId = insertFactor(project);
		project.executeCommand(NodeCommandHelper.createSetLabelCommand(insertedId, target1Text));
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
		assertEquals("Incorrect label?", target1Text, node.getLabel());
		
		undo.doIt();
		assertEquals("Should have 0 nodes again.", 0, project.getDiagramModel().getNodeCount());
	}

	public void testUndoRedoNodeSize() throws Exception
	{
		BaseId insertedId = insertFactor(project);
		DiagramNode node = project.getDiagramModel().getNodeById(insertedId);
		Dimension originalSize = node.getSize();

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
	
	private ModelNodeId insertFactor(Project p) throws CommandFailedException 
	{
		CommandInsertNode insert = new CommandInsertNode( DiagramNode.TYPE_FACTOR);
		p.executeCommand(insert);
		ModelNodeId insertedId = insert.getId();
		return insertedId;
	}

	ProjectForTesting project;
}
