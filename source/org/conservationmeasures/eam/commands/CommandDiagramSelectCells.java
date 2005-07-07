/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.commands;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class CommandDiagramSelectCells extends Command
{
	public CommandDiagramSelectCells(int[] selectedCellIds)
	{
		ids = selectedCellIds;
	}
	
	public CommandDiagramSelectCells(DataInputStream dataIn) throws IOException
	{
		int idCount = dataIn.readInt();
		ids = new int[idCount];
		for(int i=0; i < idCount; ++i)
			ids[i] = dataIn.readInt();
	}
	
	public String toString()
	{
		String result = "DIAGRAM-SELECT:";
		for(int i=0; i < ids.length; ++i)
			result += ids[i] + ",";
		return result;
	}

	public static String getCommandName()
	{
		return "DiagramSelectCells";
	}
	
	public void writeTo(OutputStream out) throws IOException
	{
		DataOutputStream dataOut = new DataOutputStream(out);
		dataOut.writeUTF(getCommandName());
		dataOut.writeInt(ids.length);
		for(int i=0; i < ids.length; ++i)
			dataOut.writeInt(ids[i]);
	}
	
	public int[] getIds()
	{
		return ids;
	}
	
	int[] ids;
}
