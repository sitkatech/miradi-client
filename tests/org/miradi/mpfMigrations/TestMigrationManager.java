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

package org.miradi.mpfMigrations;

import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

import org.martus.util.UnicodeStringReader;
import org.miradi.main.TestCaseWithProject;
import org.miradi.objecthelpers.ORef;
import org.miradi.objectpools.BaseObjectPool;
import org.miradi.objectpools.FutureStatusPool;
import org.miradi.objectpools.IndicatorPool;
import org.miradi.objects.FutureStatus;
import org.miradi.objects.Indicator;
import org.miradi.project.ProjectForTesting;
import org.miradi.project.ProjectLoader;
import org.miradi.project.ProjectSaverForTesting;

public class TestMigrationManager extends TestCaseWithProject
{
	public TestMigrationManager(String name)
	{
		super(name);
	}
	
	public void testMigrateIndicatorWithoutFutureStatusValues() throws Exception
	{
		getProject().createIndicatorWithCauseParent();
		ProjectForTesting migratedProject = migrateProject();
		final BaseObjectPool futureStatusPool = migratedProject.getFutureStatusPool();
		assertEquals("Future status should be empty?", 0, futureStatusPool.size());
	}
	
	public void testMigrateIndicatorWithFutureStatuses() throws Exception
	{
		Indicator indicator = getProject().createIndicatorWithCauseParent();
		getProject().populateIndicator(indicator);
		ProjectForTesting migratedProject = migrateProject();
		final IndicatorPool indicatorPool = migratedProject.getIndicatorPool();
		assertEquals("Incorrect indictor count?", 1, indicatorPool.size());
		
		ORef migratedIndicatorRef =  indicatorPool.getRefList().getFirstElement();
		Indicator migratedIndicator = Indicator.find(migratedProject, migratedIndicatorRef);
		HashMap<String,String> indicatorFutureStatusTagsToFutureStatusTagMap = new IndicatorFutureStatusTagsToFutureStatusTagsMap();
		Set<String> indicatorFutureStatusTags = indicatorFutureStatusTagsToFutureStatusTagMap.keySet();
		for(String indicatorFutureStatusTag : indicatorFutureStatusTags)
		{
			String indicatorFutureStatusData = migratedIndicator.getData(indicatorFutureStatusTag);
			assertEquals("Field with value should have been cleared by migration, tag=" + indicatorFutureStatusTag + "?", 0, indicatorFutureStatusData.length());
		}
		
		final FutureStatusPool futureStatusPool = migratedProject.getFutureStatusPool();
		assertEquals("Incorrect FutureStatus count?", 1, futureStatusPool.size());
		
		ORef futureStatusRef = futureStatusPool.getORefList().getFirstElement();
		FutureStatus futureStatus = FutureStatus.find(migratedProject, futureStatusRef);
		for(String indicatorFutureStatusTag : indicatorFutureStatusTags)
		{
			String futureStatusTag = indicatorFutureStatusTagsToFutureStatusTagMap.get(indicatorFutureStatusTag);
			String expectedData = indicator.getData(indicatorFutureStatusTag);
			String actualData = futureStatus.getData(futureStatusTag);
			assertEquals("Future status data was no migrated correctly?", expectedData, actualData);
		}
	}

	public void testGetMigrationType() throws Exception
	{
		verifyType(MigrationManager.MIGRATION, 10, 10, 5, 5);
		verifyType(MigrationManager.NO_MIGRATION, 10, 20, 15, 15);
		verifyType(MigrationManager.NO_MIGRATION, 10, 20, 20, 40);
		verifyType(MigrationManager.NO_MIGRATION, 10, 20, 40, 40);

		verifyType(MigrationManager.MIGRATION, 10, 20, 15, 40);
	}

	private void verifyType(int expectedMigrationType, int miradiLowVersion, int miradiHighVersion, int mpfLowVersion, int mpfHighVersion) throws Exception
	{
		VersionRange miradiVersionRange = new VersionRange(miradiLowVersion, miradiHighVersion);
		VersionRange mpfVersionRange = new VersionRange(mpfLowVersion, mpfHighVersion);
		assertEquals("incorrect migration type?", expectedMigrationType, MigrationManager.getMigrationType(miradiVersionRange, mpfVersionRange));
	}
	
	private ProjectForTesting migrateProject() throws Exception, IOException
	{
		MigrationManager migrationManager = new MigrationManager();
		String projectAsString = ProjectSaverForTesting.createSnapShot(getProject(), new VersionRange(3, 3));
		String migratedMpfFile = migrationManager.migrate(projectAsString);
		
		ProjectForTesting migratedProject = ProjectForTesting.createProjectWithoutDefaultObjects("MigratedProject");
		ProjectLoader.loadProject(new UnicodeStringReader(migratedMpfFile), migratedProject);
		
		return migratedProject;
	}
}
