/* 
Copyright 2005-2018, Foundations of Success, Bethesda, Maryland
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

package org.miradi.migrations.forward;

import java.util.Set;

import org.miradi.main.EAM;
import org.miradi.migrations.AbstractMigrationVisitor;
import org.miradi.migrations.AbstractSingleTypeMigration;
import org.miradi.migrations.MigrationResult;
import org.miradi.migrations.RawObject;
import org.miradi.migrations.RawProject;
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
	protected MigrationResult migrateForward() throws Exception
	{
		final IndicatorVisitor visitor = new IndicatorVisitor();

		return visitAllObjectsInPool(visitor);
	}
	
	@Override
	protected MigrationResult reverseMigrate() throws Exception
	{
		return MigrationResult.createSuccess();
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
	
	@Override
	protected String getDescription()
	{
		return EAM.text("This migration corrects indicator threshold escaping issues.");
	}
	
	private class IndicatorVisitor extends AbstractMigrationVisitor
	{
		public int getTypeToVisit()
		{
			return IndicatorSchema.getObjectType();
		}

		@Override
		public MigrationResult internalVisit(RawObject indicator) throws Exception
		{
			escapeThresholdValues(indicator, TAG_THRESHOLDS_MAP);
			escapeThresholdValues(indicator, TAG_THRESHOLD_DETAILS_MAP);
			
			return MigrationResult.createSuccess();
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
	
	private static final String TAG_THRESHOLDS_MAP = "IndicatorThresholds";
	private static final String TAG_THRESHOLD_DETAILS_MAP = "ThresholdDetails";
}
