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
import org.conservationmeasures.eam.project.Project;

public class CommandSetData extends Command
{
	public CommandSetData(String fieldNameToUse, String fieldDataToUse)
	{
		fieldName = fieldNameToUse;
		fieldData = fieldDataToUse;
	}

	public CommandSetData(DataInputStream dataIn) throws IOException
	{
		fieldName = dataIn.readUTF();
		fieldData = dataIn.readUTF();
		oldFieldData = dataIn.readUTF();
	}
	
	public String getCommandName()
	{
		return COMMAND_NAME;
	}

	public void execute(Project target) throws CommandFailedException
	{
		EAM.logVerbose("CommandSetData.execute: " + getFieldName() + " -> " + getFieldData());
		oldFieldData = target.getDataValue(getFieldName());
		target.setDataValue(getFieldName(), getFieldData());
	}

	public void undo(Project target) throws CommandFailedException
	{
		target.setDataValue(getFieldName(), getOldFieldData());
	}

	public void writeDataTo(DataOutputStream dataOut) throws IOException
	{
		dataOut.writeUTF(getFieldName());
		dataOut.writeUTF(getFieldData());
		dataOut.writeUTF(getOldFieldData());
	}
	
	public String getFieldName()
	{
		return fieldName;
	}

	public String getFieldData()
	{
		return fieldData;
	}

	public String getOldFieldData()
	{
		return oldFieldData;
	}


	public static final String COMMAND_NAME = "SetData";

	String fieldName;
	String fieldData;
	String oldFieldData;
}
