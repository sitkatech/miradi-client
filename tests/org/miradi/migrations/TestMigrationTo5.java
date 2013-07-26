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

package org.miradi.migrations;

import org.miradi.migrations.forward.MigrationTo5;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.Strategy;
import org.miradi.project.ProjectForTesting;
import org.miradi.questions.StrategyStatusQuestion;
import org.miradi.schemas.StrategySchema;

public class TestMigrationTo5 extends AbstractTestMigration
{
	public TestMigrationTo5(String name)
	{
		super(name);
	}
	
	public void testMigrateWithoutStrategies() throws Exception
	{
		verifyMigratingStrategyStatusQuestionRealStatusChoice("", "", 0);
	}

	public void testMigrateStrategyWithoutStatusTag() throws Exception
	{
		verifyMigratingStrategyStatusQuestionRealStatusChoice("", "", 1);
	}
	
	public void testMigrateLegacyStrategyWithStatusReal() throws Exception
	{
		verifyMigratingStrategyStatusQuestionRealStatusChoice("", MigrationTo5.LEGACY_DEFAULT_STRATEGY_STATUS_REAL, 2);
	}
	
	public void testMigrateStrategyWithDraftStatus() throws Exception
	{		
		verifyMigratingStrategyStatusQuestionRealStatusChoice(StrategyStatusQuestion.STATUS_DRAFT_CODE, StrategyStatusQuestion.STATUS_DRAFT_CODE, 1);
	}
	
	private void verifyMigratingStrategyStatusQuestionRealStatusChoice(String expectedStrategyStatusCode, String strategyStatusCode, int strategyCount) throws Exception
	{
		for (int index = 0; index < strategyCount; ++index)
		{
			Strategy strategy = getProject().createStrategy();
			getProject().fillObjectUsingCommand(strategy, MigrationTo5.TAG_STATUS, strategyStatusCode);
		}

		ProjectForTesting migratedProject = migrateProject(new VersionRange(MigrationTo5.FROM_VERSION));
		ORefList migratedStrategyRefs = migratedProject.getStrategyPool().getORefList();
		assertTrue("Incorrect number of strategies after migration?", migratedStrategyRefs.size() == strategyCount);
		
		for (int index = 0; index < migratedStrategyRefs.size(); ++index)
		{
			ORef migratedStrategyRef = migratedStrategyRefs.get(index);
			Strategy migratedStrategy = Strategy.find(migratedProject, migratedStrategyRef);
			String migratedStrategyStatus = migratedStrategy.getData(Strategy.TAG_STATUS);
			assertEquals("Incorrect migrated strategy status?", expectedStrategyStatusCode, migratedStrategyStatus);
		}
	}
	
	public void testReverseMigrateWithStrategy() throws Exception
	{
		verifyReverseMigratingDefautStatusCode(MigrationTo5.LEGACY_DEFAULT_STRATEGY_STATUS_REAL, "", 1);
	}
	
	public void testReverseMigrateStrategyWithStatusReal() throws Exception
	{
		verifyReverseMigratingDefautStatusCode(MigrationTo5.LEGACY_DEFAULT_STRATEGY_STATUS_REAL, "", 2);
	}

	private void verifyReverseMigratingDefautStatusCode(String expectedStatusCode, String strategyStatusCode, int strategyCount) throws Exception
	{
		for (int index = 0; index < strategyCount; ++index)
		{
			Strategy strategy = getProject().createStrategy();
			getProject().fillObjectUsingCommand(strategy, MigrationTo5.TAG_STATUS, strategyStatusCode);
		}
		
		RawProject migratedProject = reverseMigrate(new VersionRange(MigrationTo5.TO_VERSION));
		final RawPool strategyRawPool = migratedProject.getRawPoolForType(StrategySchema.getObjectType());
		ORefList migratedStrategyRefs = strategyRawPool.getSortedReflist();
		assertTrue("Incorrect number of strategies after migration?", migratedStrategyRefs.size() == strategyCount);
		
		for (int index = 0; index < migratedStrategyRefs.size(); ++index)
		{
			ORef migratedStrategyRef = migratedStrategyRefs.get(index);
			RawObject migratedStrategy = strategyRawPool.get(migratedStrategyRef);
			String migratedStrategyStatus = migratedStrategy.get(MigrationTo5.TAG_STATUS);
			assertEquals("Incorrect migrated strategy status?", expectedStatusCode, migratedStrategyStatus);
		}
	}
}
