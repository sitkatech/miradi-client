/* 
Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
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
import java.io.FileOutputStream;
import java.util.Calendar;

import org.martus.util.UnicodeStringReader;
import org.martus.util.UnicodeStringWriter;
import org.miradi.main.EAM;
import org.miradi.wizard.noproject.FileSystemTreeNode;

public class LastProjectModifiedTimeHelper
{
	public LastProjectModifiedTimeHelper(File projectDirToUse)
	{
		projectDir = projectDirToUse;
	}
	
	public static String readLastModifiedProjectTime(File projectDir)
	{
		try
		{
			File lastModifiedTimeFile = new File(projectDir, LAST_MODIFIED_FILE_NAME);
			if (lastModifiedTimeFile.exists())
				return UnicodeStringReader.getFileContents(lastModifiedTimeFile);
			
			long lastModifiedMillisFromOperatingSystem = projectDir.lastModified();
			return FileSystemTreeNode.timestampToString(lastModifiedMillisFromOperatingSystem);
		}
		catch (Exception e)
		{
			EAM.logException(e);
			return EAM.text("Unknown");
		}
	}
	
	public void attemptToWriteCurrentTime() throws Exception
	{
		UnicodeStringWriter writer = UnicodeStringWriter.create();
		try
		{
			String currentTime = FileSystemTreeNode.timestampToString(Calendar.getInstance().getTimeInMillis());
			writer.write(currentTime);
		}
		finally
		{
			writer.close();
		}
		
		byte[] bytes = writer.toString().getBytes("UTF-8");
		File projectLastModifiedTimeFile = getProjectLastModifiedTimeFile();
		FileOutputStream outputStream = new FileOutputStream(projectLastModifiedTimeFile);
		try
		{
			outputStream.write(bytes);
		}
		finally
		{
			outputStream.close();
		}
	}

	private File getProjectLastModifiedTimeFile()
	{
		return new File(projectDir, LAST_MODIFIED_FILE_NAME);
	}
	
	private static final String LAST_MODIFIED_FILE_NAME = "LastModifiedProjectTime.txt";
	
	private File projectDir;
}
