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
import org.conservationmeasures.eam.main.BaseProject;

public class CommandWizardNext extends Command
{
	public CommandWizardNext(String stepNameToUse)
	{
		stepName = stepNameToUse;
	}
	
	public CommandWizardNext(DataInputStream dataIn) throws IOException
	{
		stepName = dataIn.readUTF();
	}
	
	public static String getCommandName()
	{
		return "WizardNext";
	}

	public void execute(BaseProject target) throws CommandFailedException
	{
		target.setCurrentInterviewStepName("P1aT2S1");
	}

	public void undo(BaseProject target) throws CommandFailedException
	{
		target.setCurrentInterviewStepName("welcome");
	}

	public void writeTo(DataOutputStream dataOut) throws IOException
	{
		dataOut.writeUTF(getCommandName());
		dataOut.writeUTF(getStepName());
	}

	public String getStepName()
	{
		return stepName;
	}

	String stepName;

}
