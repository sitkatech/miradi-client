/* 
Copyright 2005-2013, Foundations of Success, Bethesda, Maryland 
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

package org.miradi.migrations.forward;

import java.util.Set;

import org.miradi.migrations.AbstractMigrationVisitor;
import org.miradi.migrations.AbstractSingleTypeMigration;
import org.miradi.migrations.RawObject;
import org.miradi.migrations.RawProject;
import org.miradi.migrations.VersionRange;
import org.miradi.objecthelpers.CodeToUserStringMap;
import org.miradi.schemas.IndicatorSchema;
import org.miradi.utils.XmlUtilities2;

public class MigrationTo6 extends AbstractSingleTypeMigration
{
	public MigrationTo6(RawProject rawProjectToUse)
	{
		super(rawProjectToUse);
	}
	
	@Override
	public void migrateForward() throws Exception
	{
		getRawProject().visitAllObjectsInPool(new IndicatorVisitor());
	}

	@Override
	public VersionRange getMigratableVersionRange() throws Exception
	{
		return new VersionRange(VERSION_LOW, VERSION_HIGH);
	}
	
	@Override
	protected int getToVersion()
	{
		return 6;
	}
	
	@Override
	protected int getFromVersion() 
	{
		return 5;
	}
	
	private class IndicatorVisitor extends AbstractMigrationVisitor
	{
		public int getTypeToVisit()
		{
			return IndicatorSchema.getObjectType();
		}

		@Override
		public void internalVisit(RawObject indicator) throws Exception
		{
			escapeThresholdValues(indicator, TAG_THRESHOLDS_MAP);
			escapeThresholdValues(indicator, TAG_THRESHOLD_DETAILS_MAP);
		}

		private void escapeThresholdValues(RawObject indicator, final String tag) throws Exception
		{
			if (!indicator.containsKey(tag))
				return;
			
			String codeToUserStringMapAsString = indicator.get(tag);
			CodeToUserStringMap codeToUserStringMap = new CodeToUserStringMap(codeToUserStringMapAsString);
			CodeToUserStringMap escapedMap = new CodeToUserStringMap();
			Set<String> codes = codeToUserStringMap.getCodes();
			for(String code : codes)
			{
				String userValue = codeToUserStringMap.getUserString(code);
				String decodedValue = XmlUtilities2.getXmlDecoded(userValue);
				String encodedValue = XmlUtilities2.getXmlEncoded(decodedValue);
				escapedMap.putUserString(code, encodedValue);
			}
			
			indicator.put(tag, escapedMap.toJsonString());
		}
	}
	
	private static final int VERSION_LOW = 5;
	private static final int VERSION_HIGH = 5;
	
	private static final String TAG_THRESHOLDS_MAP = "IndicatorThresholds";
	private static final String TAG_THRESHOLD_DETAILS_MAP = "ThresholdDetails";
}
