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

package org.miradi.project;

import org.martus.util.UnicodeStringReader;
import org.miradi.ids.BaseId;

public class ProjectLoader
{
	public static void loadProject(final UnicodeStringReader reader, Project project) throws Exception
	{
		while(true)
		{
			String line = reader.readLine();
			if(line == null)
				break;
			
			if(line.length() == 0)
				continue;
			
			
			if (line.startsWith(ProjectSaver.UPDATE_PROJECT_INFO_CODE))
				readProjectInfoLine(project, line);
			
			if (line.startsWith(ProjectSaver.UPDATE_PROJECT_VERSION_CODE))
				readProjectVersionLine(project, line);
			
			if (line.startsWith(ProjectSaver.UPDATE_LAST_MODIFIED_TIME_CODE))
				readLastModified(project, line);
			
			if (line.startsWith(ProjectSaver.UPDATE_SIMPLE_THREAT_RATING))
				readSimpleThreatRatingLine(project, line);
			
			if (line.startsWith(ProjectSaver.CREATE_OBJECT_CODE))
				readCreateObjectLine(project, line);
		}
	}
	
	private static void readProjectInfoLine(final Project project, final String line)
	{
		String[] splitLine = line.split(ProjectSaver.TAB);
		String tag = splitLine[1];
		String value = splitLine[2];
		if (tag.equals(ProjectInfo.TAG_PROJECT_METADATA_ID))
			project.getProjectInfo().setMetadataId(new BaseId(value));
	}

	private static void readLastModified(Project project, String line)
	{
	}

	private static void readProjectVersionLine(Project project, String line)
	{
	}
	
	private static void readSimpleThreatRatingLine(Project project, String line)
	{
	}

	private static void readCreateObjectLine(Project project, String line)
	{
	}
}
