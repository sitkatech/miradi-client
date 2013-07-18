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
import org.miradi.ids.BaseId;
import org.miradi.ids.IndicatorId;
import org.miradi.main.TestCaseWithProject;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objectpools.FutureStatusPool;
import org.miradi.objectpools.IndicatorPool;
import org.miradi.objects.FutureStatus;
import org.miradi.objects.Indicator;
import org.miradi.objects.Strategy;
import org.miradi.project.ObjectManager;
import org.miradi.project.ProjectForTesting;
import org.miradi.project.ProjectLoader;
import org.miradi.project.ProjectSaverForTesting;
import org.miradi.questions.StatusQuestion;
import org.miradi.questions.StrategyStatusQuestion;
import org.miradi.schemas.IndicatorSchema;

public class TestMigrationManager extends TestCaseWithProject
{
	public TestMigrationManager(String name)
	{
		super(name);
	}
	
	public void testMigrateWithoutStrategies() throws Exception
	{
		verifyMigratingStrategyStatusQuestionRealStatusChoice("", "");
	}

	public void testMigrateStrategyWithoutStatusTag() throws Exception
	{
		verifyMigratingStrategyStatusQuestionRealStatusChoice("", "");
	}
	
	public void testMigrateLegacyStrategyWithStatusReal() throws Exception
	{
		verifyMigratingStrategyStatusQuestionRealStatusChoice("", MigrationTo5.LEGACY_DEFAULT_STRATEGY_STATUS_REAL);
	}
	
	public void testMigrateStrategyWithDraftStatus() throws Exception
	{		
		verifyMigratingStrategyStatusQuestionRealStatusChoice(StrategyStatusQuestion.STATUS_DRAFT_CODE, StrategyStatusQuestion.STATUS_DRAFT_CODE);
	}
	
	private void verifyMigratingStrategyStatusQuestionRealStatusChoice(String expectedStrategyStatusCode, String strategyStatusCode) throws Exception
	{
		Strategy strategy = getProject().createStrategy();
		getProject().fillObjectUsingCommand(strategy, Strategy.TAG_STATUS, strategyStatusCode);
		ProjectForTesting migratedProject = migrateProject(new VersionRange(4, 4));
		ORefList migratedStrategyRefs = migratedProject.getStrategyPool().getORefList();
		assertTrue("Incorrect number of strategies after migration?", migratedStrategyRefs.size() == 1);
		
		ORef migratedStrategyRef = migratedStrategyRefs.getFirstElement();
		Strategy migratedStrategy = Strategy.find(migratedProject, migratedStrategyRef);
		String migratedStrategyStatus = migratedStrategy.getData(Strategy.TAG_STATUS);
		assertEquals("Incorrect migrated strategy status?", expectedStrategyStatusCode, migratedStrategyStatus);
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
		ProjectForTesting migratedProject = migrateProject(new VersionRange(3, 3));
		final IndicatorPool indicatorPool = migratedProject.getIndicatorPool();
		assertEquals("Incorrect indictor count?", indicatorsToMigrateFutureStatusesFrom.size(), indicatorPool.size());

		final FutureStatusPool futureStatusPool = migratedProject.getFutureStatusPool();
		assertEquals("Incorrect FutureStatus count?", indicatorsToMigrateFutureStatusesFrom.size(), futureStatusPool.size());
		
		for(int index = 0; index <  indicatorsToMigrateFutureStatusesFrom.size(); ++index)
		{
			Indicator indicatorToMigrate = indicatorsToMigrateFutureStatusesFrom.get(index);
			ORef migratedIndicatorRef =  indicatorPool.getRefList().get(index);
			Indicator migratedIndicator = Indicator.find(migratedProject, migratedIndicatorRef);
			
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

	private Vector<Indicator> createAndPopluteIndicators(int numberOfIndicatorsToCreateAndPopulate) throws Exception
	{
		Vector<Indicator> indicators = new Vector<Indicator>();
		for (int index = 0; index < numberOfIndicatorsToCreateAndPopulate; ++index)
		{
			BaseId indicatorId = getProject().getNormalIdAssigner().takeNextId();
			LegacyIndicatorWithFutureStatusFields indicator = new LegacyIndicatorWithFutureStatusFields(getObjectManager(), indicatorId);
			getProject().getIndicatorPool().put(indicator);
			
			indicator.setData(IndicatorFutureStatusTagsToFutureStatusTagsMap.TAG_INDICATOR_FUTURE_STATUS_RATING, new StatusQuestion().getCode(index));
			indicator.setData(IndicatorFutureStatusTagsToFutureStatusTagsMap.TAG_INDICATOR_FUTURE_STATUS_DATE, "2020-01-23" + index);
			indicator.setData(IndicatorFutureStatusTagsToFutureStatusTagsMap.TAG_INDICATOR_FUTURE_STATUS_COMMENTS, "Some Indicator future status comment" + index);
			indicator.setData(IndicatorFutureStatusTagsToFutureStatusTagsMap.TAG_INDICATOR_FUTURE_STATUS_DETAIL, "random Details" + index);
			indicator.setData(IndicatorFutureStatusTagsToFutureStatusTagsMap.TAG_INDICATOR_FUTURE_STATUS_SUMMARY, "FS random summary" + index);
			
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
		verifyType(ForwardMigrationManager.NO_MIGRATION, tenTwentyRange, twentyFourtyRange);
		verifyType(ForwardMigrationManager.TOO_NEW_TO_MIGRATE, tenTwentyRange, fourtyFourtyRange);
		verifyType(ForwardMigrationManager.NO_MIGRATION, tenTwentyRange, fifteenFourtyRange);
		verifyType(ForwardMigrationManager.NO_MIGRATION, tenTwentyRange, tenTwentyRange);
		
		VersionRange FifteenFifteenRange = new VersionRange(15);
		verifyType(ForwardMigrationManager.NO_MIGRATION, tenTwentyRange, FifteenFifteenRange);
		verifyType(ForwardMigrationManager.MIGRATION, fourtyFourtyRange, FifteenFifteenRange);
	}

	private void verifyType(int expectedMigrationType, VersionRange miradiVersionRange, VersionRange mpfVersionRange) throws Exception
	{
		assertEquals("incorrect migration type?", expectedMigrationType, ForwardMigrationManager.getMigrationType(miradiVersionRange, mpfVersionRange));
	}
	
	private ProjectForTesting migrateProject(final VersionRange versionRangeToUse) throws Exception, IOException
	{
		ForwardMigrationManager migrationManager = new ForwardMigrationManager();
		String projectAsString = ProjectSaverForTesting.createSnapShot(getProject(), versionRangeToUse);
		String migratedMpfFile = migrationManager.migrateForward(projectAsString);
		
		ProjectForTesting migratedProject = ProjectForTesting.createProjectWithoutDefaultObjects("MigratedProject");
		ProjectLoader.loadProject(new UnicodeStringReader(migratedMpfFile), migratedProject);
		
		return migratedProject;
	}
	
	private class LegacyIndicatorWithFutureStatusFields extends Indicator
	{
		protected LegacyIndicatorWithFutureStatusFields(ObjectManager objectManager, BaseId idToUse)
		{
			super(objectManager, new IndicatorId(idToUse.asInt()), new LegacyIndicatorSchemaWithFutureStatusFields());
		}
	}
	
	private class LegacyIndicatorSchemaWithFutureStatusFields extends IndicatorSchema
	{
		@Override
		protected void fillFieldSchemas()
		{
			super.fillFieldSchemas();
			
			createFieldSchemaChoice(IndicatorFutureStatusTagsToFutureStatusTagsMap.TAG_INDICATOR_FUTURE_STATUS_RATING, StatusQuestion.class);
			createFieldSchemaDate(IndicatorFutureStatusTagsToFutureStatusTagsMap.TAG_INDICATOR_FUTURE_STATUS_DATE);
			createFieldSchemaSingleLineUserText(IndicatorFutureStatusTagsToFutureStatusTagsMap.TAG_INDICATOR_FUTURE_STATUS_SUMMARY);
			createFieldSchemaMultiLineUserText(IndicatorFutureStatusTagsToFutureStatusTagsMap.TAG_INDICATOR_FUTURE_STATUS_DETAIL);
			createFieldSchemaMultiLineUserText(IndicatorFutureStatusTagsToFutureStatusTagsMap.TAG_INDICATOR_FUTURE_STATUS_COMMENTS);
		}
	}
}
