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
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.Project;

public class CommandDeleteNode extends Command
{
	public CommandDeleteNode(int idToDelete)
	{
		id = idToDelete;
		nodeType = Node.TYPE_INVALID;
	}

	public CommandDeleteNode(DataInputStream dataIn) throws IOException
	{
		id = dataIn.readInt();
		nodeType = dataIn.readInt();
	}
	
	public int getNodeType()
	{
		return nodeType;
	}

	public String toString()
	{
		return getCommandName() + ":" + getId();
	}
	
	public static String getCommandName()
	{
		return "DeleteNode";
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
			throw new CommandFailedException();
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
			throw new CommandFailedException();
		}
	}

	public void writeTo(DataOutputStream dataOut) throws IOException
	{
		dataOut.writeUTF(getCommandName());
		dataOut.writeInt(getId());
		dataOut.writeInt(getNodeType());
	}

	public int getId()
	{
		return id;
	}

	int id;
	int nodeType;
}
