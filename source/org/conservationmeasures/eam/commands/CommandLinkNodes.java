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
import org.conservationmeasures.eam.diagram.IdAssigner;
import org.conservationmeasures.eam.diagram.nodes.DiagramLinkage;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.project.Project;

public class CommandLinkNodes extends Command
{
	public CommandLinkNodes(int fromId, int toId)
	{
		this.fromId = fromId;
		this.toId = toId;
		linkageId = IdAssigner.INVALID_ID;
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
	
	public String getCommandName()
	{
		return COMMAND_NAME;
	}

	public void execute(Project target) throws CommandFailedException
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

	public void undo(Project target) throws CommandFailedException
	{
		DiagramModel model = target.getDiagramModel();
		try
		{
			DiagramLinkage linkageToDelete = model.getLinkageById(getLinkageId());
			model.deleteLinkage(linkageToDelete);
		}
		catch (Exception e)
		{
			EAM.logException(e);
			throw new CommandFailedException(e);
		}
		
	}

	public void writeDataTo(DataOutputStream dataOut) throws IOException
	{
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
	

	public static final String COMMAND_NAME = "LinkNodes";

	int fromId;
	int toId;
	int linkageId;
}
