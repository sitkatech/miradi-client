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
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.project.Project;

public class CommandDiagramMove extends Command
{
	public CommandDiagramMove(int deltaX, int deltaY, BaseId[] idsToMove)
	{
		this.deltaX = deltaX;
		this.deltaY = deltaY;
		this.ids = idsToMove;
	}
	
	public CommandDiagramMove(DataInputStream dataIn) throws IOException
	{
		deltaX = dataIn.readInt();
		deltaY = dataIn.readInt();

		int idCount = dataIn.readInt();
		ids = new BaseId[idCount];
		for(int i=0; i < idCount; ++i)
			ids[i] = new BaseId(dataIn.readInt());
	}
	
	public String toString()
	{
		String stringOfIds = "(";
		for(int i=0; i < ids.length; ++i)
			stringOfIds += ids[i] + ",";
		stringOfIds += ")";
		return getCommandName() + ":" + stringOfIds + "," + deltaX + "," + deltaY;
	}
	
	public String getCommandName()
	{
		return COMMAND_NAME;
	}

	public void execute(Project target) throws CommandFailedException
	{
		try
		{
			target.moveNodes(getDeltaX(), getDeltaY(), getIds());
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
			target.moveNodesWithoutNotification(-getDeltaX(), -getDeltaY(), getIds());
		}
		catch (Exception e)
		{
			EAM.logException(e);
			throw new CommandFailedException(e);
		}
	}
	
	public void writeDataTo(DataOutputStream dataOut) throws IOException
	{
		dataOut.writeInt(getDeltaX());
		dataOut.writeInt(getDeltaY());
		dataOut.writeInt(ids.length);
		for(int i=0; i < ids.length; ++i)
			dataOut.writeInt(ids[i].asInt());
	}
	
	public int getDeltaX()
	{
		return deltaX;
	}
	
	public int getDeltaY()
	{
		return deltaY;
	}
	
	public BaseId[] getIds()
	{
		return ids;
	}


	public static final String COMMAND_NAME = "DiagramMove";

	int deltaX;
	int deltaY;
	BaseId[] ids;
}
