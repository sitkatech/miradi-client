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
import java.io.IOException;
import java.text.ParseException;

import org.martus.util.DirectoryUtils;
import org.martus.util.TestCaseEnhanced;
import org.martus.util.DirectoryLock.AlreadyLockedException;
import org.miradi.database.HybridFileBasedProjectServer;
import org.miradi.objects.BaseObject;
import org.miradi.project.threatrating.SimpleThreatRatingFramework;

public class ProjectServerForTesting extends HybridFileBasedProjectServer
{
	public ProjectServerForTesting() throws IOException
	{
		super();
	}

	public void openMemoryDatabase(String nameToUse) throws IOException, AlreadyLockedException
	{
		final String tempFileName = "$$$" + TestCaseEnhanced.getCallingTestClass();
		eamDir = File.createTempFile(tempFileName, null);
		eamDir.delete();
		eamDir.mkdir();
		
		File projectDir = new File(eamDir, nameToUse);

		openNonDatabaseStore(projectDir);
	}
	
	public void writeObject(BaseObject object) throws IOException, ParseException
	{
		super.writeObject(object);
		++callsToWriteObject;
	}
	
	

	public void writeThreatRatingFramework(SimpleThreatRatingFramework framework) throws IOException
	{
		super.writeThreatRatingFramework(framework);
		++callsToWriteThreatRatingFramework;
	}
	
	public void closeAndDontDelete() throws IOException
	{
		super.close();
	}
	
	public void close() throws IOException
	{
		closeAndDontDelete();
		if(eamDir != null)
			DirectoryUtils.deleteEntireDirectoryTree(eamDir);
		eamDir = null;
	}

	File eamDir;
	public int callsToWriteObject;
	public int callsToWriteThreatRatingFramework;
}
