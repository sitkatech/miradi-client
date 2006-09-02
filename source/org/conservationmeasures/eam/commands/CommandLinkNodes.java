/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.commands;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.ModelNodeId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.project.Project;

public class CommandLinkNodes extends Command
{
	public CommandLinkNodes(ModelNodeId fromId, ModelNodeId toId)
	{
		this.fromId = fromId;
		this.toId = toId;
		linkageId = BaseId.INVALID;
	}
	
	public CommandLinkNodes(DataInputStream dataIn) throws IOException
	{
		fromId = new ModelNodeId(dataIn.readInt());
		toId = new ModelNodeId(dataIn.readInt());
		linkageId = new BaseId(dataIn.readInt());
	}
	
	public BaseId getLinkageId()
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
		try
		{
			target.deleteLinkage(getLinkageId());
		}
		catch (Exception e)
		{
			EAM.logException(e);
			throw new CommandFailedException(e);
		}
		
	}

	public void writeDataTo(DataOutputStream dataOut) throws IOException
	{
		dataOut.writeInt(getFromId().asInt());
		dataOut.writeInt(getToId().asInt());
		dataOut.writeInt(getLinkageId().asInt());
	}
	
	public ModelNodeId getFromId()
	{
		return fromId;
	}
	
	public ModelNodeId getToId()
	{
		return toId;
	}
	

	public static final String COMMAND_NAME = "LinkNodes";

	ModelNodeId fromId;
	ModelNodeId toId;
	BaseId linkageId;
}
