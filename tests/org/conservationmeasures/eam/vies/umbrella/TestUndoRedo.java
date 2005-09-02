package org.conservationmeasures.eam.vies.umbrella;
import java.util.Vector;

import org.conservationmeasures.eam.commands.CommandBeginTransaction;
import org.conservationmeasures.eam.commands.CommandEndTransaction;
import org.conservationmeasures.eam.commands.CommandInsertNode;
import org.conservationmeasures.eam.commands.CommandSetNodeText;
import org.conservationmeasures.eam.diagram.nodes.EAMGraphCell;
import org.conservationmeasures.eam.diagram.nodes.Node;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.BaseProject;
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
		BaseProject project = new BaseProject();

		String Goal1Text = "Goal 1 Text";
		project.executeCommand(new CommandBeginTransaction());
		int insertedId = insertGoal(project);
		project.executeCommand(new CommandSetNodeText(insertedId, Goal1Text));
		project.executeCommand(new CommandEndTransaction());
		assertEquals("Should have 1 cell now.", 1, project.getDiagramModel().getCellCount());
		
		project.getDiagramModel().getNodeById(insertedId);
		Undo undo = new Undo();
		undo.setProject(project);
		undo.doIt();
		assertEquals("Should have 0 cells now.", 0, project.getDiagramModel().getCellCount());

		Redo redo = new Redo();
		redo.setProject(project);
		redo.doIt();

		Vector inserted = project.getDiagramModel().getAllNodes();
		
		assertEquals("Should have 1 cells again after redo.", 1, project.getDiagramModel().getCellCount());
		assertEquals("wrong number of nodes after redo?", 1, inserted.size());
		EAMGraphCell cell = (EAMGraphCell)inserted.get(0);
		assertTrue(project.getDiagramModel().isCellInProject(cell));
		assertEquals("Incorrect label?", Goal1Text, cell.getText());
		
		undo.doIt();
		assertEquals("Should have 0 cells again.", 0, project.getDiagramModel().getCellCount());
	}

	private int insertGoal(BaseProject project) throws CommandFailedException 
	{
		CommandInsertNode insert = new CommandInsertNode( Node.TYPE_GOAL);
		project.executeCommand(insert);
		int insertedId = insert.getId();
		return insertedId;
	}

}
