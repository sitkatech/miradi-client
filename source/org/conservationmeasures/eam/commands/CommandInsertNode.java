/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.commands;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.conservationmeasures.eam.diagram.nodetypes.NodeType;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.project.Project;


public class CommandInsertNode extends Command
{
	public CommandInsertNode(NodeType nodeType)
	{
		type = nodeType;
		insertedId = new BaseId();
	}
	
	public CommandInsertNode(DataInputStream dataIn) throws IOException
	{
		type = readNodeType(dataIn);
		insertedId = new BaseId(dataIn.readInt());
	}
	
	public NodeType getNodeType()
	{
		return type;
	}
	
	public BaseId getId()
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
			insertedId = target.insertNodeAtId(getNodeType(), getId());
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
			target.deleteNode(getId());
		}
		catch (Exception e)
		{
			EAM.logException(e);
			throw new CommandFailedException(e);
		}
	}

	public void writeDataTo(DataOutputStream dataOut) throws IOException
	{
		writeNodeType(dataOut, type);
		dataOut.writeInt(insertedId.asInt());
	}
	

	public static final String COMMAND_NAME = "CommandInsertNode";

	private NodeType type;
	private BaseId insertedId;
}
