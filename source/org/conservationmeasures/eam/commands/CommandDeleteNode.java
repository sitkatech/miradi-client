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
import org.conservationmeasures.eam.diagram.nodes.Node;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.Project;

public class CommandDeleteNode extends Command
{
	public CommandDeleteNode(int idToDelete)
	{
		id = idToDelete;
	}

	public CommandDeleteNode(DataInputStream dataIn) throws IOException
	{
		id = dataIn.readInt();
	}

	public String toString()
	{
		return getCommandName() + ":" + getId();
	}
	
	public static String getCommandName()
	{
		return "DeleteNode";
	}
	
	public Object execute(Project target) throws CommandFailedException
	{
		DiagramModel model = target.getDiagramModel();
		try
		{
			Node nodeToDelete = model.getNodeById(id);
			model.deleteNode(nodeToDelete);
		}
		catch (RuntimeException e)
		{
			EAM.logException(e);
			throw new CommandFailedException();
		}
		return null;
	}

	public void writeTo(DataOutputStream dataOut) throws IOException
	{
		dataOut.writeUTF(getCommandName());
		dataOut.writeInt(getId());
	}

	public int getId()
	{
		return id;
	}

	int id;
}
