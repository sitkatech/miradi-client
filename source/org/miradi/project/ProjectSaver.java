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

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Vector;


import org.martus.util.UnicodeStringWriter;
import org.martus.util.UnicodeWriter;
import org.miradi.legacyprojects.LegacyProjectUtilities;
import org.miradi.objectdata.ObjectData;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objecthelpers.ThreatRatingBundleSorter;
import org.miradi.objectpools.EAMObjectPool;
import org.miradi.objects.BaseObject;
import org.miradi.project.threatrating.ThreatRatingBundle;
import org.miradi.utils.EnhancedJsonObject;

public class ProjectSaver extends AbstractMiradiProjectSaver
{
	protected ProjectSaver(final UnicodeStringWriter writerToUse) throws Exception
	{
		super(writerToUse);
	}
	
	private ProjectSaver(final Project projectToUse, final UnicodeStringWriter writerToUse) throws Exception
	{
		super(writerToUse);
		project = projectToUse;
	}
	
	public static void saveProject(Project projectToSave, File fileToSaveTo) throws Exception
	{
		UnicodeStringWriter stringWriter = UnicodeStringWriter.create();
		saveProject(projectToSave, stringWriter);
		UnicodeWriter fileWriter = new UnicodeWriter(fileToSaveTo);
		String contents = stringWriter.toString();
		fileWriter.write(contents);
		fileWriter.close();
	}

	public static void saveProject(final Project projectToUse, final UnicodeStringWriter writerToUse) throws Exception
	{
		final ProjectSaver projectSaver = new ProjectSaver(projectToUse, writerToUse);
		projectSaver.saveProject();
	}
	
	private void saveProject() throws Exception
	{
		writeFileHeader();
		writeSchemaVersion();
		writeProjectInfo();
		writeLastModified();
		writeAllObjectTypes();
		writeAllSimpleThreatRatings();
		writeAllQuarantinedData();
		writeExceptionsLog();
		writeStopMarker(getProject().getLastModifiedTime());
		flushWriter();
	}

	public void flushWriter() throws IOException
	{
		getWriter().flush();
	}

	private void writeLastModified() throws Exception
	{
		writeTagValue(UPDATE_LAST_MODIFIED_TIME_CODE, LAST_MODIFIED_TAG, LegacyProjectUtilities.timestampToString(getProject().getLastModifiedTime()));
	}

	private void writeProjectInfo() throws Exception
	{
		writeTagValue(UPDATE_PROJECT_INFO_CODE, ProjectInfo.TAG_HIGHEST_OBJECT_ID, Integer.toString(getProject().getProjectInfo().getNormalIdAssigner().getHighestAssignedId()));
		writeTagValue(UPDATE_PROJECT_INFO_CODE, ProjectInfo.TAG_PROJECT_METADATA_ID, getProject().getProjectInfo().getMetadataId().toString());
	}

	private void writeSchemaVersion() throws Exception
	{
		writeTagValue(UPDATE_PROJECT_VERSION_CODE, "Version", Integer.toString(LegacyProjectUtilities.DATA_VERSION));
	}

	private void writeAllQuarantinedData() throws Exception
	{
		String quarantineFileContents = getProject().getQuarantineFileContents();
		quarantineFileContents = xmlNewLineEncode(quarantineFileContents);
		writeTagValue(UPDATE_QUARANTINE_CODE, QUARANTINE_DATA_TAG, quarantineFileContents);
	}

	private void writeExceptionsLog() throws Exception
	{
		String exceptions = getProject().getExceptionLog();
		exceptions = xmlNewLineEncode(exceptions);
		exceptions = truncate(exceptions);
		writeTagValue(UPDATE_EXCEPTIONS_CODE, EXCEPTIONS_DATA_TAG, exceptions);
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
				writeRefTagValue(UPDATE_OBJECT_CODE, ref, tag, data);
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
			if(bundle.getThreatId().isInvalid())
				continue;
			if(bundle.getTargetId().isInvalid())
				continue;
			EnhancedJsonObject json = bundle.toJson();
			int threatId = bundle.getThreatId().asInt();
			int targetId = bundle.getTargetId().asInt();
			int defaultValueId = json.getInt(ThreatRatingBundle.TAG_DEFAULT_VALUE_ID);
			String ratings = json.getString(ThreatRatingBundle.TAG_VALUES);

			writeSimpleThreatRatingBundle(threatId, targetId, defaultValueId, ratings);
		}
	}

	public void writelnRaw(String data) throws IOException
	{
		getWriter().writeln(data);
	}
	
	private String truncate(String fileContent)
	{
		final int LIMIT_20K_CHARACTERS = 20000;
		final int fileContentLength = fileContent.length();
		final int startOfPortionToKeep = fileContentLength - LIMIT_20K_CHARACTERS;
		if (startOfPortionToKeep <= 0)
			return fileContent;
		
		return fileContent.substring(startOfPortionToKeep, fileContentLength);
	}
	
	private Project getProject()
	{
		return project;
	}
	
	private Project project;
}
