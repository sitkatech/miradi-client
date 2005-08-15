/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.main;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Vector;

import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.commands.CommandDoNothing;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.views.NoProjectView;
import org.conservationmeasures.eam.views.diagram.DiagramView;

public class Project extends BaseProject
{
	public Project()
	{
		storage = new FileStorage();
	}
	
	public void load(MainWindow mainWindow, File projectFile) throws IOException, CommandFailedException
	{
		if(!projectFile.exists())
		{
			FileOutputStream out = new FileOutputStream(projectFile);
			out.close();
		}
		
		getDiagramModel().clear();
		getStorage().setFile(projectFile);
		Vector commands = FileStorage.load(projectFile);
		for(int i=0; i < commands.size(); ++i)
		{
			Command command = (Command)commands.get(i);
			EAM.logDebug("Executing " + command);
			replayCommand(command);
			getStorage().addCommandWithoutSaving(command);
		}
		
		if(currentView.length() == 0)
		{
			currentView = DiagramView.getViewName();
			fireSwitchToView(currentView);
		}
		
		fireCommandExecuted(new CommandDoNothing());
	}

	public boolean isOpen()
	{
		return getStorage().hasFile();
	}
	
	public void close()
	{
		getStorage().setFile(null);
		currentView = NoProjectView.getViewName();
		fireSwitchToView(currentView);
	}

	public String getName()
	{
		if(isOpen())
			return getStorage().getName();
		return EAM.text("[No Project]");
	}

	private FileStorage getStorage()
	{
		return (FileStorage)storage;
	}
}
