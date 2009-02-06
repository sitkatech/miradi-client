/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
*/
package org.miradi.views.umbrella;
import java.awt.Dimension;

import org.miradi.commands.CommandBeginTransaction;
import org.miradi.commands.CommandCreateObject;
import org.miradi.commands.CommandEndTransaction;
import org.miradi.commands.CommandSetObjectData;
import org.miradi.diagram.cells.FactorCell;
import org.miradi.ids.DiagramFactorId;
import org.miradi.main.EAMTestCase;
import org.miradi.objecthelpers.CreateDiagramFactorParameter;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.DiagramFactor;
import org.miradi.objects.DiagramObject;
import org.miradi.objects.Factor;
import org.miradi.project.FactorCommandHelper;
import org.miradi.project.Project;
import org.miradi.project.ProjectForTesting;
import org.miradi.utils.EnhancedJsonObject;

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
		
		ORef diagramFactorRef = insertFactor(project);
		DiagramFactor diagramFactor = (DiagramFactor) project.findObject(diagramFactorRef);
		ORef insertedRef = diagramFactor.getWrappedORef();
		project.executeCommand(FactorCommandHelper.createSetLabelCommand(insertedRef, target1Text));
		
		project.executeCommand(new CommandEndTransaction());
		assertEquals("Should have 1 node now.", 1, project.getDiagramModel().getFactorCount());
		
		project.getDiagramModel().getFactorCellByWrappedRef(insertedRef);
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
		assertTrue(project.getDiagramModel().doesDiagramFactorExist(node.getRef()));
		
		Factor factor = Factor.findFactor(project, node.getWrappedORef());
		assertEquals("Incorrect label?", target1Text, factor.getLabel());
		
		undo.doIt();
		assertEquals("Should have 0 nodes again.", 0, project.getDiagramModel().getFactorCount());
	}

	public void testUndoRedoNodeSize() throws Exception
	{
		ORef insertedRef = insertFactor(project);
		FactorCell node = project.getDiagramModel().getFactorCellByRef(insertedRef);
		String originalSize = EnhancedJsonObject.convertFromDimension(node.getSize());

		String newSize1 = EnhancedJsonObject.convertFromDimension(new Dimension(5,10));
		project.executeCommand(new CommandBeginTransaction());
		project.executeCommand(new CommandSetObjectData(insertedRef, DiagramFactor.TAG_SIZE, newSize1));
		project.executeCommand(new CommandEndTransaction());

		String foundSizeAsString = getSizeAsString(insertedRef);
		assertEquals(newSize1, foundSizeAsString);

		String newSize2 = EnhancedJsonObject.convertFromDimension(new Dimension(20,30));
		project.executeCommand(new CommandBeginTransaction());
		project.executeCommand(new CommandSetObjectData(insertedRef, DiagramFactor.TAG_SIZE, newSize2));
		project.executeCommand(new CommandEndTransaction());
		
		String foundSizeAsString2 = getSizeAsString(insertedRef);
		assertEquals(newSize2, foundSizeAsString2);

		Undo undo = new Undo();
		undo.setProject(project);
		undo.doIt();
		String foundSizeAsString3 = getSizeAsString(insertedRef);
		assertEquals(newSize1, foundSizeAsString3);

		undo = new Undo();
		undo.setProject(project);
		undo.doIt();
		String foundSizeAsString4 = getSizeAsString(insertedRef);
		assertEquals(originalSize, foundSizeAsString4);

		Redo redo = new Redo();
		redo.setProject(project);
		redo.doIt();
		String foundSizeAsString5 = getSizeAsString(insertedRef);
		assertEquals(newSize1, foundSizeAsString5);

		redo = new Redo();
		redo.setProject(project);
		redo.doIt();
		String foundSizeAsString6 = getSizeAsString(insertedRef);
		assertEquals(newSize2, foundSizeAsString6);
	}

	private String getSizeAsString(ORef insertedRef)
	{
		DiagramFactor diagramFactor3 = (DiagramFactor) project.findObject(insertedRef);
		String foundSizeAsString3 = EnhancedJsonObject.convertFromDimension(diagramFactor3.getSize());
		return foundSizeAsString3;
	}
	
	private ORef insertFactor(Project p) throws Exception 
	{
		CommandCreateObject createModelNodeCommand = new CommandCreateObject(ObjectType.CAUSE);
		p.executeCommand(createModelNodeCommand);
		
		ORef factorRef = createModelNodeCommand.getObjectRef();
		CreateDiagramFactorParameter extraDiagramFactorInfo = new CreateDiagramFactorParameter(factorRef);
		CommandCreateObject createDiagramFactorCommand = new CommandCreateObject(ObjectType.DIAGRAM_FACTOR, extraDiagramFactorInfo);
		project.executeCommand(createDiagramFactorCommand);
		
		DiagramFactorId diagramFactorId = (DiagramFactorId) createDiagramFactorCommand.getCreatedId();
		DiagramObject diagramObject = project.getTestingDiagramObject();
		CommandSetObjectData addDiagramFactor = CommandSetObjectData.createAppendIdCommand(diagramObject, DiagramObject.TAG_DIAGRAM_FACTOR_IDS, diagramFactorId);
		project.executeCommand(addDiagramFactor);
		
		return createDiagramFactorCommand.getObjectRef();
	}

	ProjectForTesting project;
}
