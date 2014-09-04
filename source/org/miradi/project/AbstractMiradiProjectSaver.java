/* 
Copyright 2005-2014, Foundations of Success, Bethesda, Maryland
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
import java.text.DateFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Vector;

import org.martus.util.UnicodeWriter;
import org.miradi.legacyprojects.LegacyProjectUtilities;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objecthelpers.ThreatRatingBundleSorter;
import org.miradi.project.threatrating.SimpleThreatRatingFramework;
import org.miradi.project.threatrating.ThreatRatingBundle;
import org.miradi.utils.EnhancedJsonObject;
import org.miradi.utils.HtmlUtilities;
import org.miradi.utils.StringUtilities;

abstract public class AbstractMiradiProjectSaver
{
	public AbstractMiradiProjectSaver(UnicodeWriter writerToUse)
	{
		writer = writerToUse;
	}
		
	protected UnicodeWriter getWriter()
	{
		return writer;
	}

	protected void ensureNoNonHtmlNewlinesExists(String data)
	{
		if (data.contains(StringUtilities.NEW_LINE))
		{
			EAM.logError("Non html new lines found in data =" + data);
			throw new RuntimeException("Non html new lines found in data");
		}
	}
	
	protected void saveProject() throws Exception
	{
		writeFileHeader();
		writeSchemaVersion();
		writeProjectInfo();
		writeLastModified();
		writeAllObjectTypes();
		writeAllSimpleThreatRatings();
		writeAllQuarantinedData();
		writeExceptionsLog();
		writeStopMarker(getLastModifiedTime());
		flushWriter();
	}

	protected void writeSimpleThreatRatingBundle(int threatId, int targetId, int defaultValueId, String ratings) throws Exception
	{
		String defaultValueIdString = Integer.toString(defaultValueId);
		String bundleName = SimpleThreatRatingFramework.getBundleKey(threatId, targetId);
		MiradiProjectFileUtilities.writeValue(getWriter(), MiradiProjectFileUtilities.CREATE_SIMPLE_THREAT_RATING_BUNDLE_CODE, bundleName);
		MiradiProjectFileUtilities.writeLabelTagValue(getWriter(), MiradiProjectFileUtilities.UPDATE_SIMPLE_THREAT_RATING_BUNDLE_CODE, bundleName, ThreatRatingBundle.TAG_DEFAULT_VALUE_ID, defaultValueIdString);
		MiradiProjectFileUtilities.writeLabelTagValue(getWriter(), MiradiProjectFileUtilities.UPDATE_SIMPLE_THREAT_RATING_BUNDLE_CODE, bundleName, ThreatRatingBundle.TAG_VALUES, ratings);
	}

	protected void writeFileHeader() throws Exception
	{
		getWriter().writeln(createLowHighVersionHeaderLine(getLowVersion(), getHighVersion()));
	}

	public static String createLowHighVersionHeaderLine(final int lowVersion,	final int highVersion)
	{
		return getBasicFileHeader() + " " + lowVersion + " " + highVersion;
	}

	public void writeStopMarker(long lastModifiedMillis) throws Exception
	{
		Date lastModified = new Date(lastModifiedMillis);
		long lastModifiedForComputers = lastModified.getTime();
		String lastModifiedForHumans = DateFormat.getDateTimeInstance().format(lastModified);
		getWriter().writeln(MiradiProjectFileUtilities.STOP_MARKER + " " + lastModifiedForComputers + " " + lastModifiedForHumans);
	}
	
	protected void flushWriter() throws IOException
	{
		getWriter().flush();
	}

	protected void writeSchemaVersion() throws Exception
	{
		MiradiProjectFileUtilities.writeTagValue(getWriter(), MiradiProjectFileUtilities.UPDATE_PROJECT_VERSION_CODE, "Version", Integer.toString(LegacyProjectUtilities.DATA_VERSION));
	}

	public static String getBasicFileHeader()
	{
		String BOM_STRING = new String(new char[] {UnicodeWriter.BOM_UTF8});
		return BOM_STRING + "MiradiProjectFile";		
	}
	
	protected void writeProjectInfo() throws Exception
	{
		MiradiProjectFileUtilities.writeTagValue(getWriter(), MiradiProjectFileUtilities.UPDATE_PROJECT_INFO_CODE, ProjectInfo.TAG_HIGHEST_OBJECT_ID, Integer.toString(getHighestAssignedId()));
		MiradiProjectFileUtilities.writeTagValue(getWriter(), MiradiProjectFileUtilities.UPDATE_PROJECT_INFO_CODE, ProjectInfo.TAG_PROJECT_METADATA_ID, getProjectMetadataId());
	}

	protected void writeLastModified() throws Exception
	{
		MiradiProjectFileUtilities.writeTagValue(getWriter(), MiradiProjectFileUtilities.UPDATE_LAST_MODIFIED_TIME_CODE, MiradiProjectFileUtilities.LAST_MODIFIED_TAG, LegacyProjectUtilities.timestampToString(getLastModifiedTime()));
	}
	
	protected void writeAllSimpleThreatRatings() throws Exception
	{
		Collection<ThreatRatingBundle> allBundles = getSimpleThreatRatingBundles();
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
	
	protected void writeAllQuarantinedData() throws Exception
	{
		String quarantineFileContents = getQuarantineData();
		ensureNoNonHtmlNewlinesExists(quarantineFileContents);
		MiradiProjectFileUtilities.writeTagValue(getWriter(), MiradiProjectFileUtilities.UPDATE_QUARANTINE_CODE, MiradiProjectFileUtilities.QUARANTINE_DATA_TAG, quarantineFileContents);
	}
	
	protected void writeExceptionsLog() throws Exception
	{
		String exceptions = getExceptionLog();
		exceptions = HtmlUtilities.replaceNonHtmlNewlines(exceptions);
		ensureNoNonHtmlNewlinesExists(exceptions);
		exceptions = truncate(exceptions);
		MiradiProjectFileUtilities.writeTagValue(getWriter(), MiradiProjectFileUtilities.UPDATE_EXCEPTIONS_CODE, MiradiProjectFileUtilities.EXCEPTIONS_DATA_TAG, exceptions);
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
	
	private void writeAllObjectTypes() throws Exception
	{
		for (int type = ObjectType.FIRST_OBJECT_TYPE; type < ObjectType.OBJECT_TYPE_COUNT; ++type)
		{
			ORefList refs = getSortedRefsForType(type);
			writeObjects(refs);
		}
	}
	
	private void writeObjects(ORefList sortedObjectRefs) throws Exception
	{
		for (int index = 0; index < sortedObjectRefs.size(); ++index)
		{
			final ORef ref = sortedObjectRefs.get(index);
			MiradiProjectFileUtilities.writeNewObjectEntry(getWriter(), ref);
			writeObjectUpdateEntries(ref);
		}
	}
	
	abstract protected ORefList getSortedRefsForType(int type) throws Exception;
	
	abstract protected String getExceptionLog() throws Exception;

	abstract protected String getQuarantineData() throws Exception;
	
	abstract protected Collection<ThreatRatingBundle> getSimpleThreatRatingBundles();
	
	abstract protected String getProjectMetadataId();

	abstract protected int getHighestAssignedId();
	
	abstract protected long getLastModifiedTime();
	
	abstract protected void writeObjectUpdateEntries(ORef ref) throws Exception;
	
	abstract protected int getHighVersion();

	abstract protected int getLowVersion();
	
	private UnicodeWriter writer;
}
