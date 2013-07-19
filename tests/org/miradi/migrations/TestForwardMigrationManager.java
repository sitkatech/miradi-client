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

import java.io.IOException;

import org.martus.util.UnicodeStringReader;
import org.miradi.main.TestCaseWithProject;
import org.miradi.migrations.forward.ForwardMigrationManager;
import org.miradi.migrations.forward.MigrationTo5;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.Strategy;
import org.miradi.project.ProjectForTesting;
import org.miradi.project.ProjectLoader;
import org.miradi.project.ProjectSaverForTesting;
import org.miradi.questions.StrategyStatusQuestion;

public class TestForwardMigrationManager extends TestCaseWithProject
{
	public TestForwardMigrationManager(String name)
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
	
	protected ProjectForTesting migrateProject(final VersionRange versionRangeToUse) throws Exception, IOException
	{
		ForwardMigrationManager migrationManager = new ForwardMigrationManager();
		String projectAsString = ProjectSaverForTesting.createSnapShot(getProject(), versionRangeToUse);
		String migratedMpfFile = migrationManager.migrateForward(projectAsString);
		
		ProjectForTesting migratedProject = ProjectForTesting.createProjectWithoutDefaultObjects("MigratedProject");
		ProjectLoader.loadProject(new UnicodeStringReader(migratedMpfFile), migratedProject);
		
		return migratedProject;
	}	
}
