/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.commands;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.conservationmeasures.eam.diagram.DiagramModel;
import org.conservationmeasures.eam.diagram.nodes.DiagramNode;
import org.conservationmeasures.eam.diagram.nodetypes.NodeType;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.Logging;

public class CommandSetFactorType extends Command
{

	public CommandSetFactorType(int idToUpdate, NodeType typeToUse)
	{
		id = idToUpdate;
		type = typeToUse;
		previousType = null;
	}
	
	public CommandSetFactorType(DataInputStream dataIn) throws IOException
	{
		id = dataIn.readInt();
		type = readNodeType(dataIn);
		previousType = readNodeType(dataIn);
	
	}
	
	public void writeDataTo(DataOutputStream dataOut) throws IOException
	{
		dataOut.writeInt(getId());
		writeNodeType(dataOut, type);
		writeNodeType(dataOut, previousType);
	}
	
	public String getCommandName()
	{
		return COMMAND_NAME;
	}

	public void execute(Project target) throws CommandFailedException
	{
		previousType = doSetType(target, getCurrentType(), getPreviousType()); 
	}
	
	public void undo(Project target) throws CommandFailedException
	{
		doSetType(target, getPreviousType(), getCurrentType());
	}
	
	private NodeType doSetType(Project target, NodeType desiredType, NodeType expectedType) throws CommandFailedException
	{
		try
		{
			DiagramModel model = target.getDiagramModel();
			DiagramNode node = model.getNodeById(getId());
			NodeType currentType = node.getType();
			if(expectedType != null && !currentType.equals(expectedType))
				throw new Exception("CommandFactorSetType expected " + expectedType + " but was " + currentType);
			node.setType(desiredType);
			Logging.logDebug("Updating Type:" + desiredType);
			model.updateCell(node);
			return currentType;
		}
		catch (Exception e)
		{
			EAM.logException(e);
			throw new CommandFailedException(e);
		}
	}

	public String toString()
	{
		return getCommandName() + ": " + id + ", " + type + ", " + previousType;
	}

	public NodeType getCurrentType()
	{
		return type;
	}
	
	public NodeType getPreviousType()
	{
		return previousType;
	}

	int getId()
	{
		return id;
	}
	
	public static final String COMMAND_NAME = "SetFactorType";
	
	int id;
	NodeType type;
	NodeType previousType;
}
