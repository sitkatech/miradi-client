/* 
Copyright 2005-2015, Foundations of Success, Bethesda, Maryland
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

import org.miradi.main.EAM;
import org.miradi.migrations.*;
import org.miradi.objecthelpers.ObjectType;

import java.util.HashMap;

public class MigrationTo32 extends NewlyAddedFieldsMigration
{
	public MigrationTo32(RawProject rawProjectToUse)
	{
		super(rawProjectToUse, ObjectType.INDICATOR);
	}

	@Override
	protected HashMap<String, String> createFieldsToLabelMapToModify()
	{
		HashMap<String, String> fieldsToAdd = new HashMap<String, String>();
		fieldsToAdd.put(TAG_RELEVANT_STRATEGY_ACTIVITY_SET, EAM.text("Indicator Relevant Strategies / Activities"));

		return fieldsToAdd;
	}

	@Override
	public AbstractMigrationVisitor createMigrateForwardVisitor() throws Exception
	{
		return new IndicatorVisitor(type);
	}

	@Override
	protected int getToVersion()
	{
		return VERSION_TO;
	}
	
	@Override
	protected int getFromVersion() 
	{
		return VERSION_FROM;
	}
	
	@Override
	protected String getDescription()
	{
		return EAM.text("This migration adds a new field to the Indicator properties to track Strategy / Activity relevancy.");
	}

	private class IndicatorVisitor extends AbstractMigrationVisitor
	{
		private IndicatorVisitor(int typeToVisit)
		{
			type = typeToVisit;
		}

		public int getTypeToVisit()
		{
			return type;
		}

		@Override
		protected MigrationResult internalVisit(RawObject rawObject) throws Exception
		{
			MigrationResult migrationResult = MigrationResult.createUninitializedResult();

			if (rawObject != null)
				migrationResult = addFields(rawObject);

			return migrationResult;
		}

		private MigrationResult addFields(RawObject rawObject) throws Exception
		{
			MigrationResult migrationResult = MigrationResult.createSuccess();

			rawObject.setData(TAG_RELEVANT_STRATEGY_ACTIVITY_SET, "");

			return migrationResult;
		}

		private int type;
	}

	public static final int VERSION_FROM = 31;
	public static final int VERSION_TO = 32;

	public static final String TAG_RELEVANT_STRATEGY_ACTIVITY_SET = "RelevantStrategySet";
}
