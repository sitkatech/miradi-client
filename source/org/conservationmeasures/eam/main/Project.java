/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.main;

import java.io.File;

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
	
	public void load(File projectFile)
	{
		file = projectFile;
	}
	
	public String getName()
	{
		if(file == null)
			return EAM.text("[No Project]");
		return file.getName();
	}

	public void recordCommand(Command command)
	{
	}
	
	File file;
	DiagramModel diagramModel;
}
