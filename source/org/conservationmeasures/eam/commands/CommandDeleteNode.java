/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.commands;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.conservationmeasures.eam.diagram.nodes.DiagramNode;
import org.conservationmeasures.eam.diagram.nodetypes.NodeType;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.project.Project;

public class CommandDeleteNode extends Command
{
	public CommandDeleteNode(int idToDelete)
	{
		id = idToDelete;
		nodeType = DiagramNode.TYPE_INVALID;
	}

	public CommandDeleteNode(DataInputStream dataIn) throws IOException
	{
		id = dataIn.readInt();
		nodeType = readNodeType(dataIn);
	}
	
	public NodeType getNodeType()
	{
		return nodeType;
	}

	public String toString()
	{
		return getCommandName() + ":" + getId();
	}
	
	public String getCommandName()
	{
		return COMMAND_NAME;
	}

	public void execute(Project target) throws CommandFailedException
	{
		try
		{
			nodeType = target.deleteNode(getId());
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
			target.insertNodeAtId(getNodeType(), getId());
		}
		catch (Exception e)
		{
			EAM.logException(e);
			throw new CommandFailedException(e);
		}
	}

	public void writeDataTo(DataOutputStream dataOut) throws IOException
	{
		dataOut.writeInt(getId());
		writeNodeType(dataOut, getNodeType());
	}

	public int getId()
	{
		return id;
	}


	public static final String COMMAND_NAME = "DeleteNode";

	int id;
	NodeType nodeType;
}
