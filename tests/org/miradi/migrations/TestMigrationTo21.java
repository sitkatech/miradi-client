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

import org.miradi.migrations.forward.MigrationTo21;
import org.miradi.objecthelpers.CodeToCodeMap;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.BaseObject;
import org.miradi.project.Project;
import org.miradi.questions.CustomPlanningColumnsQuestion;
import org.miradi.utils.CodeList;


public class TestMigrationTo21 extends AbstractTestMigration
{
	public TestMigrationTo21(String name)
	{
		super(name);
	}

	public void testObjectTreeTableConfigFieldsRenamedAfterMigration() throws Exception
	{
		ORef objectTreeTableConfigRef = getProject().createAndPopulateObjectTreeTableConfiguration().getRef();

		CodeList columnCodes = new CodeList();
		columnCodes.add(MigrationTo21.LEGACY_META_ASSIGNED_WHO_TOTAL);
		columnCodes.add(MigrationTo21.LEGACY_PSEUDO_TAG_ASSIGNED_WHEN_TOTAL);
		columnCodes.add(CustomPlanningColumnsQuestion.META_CURRENT_RATING);
		getProject().fillObjectUsingCommand(objectTreeTableConfigRef, MigrationTo21.TAG_COL_CONFIGURATION, columnCodes.toJsonString());

		RawProject reverseMigratedProject = reverseMigrate(new VersionRange(MigrationTo21.VERSION_TO));

		CodeList codeListAfterReverseMigration = new CodeList(reverseMigratedProject.getData(objectTreeTableConfigRef, MigrationTo21.TAG_COL_CONFIGURATION));

		assertEquals("Reverse migration should not have changed the number of codes", columnCodes.size(), codeListAfterReverseMigration.size());
		assertTrue("Reverse migration should not have removed legacy code", codeListAfterReverseMigration.contains(MigrationTo21.LEGACY_META_ASSIGNED_WHO_TOTAL));
		assertTrue("Reverse migration should not have removed legacy code", codeListAfterReverseMigration.contains(MigrationTo21.LEGACY_PSEUDO_TAG_ASSIGNED_WHEN_TOTAL));

		assertFalse("Reverse migration should not have added new code", codeListAfterReverseMigration.contains(MigrationTo21.META_ASSIGNED_WHO_TOTAL));
		assertFalse("Reverse migration should not have added new code", codeListAfterReverseMigration.contains(MigrationTo21.PSEUDO_TAG_ASSIGNED_WHEN_TOTAL));

		migrateProject(reverseMigratedProject, new VersionRange(Project.VERSION_HIGH));

		CodeList codeListAfterForwardMigration = new CodeList(reverseMigratedProject.getData(objectTreeTableConfigRef, MigrationTo21.TAG_COL_CONFIGURATION));

		assertEquals("Forward migration should not have changed the number of codes", columnCodes.size(), codeListAfterForwardMigration.size());
		assertFalse("Forward migration should have removed legacy code", codeListAfterForwardMigration.contains(MigrationTo21.LEGACY_META_ASSIGNED_WHO_TOTAL));
		assertFalse("Forward migration should have removed legacy code", codeListAfterForwardMigration.contains(MigrationTo21.LEGACY_PSEUDO_TAG_ASSIGNED_WHEN_TOTAL));

		assertTrue("Forward migration should have added new code", codeListAfterForwardMigration.contains(MigrationTo21.META_ASSIGNED_WHO_TOTAL));
		assertTrue("Forward migration should have added new code", codeListAfterForwardMigration.contains(MigrationTo21.PSEUDO_TAG_ASSIGNED_WHEN_TOTAL));

		verifyFullCircleMigrations(new VersionRange(20, 21));
	}

	public void testTableSettingsFieldsRenamedAfterMigration() throws Exception
	{
		ORef tableSettingsRef = getProject().createAndPopulateTableSettings().getRef();

		CodeList columnCodes = new CodeList();
		columnCodes.add(MigrationTo21.LEGACY_META_ASSIGNED_WHO_TOTAL);
		columnCodes.add(BaseObject.PSEUDO_TAG_LATEST_PROGRESS_REPORT_CODE);
		columnCodes.add(MigrationTo21.LEGACY_PSEUDO_TAG_ASSIGNED_WHEN_TOTAL);
		getProject().fillObjectUsingCommand(tableSettingsRef, MigrationTo21.TAG_COLUMN_SEQUENCE_CODES, columnCodes.toJsonString());

		CodeToCodeMap columnWidthMap = new CodeToCodeMap();
		columnWidthMap.putInteger(MigrationTo21.LEGACY_META_ASSIGNED_WHO_TOTAL, 100);
		columnWidthMap.putInteger(MigrationTo21.LEGACY_PSEUDO_TAG_ASSIGNED_WHEN_TOTAL, 200);
		columnWidthMap.putInteger(BaseObject.PSEUDO_TAG_LATEST_PROGRESS_REPORT_CODE, 300);
		getProject().fillObjectUsingCommand(tableSettingsRef, MigrationTo21.TAG_COLUMN_WIDTHS, columnWidthMap.toJsonString());

		RawProject reverseMigratedProject = reverseMigrate(new VersionRange(MigrationTo21.VERSION_TO));

		CodeList codeListAfterReverseMigration = new CodeList(reverseMigratedProject.getData(tableSettingsRef, MigrationTo21.TAG_COLUMN_SEQUENCE_CODES));

		assertEquals("Reverse migration should not have changed the number of codes", columnCodes.size(), codeListAfterReverseMigration.size());

		assertTrue("Reverse migration should not have removed legacy code", codeListAfterReverseMigration.contains(MigrationTo21.LEGACY_META_ASSIGNED_WHO_TOTAL));
		assertTrue("Reverse migration should not have removed legacy code", codeListAfterReverseMigration.contains(MigrationTo21.LEGACY_PSEUDO_TAG_ASSIGNED_WHEN_TOTAL));

		assertFalse("Reverse migration should not have added new code", codeListAfterReverseMigration.contains(MigrationTo21.META_ASSIGNED_WHO_TOTAL));
		assertFalse("Reverse migration should not have added new code", codeListAfterReverseMigration.contains(MigrationTo21.PSEUDO_TAG_ASSIGNED_WHEN_TOTAL));

		CodeToCodeMap columnWidthMapAfterReverseMigration = new CodeToCodeMap(reverseMigratedProject.getData(tableSettingsRef, MigrationTo21.TAG_COLUMN_WIDTHS));

		assertEquals("Reverse migration should not have changed the number of column width codes", columnWidthMap.size(), columnWidthMapAfterReverseMigration.size());

		assertTrue("Reverse migration should not have removed legacy code", columnWidthMapAfterReverseMigration.contains(MigrationTo21.LEGACY_META_ASSIGNED_WHO_TOTAL));
		assertTrue("Reverse migration should not have removed legacy code", columnWidthMapAfterReverseMigration.contains(MigrationTo21.LEGACY_PSEUDO_TAG_ASSIGNED_WHEN_TOTAL));

		assertFalse("Reverse migration should not have added new code", columnWidthMapAfterReverseMigration.contains(MigrationTo21.META_ASSIGNED_WHO_TOTAL));
		assertFalse("Reverse migration should not have added new code", columnWidthMapAfterReverseMigration.contains(MigrationTo21.PSEUDO_TAG_ASSIGNED_WHEN_TOTAL));

		migrateProject(reverseMigratedProject, new VersionRange(Project.VERSION_HIGH));

		CodeList codeListAfterForwardMigration = new CodeList(reverseMigratedProject.getData(tableSettingsRef, MigrationTo21.TAG_COLUMN_SEQUENCE_CODES));

		assertEquals("Forward migration should not have changed the number of codes", columnCodes.size(), codeListAfterForwardMigration.size());

		assertFalse("Forward migration should have removed legacy code", codeListAfterForwardMigration.contains(MigrationTo21.LEGACY_META_ASSIGNED_WHO_TOTAL));
		assertFalse("Forward migration should have removed legacy code", codeListAfterForwardMigration.contains(MigrationTo21.LEGACY_PSEUDO_TAG_ASSIGNED_WHEN_TOTAL));

		assertTrue("Forward migration should have added new code", codeListAfterForwardMigration.contains(MigrationTo21.META_ASSIGNED_WHO_TOTAL));
		assertTrue("Forward migration should have added new code", codeListAfterForwardMigration.contains(MigrationTo21.PSEUDO_TAG_ASSIGNED_WHEN_TOTAL));

		CodeToCodeMap columnWidthMapAfterForwardMigration = new CodeToCodeMap(reverseMigratedProject.getData(tableSettingsRef, MigrationTo21.TAG_COLUMN_WIDTHS));

		assertEquals("Forward migration should not have changed the number of column width codes", columnWidthMap.size(), columnWidthMapAfterForwardMigration.size());

		assertFalse("Forward migration should have removed legacy code", columnWidthMapAfterForwardMigration.contains(MigrationTo21.LEGACY_META_ASSIGNED_WHO_TOTAL));
		assertFalse("Forward migration should have removed legacy code", columnWidthMapAfterForwardMigration.contains(MigrationTo21.LEGACY_PSEUDO_TAG_ASSIGNED_WHEN_TOTAL));

		assertTrue("Forward migration should have added new code", columnWidthMapAfterForwardMigration.contains(MigrationTo21.META_ASSIGNED_WHO_TOTAL));
		assertTrue("Forward migration should have added new code", columnWidthMapAfterForwardMigration.contains(MigrationTo21.PSEUDO_TAG_ASSIGNED_WHEN_TOTAL));

		verifyFullCircleMigrations(new VersionRange(20, 21));
	}

	@Override
	protected int getFromVersion()
	{
		return MigrationTo21.VERSION_FROM;
	}
	
	@Override
	protected int getToVersion()
	{
		return MigrationTo21.VERSION_TO;
	}
}
