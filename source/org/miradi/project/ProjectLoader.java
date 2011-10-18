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
import org.miradi.project.threatrating.RatingValueSet;
import org.miradi.project.threatrating.ThreatRatingBundle;

public class ProjectLoader
{
	private ProjectLoader(final UnicodeStringReader readerToUse, Project projectToUse) throws Exception
	{
		reader = readerToUse;
		project = projectToUse;
		
		bundleNameToBundleMap = new HashMap<String, ThreatRatingBundle>();
	}
	
	public static void loadProject(final UnicodeStringReader reader, Project project) throws Exception
	{
		final ProjectLoader projectLoader = new ProjectLoader(reader, project);
		projectLoader.load();
	}

	private void load() throws Exception
	{
		while(true)
		{
			String line = reader.readLine();
			if(line == null)
				break;
			
			if(line.length() == 0)
				continue;
			

			if (line.startsWith(ProjectSaver.UPDATE_PROJECT_VERSION_CODE))
				loadProjectVersionLine(line);

			if (line.startsWith(ProjectSaver.UPDATE_PROJECT_INFO_CODE))
				loadProjectInfoLine(line);
			
			if (line.startsWith(ProjectSaver.UPDATE_LAST_MODIFIED_TIME_CODE))
				loadLastModified(line);
			
			if (line.startsWith(ProjectSaver.CREATE_OBJECT_CODE))
				loadCreateObjectLine(line);
			
			if (line.startsWith(ProjectSaver.UPDATE_OBJECT_CODE))
				loadUpdateObjectline(line);
			
			if (line.startsWith(ProjectSaver.CREATE_SIMPLE_THREAT_RATING_BUNDLE_CODE))
				loadCreateSimpleThreatRatingLine(line);
			
			if (line.startsWith(ProjectSaver.UPDATE_SIMPLE_THREAT_RATING_BUNDLE_CODE))
				loadUpdateSimpleThreatRatingLine(line);
		}
	}
	
	private void loadProjectVersionLine(String line)
	{
	}
	
	private void loadProjectInfoLine(final String line)
	{
		String[] splitLine = line.split(ProjectSaver.TAB);
		String[] tagValue = splitLine[1].split(ProjectSaver.EQUALS);
		String tag = tagValue[0];
		String value = tagValue[1];
		if (tag.equals(ProjectInfo.TAG_PROJECT_METADATA_ID))
			getProject().getProjectInfo().setMetadataId(new BaseId(value));
	}

	private void loadLastModified(String line)
	{
	}
	
	private void loadCreateSimpleThreatRatingLine(String line) throws Exception
	{
		StringTokenizer tokenizer = new StringTokenizer(line);
		/*String command =*/ tokenizer.nextToken();
		String threatIdTargetIdString = tokenizer.nextToken();
		String[] threatIdTargetIdParts = threatIdTargetIdString.split("-");
		FactorId threatId = new FactorId(Integer.parseInt(threatIdTargetIdParts[0]));
		FactorId targetId = new FactorId(Integer.parseInt(threatIdTargetIdParts[1]));
		ThreatRatingBundle bundle = new ThreatRatingBundle(threatId, targetId, BaseId.INVALID);
		bundleNameToBundleMap.put(threatIdTargetIdString, bundle);
		getProject().getSimpleThreatRatingFramework().saveBundle(bundle);
	}

	private void loadUpdateSimpleThreatRatingLine(String line) throws Exception
	{
		StringTokenizer tokenizer = new StringTokenizer(line);
		/*String command =*/ tokenizer.nextToken();
		String threatIdTargetIdString = tokenizer.nextToken();
		ThreatRatingBundle bundleToUpdate = bundleNameToBundleMap.get(threatIdTargetIdString);
		String tag = tokenizer.nextToken(EQUALS_DELIMITER_TAB_PREFIXED);
		String value = tokenizer.nextToken(EQUALS_DELIMITER_NEWLINE_POSTFIXED);
		if (tag.equals(ThreatRatingBundle.TAG_VALUES))
		{
			RatingValueSet ratings = new RatingValueSet();
			ratings.fillFrom(value);
			bundleToUpdate.setRating(ratings);
		}
		if (tag.equals(ThreatRatingBundle.TAG_DEFAULT_VALUE_ID))
		{
			bundleToUpdate.setDefaultValueId(new BaseId(Integer.parseInt(value)));
		}
	}

	private void loadCreateObjectLine(String line) throws Exception
	{
		StringTokenizer tokenizer = new StringTokenizer(line);
		/*String command =*/ tokenizer.nextToken();
		String refString = tokenizer.nextToken();
		ORef ref = extractRef(refString);
		getProject().createObject(ref);
	}

	private void loadUpdateObjectline(String line) throws Exception
	{
		StringTokenizer tokenizer = new StringTokenizer(line);
		/*String command =*/ tokenizer.nextToken();
		String refString = tokenizer.nextToken();
		ORef ref = extractRef(refString);
		String tag = tokenizer.nextToken(EQUALS_DELIMITER_TAB_PREFIXED);
		String value = tokenizer.nextToken(EQUALS_DELIMITER_NEWLINE_POSTFIXED);
		value = value.replaceAll("<br/>", "\n");
		value = value.replaceAll("&lt;", "<");
		value = value.replaceAll("&gt;", ">");
		value = value.replaceAll("&quot;", "\"");
		value = value.replaceAll("&#39;", "'");
		value = value.replaceAll("&amp;", "&");
		getProject().setObjectData(ref, tag, value);
	}

	public ORef extractRef(String refString)
	{
		String[] refParts = refString.split(":");
		int objectType = Integer.parseInt(refParts[0]);
		BaseId objectId = new BaseId(Integer.parseInt(refParts[1]));
		
		return new ORef(objectType, objectId);
	}

	private Project getProject()
	{
		return project;
	}

	private HashMap<String, ThreatRatingBundle> bundleNameToBundleMap;
	private UnicodeStringReader reader;
	private Project project;
	
	private static final String EQUALS_DELIMITER_TAB_PREFIXED = " \t=";
	private static final String EQUALS_DELIMITER_NEWLINE_POSTFIXED = "=\n";
}
