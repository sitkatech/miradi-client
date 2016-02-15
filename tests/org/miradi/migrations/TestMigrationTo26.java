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

import org.miradi.migrations.forward.MigrationTo26;
import org.miradi.objectdata.CodeToCodeListMapData;
import org.miradi.objecthelpers.CodeToCodeListMap;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.TableSettings;
import org.miradi.project.Project;
import org.miradi.utils.CodeList;

import java.util.Vector;


public class TestMigrationTo26 extends AbstractTestMigration
{
	public TestMigrationTo26(String name)
	{
		super(name);
	}

	public void testTableSettingsFieldsChangedByMigration() throws Exception
	{
		Vector<String> newRowCodesBeingAdded = getNewRowCodesAddedByMigration();

		TableSettings tableSettingsBefore = getProject().createAndPopulateTableSettings();

		CodeToCodeListMap tableSettingsMapBefore = tableSettingsBefore.getTableSettingsMap();

		CodeList workPlanRowCodeListBefore = tableSettingsBefore.getCodeListFromTableSettingsMap(MigrationTo26.WORK_PLAN_ROW_CONFIGURATION_CODELIST_KEY);

		for (String code : newRowCodesBeingAdded)
		{
			assertTrue("Prior to reverse migration work plan row code list should contain new codes", workPlanRowCodeListBefore.contains(code));
		}

		RawProject migratedProject = reverseMigrate(new VersionRange(MigrationTo26.VERSION_TO));

		CodeToCodeListMap tableSettingsMapAfterReverseMigration = getCodeToCodeListMapData(migratedProject, tableSettingsBefore.getRef(), MigrationTo26.TAG_TABLE_SETTINGS_MAP);

		assertTrue("Reverse migration should not have removed work plan budget row code list", tableSettingsMapAfterReverseMigration.contains(MigrationTo26.WORK_PLAN_ROW_CONFIGURATION_CODELIST_KEY));

		assertTrue("Reverse migration should not have removed any codes from table settings map", tableSettingsMapBefore.getCodes().equals(tableSettingsMapAfterReverseMigration.getCodes()));

		CodeList workPlanRowCodeListAfterReverseMigration = tableSettingsMapAfterReverseMigration.getCodeList(MigrationTo26.WORK_PLAN_ROW_CONFIGURATION_CODELIST_KEY);

		for (String code : newRowCodesBeingAdded)
		{
			assertFalse("Reverse migration should have removed code from work plan budget row code list", workPlanRowCodeListAfterReverseMigration.contains(code));
		}

		assertEquals("Reverse migration should not have changed the number of codes (bar those removed)", workPlanRowCodeListAfterReverseMigration.size() + newRowCodesBeingAdded.size(), workPlanRowCodeListBefore.size());

		migrateProject(migratedProject, new VersionRange(Project.VERSION_HIGH));

		CodeToCodeListMap tableSettingsMapAfterForwardMigration = getCodeToCodeListMapData(migratedProject, tableSettingsBefore.getRef(), MigrationTo26.TAG_TABLE_SETTINGS_MAP);

		assertTrue("Forward migration should not have removed work plan row code list", tableSettingsMapAfterForwardMigration.contains(MigrationTo26.WORK_PLAN_ROW_CONFIGURATION_CODELIST_KEY));

		assertTrue("Forward migration should not have removed any codes from table settings map", tableSettingsMapBefore.getCodes().equals(tableSettingsMapAfterForwardMigration.getCodes()));

		CodeList workPlanRowCodeListAfterForwardMigration = tableSettingsMapAfterForwardMigration.getCodeList(MigrationTo26.WORK_PLAN_ROW_CONFIGURATION_CODELIST_KEY);

		for (String code : newRowCodesBeingAdded)
		{
			assertTrue("Forward migration should have added code to work plan row code list", workPlanRowCodeListAfterForwardMigration.contains(code));
		}

		assertEquals("Forward migration should have changed the number of codes", workPlanRowCodeListAfterForwardMigration.size(), workPlanRowCodeListAfterReverseMigration.size() + newRowCodesBeingAdded.size());

		verifyFullCircleMigrations(new VersionRange(25, 26));
	}

	private Vector<String> getNewRowCodesAddedByMigration()
	{
		Vector<String> result = new Vector<String>();
		result.add(MigrationTo26.RESOURCE_ASSIGNMENT);
		result.add(MigrationTo26.EXPENSE_ASSIGNMENT);
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
		return MigrationTo26.VERSION_FROM;
	}
	
	@Override
	protected int getToVersion()
	{
		return MigrationTo26.VERSION_TO;
	}
}
