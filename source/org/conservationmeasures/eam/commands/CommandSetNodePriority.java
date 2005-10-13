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
import org.conservationmeasures.eam.diagram.nodes.ThreatPriority;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.Logging;

public class CommandSetNodePriority extends Command 
{
	public CommandSetNodePriority(int idToUpdate, ThreatPriority priorityToUse)
	{
		id = idToUpdate;
		priority = priorityToUse;
		previousPriority = null;
	}

	public CommandSetNodePriority(DataInputStream dataIn) throws IOException
	{
		id = dataIn.readInt();
		priority = ThreatPriority.createFromInt(dataIn.readInt());
		previousPriority = ThreatPriority.createFromInt(dataIn.readInt());
	}
	
	public void writeDataTo(DataOutputStream dataOut) throws IOException
	{
		dataOut.writeInt(getId());
		dataOut.writeInt(getCurrentPriority().getValue());
		dataOut.writeInt(getPreviousPriority().getValue());
	}
	
	public String getCommandName() 
	{
		return COMMAND_NAME;
	}
	
	public ThreatPriority getCurrentPriority()
	{
		return priority;
	}
	
	public ThreatPriority getPreviousPriority()
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
	
	private ThreatPriority doSetPriority(Project target, ThreatPriority desiredPriority, ThreatPriority expectedPriority) throws CommandFailedException
	{
		try
		{
			DiagramModel model = target.getDiagramModel();
			DiagramNode node = model.getNodeById(getId());
			ThreatPriority currentPriority = node.getThreatPriority();
			if(expectedPriority != null && !currentPriority.equals(expectedPriority))
				throw new Exception("CommandSetNodePriority expected " + expectedPriority + " but was " + currentPriority);
			node.setNodePriority(desiredPriority);
			Logging.logDebug("Updating Priority:"+desiredPriority.getStringValue());
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
	
	public static final String COMMAND_NAME = "SetNodePriority";

	int id;
	ThreatPriority priority;
	ThreatPriority previousPriority;

}
