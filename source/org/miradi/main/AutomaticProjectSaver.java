/* 
Copyright 2005-2011, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
*/ 

package org.miradi.main;

import java.io.File;

import org.martus.util.UnicodeStringWriter;
import org.martus.util.UnicodeWriter;
import org.miradi.project.Project;
import org.miradi.project.ProjectSaver;

public class AutomaticProjectSaver implements CommandExecutedListener
{
	public AutomaticProjectSaver(Project projectToTrack)
	{
		project = projectToTrack;
		project.addCommandExecutedListener(this);
	}
	
	public void startSaving(File projectFileToUse)
	{
		projectFile = projectFileToUse;
	}
	
	public void stopSaving()
	{
		projectFile = null;
	}
	
	public void commandExecuted(CommandExecutedEvent event)
	{
		if(projectFile == null)
			return;
		
		if(project.isInTransaction())
			return;
		
		try
		{
			long startedAt = System.currentTimeMillis();
			UnicodeStringWriter stringWriter = UnicodeStringWriter.create();
			ProjectSaver.saveProject(project, stringWriter);
			
			// TODO: Need safe writing here
			UnicodeWriter fileWriter = new UnicodeWriter(projectFile);
			fileWriter.write(stringWriter.toString());
			fileWriter.close();
			long endedAt = System.currentTimeMillis();
			EAM.logDebug("Saved project: " + (endedAt - startedAt) + "ms");
		}
		catch(Exception e)
		{
			throw new RuntimeException(e);
		}
	}
	
	private File projectFile;
	private Project project;
}
