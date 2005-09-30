/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.commands;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.conservationmeasures.eam.exceptions.AlreadyInThatViewException;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.BaseProject;

public class CommandSwitchView extends Command
{
	public CommandSwitchView(String destinationView)
	{
		toView = destinationView;
	}

	public CommandSwitchView(DataInputStream dataIn) throws IOException
	{
		toView = dataIn.readUTF();
		fromView = dataIn.readUTF();
	}
	
	public String getDestinationView()
	{
		return toView;
	}
	
	public String getPreviousView()
	{
		return fromView;
	}

	public String getCommandName()
	{
		return COMMAND_NAME;
	}

	public void execute(BaseProject target) throws CommandFailedException
	{
		fromView = target.getCurrentView();
		if(fromView.equals(toView))
			throw new AlreadyInThatViewException("Already in view " + toView);
		
		target.switchToView(toView);
	}
	
	public String toString()
	{
		StringBuffer string = new StringBuffer();
		string.append(getCommandName());
		string.append(" from ");
		string.append(getPreviousView());
		string.append( " to ");
		string.append(getDestinationView());
		return string.toString();
	}

	public void undo(BaseProject target) throws CommandFailedException
	{
		target.switchToView(fromView);
	}
	
	public void writeTo(DataOutputStream dataOut) throws IOException
	{
		dataOut.writeUTF(getCommandName());
		dataOut.writeUTF(getDestinationView());
		dataOut.writeUTF(getPreviousView());
	}


	public static final String COMMAND_NAME = "SwitchView";

	String toView;
	String fromView;
}
