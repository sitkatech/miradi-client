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
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.ThreatRatingValue;
import org.conservationmeasures.eam.project.Project;

public class CommandSetNodePriority extends Command 
{
	public CommandSetNodePriority(int idToUpdate, ThreatRatingValue priorityToUse)
	{
		id = idToUpdate;
		priority = priorityToUse;
		previousPriority = null;
	}

	public CommandSetNodePriority(DataInputStream dataIn) throws IOException
	{
		id = dataIn.readInt();
		priority = ThreatRatingValue.createFromInt(dataIn.readInt());
		previousPriority = ThreatRatingValue.createFromInt(dataIn.readInt());
	}
	
	public void writeDataTo(DataOutputStream dataOut) throws IOException
	{
		dataOut.writeInt(getId());
		dataOut.writeInt(getCurrentPriority().getRatingOptionId());
		dataOut.writeInt(getPreviousPriority().getRatingOptionId());
	}
	
	public String getCommandName() 
	{
		return COMMAND_NAME;
	}
	
	public ThreatRatingValue getCurrentPriority()
	{
		return priority;
	}
	
	public ThreatRatingValue getPreviousPriority()
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
	
	private ThreatRatingValue doSetPriority(Project target, ThreatRatingValue desiredPriority, ThreatRatingValue expectedPriority) throws CommandFailedException
	{
		EAM.logVerbose("Warning: Ignoring obsolete SetNodePriority command");
		return ThreatRatingValue.createNotUsed();
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
	ThreatRatingValue priority;
	ThreatRatingValue previousPriority;

}
