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

import org.martus.util.UnicodeWriter;
import org.miradi.database.ProjectServer;

public class ProjectSaver
{
	public static void saveProject(final Project project, UnicodeWriter writer) throws Exception
	{
		writeTagValue(writer, UPDATE_PROJECT_INFO_CODE, ProjectInfo.TAG_HIGHEST_OBJECT_ID, Integer.toString(project.getProjectInfo().getNormalIdAssigner().getHighestAssignedId()));
		writeTagValue(writer, UPDATE_PROJECT_INFO_CODE, ProjectInfo.TAG_PROJECT_METADATA_ID, project.getProjectInfo().getMetadataId().toString());
		writeTagValue(writer, UPDATE_PROJECT_VERSION_CODE, ProjectServer.TAG_VERSION, Integer.toString(ProjectServer.DATA_VERSION));
		writeSimpleThreatRating(writer, project);
	}
	
	private static void writeSimpleThreatRating(UnicodeWriter writer, Project project) throws Exception
	{
//FIXME uncomment and make it work		
//		Collection<ThreatRatingBundle> allBundles = SimpleThreatRatingFramework.loadSimpleThreatRatingBundles(project.getDatabase());
//		for(ThreatRatingBundle bundle : allBundles)
//		{
//			String contents = bundle.toJson().toString();
//			String bundleName = SimpleThreatRatingFramework.getBundleKey(bundle.getThreatId(), bundle.getTargetId());
//			System.out.println("contents= "+ bundleName);
//			System.out.println("contents= "+ contents);
//		}
	}

	private static void writeTagValue(final UnicodeWriter writer, final String actionCode, final String tag, final String value) throws Exception
	{
		writer.write(actionCode);
		writer.write(TAB);
		writer.write(tag);
		writer.write(EQUALS);
		writer.write(value);
		writer.writeln();
	}
	
	private static final String TAB = "	";
	private static final String EQUALS = "=";
	private static final String UPDATE_PROJECT_INFO_CODE = "UP";
	private static final String UPDATE_PROJECT_VERSION_CODE = "UV";
}
