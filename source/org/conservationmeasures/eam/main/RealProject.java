/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.main;

import java.io.File;
import java.io.IOException;
import java.util.Vector;

import org.conservationmeasures.eam.diagram.nodes.Linkage;
import org.conservationmeasures.eam.diagram.nodes.Node;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.exceptions.UnknownCommandException;

public class RealProject extends Project
{
	public RealProject() throws IOException
	{
		super(new FileStorage());
	}
	
	public boolean isOpen()
	{
		return getStorage().doesProjectExist();
	}
	
	public void load(File projectDirectory) throws IOException, CommandFailedException, UnknownCommandException
	{
		getStorage().setDirectory(projectDirectory);
		if(!isOpen())
			createEmpty();
		
		Vector commands = getStorage().load();
		loadCommands(commands);
	}

	private void createEmpty() throws IOException
	{
		getStorage().createEmpty();
	}

	public void closeDatabase()
	{
		try
		{
			getStorage().close();
		}
		catch (IOException e)
		{
			EAM.logException(e);
		}
	}

	public String getName()
	{
		if(isOpen())
			return getStorage().getName();
		return EAM.text("[No Project]");
	}

	public void deleteNodeFromDatabase(int id)
	{
	}
	
	public void insertNodeInDatabase(Node node)
	{
	}
	
	public void deleteLinkageFromDatabase(int id)
	{
		
	}
	
	public void insertLinkageInDatabase(Linkage linkage)
	{
		
	}

	private FileStorage getStorage()
	{
		return (FileStorage)storage;
	}
}
