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
import org.conservationmeasures.eam.exceptions.CommandFailedException;

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
			command.execute(this);
			getStorage().addCommandWithoutSaving(command);
		}
	}

	public String getName()
	{
		if(getStorage().hasFile())
			return getStorage().getName();
		return EAM.text("[No Project]");
	}

	private FileStorage getStorage()
	{
		return (FileStorage)storage;
	}
}
