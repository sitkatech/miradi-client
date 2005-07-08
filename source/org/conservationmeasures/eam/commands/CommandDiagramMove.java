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

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;

public class CommandDiagramMove extends Command
{
	public CommandDiagramMove(int deltaX, int deltaY)
	{
		this.deltaX = deltaX;
		this.deltaY = deltaY;
	}
	
	public CommandDiagramMove(DataInputStream dataIn) throws IOException
	{
		deltaX = dataIn.readInt();
		deltaY = dataIn.readInt();
	}
	
	public static String getCommandName()
	{
		return "DiagramMove";
	}
	
	public String toString()
	{
		return getCommandName() + ":" + deltaX + "," + deltaY;
	}
	
	public void execute(MainWindow target)
	{
		EAM.logWarning("CommandDiagramMove.execute not implemented");
	}
	
	public void writeTo(OutputStream out) throws IOException
	{
		DataOutputStream dataOut = new DataOutputStream(out);
		dataOut.writeUTF(getCommandName());
		dataOut.writeInt(getDeltaX());
		dataOut.writeInt(getDeltaY());
		
	}
	
	public int getDeltaX()
	{
		return deltaX;
	}
	
	public int getDeltaY()
	{
		return deltaY;
	}

	int deltaX;
	int deltaY;
}
