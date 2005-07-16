/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.main;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.commands.CommandFailedException;
import org.conservationmeasures.eam.diagram.DiagramModel;

public class Project
{
	public Project()
	{
		diagramModel = new DiagramModel();
	}
	
	public DiagramModel getDiagramModel()
	{
		return diagramModel;
	}
	
	public void load(MainWindow mainWindow, File projectFile) throws IOException, CommandFailedException
	{
		getDiagramModel().clear();
		
		file = projectFile;
		if(!file.exists())
		{
			FileOutputStream out = new FileOutputStream(file);
			out.close();
		}
		
		FileInputStream in = new FileInputStream(file);
		try
		{
			load(in);
		}
		finally
		{
			in.close();
		}
		
	}

	private void load(FileInputStream in) throws IOException, CommandFailedException
	{
		DataInputStream dataIn = new DataInputStream(in);
		try
		{
			load(dataIn);
		}
		finally
		{
			dataIn.close();
		}
	}

	private void load(DataInputStream dataIn) throws IOException, CommandFailedException
	{
		EAM.logDebug("---Loading---");
		while(true)
		{
			if(dataIn.read() < 0)
				break;
			Command command = Command.readFrom(dataIn);
			EAM.logDebug(command.toString());
			command.execute(this);
		}
		EAM.logDebug("---Finished---");
	}
	
	public String getName()
	{
		if(file == null)
			return EAM.text("[No Project]");
		return file.getName();
	}

	public Object executeCommand(Command command) throws CommandFailedException
	{
		Object result = command.execute(this);
		recordCommand(command);
		return result;
	}
	
	public void recordCommand(Command command)
	{
		try
		{
			appendCommandToProjectFile(command);
		}
		catch (IOException e)
		{
			EAM.logException(e);
		}
	}
	
	private void appendCommandToProjectFile(Command command) throws IOException
	{
		FileOutputStream out = new FileOutputStream(file, true);
		try
		{
			appendCommandToStorage(out, command);
		}
		finally
		{
			out.close();
		}
		
	}

	private void appendCommandToStorage(FileOutputStream out, Command command) throws IOException
	{
		DataOutputStream dataOut = new DataOutputStream(out);
		try
		{
			appendCommandToStorage(dataOut, command);
		}
		finally
		{
			dataOut.close();
		}
	}

	private void appendCommandToStorage(DataOutputStream dataOut, Command command) throws IOException
	{
		dataOut.write(0);
		command.writeTo(dataOut);
		EAM.logDebug("wrote: " + command.toString());
	}
	
	
	File file;
	DiagramModel diagramModel;
}
