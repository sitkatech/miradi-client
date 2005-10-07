/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.commands;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.conservationmeasures.eam.diagram.nodes.Node;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.project.Project;


public class CommandInsertNode extends Command
{
	public CommandInsertNode(int nodeType)
	{
		type = nodeType;
		insertedId = Node.INVALID_ID;
	}
	
	public CommandInsertNode(DataInputStream dataIn) throws IOException
	{
		type = dataIn.readInt();
		insertedId = dataIn.readInt();
	}
	
	public int getNodeType()
	{
		return type;
	}
	
	public int getId()
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
		dataOut.writeInt(type);
		dataOut.writeInt(insertedId);
	}
	

	public static final String COMMAND_NAME = "CommandInsertNode";

	private int type;
	private int insertedId;
}
