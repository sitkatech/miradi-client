/* 
Copyright 2005-2017, Foundations of Success, Bethesda, Maryland
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
import org.miradi.objectdata.CodeToCodeListMapData;
import org.miradi.objecthelpers.CodeToCodeListMap;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.TableSettings;
import org.miradi.utils.CodeList;

import java.util.Vector;

public class MigrationTo21 extends AbstractMigration
{
	public MigrationTo21(RawProject rawProjectToUse)
	{
		super(rawProjectToUse);
	}

	@Override
	protected MigrationResult migrateForward() throws Exception
	{
		return migrate(false);
	}

	@Override
	protected MigrationResult reverseMigrate() throws Exception
	{
		return migrate(true);
	}

	private MigrationResult migrate(boolean reverseMigration) throws Exception
	{
		MigrationResult migrationResult = MigrationResult.createUninitializedResult();

		Vector<Integer> typesToVisit = getTypesToMigrate();

		for(Integer typeToVisit : typesToVisit)
		{
			final WorkPlanBudgetColumnCodeListVisitor visitor = new WorkPlanBudgetColumnCodeListVisitor(typeToVisit, reverseMigration);
			visitAllORefsInPool(visitor);
			final MigrationResult thisMigrationResult = visitor.getMigrationResult();
			if (migrationResult == null)
				migrationResult = thisMigrationResult;
			else
				migrationResult.merge(thisMigrationResult);
		}

		return migrationResult;
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
		return EAM.text("This migration adds new default assigned who / when total columns to the work plan customize panel.");
	}

	private Vector<Integer> getTypesToMigrate()
	{
		Vector<Integer> typesToMigrate = new Vector<Integer>();
		typesToMigrate.add(ObjectType.TABLE_SETTINGS);

		return typesToMigrate;
	}

	private class WorkPlanBudgetColumnCodeListVisitor extends AbstractMigrationORefVisitor
	{
		public WorkPlanBudgetColumnCodeListVisitor(int typeToVisit, boolean reverseMigration)
		{
			type = typeToVisit;
			isReverseMigration = reverseMigration;
		}

		public int getTypeToVisit()
		{
			return type;
		}

		@Override
		public MigrationResult internalVisit(ORef rawObjectRef) throws Exception
		{
			MigrationResult migrationResult = MigrationResult.createUninitializedResult();

			RawObject rawObject = getRawProject().findObject(rawObjectRef);
			if (rawObject != null)
			{
				if (isReverseMigration)
					migrationResult = removeFields(rawObject);
				else
					migrationResult = addFields(rawObject);
			}

			return migrationResult;
		}

		private MigrationResult addFields(RawObject rawObject) throws Exception
		{
			MigrationResult migrationResult = MigrationResult.createSuccess();

			CodeToCodeListMap tableSettingsMap = getCodeToCodeListMapData(rawObject, TAG_TABLE_SETTINGS_MAP);

			CodeList workPlanBudgetColumnCodeList = new CodeList();
			if (tableSettingsMap.contains(WORK_PLAN_BUDGET_COLUMNS_CODELIST_KEY))
				workPlanBudgetColumnCodeList = tableSettingsMap.getCodeList(WORK_PLAN_BUDGET_COLUMNS_CODELIST_KEY);

			if (!workPlanBudgetColumnCodeList.contains(META_ASSIGNED_WHO_TOTAL))
				workPlanBudgetColumnCodeList.add(META_ASSIGNED_WHO_TOTAL);

			tableSettingsMap.putCodeList(TableSettings.WORK_PLAN_BUDGET_COLUMNS_CODELIST_KEY, workPlanBudgetColumnCodeList);

			rawObject.setData(TAG_TABLE_SETTINGS_MAP, tableSettingsMap.toJsonString());

			return migrationResult;
		}

		private MigrationResult removeFields(RawObject rawObject) throws Exception
		{
			MigrationResult migrationResult = MigrationResult.createSuccess();

			CodeToCodeListMap tableSettingsMap = getCodeToCodeListMapData(rawObject, TAG_TABLE_SETTINGS_MAP);
			if (tableSettingsMap.contains(WORK_PLAN_BUDGET_COLUMNS_CODELIST_KEY))
			{
				CodeList workPlanBudgetColumnCodeList = tableSettingsMap.getCodeList(WORK_PLAN_BUDGET_COLUMNS_CODELIST_KEY);

				if (workPlanBudgetColumnCodeList.contains(META_ASSIGNED_WHO_TOTAL))
					workPlanBudgetColumnCodeList.removeCode(META_ASSIGNED_WHO_TOTAL);
				if (workPlanBudgetColumnCodeList.contains(META_ASSIGNED_WHEN_TOTAL))
					workPlanBudgetColumnCodeList.removeCode(META_ASSIGNED_WHEN_TOTAL);

				tableSettingsMap.putCodeList(TableSettings.WORK_PLAN_BUDGET_COLUMNS_CODELIST_KEY, workPlanBudgetColumnCodeList);

				rawObject.setData(TAG_TABLE_SETTINGS_MAP, tableSettingsMap.toJsonString());
			}

			return migrationResult;
		}

		private CodeToCodeListMap getCodeToCodeListMapData(RawObject rawObject, String tag) throws Exception
		{
			String rawValue = rawObject.getData(tag);
			CodeToCodeListMapData map = new CodeToCodeListMapData(tag);
			if (rawValue != null)
				map.set(rawValue);
			return map.getStringToCodeListMap();
		}

		private int type;
		private boolean isReverseMigration;
	}

	public static final int VERSION_FROM = 20;
	public static final int VERSION_TO = 21;

	public static final String TAG_TABLE_SETTINGS_MAP = "TagTableSettingsMap";
	public static final String WORK_PLAN_BUDGET_COLUMNS_CODELIST_KEY = "WorkPlanBudgetColumnCodeListKey";

	public static final String META_ASSIGNED_WHO_TOTAL = "MetaAssignedWhoTotal";
	public static final String META_ASSIGNED_WHEN_TOTAL = "AssignedEffortDatesTotal";
}