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
package org.miradi.commands;

import java.awt.Dimension;
import java.awt.Point;
import java.util.Vector;

import org.miradi.commands.CommandBeginTransaction;
import org.miradi.commands.CommandCreateObject;
import org.miradi.commands.CommandDeleteObject;
import org.miradi.commands.CommandEndTransaction;
import org.miradi.commands.CommandSetObjectData;
import org.miradi.commands.CommandSetThreatRating;
import org.miradi.diagram.DiagramModel;
import org.miradi.diagram.cells.FactorCell;
import org.miradi.diagram.cells.LinkCell;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.exceptions.NothingToRedoException;
import org.miradi.exceptions.NothingToUndoException;
import org.miradi.ids.BaseId;
import org.miradi.ids.DiagramFactorId;
import org.miradi.ids.DiagramLinkId;
import org.miradi.ids.FactorId;
import org.miradi.ids.FactorLinkId;
import org.miradi.main.CommandExecutedEvent;
import org.miradi.main.CommandExecutedListener;
import org.miradi.main.EAM;
import org.miradi.main.EAMTestCase;
import org.miradi.objecthelpers.CreateDiagramFactorLinkParameter;
import org.miradi.objecthelpers.CreateDiagramFactorParameter;
import org.miradi.objecthelpers.CreateFactorLinkParameter;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.BaseObject;
import org.miradi.objects.DiagramFactor;
import org.miradi.objects.DiagramLink;
import org.miradi.objects.DiagramObject;
import org.miradi.objects.RatingCriterion;
import org.miradi.project.Project;
import org.miradi.project.ProjectForTesting;
import org.miradi.project.threatrating.SimpleThreatRatingFramework;
import org.miradi.utils.EnhancedJsonObject;

public class TestCommands extends EAMTestCase
{
	public TestCommands(String name)
	{
		super(name);
	}
	
	public void setUp() throws Exception
	{
		project = new ProjectForTesting(getName());
		consumeNodeIdZero();
		super.setUp();
	}

	private void consumeNodeIdZero() throws Exception
	{
		project.createFactorAndReturnId(ObjectType.TARGET);
	}
	
	public void tearDown() throws Exception
	{
		super.tearDown();
		project.close();
		project = null;
	}
	
	public void testCommandSetObjectData_RatingCriterion() throws Exception
	{
		int type = ObjectType.RATING_CRITERION;
		BaseId createdId = project.createObjectAndReturnId(type);
		RatingCriterion criterion = project.getSimpleThreatRatingFramework().getCriterion(createdId);
		
		String field = RatingCriterion.TAG_LABEL;
		String value = "Blah";
		CommandSetObjectData cmd = new CommandSetObjectData(type, createdId, field, value);
		assertEquals("wrong type?", type, cmd.getObjectType());
		assertEquals("wrong id?", createdId, cmd.getObjectId());
		assertEquals("wrong field?", field, cmd.getFieldTag());
		assertEquals("wrong value?", value, cmd.getDataValue());
		
		project.executeCommand(cmd);
		assertEquals("didn't set value?", value, criterion.getLabel());
		
		CommandSetObjectData badId = new CommandSetObjectData(type, new BaseId(-99), field, value);
		try
		{
			ignoreLogs();
			project.executeCommand(badId);
			fail("Should have thrown for bad id");
		}
		catch (CommandFailedException ignoreExpected)
		{
		}
		finally 
		{
			logToConsole();
		}
		
		CommandSetObjectData badField = new CommandSetObjectData(type, createdId, "bogus", value);
		try
		{
			ignoreLogs();
			project.executeCommand(badField);
			fail("Should have thrown for bad field tag");
		}
		catch (CommandFailedException ignoreExpected)
		{
		}
		finally
		{
			logToConsole();
		}
		
	}

	private void logToConsole()
	{
		EAM.setLogToConsole();
	}

	private void ignoreLogs()
	{
		EAM.setLogToString();
	}
	
	public void testCommandDeleteObject_ThreatRatingValueOption() throws Exception
	{
		int type = ObjectType.VALUE_OPTION;
		BaseId createdId = project.createObjectAndReturnId(type);
		
		CommandDeleteObject cmd = new CommandDeleteObject(type, createdId);
		assertEquals("wrong type?", type, cmd.getObjectType());
		assertEquals("wrong id?", createdId, cmd.getObjectId());
		
		project.executeCommand(cmd);
		assertNull("Got deleted object?", project.getSimpleThreatRatingFramework().getValueOption(createdId));
		
		project.undo();
		assertNotNull("Didn't undelete?", project.getSimpleThreatRatingFramework().getValueOption(createdId));
	}
	
	public void testCommandDeleteObject_ThreatRatingCriterion() throws Exception
	{
		int type = ObjectType.RATING_CRITERION;
		BaseId createdId = project.createObjectAndReturnId(type);
		
		CommandDeleteObject cmd = new CommandDeleteObject(type, createdId);
		assertEquals("wrong type?", type, cmd.getObjectType());
		assertEquals("wrong id?", createdId, cmd.getObjectId());
		
		project.executeCommand(cmd);
		assertNull("Got deleted object?", project.getSimpleThreatRatingFramework().getCriterion(createdId));
		
		project.undo();
		assertNotNull("Didn't undelete?", project.getSimpleThreatRatingFramework().getCriterion(createdId));
	}
	
	public void testCommandCreateObject_ThreatRatingCriterion() throws Exception
	{
		SimpleThreatRatingFramework framework = project.getSimpleThreatRatingFramework();
		int type = ObjectType.RATING_CRITERION;
		CommandCreateObject cmd = new CommandCreateObject(type);
		assertEquals("wrong type?", type, cmd.getObjectType());
		assertEquals("created id already set?", BaseId.INVALID, cmd.getCreatedId());

		int oldCount = framework.getCriteria().length;
		project.executeCommand(cmd);
		assertEquals("added to framework?", oldCount, framework.getCriteria().length);
		RatingCriterion criterion = framework.getCriterion(cmd.getCreatedId());
		assertEquals("wrong default label?", BaseObject.DEFAULT_LABEL, criterion.getLabel());
		
		assertNotNull("didn't create?", framework.getCriterion(cmd.getCreatedId()));
		
		project.undo();
		assertEquals("didn't undo?", oldCount, framework.getCriteria().length);
	}
	
	public void testDiagramFactorsMove() throws Exception
	{
		Point moveTo = new Point(25, -68);
		Point zeroZero = new Point(0, 0);

		ORef diagramFactorRef = insertTarget();
		DiagramFactor target = (DiagramFactor) project.findObject(diagramFactorRef);
		String newLocation = EnhancedJsonObject.convertFromPoint(moveTo);
		CommandSetObjectData moveDiagramFactor1 = new CommandSetObjectData(ObjectType.DIAGRAM_FACTOR, target.getDiagramFactorId(), DiagramFactor.TAG_LOCATION, newLocation);
		project.executeCommand(moveDiagramFactor1);
		
		DiagramFactor diagramFactor1 = (DiagramFactor) project.findObject(diagramFactorRef);
		assertEquals("didn't set location?", moveTo, diagramFactor1.getLocation());
		//undo move
		project.undo();
		DiagramFactor diagramFactor2 = (DiagramFactor) project.findObject(diagramFactorRef);
		assertEquals("didn't restore original location?", zeroZero, diagramFactor2.getLocation());

		
		DiagramFactorId factorId = insertContributingFactor().getDiagramFactorId();
		DiagramFactor factor = (DiagramFactor) project.findObject(ObjectType.DIAGRAM_FACTOR, factorId);
		String newFactorLocation = EnhancedJsonObject.convertFromPoint(moveTo);
		CommandSetObjectData moveDiagramFactor2 = new CommandSetObjectData(ObjectType.DIAGRAM_FACTOR, factor.getDiagramFactorId(), DiagramFactor.TAG_LOCATION, newFactorLocation);
		project.executeCommand(moveDiagramFactor2);
		
		DiagramFactor diagramFactor3 = (DiagramFactor) project.findObject(ObjectType.DIAGRAM_FACTOR, factorId);
		assertEquals("didn't set location?", moveTo, diagramFactor3.getLocation());
		//undo move
		project.undo();
		DiagramFactor diagramFactor4 = (DiagramFactor) project.findObject(ObjectType.DIAGRAM_FACTOR, factorId);
		assertEquals("didn't restore original location?", zeroZero, diagramFactor4.getLocation());


		DiagramFactorId interventionId = insertIntervention();
		DiagramFactor intervention = (DiagramFactor) project.findObject(ObjectType.DIAGRAM_FACTOR, interventionId);
		String newInterventionLocation = EnhancedJsonObject.convertFromPoint(moveTo);
		CommandSetObjectData moveDiagramFactor3 = new CommandSetObjectData(ObjectType.DIAGRAM_FACTOR, intervention.getDiagramFactorId(), DiagramFactor.TAG_LOCATION, newInterventionLocation);
		project.executeCommand(moveDiagramFactor3);
		
		DiagramFactor diagramFactor5 = (DiagramFactor) project.findObject(ObjectType.DIAGRAM_FACTOR, interventionId);
		assertEquals("didn't set location?", moveTo, diagramFactor5.getLocation());
		//undo move
		project.undo();
		DiagramFactor diagramFactor6 = (DiagramFactor) project.findObject(ObjectType.DIAGRAM_FACTOR, interventionId);
		assertEquals("didn't restore original location?", zeroZero, diagramFactor6.getLocation());
	}
	
	public void testCommandSetThreatRating() throws Exception
	{
		FactorId threatId = new FactorId(100);
		FactorId targetId = new FactorId(101);
		BaseId criterionId = new BaseId(102);
		BaseId valueId = new BaseId(103);
		SimpleThreatRatingFramework framework = project.getSimpleThreatRatingFramework();
		BaseId defaultValueId = framework.getDefaultValueId();
		
		CommandSetThreatRating cmd = new CommandSetThreatRating(threatId, targetId, criterionId, valueId);
		project.executeCommand(cmd);
		assertEquals("Didn't memorize old value?", defaultValueId, cmd.getPreviousValueId());
		assertEquals("Didn't set new value?", valueId, framework.getBundle(threatId, targetId).getValueId(criterionId));
		
		project.undo();
		assertEquals("Didn't undo?", defaultValueId, framework.getBundle(threatId, targetId).getValueId(criterionId));
	}
	
	public void testCommandNodeResized() throws Exception
	{
		ORef diagramFactorRef = insertTarget();
		String defaultSize = EnhancedJsonObject.convertFromDimension(new Dimension(120, 60));
		DiagramModel diagramModel = project.getDiagramModel();
		FactorCell node = diagramModel.getFactorCellByRef(diagramFactorRef);
		String originalSize = EnhancedJsonObject.convertFromDimension(node.getSize());
		assertEquals(defaultSize, originalSize);
		
		String newSize = EnhancedJsonObject.convertFromDimension(new Dimension(88, 22));
		
		DiagramFactorId diagramFactorId = node.getDiagramFactorId();
		CommandSetObjectData cmd = new CommandSetObjectData(ObjectType.DIAGRAM_FACTOR, diagramFactorId, DiagramFactor.TAG_SIZE, newSize);
		project.executeCommand(cmd);
 
		DiagramFactor diagramFactor = (DiagramFactor) project.findObject(ObjectType.DIAGRAM_FACTOR, diagramFactorId);
		assertEquals("didn't memorize old size?", originalSize, cmd.getPreviousDataValue());
		assertEquals("didn't change to new size?", newSize, EnhancedJsonObject.convertFromDimension(diagramFactor.getSize()));

		project.undo();
		Dimension size = diagramModel.getFactorCellByRef(diagramFactorRef).getSize();
		String sizeAsString = EnhancedJsonObject.convertFromDimension(size);
		assertEquals("didn't undo?", originalSize, sizeAsString);
	}

	public void testCommandDiagramAddLinkage() throws Exception
	{
		DiagramModel model = project.getDiagramModel();

		DiagramFactor from = insertNode(ObjectType.CAUSE);
		DiagramFactor to = insertNode(ObjectType.TARGET);
		
		CreateFactorLinkParameter extraInfo = new CreateFactorLinkParameter(from.getWrappedORef(), to.getWrappedORef());
		CommandCreateObject createModelLinkage = new CommandCreateObject(ObjectType.FACTOR_LINK, extraInfo);
		project.executeCommand(createModelLinkage);
		
		FactorLinkId modelLinkageId = (FactorLinkId)createModelLinkage.getCreatedId();
		ORef factorLinkRef = createModelLinkage.getObjectRef();
		DiagramFactorId fromDiagramFactorId = from.getDiagramFactorId();
		DiagramFactorId toDiagramFactorId = to.getDiagramFactorId();
		CreateDiagramFactorLinkParameter diagramLinkExtraInfo = new CreateDiagramFactorLinkParameter(modelLinkageId, fromDiagramFactorId, toDiagramFactorId);
		
		CommandCreateObject createDiagramLinkCommand =  new CommandCreateObject(ObjectType.DIAGRAM_LINK, diagramLinkExtraInfo);
    	project.executeCommand(createDiagramLinkCommand);
    	
    	BaseId createdId = createDiagramLinkCommand.getCreatedId();
		DiagramLinkId diagramFactorLinkId = new DiagramLinkId(createdId.asInt()); 
		DiagramObject diagramObject = project.getTestingDiagramObject();
		CommandSetObjectData addLink = CommandSetObjectData.createAppendIdCommand(diagramObject, DiagramObject.TAG_DIAGRAM_FACTOR_LINK_IDS, diagramFactorLinkId);
		project.executeCommand(addLink);
		
		DiagramLink inserted = model.getDiagramLinkByWrappedRef(factorLinkRef);
		LinkCell cell = model.findLinkCell(inserted);
		ORef fromDiagramFactorRef = cell.getFrom().getDiagramFactorRef();
		assertEquals("wrong source?", from.getRef(), fromDiagramFactorRef);
		ORef toDiagramFactorRef = cell.getTo().getDiagramFactorRef();
		assertEquals("wrong dest?", to.getRef(), toDiagramFactorRef);

		assertTrue("linkage not created?", project.getDiagramModel().areDiagramFactorsLinked(fromDiagramFactorRef, toDiagramFactorRef));
		project.undo();
		
		project.undo();
		assertFalse("didn't remove linkage?", project.getDiagramModel().areDiagramFactorsLinked(fromDiagramFactorRef, toDiagramFactorRef));
		
		project.undo();
		assertNull("didn't delete linkage from pool?", project.getFactorLinkPool().find(modelLinkageId));
	}

	public void testBeginTransaction() throws Exception
	{
		CommandBeginTransaction cmd = new CommandBeginTransaction();
		assertTrue(cmd.isBeginTransaction());
		assertFalse(cmd.isEndTransaction());
		project.executeCommand(cmd);

		EAM.setLogToConsole();
	}

	public void testEndTransaction() throws Exception
	{
		CommandEndTransaction cmd = new CommandEndTransaction();
		assertTrue(cmd.isEndTransaction());
		assertFalse(cmd.isBeginTransaction());
		project.executeCommand(cmd);

		EAM.setLogToConsole();
	}

	public void testUndoWhenNothingToUndo() throws Exception
	{
		Project emptyProject = new ProjectForTesting(getName());
		try
		{
			EAM.setLogToString();
			emptyProject.undo();
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
		ORef targetRef = insertTarget();
		project.undo();
		project.redo();
		
		FactorCell inserted = project.getDiagramModel().getFactorCellByRef(targetRef);
		assertTrue("wrong node?", inserted.isTarget());
		
	}
	
	public void testRedoWhenNothingToRedo() throws Exception
	{
		Project emptyProject = new ProjectForTesting(getName());
		try
		{
			EAM.setLogToString();
			emptyProject.redo();
			fail("Should have thrown");
		}
		catch(NothingToRedoException ignoreExpected)
		{
		}
		EAM.setLogToConsole();
		emptyProject.close();
	}
	
	static class UndoListener implements CommandExecutedListener
	{
		public UndoListener()
		{
			undoneCommands = new Vector();
		}
		
		public void commandExecuted(CommandExecutedEvent event)
		{
			undoneCommands.add(event.getCommand());
			
		}

		Vector undoneCommands;
	}
	
	public void testUndoFiresCommandUndone() throws Exception
	{
		UndoListener undoListener = new UndoListener();
		project.addCommandExecutedListener(undoListener);
		CommandCreateObject cmd = new CommandCreateObject(ObjectType.TASK);
		project.executeCommand(cmd);
		project.undo();
		assertEquals("didn't undo one command?", 2, undoListener.undoneCommands.size());
		assertEquals("didn't fire proper undo?", cmd.toString(), undoListener.undoneCommands.get(0).toString());
		project.removeCommandExecutedListener(undoListener);
	}
	
	private ORef insertTarget() throws Exception
	{
		return insertNode(ObjectType.TARGET).getRef();
	}
	
	private DiagramFactor insertContributingFactor() throws Exception
	{
		return insertNode(ObjectType.CAUSE);
	}

	private DiagramFactorId insertIntervention() throws Exception
	{
		return insertNode(ObjectType.STRATEGY).getDiagramFactorId();
	}

	private DiagramFactor insertNode(int type) throws Exception
	{
		FactorId factorId = project.createFactorAndReturnId(type);
		CreateDiagramFactorParameter extraInfo = new CreateDiagramFactorParameter(new ORef(type, factorId));
		CommandCreateObject createDiagramFactorCommand = new CommandCreateObject(ObjectType.DIAGRAM_FACTOR, extraInfo);
		project.executeCommand(createDiagramFactorCommand);
		
		DiagramFactorId diagramFactorId = (DiagramFactorId) createDiagramFactorCommand.getCreatedId();
		DiagramObject diagramObject = project.getTestingDiagramObject();
		CommandSetObjectData addDiagramFactor = CommandSetObjectData.createAppendIdCommand(diagramObject, DiagramObject.TAG_DIAGRAM_FACTOR_IDS, diagramFactorId);
		project.executeCommand(addDiagramFactor);
		
		return (DiagramFactor) project.findObject(new ORef(ObjectType.DIAGRAM_FACTOR, diagramFactorId));
	}
	
	ProjectForTesting project;
}
