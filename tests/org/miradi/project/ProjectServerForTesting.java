/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
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
package org.miradi.project;

import java.io.File;

import org.martus.util.DirectoryUtils;
import org.miradi.database.ProjectServer;
import org.miradi.objects.BaseObject;
import org.miradi.project.threatrating.SimpleThreatRatingFramework;

public class ProjectServerForTesting extends ProjectServer
{
	public ProjectServerForTesting() throws Exception
	{
		super();
	}
	
	@Override
	public void createProject(String projectName) throws Exception
	{
		super.createProject(projectName);
	}
	
	public void closeAndDontDelete() throws Exception
	{
		super.close();
	}
	
	public void close() throws Exception
	{
		String dataLocation = getDataLocation();
		String projectName = getCurrentProjectName();
		File projectDirectory = new File(dataLocation, projectName);
		closeAndDontDelete();
		DirectoryUtils.deleteEntireDirectoryTree(projectDirectory);
	}

	public void writeObject(BaseObject object) throws Exception
	{
		super.writeObject(object);
		++callsToWriteObject;
	}
	
	

	public void writeThreatRatingFramework(SimpleThreatRatingFramework framework) throws Exception
	{
		super.writeThreatRatingFramework(framework);
		++callsToWriteThreatRatingFramework;
	}
	
	public int callsToWriteObject;
	public int callsToWriteThreatRatingFramework;
}
