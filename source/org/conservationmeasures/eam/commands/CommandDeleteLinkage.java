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
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.project.IdAssigner;
import org.conservationmeasures.eam.project.Project;

public class CommandDeleteLinkage extends Command
{
	public CommandDeleteLinkage(int idToDelete)
	{
		id = idToDelete;
		wasFrom = IdAssigner.INVALID_ID;
		wasTo = IdAssigner.INVALID_ID;
	}
	
	public CommandDeleteLinkage(DataInputStream dataIn) throws IOException
	{
		id = dataIn.readInt();
		wasFrom = dataIn.readInt();
		wasTo = dataIn.readInt();
	}
	
	public int getWasFromId()
	{
		return wasFrom;
	}
	
	public int getWasToId()
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
		dataOut.writeInt(getId());
		dataOut.writeInt(getWasFromId());
		dataOut.writeInt(getWasToId());
	}

	public int getId()
	{
		return id;
	}


	public static final String COMMAND_NAME = "DeleteLinkage";

	int id;
	int wasFrom;
	int wasTo;
}
