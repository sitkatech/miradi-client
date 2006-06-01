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
import org.conservationmeasures.eam.diagram.nodes.DiagramNode;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.project.IdAssigner;
import org.conservationmeasures.eam.project.Project;

public class CommandSetIndicator extends Command 
{
	public CommandSetIndicator(int idToUpdate, int indicatorToUse)
	{
		id = idToUpdate;
		indicatorId = indicatorToUse;
		previousIndicator = IdAssigner.INVALID_ID;
	}

	public String getCommandName() 
	{
		return COMMAND_NAME;
	}

	public CommandSetIndicator(DataInputStream dataIn) throws IOException
	{
		id = dataIn.readInt();
		indicatorId = dataIn.readInt();
		previousIndicator = dataIn.readInt();
	
	}
	
	public void writeDataTo(DataOutputStream dataOut) throws IOException
	{
		dataOut.writeInt(getId());
		dataOut.writeInt(indicatorId);
		dataOut.writeInt(previousIndicator);
	}

	public void execute(Project target) throws CommandFailedException
	{
		previousIndicator = doSetIndicator(target, getCurrentIndicator(), getPreviousIndicator()); 
	}
	
	public void undo(Project target) throws CommandFailedException
	{
		doSetIndicator(target, getPreviousIndicator(), getCurrentIndicator());
	}
	
	private int doSetIndicator(Project target, int desiredIndicator, int expectedIndicator) throws CommandFailedException
	{
		try
		{
			DiagramModel model = target.getDiagramModel();
			DiagramNode node = model.getNodeById(getId());
			int oldIndicator = node.getIndicatorId();
			if(expectedIndicator != IdAssigner.INVALID_ID && oldIndicator != expectedIndicator)
				throw new Exception("CommandSetIndicator expected " + expectedIndicator + " but was " + oldIndicator);
			target.setIndicator(getId(), desiredIndicator);
			return oldIndicator;
		}
		catch (Exception e)
		{
			EAM.logException(e);
			throw new CommandFailedException(e);
		}
	}
	
	public String toString()
	{
		return getCommandName() + ": " + id + ", " + indicatorId + ", " + previousIndicator;
	}

	public int getCurrentIndicator()
	{
		return indicatorId;
	}
	
	public int getPreviousIndicator()
	{
		return previousIndicator;
	}

	int getId()
	{
		return id;
	}
	
	public static final String COMMAND_NAME = "SetIndicator";

	int id;
	int indicatorId;
	int previousIndicator;
}
