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
package org.miradi.diagram;


import org.miradi.commands.CommandCreateObject;
import org.miradi.commands.CommandSetObjectData;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.ids.DiagramFactorId;
import org.miradi.main.EAM;
import org.miradi.main.EAMTestCase;
import org.miradi.objecthelpers.CreateDiagramFactorParameter;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.DiagramFactor;
import org.miradi.objects.DiagramLink;
import org.miradi.objects.DiagramObject;
import org.miradi.objects.Stress;
import org.miradi.project.Project;
import org.miradi.project.ProjectForTesting;
import org.miradi.views.diagram.LinkCreator;

public class TestUndoAndRedo extends EAMTestCase
{
	public TestUndoAndRedo(String name)
	{
		super(name);
	}
	
	public void setUp() throws Exception
	{
		super.setUp();
		project = new ProjectForTesting(getName());

		DiagramFactor fromDiagramFactor = createModelAndDiagramNodeWithCommands(ObjectType.CAUSE); 
		fromFactorRef = fromDiagramFactor.getWrappedORef();
		
		DiagramFactor toDiagramFactor = createModelAndDiagramNodeWithCommands(ObjectType.STRATEGY);
		toFactorRef = toDiagramFactor.getWrappedORef();
		LinkCreator linkCreator = new LinkCreator(project);
		ORef factorLinkRef = linkCreator.createFactorLinkAndAddToDiagramUsingCommands(project.getDiagramModel(), fromDiagramFactor, toDiagramFactor);
		DiagramLink diagramLink = project.getDiagramModel().getDiagramLinkByWrappedRef(factorLinkRef);
		diagramLinkRef = diagramLink.getRef();
	}
	
	public void tearDown() throws Exception
	{
		project.close();
		project = null;
		super.tearDown();
	}

	public void testSingleUndo() throws Exception
	{
		DiagramModel model = project.getDiagramModel();
		assertTrue("no link?", model.areDiagramFactorsLinked(model.getDiagramFactor(fromFactorRef).getRef(), model.getDiagramFactor(toFactorRef).getRef()));
		
		// undo add linkage to diagram
		project.undo();

		// undo create model linkage
		project.undo();
		
		assertFalse("didn't undo?", model.areDiagramFactorsLinked(model.getDiagramFactor(fromFactorRef).getRef(), model.getDiagramFactor(toFactorRef).getRef()));
	}
	
	public void testMultipleUndo() throws Exception
	{
		DiagramModel model = project.getDiagramModel();
		
		//undo create diagram lingate
		project.undo();
		
		// undo add linkage to diagram
		project.undo();
		
		// undo create model linkage
		project.undo();
		
		ORef fromDiagramFactorRef = model.getDiagramFactor(fromFactorRef).getRef();
		ORef toDiagramFactorRef = model.getDiagramFactor(toFactorRef).getRef();
		assertFalse("didn't undo?", model.areDiagramFactorsLinked(fromDiagramFactorRef, toDiagramFactorRef));
		verifyLinkageNotPresent(getDiagramLinkRef());

		// undo diagram node add
		project.undo();
		
		// undo diagram node create
		project.undo();
		
		// undo model node create
		project.undo();
		
		verifyNodeNotPresent(toFactorRef);

		// undo diagram node add
		project.undo();
		
		// undo diagram node create
		project.undo();
		
		// undo model node create
		project.undo();
		
		verifyNodeNotPresent(fromFactorRef);

		try
		{
			EAM.setLogToString();
			project.undo();
			fail("Should have thrown");
		}
		catch(CommandFailedException ignoreExpected)
		{
		}
		EAM.setLogToConsole();
	}
	
	public void testUndoThenCommandThenUndo() throws Exception
	{
		project.undo();
		
		ORef factorRef = project.createFactorAndReturnRef(ObjectType.CAUSE);
		CreateDiagramFactorParameter extraDiagramFactorInfo = new CreateDiagramFactorParameter(factorRef);
		CommandCreateObject createDiagramFactorCommand = new CommandCreateObject(ObjectType.DIAGRAM_FACTOR, extraDiagramFactorInfo);
		project.executeCommand(createDiagramFactorCommand);
		
		DiagramFactorId diagramFactorId = (DiagramFactorId) createDiagramFactorCommand.getCreatedId();
		ORef diagramFactorRef = createDiagramFactorCommand.getObjectRef();
		
		DiagramObject diagramObject = project.getTestingDiagramObject();
		CommandSetObjectData addDiagramFactor = CommandSetObjectData.createAppendIdCommand(diagramObject, DiagramObject.TAG_DIAGRAM_FACTOR_IDS, diagramFactorId);
		project.executeCommand(addDiagramFactor);
		verifyFactorCellPresent(diagramFactorRef);
		project.undo();
		project.undo();
		
		verifyFactorCellNotPresent(diagramFactorRef);
		project.undo();
		
		verifyLinkageNotPresent(getDiagramLinkRef());
	
	}
	
	public void testGetIndexToUndoAndRedo() throws Exception
	{
		Project p = new ProjectForTesting(getName());
		
		assertFalse("already an undoable?", p.canUndo());
		assertFalse("already a redoable?", p.canRedo());
		p.executeCommand(new CommandCreateObject(Stress.getObjectType()));
		assertTrue("can't undo first?", p.canUndo());
		assertFalse("redo before first undo?", p.canRedo());
		p.executeCommand(new CommandCreateObject(Stress.getObjectType()));
		assertTrue("can't undo second?", p.canUndo());
		assertFalse("redo when still no undo?", p.canRedo());
		p.undo();
		assertTrue("can't undo twice?", p.canUndo());
		assertTrue("can't redo first undo?", p.canRedo());
		p.executeCommand(new CommandCreateObject(Stress.getObjectType()));
		assertTrue("can't undo latest?", p.canUndo());
		assertFalse("can redo when undo not last?", p.canRedo());
		p.undo();
		assertTrue("can't undo earlier command?", p.canUndo());
		assertTrue("can't redo very latest?", p.canRedo());
		p.undo();
		assertFalse("can undo beyond first?", p.canUndo());
		assertTrue("can't redo after two undos?", p.canRedo());
		
		p.redo();
		assertTrue("can't undo after redo?", p.canUndo());
		assertTrue("can't redo after redo?", p.canRedo());
		
		p.close();
		
	}
	
	public void testUndoAndRedoAreStored()
	{
		
	}
	
	private void verifyFactorCellPresent(ORef diagramFactorRef) throws Exception
	{
		DiagramModel model = project.getDiagramModel();
		assertNotNull("Node not present?", model.getFactorCellByRef(diagramFactorRef));
	}
	
	private void verifyFactorCellNotPresent(ORef diagramFactorRef)
	{
		DiagramModel model = project.getDiagramModel();
		
		EAM.setLogToString();
		try
		{
			model.getFactorCellByRef(diagramFactorRef);
			fail("Cell should be gone: " + diagramFactorRef);
		}
		catch(Exception ignoreExpected)
		{
		}
		EAM.setLogToConsole();
	}
	
	private void verifyNodeNotPresent(ORef factorRef)
	{
		DiagramModel model = project.getDiagramModel();
		
		EAM.setLogToString();
		assertNull("Found the deleted factor?", model.getFactorCellByWrappedRef(factorRef));
		EAM.setLogToConsole();
	}
	
	private void verifyLinkageNotPresent(ORef diagramLinkRefToUse)
	{
		DiagramModel model = project.getDiagramModel();
		
		EAM.setLogToString();
		try
		{
			model.getDiagramLinkByRef(diagramLinkRefToUse);
			fail("Cell should be gone: " + diagramLinkRefToUse);
		}
		catch(Exception ignoreExpected)
		{
		}
		EAM.setLogToConsole();
	}
	
	private DiagramFactor createModelAndDiagramNodeWithCommands(int type) throws Exception
	{
		CommandCreateObject createModelNodeCommand = new CommandCreateObject(ObjectType.CAUSE);
		project.executeCommand(createModelNodeCommand);
		
		ORef factorRef = createModelNodeCommand.getObjectRef();
		CreateDiagramFactorParameter extraDiagramFactorInfo = new CreateDiagramFactorParameter(factorRef);
		CommandCreateObject createDiagramFactorCommand = new CommandCreateObject(ObjectType.DIAGRAM_FACTOR, extraDiagramFactorInfo);
		project.executeCommand(createDiagramFactorCommand);
		
		DiagramFactorId diagramFactorId = (DiagramFactorId) createDiagramFactorCommand.getCreatedId();
		DiagramFactor diagramFactor = (DiagramFactor) project.findObject(new ORef(ObjectType.DIAGRAM_FACTOR, diagramFactorId));
		
		DiagramObject diagramObject = project.getTestingDiagramObject();
		CommandSetObjectData addDiagramFactor = CommandSetObjectData.createAppendIdCommand(diagramObject, DiagramObject.TAG_DIAGRAM_FACTOR_IDS, diagramFactorId);
		project.executeCommand(addDiagramFactor);
		
		return diagramFactor;
	}

	private ORef getDiagramLinkRef()
	{
		return diagramLinkRef;
	}
	
	private ProjectForTesting project;
	private ORef fromFactorRef;
	private ORef toFactorRef;
	private ORef diagramLinkRef;
}
