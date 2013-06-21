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
import java.util.Vector;

import org.martus.util.UnicodeStringReader;
import org.miradi.main.TestCaseWithProject;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objectpools.FutureStatusPool;
import org.miradi.objectpools.IndicatorPool;
import org.miradi.objects.FutureStatus;
import org.miradi.objects.Indicator;
import org.miradi.project.ProjectForTesting;
import org.miradi.project.ProjectLoader;
import org.miradi.project.ProjectSaverForTesting;
import org.miradi.questions.StatusQuestion;

public class TestMigrationManager extends TestCaseWithProject
{
	public TestMigrationManager(String name)
	{
		super(name);
	}
	
	public void testMigrateWithoutIndicators() throws Exception
	{
		verifyFutureStatusesCreatedFromIndicators(0);
	}
	
	public void testMigrateIndicatorWithFutureStatuses() throws Exception
	{
		verifyFutureStatusesCreatedFromIndicators(1);
	}
	
	public void testMigrateIndicatorsWithFutureStatuses() throws Exception
	{
		verifyFutureStatusesCreatedFromIndicators(2);
	}

	private void verifyFutureStatusesCreatedFromIndicators(final int indicatorCount) throws Exception
	{
		Vector<Indicator> indicatorsToMigrateFutureStatusesFrom = createAndPopluteIndicators(indicatorCount);
		ProjectForTesting migratedProject = migrateProject();
		final IndicatorPool indicatorPool = migratedProject.getIndicatorPool();
		assertEquals("Incorrect indictor count?", indicatorsToMigrateFutureStatusesFrom.size(), indicatorPool.size());

		final FutureStatusPool futureStatusPool = migratedProject.getFutureStatusPool();
		assertEquals("Incorrect FutureStatus count?", indicatorsToMigrateFutureStatusesFrom.size(), futureStatusPool.size());
		
		for(int index = 0; index <  indicatorsToMigrateFutureStatusesFrom.size(); ++index)
		{
			Indicator indicatorToMigrate = indicatorsToMigrateFutureStatusesFrom.get(index);
			ORef migratedIndicatorRef =  indicatorPool.getRefList().get(index);
			Indicator migratedIndicator = Indicator.find(migratedProject, migratedIndicatorRef);
			verifyIndicatorFutureStatusFieldsWereCleared(migratedIndicator);
			
			ORefList futureStatusRefs = migratedIndicator.getSafeRefListData(Indicator.TAG_FUTURE_STATUS_REFS);
			assertEquals("Migrated indictor should refer to only 1 future status?", 1, futureStatusRefs.size());
			
			VerifyMigratedFutureStatusData(migratedProject, indicatorToMigrate, futureStatusRefs.getFirstElement());
		}
	}

	private void VerifyMigratedFutureStatusData(ProjectForTesting migratedProject, Indicator indicatorToMigrate, ORef futureStatusRef)
	{
		HashMap<String,String> indicatorFutureStatusTagsToFutureStatusTagMap = new IndicatorFutureStatusTagsToFutureStatusTagsMap();
		Set<String> indicatorFutureStatusTags = indicatorFutureStatusTagsToFutureStatusTagMap.keySet();
		FutureStatus futureStatus = FutureStatus.find(migratedProject, futureStatusRef);
		for(String indicatorFutureStatusTag : indicatorFutureStatusTags)
		{
			String futureStatusTag = indicatorFutureStatusTagsToFutureStatusTagMap.get(indicatorFutureStatusTag);
			String expectedData = indicatorToMigrate.getData(indicatorFutureStatusTag);
			String actualData = futureStatus.getData(futureStatusTag);
			assertEquals("Future status data was no migrated correctly for tag= " + futureStatusTag + "? " , expectedData, actualData);
		}
	}

	private void verifyIndicatorFutureStatusFieldsWereCleared(Indicator migratedIndicator)
	{
		HashMap<String,String> indicatorFutureStatusTagsToFutureStatusTagMap = new IndicatorFutureStatusTagsToFutureStatusTagsMap();
		Set<String> indicatorFutureStatusTags = indicatorFutureStatusTagsToFutureStatusTagMap.keySet();
		for(String indicatorFutureStatusTag : indicatorFutureStatusTags)
		{
			String indicatorFutureStatusData = migratedIndicator.getData(indicatorFutureStatusTag);
			assertEquals("Field with value should have been cleared by migration, tag=" + indicatorFutureStatusTag + "?", 0, indicatorFutureStatusData.length());
		}
	}
	
	private Vector<Indicator> createAndPopluteIndicators(int numberOfIndicatorsToCreateAndPopulate) throws Exception
	{
		Vector<Indicator> indicators = new Vector<Indicator>();
		for (int index = 0; index < numberOfIndicatorsToCreateAndPopulate; ++index)
		{
			Indicator indicator = getProject().createIndicatorWithCauseParent();
			getProject().fillObjectUsingCommand(indicator, IndicatorFutureStatusTagsToFutureStatusTagsMap.TAG_INDICATOR_FUTURE_STATUS_RATING, new StatusQuestion().getCode(index));
			getProject().fillObjectUsingCommand(indicator, IndicatorFutureStatusTagsToFutureStatusTagsMap.TAG_INDICATOR_FUTURE_STATUS_DATE, "2020-01-23" + index);
			getProject().fillObjectUsingCommand(indicator, IndicatorFutureStatusTagsToFutureStatusTagsMap.TAG_INDICATOR_FUTURE_STATUS_COMMENTS, "Some Indicator future status comment" + index);
			getProject().fillObjectUsingCommand(indicator, IndicatorFutureStatusTagsToFutureStatusTagsMap.TAG_INDICATOR_FUTURE_STATUS_DETAIL, "random Details" + index);
			getProject().fillObjectUsingCommand(indicator, IndicatorFutureStatusTagsToFutureStatusTagsMap.TAG_INDICATOR_FUTURE_STATUS_SUMMARY, "FS random summary" + index);
			
			indicators.add(indicator);
		}
		
		assertEquals("correct number of indicators created?", numberOfIndicatorsToCreateAndPopulate, indicators.size());
		
		return indicators;
	}

	public void testGetMigrationType() throws Exception
	{
		VersionRange tenTwentyRange = new VersionRange(10, 20);
		VersionRange twentyFourtyRange = new VersionRange(20, 40);
		VersionRange fourtyFourtyRange = new VersionRange(40);
		VersionRange fifteenFourtyRange = new VersionRange(15, 40);
		verifyType(MigrationManager.NO_MIGRATION, tenTwentyRange, twentyFourtyRange);
		verifyType(MigrationManager.TOO_NEW_TO_MIGRATE, tenTwentyRange, fourtyFourtyRange);
		verifyType(MigrationManager.NO_MIGRATION, tenTwentyRange, fifteenFourtyRange);
		verifyType(MigrationManager.NO_MIGRATION, tenTwentyRange, tenTwentyRange);
		
		VersionRange FifteenFifteenRange = new VersionRange(15);
		verifyType(MigrationManager.NO_MIGRATION, tenTwentyRange, FifteenFifteenRange);
		verifyType(MigrationManager.MIGRATION, fourtyFourtyRange, FifteenFifteenRange);
	}

	private void verifyType(int expectedMigrationType, VersionRange miradiVersionRange, VersionRange mpfVersionRange) throws Exception
	{
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
