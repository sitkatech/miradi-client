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

import java.util.HashMap;
import java.util.StringTokenizer;

import org.martus.util.UnicodeStringReader;
import org.miradi.ids.BaseId;
import org.miradi.ids.FactorId;
import org.miradi.objecthelpers.ORef;
import org.miradi.project.threatrating.ThreatRatingBundle;

public class ProjectLoader
{
	private ProjectLoader(final UnicodeStringReader readerToUse, Project projectToUse) throws Exception
	{
		reader = readerToUse;
		project = projectToUse;
	}
	
	public static void loadProjectNew(final UnicodeStringReader reader, Project project) throws Exception
	{
		new ProjectLoader(reader, project).load();
	}

	private void load()
	{
		
	}

	public static void loadProject(final UnicodeStringReader reader, Project project) throws Exception
	{
		while(true)
		{
			String line = reader.readLine();
			if(line == null)
				break;
			
			if(line.length() == 0)
				continue;
			

			if (line.startsWith(ProjectSaver.UPDATE_PROJECT_VERSION_CODE))
				readProjectVersionLine(project, line);

			if (line.startsWith(ProjectSaver.UPDATE_PROJECT_INFO_CODE))
				readProjectInfoLine(project, line);
			
			if (line.startsWith(ProjectSaver.UPDATE_LAST_MODIFIED_TIME_CODE))
				readLastModified(project, line);
			
			if (line.startsWith(ProjectSaver.CREATE_OBJECT_CODE))
				readCreateObjectLine(project, line);
			
			if (line.startsWith(ProjectSaver.UPDATE_OBJECT_CODE))
				readUpdateObjectline(project, line);
			
			if (line.startsWith(ProjectSaver.CREATE_SIMPLE_THREAT_RATING_BUNDLE_CODE))
				readCreateSimpleThreatRatingLine(project, line);
			
			if (line.startsWith(ProjectSaver.UPDATE_SIMPLE_THREAT_RATING_BUNDLE_CODE))
				readUpdateSimpleThreatRatingLine(project, line);
		}
	}
	
	private static void readProjectVersionLine(Project project, String line)
	{
	}
	
	private static void readProjectInfoLine(final Project project, final String line)
	{
		String[] splitLine = line.split(ProjectSaver.TAB);
		String[] tagValue = splitLine[1].split(ProjectSaver.EQUALS);
		String tag = tagValue[0];
		String value = tagValue[1];
		if (tag.equals(ProjectInfo.TAG_PROJECT_METADATA_ID))
			project.getProjectInfo().setMetadataId(new BaseId(value));
	}

	private static void readLastModified(Project project, String line)
	{
	}
	
	private static void readCreateSimpleThreatRatingLine(Project project, String line)
	{
		StringTokenizer tokenizer = new StringTokenizer(line);
		/*String command =*/ tokenizer.nextToken();
		String threatIdTargetIdString = tokenizer.nextToken();
		String[] threatIdTargetIdParts = threatIdTargetIdString.split("-");
		FactorId threatId = new FactorId(Integer.parseInt(threatIdTargetIdParts[0]));
		FactorId targetId = new FactorId(Integer.parseInt(threatIdTargetIdParts[1]));
		ThreatRatingBundle bundle = new ThreatRatingBundle(threatId, targetId, BaseId.INVALID);
		bundleNameToBundleMap.put(threatIdTargetIdString, bundle);
	}

	private static void readUpdateSimpleThreatRatingLine(Project project, String line)
	{
		StringTokenizer tokenizer = new StringTokenizer(line);
		/*String command =*/ tokenizer.nextToken();
		String threatIdTargetIdString = tokenizer.nextToken();
		bundleNameToBundleMap.get(threatIdTargetIdString);
		//FIXME get and update bundle.  
	}

	private static void readCreateObjectLine(Project project, String line) throws Exception
	{
		StringTokenizer tokenizer = new StringTokenizer(line);
		/*String command =*/ tokenizer.nextToken();
		String refString = tokenizer.nextToken();
		String[] refParts = refString.split(":");
		int objectType = Integer.parseInt(refParts[0]);
		BaseId objectId = new BaseId(Integer.parseInt(refParts[1]));
		project.createObject(objectType, objectId);
	}

	private static void readUpdateObjectline(Project project, String line) throws Exception
	{
		StringTokenizer tokenizer = new StringTokenizer(line);
		/*String command =*/ tokenizer.nextToken();
		String refString = tokenizer.nextToken();
		String[] refParts = refString.split(":");
		int objectType = Integer.parseInt(refParts[0]);
		BaseId objectId = new BaseId(Integer.parseInt(refParts[1]));
		ORef ref = new ORef(objectType, objectId);
		String tag = tokenizer.nextToken(" \t=");
		String value = tokenizer.nextToken("=\n");
		value = value.replaceAll("<br/>", "\n");
		value = value.replaceAll("&lt;", "<");
		value = value.replaceAll("&gt;", ">");
		value = value.replaceAll("&quot;", "\"");
		value = value.replaceAll("&#39;", "'");
		value = value.replaceAll("&amp;", "&");
		project.setObjectData(ref, tag, value);
	}

	private static HashMap<String, ThreatRatingBundle> bundleNameToBundleMap;
	private UnicodeStringReader reader;
	private Project project;
}
