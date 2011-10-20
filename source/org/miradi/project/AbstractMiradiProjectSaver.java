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

import org.martus.util.UnicodeStringWriter;
import org.martus.util.xml.XmlUtilities;
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
		data = XmlUtilities.getXmlEncoded(data);
		data = data.replaceAll("\\n", "<br/>");
		
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
	
	public static final String TAB = "\t";
	public static final String EQUALS = "=";

	public static final String UPDATE_PROJECT_INFO_CODE = "UP";
	public static final String UPDATE_PROJECT_VERSION_CODE = "UV";
	public static final String CREATE_OBJECT_CODE = "CO";
	public static final String UPDATE_OBJECT_CODE = "UO";
	public static final String CREATE_SIMPLE_THREAT_RATING_BUNDLE_CODE = "CT";
	public static final String UPDATE_SIMPLE_THREAT_RATING_BUNDLE_CODE = "UT";

	private UnicodeStringWriter writer;
}
