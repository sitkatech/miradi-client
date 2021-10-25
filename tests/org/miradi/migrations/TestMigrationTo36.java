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

import org.miradi.migrations.forward.MigrationTo36;
import org.miradi.migrations.forward.MigrationTo39;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.ExtendedProgressReport;
import org.miradi.objects.ProjectMetadata;
import org.miradi.questions.ProgressReportLongStatusQuestion;
import org.miradi.utils.MiradiMultiCalendar;

public class TestMigrationTo36 extends AbstractTestMigration
{
	public TestMigrationTo36(String name)
	{
		super(name);
	}
	
	public void testPoolRemovedAfterReverseMigration() throws Exception
	{
		ProjectMetadata metadata = getProject().getMetadata();

		ExtendedProgressReport progressReport = createLatestProgressReport();
		getProject().addExtendedProgressReport(metadata, progressReport);

		assertEquals(metadata.getData(MigrationTo39.TAG_PROJECT_STATUS), "");
		assertEquals(metadata.getData(MigrationTo39.TAG_TNC_LESSONS_LEARNED), "");
		assertEquals(metadata.getData(MigrationTo39.TAG_NEXT_STEPS), "");

		RawProject rawProject = reverseMigrate(new VersionRange(MigrationTo36.VERSION_TO));

		assertFalse("Pool should have been removed during reverse migration", rawProject.containsAnyObjectsOfType(ObjectType.EXTENDED_PROGRESS_REPORT));
	}

	private ExtendedProgressReport createLatestProgressReport() throws Exception
	{
		MiradiMultiCalendar calendar = new MiradiMultiCalendar();
		String currentDate = calendar.toIsoDateString();
		String latestDetails = "latest progress report details";
		String latestNextSteps = "latest progress report next steps";
		String latestLessonsLearned = "latest progress report lessons learned";

		ExtendedProgressReport progressReport = getProject().createExtendedProgressReport();
		getProject().populateExtendedProgressReport(progressReport, currentDate, ProgressReportLongStatusQuestion.COMPLETED_CODE, latestDetails, latestNextSteps, latestLessonsLearned);

		return progressReport;
	}

	@Override
	protected int getFromVersion()
	{
		return MigrationTo36.VERSION_FROM;
	}
	
	@Override
	protected int getToVersion()
	{
		return MigrationTo36.VERSION_TO;
	}
}
