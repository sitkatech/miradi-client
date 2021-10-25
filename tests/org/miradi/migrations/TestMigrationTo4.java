/* 
Copyright 2005-2021, Foundations of Success, Bethesda, Maryland
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

import java.util.HashMap;
import java.util.Set;
import java.util.Vector;

import org.miradi.ids.BaseId;
import org.miradi.ids.IndicatorId;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objectpools.FutureStatusPool;
import org.miradi.objectpools.IndicatorPool;
import org.miradi.objects.FutureStatus;
import org.miradi.objects.Indicator;
import org.miradi.project.ObjectManager;
import org.miradi.project.ProjectForTesting;
import org.miradi.questions.StatusQuestion;
import org.miradi.schemas.IndicatorSchema;

public class TestMigrationTo4 extends AbstractTestMigration
{
	public TestMigrationTo4(String name)
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
		Vector<Indicator> indicatorsToMigrateFutureStatusesFrom = createAndPopulateIndicators(indicatorCount);
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

	private Vector<Indicator> createAndPopulateIndicators(int numberOfIndicatorsToCreateAndPopulate) throws Exception
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
	
	@Override
	protected int getFromVersion()
	{
		return 3;
	}
	
	@Override
	protected int getToVersion()
	{
		return 4;
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
