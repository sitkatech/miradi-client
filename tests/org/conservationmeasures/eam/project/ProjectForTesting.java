/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.project;

import java.util.Vector;

import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.diagram.factortypes.FactorType;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.FactorId;
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
	

	public FactorId createNodeAndAddToDiagram(FactorType nodeType, BaseId id) throws Exception
	{
		CreateFactorParameter parameter = new CreateFactorParameter(nodeType);
		FactorId nodeId = (FactorId)createObject(ObjectType.FACTOR, id, parameter);
		addFactorToDiagram(nodeId);
		return nodeId;
	}


	Vector commandStack;
}
