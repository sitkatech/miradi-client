/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.project;

import java.util.Vector;

import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.diagram.nodetypes.NodeType;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.ModelNodeId;
import org.conservationmeasures.eam.objecthelpers.CreateModelNodeParameter;
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
	
	public ModelNodeId createNode(NodeType nodeType) throws Exception
	{
		CreateModelNodeParameter createTarget = new CreateModelNodeParameter(nodeType);
		ModelNodeId cmTargetId = (ModelNodeId)createObject(ObjectType.MODEL_NODE, BaseId.INVALID, createTarget);
		return cmTargetId;
	}
	

	public ModelNodeId createNodeAndAddToDiagram(NodeType nodeType, BaseId id) throws Exception
	{
		CreateModelNodeParameter parameter = new CreateModelNodeParameter(nodeType);
		ModelNodeId nodeId = (ModelNodeId)createObject(ObjectType.MODEL_NODE, id, parameter);
		addNodeToDiagram(nodeId);
		return nodeId;
	}


	Vector commandStack;
}
