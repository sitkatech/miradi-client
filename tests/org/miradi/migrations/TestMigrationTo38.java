/* 
Copyright 2005-2015, Foundations of Success, Bethesda, Maryland
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

import org.miradi.migrations.forward.MigrationTo38;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.ExtendedProgressReport;
import org.miradi.objects.ProjectMetadata;
import org.miradi.project.Project;
import org.miradi.questions.ProgressReportLongStatusQuestion;
import org.miradi.utils.MiradiMultiCalendar;


public class TestMigrationTo38 extends AbstractTestMigration
{
	public TestMigrationTo38(String name)
	{
		super(name);
	}

	public void testProjectProgressReportsCreatedByForwardMigration() throws Exception
	{
		ProjectMetadata metadata = getProject().getMetadata();

		String latestDetails = "latest progress report details";
		String lessonsLearned = "latest progress report lessons learned";
		String nextSteps = "latest progress report next steps";
		getProject().fillObjectUsingCommand(metadata, MigrationTo38.TAG_PROJECT_STATUS, latestDetails);
		getProject().fillObjectUsingCommand(metadata, MigrationTo38.TAG_TNC_LESSONS_LEARNED, lessonsLearned);
		getProject().fillObjectUsingCommand(metadata, MigrationTo38.TAG_NEXT_STEPS, nextSteps);

		RawProject reverseMigratedProject = reverseMigrate(new VersionRange(MigrationTo38.VERSION_TO));
		migrateProject(reverseMigratedProject, new VersionRange(Project.VERSION_HIGH));

		RawObject rawMetadata = reverseMigratedProject.findObject(metadata.getRef());
		assertNotNull(rawMetadata);
		assertEquals(rawMetadata.getData(MigrationTo38.TAG_PROJECT_STATUS), "");
		assertEquals(rawMetadata.getData(MigrationTo38.TAG_TNC_LESSONS_LEARNED), "");
		assertEquals(rawMetadata.getData(MigrationTo38.TAG_NEXT_STEPS), "");

		String projectProgressReportRefsAsString = rawMetadata.getData(MigrationTo38.TAG_EXTENDED_PROGRESS_REPORT_REFS);
		assertNotNull(projectProgressReportRefsAsString);

		ORefList progressReportRefList = new ORefList(projectProgressReportRefsAsString);
		assertEquals(progressReportRefList.size(), 1);

		RawObject progressReport = reverseMigratedProject.findObject(progressReportRefList.get(0));
		assertEquals(progressReport.getData(MigrationTo38.TAG_DETAILS), latestDetails);
		assertEquals(progressReport.getData(MigrationTo38.TAG_LESSONS_LEARNED), lessonsLearned);
		assertEquals(progressReport.getData(MigrationTo38.TAG_NEXT_STEPS), nextSteps);

		verifyFullCircleMigrations(new VersionRange(37, 38));
	}

	public void testProjectProgressReportsRemovedByReverseMigration() throws Exception
	{
		ProjectMetadata metadata = getProject().getMetadata();

		ExtendedProgressReport progressReport = createLatestProgressReport();
		getProject().addExtendedProgressReport(metadata, progressReport);

		assertEquals(metadata.getData(MigrationTo38.TAG_PROJECT_STATUS), "");
		assertEquals(metadata.getData(MigrationTo38.TAG_TNC_LESSONS_LEARNED), "");
		assertEquals(metadata.getData(MigrationTo38.TAG_NEXT_STEPS), "");

		RawProject rawProject = reverseMigrate(new VersionRange(MigrationTo38.VERSION_TO));

		RawObject rawMetadata = rawProject.findObject(metadata.getRef());
		assertNotNull(rawMetadata);
		assertFalse("Field should have been removed during reverse migration?", rawMetadata.containsKey(MigrationTo38.TAG_EXTENDED_PROGRESS_REPORT_REFS));

		assertTrue("Field should have been added during reverse migration?", rawMetadata.containsKey(MigrationTo38.TAG_PROJECT_STATUS));
		assertEquals(rawMetadata.getData(MigrationTo38.TAG_PROJECT_STATUS), progressReport.getDetails());
		assertTrue("Field should have been added during reverse migration?", rawMetadata.containsKey(MigrationTo38.TAG_TNC_LESSONS_LEARNED));
		assertEquals(rawMetadata.getData(MigrationTo38.TAG_TNC_LESSONS_LEARNED), progressReport.getLessonsLearned());
		assertTrue("Field should have been added during reverse migration?", rawMetadata.containsKey(MigrationTo38.TAG_NEXT_STEPS));
		assertEquals(rawMetadata.getData(MigrationTo38.TAG_NEXT_STEPS), progressReport.getNextSteps());
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
		return MigrationTo38.VERSION_FROM;
	}
	
	@Override
	protected int getToVersion()
	{
		return MigrationTo38.VERSION_TO;
	}
}
