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
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.schemas.ProjectMetadataSchema;
import org.miradi.utils.DateUnitEffort;
import org.miradi.utils.DateUnitEffortList;

import java.util.HashMap;
import java.util.Set;
import java.util.Vector;

public class MigrationTo23 extends NewlyAddedFieldsMigration
{
	public MigrationTo23(RawProject rawProjectToUse)
	{
		super(rawProjectToUse, ProjectMetadataSchema.getObjectType());
	}

	@Override
	public AbstractMigrationVisitor createMigrateForwardVisitor() throws Exception
	{
		return new AddAndPopulateDayColumnsVisibilityVisitor(type);
	}

	@Override
	protected HashMap<String, String> createFieldsToLabelMapToModify()
	{
		HashMap<String, String> fieldsToAdd = new HashMap<String, String>();
		fieldsToAdd.put(TAG_DAY_COLUMNS_VISIBILITY, EAM.text("Day Columns Visibility"));
		
		return fieldsToAdd;
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
		return EAM.text("This migration adds a new day columns visibility field to the Project Metadata properties.");
	}

	private class AddAndPopulateDayColumnsVisibilityVisitor extends AbstractMigrationVisitor
	{
		public AddAndPopulateDayColumnsVisibilityVisitor(int typeToVisit)
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
			if (projectHasDayData())
				rawObject.setData(TAG_DAY_COLUMNS_VISIBILITY, SHOW_DAY_COLUMNS_CODE);
			else
				rawObject.setData(TAG_DAY_COLUMNS_VISIBILITY, HIDE_DAY_COLUMNS_CODE);

			return MigrationResult.createSuccess();
		}

		private Vector<Integer> getTypesToCheckForDayData()
		{
			Vector<Integer> typesToMigrate = new Vector<Integer>();
			typesToMigrate.add(ObjectType.RESOURCE_ASSIGNMENT);
			typesToMigrate.add(ObjectType.EXPENSE_ASSIGNMENT);

			return typesToMigrate;
		}

		private boolean projectHasDayData() throws Exception
		{
			for (int typeToCheck : getTypesToCheckForDayData())
			{
				if (typeHasDayData(typeToCheck))
					return true;
			}

			return false;
		}

		private boolean typeHasDayData(int typeToCheck) throws Exception
		{
			if (!getRawProject().containsAnyObjectsOfType(typeToCheck))
				return false;

			RawPool rawPool = getRawProject().getRawPoolForType(typeToCheck);
			Set<ORef> refs = rawPool.keySet();
			for(ORef ref : refs)
			{
				RawObject rawObject = getRawProject().findObject(ref);
				if (rawObject != null && rawObject.containsKey(TAG_DATEUNIT_EFFORTS))
				{
					DateUnitEffortList rawObjectDateUnitEffortList = new DateUnitEffortList(rawObject.get(TAG_DATEUNIT_EFFORTS));

					for (int index = 0; index < rawObjectDateUnitEffortList.size(); ++index)
					{
						DateUnitEffort rawObjectDateUnitEffort = rawObjectDateUnitEffortList.getDateUnitEffort(index);
						if (rawObjectDateUnitEffort.getDateUnit().isDay())
							return true;
					}
				}
			}

			return false;
		}

		private int type;
	}

	public static final int VERSION_FROM = 22;
	public static final int VERSION_TO = 23;

	public static final String TAG_DATEUNIT_EFFORTS = "Details";
	public static final String TAG_DAY_COLUMNS_VISIBILITY = "DayColumnsVisibility";
	public static final String SHOW_DAY_COLUMNS_CODE = "";
	public static final String HIDE_DAY_COLUMNS_CODE = "HideDayColumns";
}
