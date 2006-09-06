/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.commands;

import org.conservationmeasures.eam.diagram.nodetypes.NodeType;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.ModelNodeId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.project.Project;


public class CommandInsertNode extends Command
{
	public CommandInsertNode(NodeType nodeType)
	{
		type = nodeType;
		insertedId = new ModelNodeId(BaseId.INVALID.asInt());
	}
	
	public NodeType getNodeType()
	{
		return type;
	}
	
	public ModelNodeId getId()
	{
		return insertedId;
	}

	public String toString()
	{
		return getCommandName() + ":" + getNodeType() + ","+ getId();
	}
	
	public String getCommandName()
	{
		return COMMAND_NAME;
	}

	public void execute(Project target) throws CommandFailedException
	{
		try
		{
			insertedId = createNode(target, getNodeType(), getId());
		}
		catch (Exception e)
		{
			EAM.logException(e);
			throw new CommandFailedException(e);
		}
	}

	public void undo(Project target) throws CommandFailedException
	{
		try
		{
			CommandDeleteNode.deleteNode(target, getId());
		}
		catch (Exception e)
		{
			EAM.logException(e);
			throw new CommandFailedException(e);
		}
	}

	public static ModelNodeId createNode(Project target, NodeType nodeType, BaseId id) throws Exception
	{
		ModelNodeId nodeId = target.createModelNode(nodeType, id);
		target.addNodeToDiagram(nodeId);
		return nodeId;
	}


	public static final String COMMAND_NAME = "CommandInsertNode";

	private NodeType type;
	private ModelNodeId insertedId;
}
