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
import org.conservationmeasures.eam.project.IdAssigner;
import org.conservationmeasures.eam.project.Project;

public class CommandSetNodePriority extends Command 
{
	public CommandSetNodePriority(int idToUpdate, ThreatRatingValue priorityToUse)
	{
		id = idToUpdate;
	}

	public CommandSetNodePriority(DataInputStream dataIn) throws IOException
	{
		id = dataIn.readInt();
		dataIn.readInt();
		dataIn.readInt();
	}
	
	public void writeDataTo(DataOutputStream dataOut) throws IOException
	{
		dataOut.writeInt(getId());
		dataOut.writeInt(IdAssigner.INVALID_ID);
		dataOut.writeInt(IdAssigner.INVALID_ID);
	}
	
	public String getCommandName() 
	{
		return COMMAND_NAME;
	}
	
	public void execute(Project target) throws CommandFailedException
	{
		doSetPriority(); 
	}
	
	public void undo(Project target) throws CommandFailedException
	{
		doSetPriority();
	}
	
	private void doSetPriority() throws CommandFailedException
	{
		EAM.logVerbose("Warning: Ignoring obsolete SetNodePriority command");
	}
	

	public String toString()
	{
		return getCommandName() + ": " + id;
	}
	
	int getId()
	{
		return id;
	}
	
	public static final String COMMAND_NAME = "SetNodePriority";

	int id;
}
