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
import org.conservationmeasures.eam.diagram.nodes.Linkage;
import org.conservationmeasures.eam.diagram.nodes.Node;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.BaseProject;
import org.conservationmeasures.eam.main.EAM;

public class CommandLinkNodes extends Command
{
	public CommandLinkNodes(int fromId, int toId)
	{
		this.fromId = fromId;
		this.toId = toId;
		linkageId = Node.INVALID_ID;
	}
	
	public CommandLinkNodes(DataInputStream dataIn) throws IOException
	{
		fromId = dataIn.readInt();
		toId = dataIn.readInt();
		linkageId = dataIn.readInt();
	}
	
	public int getLinkageId()
	{
		return linkageId;
	}

	public String toString()
	{
		return getCommandName() + ": " + linkageId + "," + fromId + ", " + toId;
	}
	
	public static String getCommandName()
	{
		return "LinkNodes";
	}
	
	public void execute(BaseProject target) throws CommandFailedException
	{
		try
		{
			linkageId = target.insertLinkageAtId(getLinkageId(), fromId, toId);
		}
		catch (Exception e)
		{
			EAM.logException(e);
			throw new CommandFailedException(e);
		}
	}

	public void undo(BaseProject target) throws CommandFailedException
	{
		DiagramModel model = target.getDiagramModel();
		try
		{
			Linkage linkageToDelete = model.getLinkageInProject(getLinkageId());
			model.deleteLinkage(linkageToDelete);
		}
		catch (Exception e)
		{
			EAM.logException(e);
			throw new CommandFailedException(e);
		}
		
	}

	public void writeTo(DataOutputStream dataOut) throws IOException
	{
		dataOut.writeUTF(getCommandName());
		dataOut.writeInt(getFromId());
		dataOut.writeInt(getToId());
		dataOut.writeInt(getLinkageId());
	}
	
	public int getFromId()
	{
		return fromId;
	}
	
	public int getToId()
	{
		return toId;
	}
	
	int fromId;
	int toId;
	int linkageId;
}
