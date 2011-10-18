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
import java.util.Collection;
import java.util.Collections;
import java.util.Vector;

import org.martus.util.UnicodeStringWriter;
import org.martus.util.xml.XmlUtilities;
import org.miradi.database.ProjectServer;
import org.miradi.objectdata.ObjectData;
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
	private ProjectSaver(final Project projectToUse, final UnicodeStringWriter writerToUse) throws Exception
	{
		project = projectToUse;
		writer = writerToUse;
	}
	
	public static void saveProject(final Project projectToUse, final UnicodeStringWriter writerToUse) throws Exception
	{
		final ProjectSaver projectSaver = new ProjectSaver(projectToUse, writerToUse);
		projectSaver.saveProject();
	}
	
	private void saveProject() throws Exception
	{
		writeProjectVersion();
		writeSchemaVersion();
		writeProjectInfo();
		writeLastModified();
		writeAllObjectTypes();
		writeAllSimpleThreatRatings();
		writeAllQuarantinedData();
		writeStopMarker();
		flushWriter();
	}

	public void flushWriter() throws IOException
	{
		getWriter().flush();
	}

	public void writeStopMarker() throws IOException
	{
		writelnRaw(STOP_MARKER);
	}

	private void writeLastModified() throws Exception
	{
		writeTagValue(UPDATE_LAST_MODIFIED_TIME_CODE, LAST_MODIFIED_TAG, ProjectServer.timestampToString(getProject().getLastModifiedTime()));
	}

	private void writeProjectInfo() throws Exception
	{
		writeTagValue(UPDATE_PROJECT_INFO_CODE, ProjectInfo.TAG_HIGHEST_OBJECT_ID, Integer.toString(getProject().getProjectInfo().getNormalIdAssigner().getHighestAssignedId()));
		writeTagValue(UPDATE_PROJECT_INFO_CODE, ProjectInfo.TAG_PROJECT_METADATA_ID, getProject().getProjectInfo().getMetadataId().toString());
	}

	private void writeSchemaVersion() throws Exception
	{
		writeTagValue(UPDATE_PROJECT_VERSION_CODE, ProjectServer.TAG_VERSION, Integer.toString(ProjectServer.DATA_VERSION));
	}

	private void writeProjectVersion() throws Exception
	{
		writeTagValue(UPDATE_PROJECT_VERSION_CODE, ProjectServer.TAG_VERSION, Integer.toString(ProjectServer.DATA_VERSION));
	}
	
	private void writeAllQuarantinedData() throws Exception
	{
		String quarantineFileContents = getProject().getQuarantineFileContents();
		quarantineFileContents = xmlNewLineEncode(quarantineFileContents);
		write(quarantineFileContents);
	}

	public String xmlNewLineEncode(String data)
	{
		data = XmlUtilities.getXmlEncoded(data);
		data = data.replaceAll("\\n", "<br/>");
		
		return data;
	}

	private void writeAllObjectTypes() throws Exception
	{
		for (int type = ObjectType.FIRST_OBJECT_TYPE; type < ObjectType.OBJECT_TYPE_COUNT; ++type)
		{
			EAMObjectPool pool = getProject().getPool(type);
			if (pool != null)
			{
				ORefList sortedObjectRefs = pool.getSortedRefList();
				writeObjects(sortedObjectRefs);
			}
		}
	}

	private void writeObjects(ORefList sortedObjectRefs) throws Exception
	{
		for (int index = 0; index < sortedObjectRefs.size(); ++index)
		{
			final ORef ref = sortedObjectRefs.get(index);
			writeObject(ref);
		}
	}

	private void writeObject(ORef ref) throws Exception
	{
		BaseObject baseObject = getProject().findObject(ref);
		writeValue(CREATE_OBJECT_CODE, createSimpleRefString(ref));
		Vector<String> fieldTags = baseObject.getStoredFieldTags();
		for(int field = 0; field < fieldTags.size(); ++field)
		{
			String tag = fieldTags.get(field);
			String data = baseObject.getData(tag);
			if (data.length() > 0)
			{
				ObjectData dataField = baseObject.getField(tag);
				if(needsEncoding(dataField))
				{
					data = xmlNewLineEncode(data);
				}
				writeTagValue(UPDATE_OBJECT_CODE, ref, tag, data);
			}
		}
	}

	private boolean needsEncoding(ObjectData dataField)
	{
		return dataField.isUserText();
	}

	private void writeAllSimpleThreatRatings() throws Exception
	{
		Collection<ThreatRatingBundle> allBundles = getProject().getSimpleThreatRatingFramework().getAllBundles();
		Vector<ThreatRatingBundle> sortedBundles = new Vector<ThreatRatingBundle>(allBundles);
		Collections.sort(sortedBundles, new ThreatRatingBundleSorter());
		for(ThreatRatingBundle bundle : sortedBundles)
		{
			EnhancedJsonObject json = bundle.toJson();
			String bundleName = SimpleThreatRatingFramework.getBundleKey(bundle.getThreatId(), bundle.getTargetId());
			writeValue(CREATE_SIMPLE_THREAT_RATING_BUNDLE_CODE, bundleName);
			writeTagValue(UPDATE_SIMPLE_THREAT_RATING_BUNDLE_CODE, bundleName, ThreatRatingBundle.TAG_VALUES, json.getString(ThreatRatingBundle.TAG_VALUES));
			writeTagValue(UPDATE_SIMPLE_THREAT_RATING_BUNDLE_CODE, bundleName, ThreatRatingBundle.TAG_DEFAULT_VALUE_ID, json.getString(ThreatRatingBundle.TAG_DEFAULT_VALUE_ID));
		}
	}

	private void writeValue(final String actionCode, final String value) throws Exception
	{
		write(actionCode);
		write(TAB);
		write(value);
		getWriter().writeln();
	}
	
	private void writeTagValue(final String actionCode, final String tag, final String value) throws Exception
	{
		write(actionCode);
		write(TAB);
		write(tag);
		write(EQUALS);
		write(value);
		getWriter().writeln();
	}

	private void writeTagValue(final String actionCode, ORef ref, final String tag, final String value) throws Exception
	{
		writeTagValue(actionCode, createSimpleRefString(ref), tag, value);
	}

	public void writeTagValue(final String actionCode, final String lineKey, final String tag, final String value) throws Exception, IOException
	{
		write(actionCode);
		write(TAB);
		
		write(lineKey);
		write(TAB);
		
		write(tag);
		write(EQUALS);
		write(value);
		
		getWriter().writeln();
	}
	
	private void write(final String data) throws Exception
	{
		writeRaw(data);
	}
	
	public void writeRaw(String data) throws IOException
	{
		getWriter().write(data);
	}
	
	public void writelnRaw(String data) throws IOException
	{
		getWriter().writeln(data);
	}
	
	private String createSimpleRefString(final ORef ref)
	{
		return Integer.toString(ref.getObjectType()) +  ":" + ref.getObjectId().toString();
	}
	
	private Project getProject()
	{
		return project;
	}
	
	private UnicodeStringWriter getWriter()
	{
		return writer;
	}
	
	private Project project;
	private UnicodeStringWriter writer;

	public static final String NEW_LINE = "\n";
	public static final String HTML_NEW_LINE = "<br/>";
	public static final String TAB = "\t";
	public static final String EQUALS = "=";
	public static final String STOP_MARKER = "--";
	public static final String UPDATE_PROJECT_INFO_CODE = "UP";
	public static final String UPDATE_PROJECT_VERSION_CODE = "UV";
	public static final String UPDATE_LAST_MODIFIED_TIME_CODE = "UL";
	public static final String CREATE_SIMPLE_THREAT_RATING_BUNDLE_CODE = "CT";
	public static final String UPDATE_SIMPLE_THREAT_RATING_BUNDLE_CODE = "UT";
	public static final String CREATE_OBJECT_CODE = "CO";
	public static final String UPDATE_OBJECT_CODE = "UO";
	
	public static final String LAST_MODIFIED_TAG = "LastModified";
}
