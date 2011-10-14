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

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Vector;

import org.martus.util.UnicodeWriter;
import org.martus.util.xml.XmlUtilities;
import org.miradi.database.ProjectServer;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objecthelpers.ThreatRatingBundleSorter;
import org.miradi.objectpools.EAMObjectPool;
import org.miradi.objects.BaseObject;
import org.miradi.project.threatrating.SimpleThreatRatingFramework;
import org.miradi.project.threatrating.ThreatRatingBundle;
import org.miradi.utils.EnhancedJsonObject;

public class ProjectSaver
{
	public static void saveProject(final Project project, UnicodeWriter writer) throws Exception
	{
		writeTagValue(writer, UPDATE_PROJECT_VERSION_CODE, ProjectServer.TAG_VERSION, Integer.toString(ProjectServer.DATA_VERSION));
		writeTagValue(writer, UPDATE_PROJECT_VERSION_CODE, ProjectServer.TAG_VERSION, Integer.toString(ProjectServer.DATA_VERSION));
		writeTagValue(writer, UPDATE_PROJECT_INFO_CODE, ProjectInfo.TAG_HIGHEST_OBJECT_ID, Integer.toString(project.getProjectInfo().getNormalIdAssigner().getHighestAssignedId()));
		writeTagValue(writer, UPDATE_PROJECT_INFO_CODE, ProjectInfo.TAG_PROJECT_METADATA_ID, project.getProjectInfo().getMetadataId().toString());
		writeTagValue(writer, UPDATE_LAST_MODIFIED_TIME_CODE, LAST_MODIFIED_TAG, project.getDatabase().getLastModifiedTime(null));
		writeAllObjectTypes(writer, project);
		writeSimpleThreatRating(writer, project);
		writeQuarantinedData(writer, project);
		writelnRaw(writer, STOP_MARKER);
		
		writer.flush();
	}
	
	private static void writeQuarantinedData(UnicodeWriter writer, Project project) throws Exception
	{
		String quarantineFileContents = project.getQuarantineFileContents();
		write(writer, quarantineFileContents);
	}

	private static void writeAllObjectTypes(UnicodeWriter writer, Project project) throws Exception
	{
		for (int type = ObjectType.FIRST_OBJECT_TYPE; type < ObjectType.OBJECT_TYPE_COUNT; ++type)
		{
			EAMObjectPool pool = project.getPool(type);
			if (pool != null)
			{
				ORefList sortedObjectRefs = pool.getSortedRefList();
				writeObjects(writer, project, sortedObjectRefs);
			}
		}
	}

	private static void writeObjects(UnicodeWriter writer, Project project, ORefList sortedObjectRefs) throws Exception
	{
		for (int index = 0; index < sortedObjectRefs.size(); ++index)
		{
			final ORef ref = sortedObjectRefs.get(index);
			writeObject(writer, project, ref);
		}
	}

	private static void writeObject(UnicodeWriter writer, Project project, ORef ref) throws Exception
	{
		BaseObject baseObject = project.findObject(ref);
		writeValue(writer, CREATE_OBJECT_CODE, createSimpleRefString(ref));
		Vector<String> fieldTags = baseObject.getStoredFieldTags();
		for(int field = 0; field < fieldTags.size(); ++field)
		{
			String tag = fieldTags.get(field);
			final String data = baseObject.getData(tag);
			if (data.length() > 0)
			{
				writeTagValue(writer, UPDATE_OBJECT_CODE, ref, tag, data);
			}
		}
	}

	private static void writeSimpleThreatRating(UnicodeWriter writer, Project project) throws Exception
	{
		HashSet<ThreatRatingBundle> allBundles = SimpleThreatRatingFramework.loadSimpleThreatRatingBundles(project.getDatabase());
		Vector<ThreatRatingBundle> sortedBundles = new Vector<ThreatRatingBundle>(allBundles);
		Collections.sort(sortedBundles, new ThreatRatingBundleSorter());
		for(ThreatRatingBundle bundle : sortedBundles)
		{
			EnhancedJsonObject json = bundle.toJson();
			String bundleName = SimpleThreatRatingFramework.getBundleKey(bundle.getThreatId(), bundle.getTargetId());
			writeTagValue(writer, UPDATE_SIMPLE_THREAT_RATING, SIMPLE_THREAT_RATING_BUNDLE_NAME_TAG, bundleName);
			writeTagValue(writer, UPDATE_SIMPLE_THREAT_RATING, ThreatRatingBundle.TAG_VALUES, json.getString(ThreatRatingBundle.TAG_VALUES));
			writeTagValue(writer, UPDATE_SIMPLE_THREAT_RATING, ThreatRatingBundle.TAG_DEFAULT_VALUE_ID, json.getString(ThreatRatingBundle.TAG_DEFAULT_VALUE_ID));
			writeTagValue(writer, UPDATE_SIMPLE_THREAT_RATING, ThreatRatingBundle.TAG_THREAT_ID, json.getString(ThreatRatingBundle.TAG_THREAT_ID));
			writeTagValue(writer, UPDATE_SIMPLE_THREAT_RATING, ThreatRatingBundle.TAG_TARGET_ID, json.getString(ThreatRatingBundle.TAG_TARGET_ID));
		}
	}

	private static void writeValue(final UnicodeWriter writer, final String actionCode, final String value) throws Exception
	{
		write(writer, actionCode);
		write(writer, TAB);
		write(writer, value);
		writer.writeln();
	}
	
	private static void writeTagValue(final UnicodeWriter writer, final String actionCode, final String tag, final String value) throws Exception
	{
		write(writer, actionCode);
		write(writer, TAB);
		write(writer, tag);
		write(writer, EQUALS);
		write(writer, value);
		writer.writeln();
	}
	
	private static void writeTagValue(final UnicodeWriter writer, final String actionCode, ORef ref, final String tag, final String value) throws Exception
	{
		write(writer, actionCode);
		write(writer, TAB);
		
		write(writer, createSimpleRefString(ref));
		write(writer, TAB);
		
		write(writer, tag);
		write(writer, EQUALS);
		write(writer, value);
		
		writer.writeln();
	}
	
	private static void write(final UnicodeWriter writer, final String data) throws Exception
	{
		String xmlEncodedData = XmlUtilities.getXmlEncoded(data);
		String dataWithHtmlNewLines = xmlEncodedData.replaceAll(NEW_LINE, HTML_NEW_LINE);
		writeRaw(writer, dataWithHtmlNewLines);
	}

	public static void writeRaw(final UnicodeWriter writer,	String data) throws IOException
	{
		writer.write(data);
	}
	
	public static void writelnRaw(final UnicodeWriter writer,	String data) throws IOException
	{
		writer.writeln(data);
	}
	
	private static String createSimpleRefString(final ORef ref)
	{
		return Integer.toString(ref.getObjectType()) +  ":" + ref.getObjectId().toString();
	}

	public static final String NEW_LINE = "\n";
	public static final String HTML_NEW_LINE = "<br/>";
	public static final String TAB = "\t";
	public static final String EQUALS = "=";
	public static final String STOP_MARKER = "--";
	public static final String UPDATE_PROJECT_INFO_CODE = "UP";
	public static final String UPDATE_PROJECT_VERSION_CODE = "UV";
	public static final String UPDATE_LAST_MODIFIED_TIME_CODE = "UL";
	public static final String UPDATE_SIMPLE_THREAT_RATING = "UT";
	public static final String CREATE_OBJECT_CODE = "CO";
	public static final String UPDATE_OBJECT_CODE = "UO";
	
	public static final String SIMPLE_THREAT_RATING_BUNDLE_NAME_TAG = "BundleName";
	public static final String LAST_MODIFIED_TAG = "LastModified";
}
