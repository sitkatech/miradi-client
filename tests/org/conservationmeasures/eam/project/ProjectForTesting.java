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
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.DiagramFactorId;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.objecthelpers.CreateDiagramFactorParameter;
import org.conservationmeasures.eam.objecthelpers.CreateFactorParameter;
import org.conservationmeasures.eam.objecthelpers.ObjectType;



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
	
	public FactorId createNode(FactorType nodeType) throws Exception
	{
		CreateFactorParameter createTarget = new CreateFactorParameter(nodeType);
		FactorId cmTargetId = (FactorId)createObject(ObjectType.FACTOR, BaseId.INVALID, createTarget);
		return cmTargetId;
	}
	

	public FactorId createNodeAndAddToDiagram(FactorType nodeType) throws Exception
	{
		FactorId factorId = createNode(nodeType);
		CreateDiagramFactorParameter extraDiagramFactorInfo = new CreateDiagramFactorParameter(factorId);
		CommandCreateObject createDiagramFactorCommand = new CommandCreateObject(ObjectType.DIAGRAM_FACTOR, extraDiagramFactorInfo);
		executeCommand(createDiagramFactorCommand);
		
		DiagramFactorId diagramFactorId = (DiagramFactorId) createDiagramFactorCommand.getCreatedId();
		CommandDiagramAddFactor addDiagramFactorCommand = new CommandDiagramAddFactor(diagramFactorId);
		executeCommand(addDiagramFactorCommand);
				
		return factorId;
	}
	
	public FactorCell createFactorCell(FactorType nodeType) throws Exception
	{
		FactorId insertedId = createNodeAndAddToDiagram(nodeType);
		return getDiagramModel().getDiagramFactorByWrappedId(insertedId);
	}
	


	Vector commandStack;
}
