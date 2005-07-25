/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.main;

import java.io.File;
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
		storage = new FileStorage();
	}
	
	public DiagramModel getDiagramModel()
	{
		return diagramModel;
	}
	
	public void load(MainWindow mainWindow, File projectFile) throws IOException, CommandFailedException
	{
		if(!projectFile.exists())
		{
			FileOutputStream out = new FileOutputStream(projectFile);
			out.close();
		}
		
		getDiagramModel().clear();
		storage.load(projectFile);
		for(int i=0; i < storage.getCommandCount(); ++i)
		{
			storage.getCommand(i).execute(this);
		}
	}

	public String getName()
	{
		if(storage.hasFile())
			return storage.getName();
		return EAM.text("[No Project]");
	}

	public void executeCommand(Command command) throws CommandFailedException
	{
		command.execute(this);
		recordCommand(command);
	}
	
	public void recordCommand(Command command)
	{
		try
		{
			storage.appendCommand(command);
		}
		catch (IOException e)
		{
			EAM.logException(e);
		}
	}
	
	FileStorage storage;
	DiagramModel diagramModel;
}
