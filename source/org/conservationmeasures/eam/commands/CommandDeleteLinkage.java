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
import org.conservationmeasures.eam.diagram.nodes.DiagramLinkage;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.project.Project;

public class CommandDeleteLinkage extends Command
{
	public CommandDeleteLinkage(BaseId idToDelete)
	{
		id = idToDelete;
		wasFrom = BaseId.INVALID;
		wasTo = BaseId.INVALID;
	}
	
	public CommandDeleteLinkage(DataInputStream dataIn) throws IOException
	{
		id = new BaseId(dataIn.readInt());
		wasFrom = new BaseId(dataIn.readInt());
		wasTo = new BaseId(dataIn.readInt());
	}
	
	public BaseId getWasFromId()
	{
		return wasFrom;
	}
	
	public BaseId getWasToId()
	{
		return wasTo;
	}

	public String toString()
	{
		return getCommandName() + ":" + getId() + "," + getWasFromId() + "," + getWasToId();
	}
	
	public String getCommandName()
	{
		return COMMAND_NAME;
	}

	public void execute(Project target) throws CommandFailedException
	{
		DiagramModel model = target.getDiagramModel();
		try
		{
			DiagramLinkage linkageToDelete = model.getLinkageById(id);
			wasFrom = linkageToDelete.getFromNode().getId();
			wasTo = linkageToDelete.getToNode().getId();
			target.deleteLinkage(id);
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
			target.insertLinkageAtId(getId(), getWasFromId(), getWasToId());
		}
		catch (Exception e)
		{
			EAM.logException(e);
			throw new CommandFailedException(e);
		}
	}

	public void writeDataTo(DataOutputStream dataOut) throws IOException
	{
		dataOut.writeInt(getId().asInt());
		dataOut.writeInt(getWasFromId().asInt());
		dataOut.writeInt(getWasToId().asInt());
	}

	public BaseId getId()
	{
		return id;
	}


	public static final String COMMAND_NAME = "DeleteLinkage";

	BaseId id;
	BaseId wasFrom;
	BaseId wasTo;
}
