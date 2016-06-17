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

package org.miradi.migrations;

import org.miradi.migrations.forward.MigrationTo34;
import org.miradi.objectdata.CodeToCodeListMapData;
import org.miradi.objecthelpers.CodeToCodeListMap;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.Strategy;
import org.miradi.objects.TableSettings;
import org.miradi.project.Project;
import org.miradi.utils.CodeList;

import java.util.Vector;


public class TestMigrationTo34 extends AbstractTestMigration
{
	public TestMigrationTo34(String name)
	{
		super(name);
	}

	public void testTableSettingsFieldsChangedByMigration() throws Exception
	{
		Strategy strategy = getProject().createStrategy();
		getProject().addTimeframe(strategy);

		Vector<String> newColumnCodesBeingAdded = getNewColumnCodesAddedByMigration();
		TableSettings tableSettingsBefore = getProject().createAndPopulateTableSettings();
		CodeToCodeListMap tableSettingsMapBefore = tableSettingsBefore.getTableSettingsMap();

		CodeList workPlanBudgetColumnsCodeListBefore = tableSettingsBefore.getCodeListFromTableSettingsMap(MigrationTo34.WORK_PLAN_BUDGET_COLUMNS_CODELIST_KEY);
		for (String code : newColumnCodesBeingAdded)
		{
			assertTrue("Prior to reverse migration work plan budget columns code list should contain new columns", workPlanBudgetColumnsCodeListBefore.contains(code));
		}

		RawProject migratedProject = reverseMigrate(new VersionRange(MigrationTo34.VERSION_TO));

		CodeToCodeListMap tableSettingsMapAfterReverseMigration = getCodeToCodeListMapData(migratedProject, tableSettingsBefore.getRef(), MigrationTo34.TAG_TABLE_SETTINGS_MAP);

		assertTrue("Reverse migration should not have removed work plan budget columns code list", tableSettingsMapAfterReverseMigration.contains(MigrationTo34.WORK_PLAN_BUDGET_COLUMNS_CODELIST_KEY));
		assertTrue("Reverse migration should not have removed any codes from table settings map", tableSettingsMapBefore.getCodes().equals(tableSettingsMapAfterReverseMigration.getCodes()));

		CodeList workPlanBudgetColumnCodeListAfterReverseMigration = tableSettingsMapAfterReverseMigration.getCodeList(MigrationTo34.WORK_PLAN_BUDGET_COLUMNS_CODELIST_KEY);
		for (String code : newColumnCodesBeingAdded)
		{
			assertFalse("Reverse migration should have removed code from work plan budget columns code list", workPlanBudgetColumnCodeListAfterReverseMigration.contains(code));
		}

		migrateProject(migratedProject, new VersionRange(Project.VERSION_HIGH));

		CodeToCodeListMap tableSettingsMapAfterForwardMigration = getCodeToCodeListMapData(migratedProject, tableSettingsBefore.getRef(), MigrationTo34.TAG_TABLE_SETTINGS_MAP);

		assertTrue("Forward migration should not have removed work plan budget columns code list", tableSettingsMapAfterForwardMigration.contains(MigrationTo34.WORK_PLAN_BUDGET_COLUMNS_CODELIST_KEY));
		assertTrue("Forward migration should not have removed any codes from table settings map", tableSettingsMapBefore.getCodes().equals(tableSettingsMapAfterForwardMigration.getCodes()));

		CodeList workPlanBudgetColumnCodeListAfterForwardMigration = tableSettingsMapAfterForwardMigration.getCodeList(MigrationTo34.WORK_PLAN_BUDGET_COLUMNS_CODELIST_KEY);
		for (String code : newColumnCodesBeingAdded)
		{
			assertTrue("Forward migration should have added code to work plan budget columns code list", workPlanBudgetColumnCodeListAfterForwardMigration.contains(code));
		}

		verifyFullCircleMigrations(new VersionRange(33, 34));
	}

	private Vector<String> getNewColumnCodesAddedByMigration()
	{
		Vector<String> result = new Vector<String>();
		result.add(MigrationTo34.META_TIMEFRAME_TOTAL);
		return result;
	}

	private CodeToCodeListMap getCodeToCodeListMapData(RawProject rawProject, ORef oRef, String tag) throws Exception
	{
		String rawValue = rawProject.getData(oRef, tag);
		CodeToCodeListMapData map = new CodeToCodeListMapData(tag);
		if (rawValue != null)
			map.set(rawValue);
		return map.getStringToCodeListMap();
	}

	@Override
	protected int getFromVersion()
	{
		return MigrationTo34.VERSION_FROM;
	}
	
	@Override
	protected int getToVersion()
	{
		return MigrationTo34.VERSION_TO;
	}
}
