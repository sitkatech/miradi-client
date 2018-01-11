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

package org.miradi.migrations;

import org.miradi.migrations.forward.MigrationTo40;
import org.miradi.objectdata.CodeToCodeListMapData;
import org.miradi.objecthelpers.CodeToCodeListMap;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.ProjectResource;
import org.miradi.objects.TableSettings;
import org.miradi.project.Project;


public class TestMigrationTo40 extends AbstractTestMigration
{
	public TestMigrationTo40(String name)
	{
		super(name);
	}

	public void testTableSettingsProjectResourceFilterChangedByMigration() throws Exception
	{
		TableSettings tableSettings = getProject().createAndPopulateTableSettings();

		// add project resources to filter then delete one of them...
		ProjectResource projectResourceToDelete = getProject().createProjectResource();
		ProjectResource projectResourceToKeep = getProject().createProjectResource();
		ORefList projectResourceRefs = new ORefList();
		projectResourceRefs.add(projectResourceToDelete);
		projectResourceRefs.add(projectResourceToKeep);

		CodeToCodeListMap tableSettingsMap = tableSettings.getTableSettingsMap();
		tableSettingsMap.putRefList(MigrationTo40.WORK_PLAN_PROJECT_RESOURCE_FILTER_CODELIST_KEY, projectResourceRefs);
		tableSettings.setData(MigrationTo40.TAG_TABLE_SETTINGS_MAP, tableSettingsMap.toJsonString());
		getProject().deleteObject(projectResourceToDelete);

		CodeToCodeListMap tableSettingsMapBefore = tableSettings.getTableSettingsMap();
		ORefList filterProjectResourceRefsBefore = tableSettingsMapBefore.getRefList(MigrationTo40.WORK_PLAN_PROJECT_RESOURCE_FILTER_CODELIST_KEY);

		assertTrue("Prior to reverse migration filter should contain deleted project resource ref", filterProjectResourceRefsBefore.contains(projectResourceToDelete.getRef()));
		assertTrue("Prior to reverse migration filter should contain retained project resource ref", filterProjectResourceRefsBefore.contains(projectResourceToKeep.getRef()));

		RawProject migratedProject = reverseMigrate(new VersionRange(MigrationTo40.VERSION_TO));

		CodeToCodeListMap tableSettingsMapAfterReverseMigration = getCodeToCodeListMapData(migratedProject, tableSettings.getRef(), MigrationTo40.TAG_TABLE_SETTINGS_MAP);
		ORefList filterProjectResourceRefsAfterReverseMigration = tableSettingsMapAfterReverseMigration.getRefList(MigrationTo40.WORK_PLAN_PROJECT_RESOURCE_FILTER_CODELIST_KEY);

		assertTrue("After reverse migration filter should still contain deleted project resource ref", filterProjectResourceRefsAfterReverseMigration.contains(projectResourceToDelete.getRef()));
		assertTrue("After reverse migration filter should still contain retained project resource ref", filterProjectResourceRefsAfterReverseMigration.contains(projectResourceToKeep.getRef()));

		migrateProject(migratedProject, new VersionRange(Project.VERSION_HIGH));

		CodeToCodeListMap tableSettingsMapAfterForwardMigration = getCodeToCodeListMapData(migratedProject, tableSettings.getRef(), MigrationTo40.TAG_TABLE_SETTINGS_MAP);
		ORefList filterProjectResourceRefsAfterForwardMigration = tableSettingsMapAfterForwardMigration.getRefList(MigrationTo40.WORK_PLAN_PROJECT_RESOURCE_FILTER_CODELIST_KEY);

		assertFalse("After forward migration filter should not contain deleted project resource ref", filterProjectResourceRefsAfterForwardMigration.contains(projectResourceToDelete.getRef()));
		assertTrue("After forward migration filter should contain retained project resource ref", filterProjectResourceRefsAfterForwardMigration.contains(projectResourceToKeep.getRef()));

		verifyFullCircleMigrations(new VersionRange(39, 40));
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
		return MigrationTo40.VERSION_FROM;
	}
	
	@Override
	protected int getToVersion()
	{
		return MigrationTo40.VERSION_TO;
	}
}
