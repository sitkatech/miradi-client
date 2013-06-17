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

import java.util.HashMap;
import java.util.Set;

import org.martus.util.UnicodeStringReader;
import org.miradi.main.TestCaseWithProject;
import org.miradi.objecthelpers.ORef;
import org.miradi.objectpools.FutureStatusPool;
import org.miradi.objectpools.IndicatorPool;
import org.miradi.objects.FutureStatus;
import org.miradi.objects.Indicator;
import org.miradi.project.ProjectForTesting;
import org.miradi.project.ProjectLoader;
import org.miradi.project.ProjectSaver;

public class TestMigrationManager extends TestCaseWithProject
{
	public TestMigrationManager(String name)
	{
		super(name);
	}
	
	public void testMigrateIndicatorFutureStatuses() throws Exception
	{
		Indicator indicator = getProject().createIndicatorWithCauseParent();
		MigrationManager migrationManager = new MigrationManager();
		String migratedMpfFile = migrationManager.migrate(ProjectSaver.createSnapShot(getProject()));
		
		ProjectForTesting migratedProject = ProjectForTesting.createProjectWithoutDefaultObjects("MigratedProject");
		ProjectLoader.loadProject(new UnicodeStringReader(migratedMpfFile), migratedProject);
		final IndicatorPool indicatorPool = migratedProject.getIndicatorPool();
		assertEquals("Incorrect indictor count?", 1, indicatorPool.size());
		ORef migratedIndicatorRef =  indicatorPool.getRefList().getFirstElement();
		Indicator migratedIndicator = Indicator.find(migratedProject, migratedIndicatorRef);
		HashMap<String,String> indicatorFutureStatusTagsToFutureStatusTagMap = new IndicatorFutureStatusTagsToFutureStatusTagsMap();
		Set<String> indicatorFutureStatusTags = indicatorFutureStatusTagsToFutureStatusTagMap.keySet();
		for(String futureStatusTag : indicatorFutureStatusTags)
		{
			assertTrue("Field should have been removed by migration?", migratedIndicator.doesFieldExist(futureStatusTag));
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
		verifyType(MigrationManager.NO_MIGRATION, 10, 10, 5, 5);
		verifyType(MigrationManager.NO_MIGRATION, 10, 20, 15, 15);
		verifyType(MigrationManager.NO_MIGRATION, 10, 20, 20, 40);
		verifyType(MigrationManager.NO_MIGRATION, 10, 20, 40, 40);

		verifyType(MigrationManager.MIGRATION, 10, 20, 15, 40);
	}

	private void verifyType(String expectedMigrationType, int miradiLowVersion, int miradiHighVersion, int mpfLowVersion, int mpfHighVersion) throws Exception
	{
		VersionRange miradiVersionRange = new VersionRange(miradiLowVersion, miradiHighVersion);
		VersionRange mpfVersionRange = new VersionRange(mpfLowVersion, mpfHighVersion);
		assertEquals("incorrect migration type?", expectedMigrationType, MigrationManager.getMigrationType(miradiVersionRange, mpfVersionRange));
	}
}
