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
import java.util.Set;

import org.miradi.migrations.forward.MigrationManager;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.FutureStatus;
import org.miradi.objects.Indicator;
import org.miradi.project.ProjectSaverForTesting;
import org.miradi.schemas.FutureStatusSchema;
import org.miradi.schemas.IndicatorSchema;

public class TestReverseMigrationTo4 extends AbstractTestReverseMigration
{
	public TestReverseMigrationTo4(String name)
	{
		super(name);
	}
	
	public void testMigrationTo4WithoutFutureStatus() throws Exception
	{
		verifyFutureStatusDataTransferToIndicator(0);
	}
	
	public void testMigrationTo4WithOneFutureStatus() throws Exception
	{
		verifyFutureStatusDataTransferToIndicator(1);
	}
	
	public void testMigrationTo4WithTwoFutureStatuses() throws Exception
	{
		verifyFutureStatusDataTransferToIndicator(2);
	}

	private void verifyFutureStatusDataTransferToIndicator(int futureStatusCount) throws Exception
	{
		Indicator indicator = getProject().createIndicator(getProject().createCause());
		createFutureStatuses(indicator, futureStatusCount);
		RawProject reverseMigratedProject = reverseMigrateProject();
		assertEquals("Future statuses should have been deleted?", null, reverseMigratedProject.getRawPoolForType(FutureStatusSchema.getObjectType()));

		ORefList migratedIndicatorRefs = reverseMigratedProject.getRawPoolForType(IndicatorSchema.getObjectType()).getSortedReflist();
		assertTrue("Incorrect indicator count after migration?", migratedIndicatorRefs.size() == 1);

		RawObject migratedIndicator = reverseMigratedProject.findObject(migratedIndicatorRefs.getFirstElement());
		IndicatorFutureStatusTagsToFutureStatusTagsMap map = new IndicatorFutureStatusTagsToFutureStatusTagsMap();
		Set<String> indicatorFutureStatusTags = map.getIndicatorFutureStatusTags();
		ORef latestFutureStatusRef = indicator.getLatestFutureStatusRef();
		if (latestFutureStatusRef.isInvalid())
			return;
		
		FutureStatus latestFutureStatus = FutureStatus.find(getProject(), latestFutureStatusRef);
		for(String indicatorFutureStatusTag : indicatorFutureStatusTags)
		{
			String futureStatusTag = map.get(indicatorFutureStatusTag);
			String dataFromLatestFutureStatus = latestFutureStatus.getData(futureStatusTag);
			String dataFromMigratedIndicator = migratedIndicator.get(indicatorFutureStatusTag);
			assertEquals("Future status data was not correctly migrated to indicator?", dataFromMigratedIndicator, dataFromLatestFutureStatus);
		}
	}

	private void createFutureStatuses(Indicator indicator, int futureStatusCount) throws Exception
	{
		for (int index = 0; index < futureStatusCount; ++index)
		{
			FutureStatus futureStatus = getProject().createAndPopulateFutureStatus(indicator);
			getProject().fillObjectUsingCommand(futureStatus, FutureStatusSchema.TAG_FUTURE_STATUS_DATE, "2007-03-" + index + 1);
		}
	}
	
	private RawProject reverseMigrateProject() throws Exception, IOException
	{
		MigrationManager migrationManager = new MigrationManager();
		String projectAsString = ProjectSaverForTesting.createSnapShot(getProject(), new VersionRange(4, 4));
		String migratedMpfFile = migrationManager.migrateReverse(projectAsString);
	
		return RawProjectLoader.loadProject(migratedMpfFile);
	}
}
