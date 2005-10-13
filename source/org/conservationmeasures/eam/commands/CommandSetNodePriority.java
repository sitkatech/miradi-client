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
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.Logging;

public class CommandSetNodePriority extends Command 
{
	public CommandSetNodePriority(int idToUpdate, int priorityToUse)
	{
		id = idToUpdate;
		priority = priorityToUse;
		previousPriority = INVALID;
	}

	public CommandSetNodePriority(DataInputStream dataIn) throws IOException
	{
		id = dataIn.readInt();
		priority = dataIn.readInt();
		previousPriority = dataIn.readInt();
	}
	
	public void writeDataTo(DataOutputStream dataOut) throws IOException
	{
		dataOut.writeInt(getId());
		dataOut.writeInt(getCurrentPriority());
		dataOut.writeInt(getPreviousPriority());
	}
	
	public String getCommandName() 
	{
		return COMMAND_NAME;
	}
	
	public int getCurrentPriority()
	{
		return priority;
	}
	
	public int getPreviousPriority()
	{
		return previousPriority;
	}

	public void execute(Project target) throws CommandFailedException
	{
		previousPriority = doSetPriority(target, getCurrentPriority(), getPreviousPriority()); 
	}
	
	public void undo(Project target) throws CommandFailedException
	{
		doSetPriority(target, getPreviousPriority(), getCurrentPriority());
	}
	
	private int doSetPriority(Project target, int desiredPriority, int expectedPriority) throws CommandFailedException
	{
		try
		{
			DiagramModel model = target.getDiagramModel();
			DiagramNode node = model.getNodeById(getId());
			int currentPriority = node.getNodePriority();
			if(expectedPriority != INVALID && currentPriority != expectedPriority)
				throw new Exception("CommandSetNodePriority expected " + expectedPriority + " but was " + currentPriority);
			node.setNodePriority(desiredPriority);
			Logging.logDebug("Updating Priority:"+DiagramNode.getNodePriorityString(desiredPriority));
			model.updateCell(node);
			return currentPriority;
		}
		catch (Exception e)
		{
			EAM.logException(e);
			throw new CommandFailedException(e);
		}
	}
	

	public String toString()
	{
		return getCommandName() + ": " + id + ", " + priority + ", " + previousPriority;
	}
	
	int getId()
	{
		return id;
	}
	
	private static final int INVALID = -1;
	public static final String COMMAND_NAME = "SetNodePriority";

	int id;
	int priority;
	int previousPriority;

}
