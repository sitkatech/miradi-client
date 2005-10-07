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
import org.conservationmeasures.eam.project.Project;

public class CommandInterviewSetStep extends Command
{
	public CommandInterviewSetStep(String destinationStepName)
	{
		toStep = destinationStepName;
	}

	public CommandInterviewSetStep(DataInputStream dataIn) throws IOException
	{
		toStep = dataIn.readUTF();
		fromStep = dataIn.readUTF();
	}
	
	public String getCommandName()
	{
		return COMMAND_NAME;
	}

	public void execute(Project target) throws CommandFailedException
	{
		fromStep = target.getCurrentInterviewStepName();
		target.setCurrentInterviewStepName(getToStep());
	}

	public void undo(Project target) throws CommandFailedException
	{
		target.setCurrentInterviewStepName(fromStep);
	}

	public void writeDataTo(DataOutputStream dataOut) throws IOException
	{
		dataOut.writeUTF(getToStep());
		dataOut.writeUTF(getFromStep());
	}
	
	public String getToStep()
	{
		return toStep;
	}
	
	public String getFromStep()
	{
		return fromStep;
	}


	public static final String COMMAND_NAME = "InterviewSetStep";

	private String toStep;
	private String fromStep;
}
