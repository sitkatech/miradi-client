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
import org.conservationmeasures.eam.main.EAM;

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

	public void execute(BaseProject target) throws CommandFailedException
	{
		EAM.logVerbose("CommandSetData.execute: " + getFieldName() + " -> " + getFieldData());
		oldFieldData = target.getDataValue(getFieldName());
		target.setDataValue(getFieldName(), getFieldData());
	}

	public void undo(BaseProject target) throws CommandFailedException
	{
		target.setDataValue(getFieldName(), getOldFieldData());
	}

	public void writeTo(DataOutputStream dataOut) throws IOException
	{
		dataOut.writeUTF(getCommandName());
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
