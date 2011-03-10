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

import java.io.IOException;

import org.miradi.database.ProjectServer;
import org.miradi.ids.BaseId;
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
	
	public void setFailAllDeletes(boolean shouldFailAllDeletes)
	{
		failDeletes = shouldFailAllDeletes;
	}
	
	@Override
	public void close() throws Exception
	{
		String projectName = getCurrentProjectName();
		closeAndDontDelete();
		deleteProject(projectName);
	}

	@Override
	public void writeObject(BaseObject object) throws Exception
	{
		super.writeObject(object);
		++callsToWriteObject;
	}

	@Override
	public void writeThreatRatingFramework(SimpleThreatRatingFramework framework) throws Exception
	{
		super.writeThreatRatingFramework(framework);
		++callsToWriteThreatRatingFramework;
	}
	
	@Override
	public void deleteObject(int type, BaseId id) throws Exception
	{
		super.deleteObject(type, id);
		if(failDeletes)
			throw new IOException("TestDatabase set to fail all deletes");
	}
	
	public int callsToWriteObject;
	public int callsToWriteThreatRatingFramework;
	private boolean failDeletes;
}
