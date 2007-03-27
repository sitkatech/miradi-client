package org.conservationmeasures.eam.views.umbrella;
import java.awt.Dimension;
import java.util.Vector;

import org.conservationmeasures.eam.commands.CommandBeginTransaction;
import org.conservationmeasures.eam.commands.CommandCreateObject;
import org.conservationmeasures.eam.commands.CommandDiagramAddFactor;
import org.conservationmeasures.eam.commands.CommandEndTransaction;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.diagram.cells.FactorCell;
import org.conservationmeasures.eam.ids.DiagramFactorId;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.main.EAMTestCase;
import org.conservationmeasures.eam.objecthelpers.CreateDiagramFactorParameter;
import org.conservationmeasures.eam.objecthelpers.CreateFactorParameter;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.DiagramFactor;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.project.FactorCommandHelper;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.project.ProjectForTesting;
import org.conservationmeasures.eam.utils.EnhancedJsonObject;

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
		
		DiagramFactorId diagramFactorId = insertFactor(project);
		DiagramFactor diagramFactor = (DiagramFactor) project.findObject(ObjectType.DIAGRAM_FACTOR, diagramFactorId);
		FactorId insertedId = diagramFactor.getWrappedId();
		project.executeCommand(FactorCommandHelper.createSetLabelCommand(insertedId, target1Text));
		
		project.executeCommand(new CommandEndTransaction());
		assertEquals("Should have 1 node now.", 1, project.getDiagramModel().getFactorCount());
		
		project.getDiagramModel().getDiagramFactorByWrappedId(insertedId);
		Undo undo = new Undo();
		undo.setProject(project);
		undo.doIt();
		assertEquals("Should have 0 nodes now.", 0, project.getDiagramModel().getFactorCount());

		Redo redo = new Redo();
		redo.setProject(project);
		redo.doIt();

		Vector inserted = project.getDiagramModel().getAllDiagramFactors();
		
		assertEquals("Should have 1 node again after redo.", 1, project.getDiagramModel().getFactorCount());
		assertEquals("wrong number of nodes after redo?", 1, inserted.size());
		FactorCell node = (FactorCell)inserted.get(0);
		assertTrue(project.getDiagramModel().doesDiagramFactorExist(node));
		assertEquals("Incorrect label?", target1Text, node.getLabel());
		
		undo.doIt();
		assertEquals("Should have 0 nodes again.", 0, project.getDiagramModel().getFactorCount());
	}

	public void testUndoRedoNodeSize() throws Exception
	{
		DiagramFactorId insertedId = insertFactor(project);
		FactorCell node = project.getDiagramModel().getFactorCellById(insertedId);
		String originalSize = EnhancedJsonObject.convertFromDimension(node.getSize());

		String newSize1 = EnhancedJsonObject.convertFromDimension(new Dimension(5,10));
		project.executeCommand(new CommandBeginTransaction());
		project.executeCommand(new CommandSetObjectData(ObjectType.DIAGRAM_FACTOR, insertedId, DiagramFactor.TAG_SIZE, newSize1, originalSize));
		project.executeCommand(new CommandEndTransaction());

		String foundSizeAsString = getSizeAsString(insertedId);
		assertEquals(newSize1, foundSizeAsString);

		String newSize2 = EnhancedJsonObject.convertFromDimension(new Dimension(20,30));
		project.executeCommand(new CommandBeginTransaction());
		project.executeCommand(new CommandSetObjectData(ObjectType.DIAGRAM_FACTOR, insertedId, DiagramFactor.TAG_SIZE, newSize2, newSize1));
		project.executeCommand(new CommandEndTransaction());
		
		String foundSizeAsString2 = getSizeAsString(insertedId);
		assertEquals(newSize2, foundSizeAsString2);

		Undo undo = new Undo();
		undo.setProject(project);
		undo.doIt();
		String foundSizeAsString3 = getSizeAsString(insertedId);
		assertEquals(newSize1, foundSizeAsString3);

		undo = new Undo();
		undo.setProject(project);
		undo.doIt();
		String foundSizeAsString4 = getSizeAsString(insertedId);
		assertEquals(originalSize, foundSizeAsString4);

		Redo redo = new Redo();
		redo.setProject(project);
		redo.doIt();
		String foundSizeAsString5 = getSizeAsString(insertedId);
		assertEquals(newSize1, foundSizeAsString5);

		redo = new Redo();
		redo.setProject(project);
		redo.doIt();
		String foundSizeAsString6 = getSizeAsString(insertedId);
		assertEquals(newSize2, foundSizeAsString6);
	}

	private String getSizeAsString(DiagramFactorId insertedId)
	{
		ORef oRef3 = new ORef(ObjectType.DIAGRAM_FACTOR, insertedId);
		DiagramFactor diagramFactor3 = (DiagramFactor) project.findObject(oRef3);
		String foundSizeAsString3 = EnhancedJsonObject.convertFromDimension(diagramFactor3.getSize());
		return foundSizeAsString3;
	}
	
	private DiagramFactorId insertFactor(Project p) throws Exception 
	{
		CreateFactorParameter extraInfo = new CreateFactorParameter(Factor.TYPE_CAUSE);
		CommandCreateObject createModelNodeCommand = new CommandCreateObject(ObjectType.FACTOR, extraInfo);
		p.executeCommand(createModelNodeCommand);
		
		FactorId factorId = (FactorId)createModelNodeCommand.getCreatedId();
		CreateDiagramFactorParameter extraDiagramFactorInfo = new CreateDiagramFactorParameter(factorId);
		CommandCreateObject createDiagramFactorCommand = new CommandCreateObject(ObjectType.DIAGRAM_FACTOR, extraDiagramFactorInfo);
		project.executeCommand(createDiagramFactorCommand);
		
		DiagramFactorId diagramFactorId = (DiagramFactorId) createDiagramFactorCommand.getCreatedId();
		CommandDiagramAddFactor addToDiagramCommand = new CommandDiagramAddFactor(diagramFactorId);
		p.executeCommand(addToDiagramCommand);
		
		return addToDiagramCommand.getInsertedId();
	}

	ProjectForTesting project;
}
