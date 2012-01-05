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
import java.text.DateFormat;
import java.util.Date;

import org.martus.util.UnicodeStringWriter;
import org.martus.util.UnicodeWriter;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.project.threatrating.SimpleThreatRatingFramework;
import org.miradi.project.threatrating.ThreatRatingBundle;

abstract public class AbstractMiradiProjectSaver
{
	public AbstractMiradiProjectSaver(UnicodeStringWriter writerToUse)
	{
		writer = writerToUse;
	}

	protected UnicodeStringWriter getWriter()
	{
		return writer;
	}
	
	protected static String xmlNewLineEncode(String data)
	{
		if (data.contains("\n"))
		{
			EAM.logError("Non html new lines found in data =" + data);
			throw new RuntimeException("Non html new lines found in data");
		}

		return data;
	}

	protected String createSimpleRefString(final ORef ref)
	{
		return Integer.toString(ref.getObjectType()) +  ":" + ref.getObjectId().toString();
	}
	
	protected void writeRefTagValue(final String actionCode, ORef ref, final String tag, final String value) throws Exception
	{
		writeLabelTagValue(actionCode, createSimpleRefString(ref), tag, value);
	}

	protected void writeLabelTagValue(final String actionCode, final String lineKey, final String tag, final String value) throws Exception, IOException
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
	
	protected void writeTagValue(final String actionCode, final String tag, final String value) throws Exception
	{
		write(actionCode);
		write(TAB);
		write(tag);
		write(EQUALS);
		write(value);
		getWriter().writeln();
	}

	protected void writeValue(final String actionCode, final String value) throws Exception
	{
		write(actionCode);
		write(TAB);
		write(value);
		getWriter().writeln();
	}
	
	protected void writeSimpleThreatRatingBundle(int threatId, int targetId, int defaultValueId, String ratings) throws Exception, IOException
	{
		String defaultValueIdString = Integer.toString(defaultValueId);
		String bundleName = SimpleThreatRatingFramework.getBundleKey(threatId, targetId);
		writeValue(CREATE_SIMPLE_THREAT_RATING_BUNDLE_CODE, bundleName);
		writeLabelTagValue(UPDATE_SIMPLE_THREAT_RATING_BUNDLE_CODE, bundleName, ThreatRatingBundle.TAG_DEFAULT_VALUE_ID, defaultValueIdString);
		writeLabelTagValue(UPDATE_SIMPLE_THREAT_RATING_BUNDLE_CODE, bundleName, ThreatRatingBundle.TAG_VALUES, ratings);
	}

	protected void write(final String data) throws Exception
	{
		getWriter().write(data);
	}
	
	protected void writeFileHeader() throws Exception
	{
		getWriter().writeln(getBasicFileHeader() + " " + VERSION_LOW + " " + VERSION_HIGH);
	}
	
	public void writeStopMarker(long lastModifiedMillis) throws Exception
	{
		Date lastModified = new Date(lastModifiedMillis);
		long lastModifiedForComputers = lastModified.getTime();
		String lastModifiedForHumans = DateFormat.getDateTimeInstance().format(lastModified);
		getWriter().writeln(STOP_MARKER + " " + lastModifiedForComputers + " " + lastModifiedForHumans);
	}
	
	public static String getBasicFileHeader()
	{
		String BOM_STRING = new String(new char[] {UnicodeWriter.BOM_UTF8});
		return BOM_STRING + "MiradiProjectFile";		
	}
	
	public static final String TAB = "\t";
	public static final String EQUALS = "=";

	public static final int VERSION_LOW = 1;
	public static final int VERSION_HIGH = 1;
	
	public static final String UPDATE_LAST_MODIFIED_TIME_CODE = "UL";
	public static final String UPDATE_PROJECT_INFO_CODE = "UP";
	public static final String UPDATE_PROJECT_VERSION_CODE = "UV";
	public static final String CREATE_OBJECT_CODE = "CO";
	public static final String UPDATE_OBJECT_CODE = "UO";
	public static final String CREATE_SIMPLE_THREAT_RATING_BUNDLE_CODE = "CT";
	public static final String UPDATE_SIMPLE_THREAT_RATING_BUNDLE_CODE = "UT";
	public static final String UPDATE_QUARANTINE_CODE = "UQ";
	public static final String UPDATE_EXCEPTIONS_CODE = "UE";
	
	public static final String LAST_MODIFIED_TAG = "LastModified";
	public static final String QUARANTINE_DATA_TAG = "Data";
	public static final String EXCEPTIONS_DATA_TAG = "Data";

	public static final String STOP_MARKER = "--";

	private UnicodeStringWriter writer;
}
