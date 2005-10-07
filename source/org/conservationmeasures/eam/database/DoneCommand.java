/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.database;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.conservationmeasures.eam.commands.Command;

public class DoneCommand
{
	public static DoneCommand buildFromCommand(Command buildFrom) throws IOException
	{
		DoneCommand done = new DoneCommand();
		done.setName(buildFrom.getCommandName());
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		DataOutputStream dataOut = new DataOutputStream(out);
		buildFrom.writeDataTo(dataOut);
		done.setData(out.toByteArray());
		return done;
	}
	
	public String getName()
	{
		return name;
	}
	
	public void setName(String nameToUse)
	{
		name = nameToUse;
	}

	public byte[] getData()
	{
		return data;
	}
	
	public void setData(byte[] dataToUse)
	{
		data = dataToUse;
	}
	
	String name;
	byte[] data;
}
