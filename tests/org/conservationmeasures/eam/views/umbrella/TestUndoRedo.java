package org.conservationmeasures.eam.views.umbrella;
import java.awt.Dimension;

import org.conservationmeasures.eam.commands.CommandBeginTransaction;
import org.conservationmeasures.eam.commands.CommandCreateObject;
import org.conservationmeasures.eam.commands.CommandEndTransaction;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.diagram.cells.FactorCell;
import org.conservationmeasures.eam.ids.DiagramFactorId;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.main.EAMTestCase;
import org.conservationmeasures.eam.objecthelpers.CreateDiagramFactorParameter;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.DiagramFactor;
import org.conservationmeasures.eam.objects.DiagramObject;
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
		project = null;
	}
	
	public void testBasics() throws Exception
	{
		String target1Text = "Target 1 Text";
		project.executeCommand(new CommandBeginTransaction());
		
		DiagramFactorId diagramFactorId = insertFactor(project);
		DiagramFactor diagramFactor = (DiagramFactor) project.findObject(ObjectType.DIAGRAM_FACTOR, diagramFactorId);
		ORef insertedRef = diagramFactor.getWrappedORef();
		project.executeCommand(FactorCommandHelper.createSetLabelCommand(insertedRef, target1Text));
		
		project.executeCommand(new CommandEndTransaction());
		assertEquals("Should have 1 node now.", 1, project.getDiagramModel().getFactorCount());
		
		FactorId factorId = new FactorId(insertedRef.getObjectId().asInt());
		project.getDiagramModel().getFactorCellByWrappedId(factorId);
		Undo undo = new Undo();
		undo.setProject(project);
		undo.doIt();
		assertEquals("Should have 0 nodes now.", 0, project.getDiagramModel().getFactorCount());

		Redo redo = new Redo();
		redo.setProject(project);
		redo.doIt();

		DiagramFactor[] diagramFactors = project.getAllDiagramFactors();
		
		assertEquals("Should have 1 node again after redo.", 1, project.getDiagramModel().getFactorCount());
		assertEquals("wrong number of nodes after redo?", 1, diagramFactors.length);
		DiagramFactor node = diagramFactors[0];
		assertTrue(project.getDiagramModel().doesDiagramFactorExist(node.getDiagramFactorId()));
		
		Factor factor = (Factor) project.findObject(new ORef(ObjectType.FACTOR, node.getWrappedId()));
		assertEquals("Incorrect label?", target1Text, factor.getLabel());
		
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
		project.executeCommand(new CommandSetObjectData(ObjectType.DIAGRAM_FACTOR, insertedId, DiagramFactor.TAG_SIZE, newSize1));
		project.executeCommand(new CommandEndTransaction());

		String foundSizeAsString = getSizeAsString(insertedId);
		assertEquals(newSize1, foundSizeAsString);

		String newSize2 = EnhancedJsonObject.convertFromDimension(new Dimension(20,30));
		project.executeCommand(new CommandBeginTransaction());
		project.executeCommand(new CommandSetObjectData(ObjectType.DIAGRAM_FACTOR, insertedId, DiagramFactor.TAG_SIZE, newSize2));
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
		CommandCreateObject createModelNodeCommand = new CommandCreateObject(ObjectType.CAUSE);
		p.executeCommand(createModelNodeCommand);
		
		ORef factorRef = createModelNodeCommand.getObjectRef();
		CreateDiagramFactorParameter extraDiagramFactorInfo = new CreateDiagramFactorParameter(factorRef);
		CommandCreateObject createDiagramFactorCommand = new CommandCreateObject(ObjectType.DIAGRAM_FACTOR, extraDiagramFactorInfo);
		project.executeCommand(createDiagramFactorCommand);
		
		DiagramFactorId diagramFactorId = (DiagramFactorId) createDiagramFactorCommand.getCreatedId();
		DiagramObject diagramObject = project.getDiagramObject();
		CommandSetObjectData addDiagramFactor = CommandSetObjectData.createAppendIdCommand(diagramObject, DiagramObject.TAG_DIAGRAM_FACTOR_IDS, diagramFactorId);
		project.executeCommand(addDiagramFactor);
		
		return diagramFactorId;
	}

	ProjectForTesting project;
}
