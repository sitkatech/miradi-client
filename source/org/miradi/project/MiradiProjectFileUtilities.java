/* 
Copyright 2005-2021, Foundations of Success, Bethesda, Maryland
on behalf of the Conservation Measures Partnership ("CMP").
Material developed between 2005-2013 is jointly copyright by Beneficent Technology, Inc. ("The Benetech Initiative"), Palo Alto, California.

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

import org.martus.util.UnicodeWriter;
import org.miradi.objecthelpers.ORef;

public class MiradiProjectFileUtilities
{
	public static void writeNewObjectEntry(UnicodeWriter writer, ORef ref) throws Exception
	{
		writeValue(writer, CREATE_OBJECT_CODE, createSimpleRefString(ref));
	}
	
	public static void writeUpdateObjectLine(UnicodeWriter writer, ORef ref, String tag, String data) throws Exception
	{
		writeRefTagValue(writer, UPDATE_OBJECT_CODE, ref, tag, data);
	}
	
	protected static void writeRefTagValue(UnicodeWriter writer, final String actionCode, ORef ref, final String tag, final String value) throws Exception
	{
		writeLabelTagValue(writer, actionCode, createSimpleRefString(ref), tag, value);
	}

	protected static void writeLabelTagValue(UnicodeWriter writer, final String actionCode, final String lineKey, final String tag, final String value) throws Exception, IOException
	{
		write(writer, actionCode);
		write(writer, TAB);
		
		write(writer, lineKey);
		write(writer, TAB);
		
		write(writer, tag);
		write(writer, EQUALS);
		write(writer, value);
		
		writer.writeln();
	}
	
	protected static void writeTagValue(UnicodeWriter writer, final String actionCode, final String tag, final String value) throws Exception
	{
		write(writer, actionCode);
		write(writer, TAB);
		write(writer, tag);
		write(writer, EQUALS);
		write(writer, value);
		writer.writeln();
	}

	protected static void writeValue(UnicodeWriter writer, final String actionCode, final String value) throws Exception
	{
		write(writer, actionCode);
		write(writer, TAB);
		write(writer, value);
		writer.writeln();
	}
	
	protected static void write(UnicodeWriter writer, final String data) throws Exception
	{
		writer.write(data);
	}
	
	protected static String createSimpleRefString(final ORef ref)
	{
		return Integer.toString(ref.getObjectType()) +  ":" + ref.getObjectId().toString();
	}
	
	public static final String TAB = "\t";
	public static final String EQUALS = "=";
	
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
}
