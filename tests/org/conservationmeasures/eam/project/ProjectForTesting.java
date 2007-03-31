/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.project;

import java.util.Vector;

import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.commands.CommandCreateObject;
import org.conservationmeasures.eam.commands.CommandDiagramAddFactor;
import org.conservationmeasures.eam.diagram.cells.FactorCell;
import org.conservationmeasures.eam.diagram.factortypes.FactorType;
import org.conservationmeasures.eam.ids.AssignmentId;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.DiagramFactorId;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.ids.TaskId;
import org.conservationmeasures.eam.objecthelpers.CreateAssignmentParameter;
import org.conservationmeasures.eam.objecthelpers.CreateDiagramFactorParameter;
import org.conservationmeasures.eam.objecthelpers.CreateFactorParameter;
import org.conservationmeasures.eam.objecthelpers.CreateTaskParameter;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.DiagramFactor;



public class ProjectForTesting extends Project
{
	public ProjectForTesting(String testName) throws Exception
	{
		super(new ProjectServerForTesting());
		getTestDatabase().openMemoryDatabase(testName);
		finishOpening();
		commandStack = new Vector();
	}

	void fireCommandExecuted(Command command) 
	{
		super.fireCommandExecuted(command);
		if(commandStack != null)
			commandStack.add(command);
	}

	Command getLastCommand()
	{
		return (Command)commandStack.remove(commandStack.size()-1);
	}
	
	public ProjectServerForTesting getTestDatabase()
	{
		return (ProjectServerForTesting)getDatabase();
	}
	

	public FactorId createFactor(FactorType nodeType) throws Exception
	{
		CreateFactorParameter createTarget = new CreateFactorParameter(nodeType);
		FactorId cmTargetId = (FactorId)createObject(ObjectType.FACTOR, BaseId.INVALID, createTarget);
		return cmTargetId;
	}
	
	
	public BaseId addItemToKeyEcologicalAttributeList(BaseId id, int type, String tag) throws Exception
	{
		return addItemToList(ObjectType.KEY_ECOLOGICAL_ATTRIBUTE, id,  type,  tag);
	}
	
	
	public BaseId addItemToIndicatorList(BaseId id, int type, String tag) throws Exception
	{
		return addItemToList(ObjectType.INDICATOR, id,  type,  tag);
	}
	
	
	public BaseId addItemToFactorList(BaseId id, int type, String tag) throws Exception
	{
		return addItemToList(ObjectType.FACTOR, id,  type,  tag);
	}
	
	public BaseId addItemToList(int sourceType, BaseId id, int type, String tag) throws Exception
	{
		BaseId baseId = createObject(type);
		IdList idList = new IdList(new BaseId[] {baseId});
		setObjectData(sourceType, id, tag, idList.toString());
		return baseId;
	}


	
	public TaskId createTask(ORef oref) throws Exception
	{
		CreateTaskParameter createTask = new CreateTaskParameter(oref);
		TaskId cmTaskId = (TaskId)createObject(ObjectType.TASK, BaseId.INVALID, createTask);
		return cmTaskId;
	}

	public AssignmentId createAssignment(ORef oref) throws Exception
	{
		CreateAssignmentParameter createAssignment = new CreateAssignmentParameter((TaskId)oref.getObjectId());
		AssignmentId cmAssignmentId = (AssignmentId)createObject(ObjectType.ASSIGNMENT, BaseId.INVALID, createAssignment);
		return cmAssignmentId;
	}
	
	public DiagramFactorId createAndAddFactorToDiagram(FactorType nodeType) throws Exception
	{
		FactorId factorId = createFactor(nodeType);
		CreateDiagramFactorParameter extraDiagramFactorInfo = new CreateDiagramFactorParameter(factorId);
		CommandCreateObject createDiagramFactorCommand = new CommandCreateObject(ObjectType.DIAGRAM_FACTOR, extraDiagramFactorInfo);
		executeCommand(createDiagramFactorCommand);
		
		DiagramFactorId diagramFactorId = (DiagramFactorId) createDiagramFactorCommand.getCreatedId();
		CommandDiagramAddFactor addDiagramFactorCommand = new CommandDiagramAddFactor(diagramFactorId);
		executeCommand(addDiagramFactorCommand);
		
		return diagramFactorId;
	}
	
	//TODO fix method name (remove 2 and come up with better name)
	public DiagramFactor createNodeAndAddToDiagram2(FactorType nodeType) throws Exception
	{
		DiagramFactorId diagramFactorId = createAndAddFactorToDiagram(nodeType);
		DiagramFactor diagramFactor = (DiagramFactor) findObject(new ORef(ObjectType.DIAGRAM_FACTOR, diagramFactorId));

		return diagramFactor;
	}

	public FactorId createNodeAndAddToDiagram(FactorType nodeType) throws Exception
	{
		DiagramFactorId diagramFactorId = createAndAddFactorToDiagram(nodeType);
		DiagramFactor diagramFactor = (DiagramFactor) findObject(new ORef(ObjectType.DIAGRAM_FACTOR, diagramFactorId));
	
		return diagramFactor.getWrappedId();
	}
	
	public FactorCell createFactorCell(FactorType nodeType) throws Exception
	{
		FactorId insertedId = createNodeAndAddToDiagram(nodeType);
		return getDiagramModel().getFactorCellByWrappedId(insertedId);
	}
	


	Vector commandStack;
}
