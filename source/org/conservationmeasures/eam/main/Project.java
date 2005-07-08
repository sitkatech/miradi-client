/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.conservationmeasures.eam.commands.Command;
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
	
	public void load(MainWindow mainWindow, File projectFile) throws IOException
	{
		file = projectFile;
		if(!file.exists())
		{
			FileOutputStream out = new FileOutputStream(file);
			out.close();
		}
		
		FileInputStream in = new FileInputStream(file);
		try
		{
			while(true)
			{
				if(in.read() < 0)
					break;
				Command command = Command.readFrom(in);
				command.execute(mainWindow);
			}
		}
		finally
		{
			in.close();
		}
	}
	
	public String getName()
	{
		if(file == null)
			return EAM.text("[No Project]");
		return file.getName();
	}

	public void executeCommand(MainWindow mainWindow, Command command)
	{
		command.execute(mainWindow);
		recordCommand(command);
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
	
	public void replayCommand(Command command)
	{
	}
	
	private void appendCommandToProjectFile(Command command) throws IOException
	{
		FileOutputStream out = new FileOutputStream(file, true);
		try
		{
			out.write(0);
			command.writeTo(out);
		}
		finally
		{
			out.close();
		}
		
	}
	
	File file;
	DiagramModel diagramModel;
}
