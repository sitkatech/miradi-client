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

import org.miradi.migrations.forward.MigrationTo67;
import org.miradi.migrations.forward.MigrationTo68;
import org.miradi.objects.AbstractProgressReport;
import org.miradi.objects.ExtendedProgressReport;
import org.miradi.objects.ProgressReport;
import org.miradi.project.ProjectSaverForTesting;
import org.miradi.questions.ProgressReportLongStatusQuestion;

public class TestMigrationTo68 extends AbstractTestMigration
{
    public TestMigrationTo68(String name)
    {
        super(name);
    }

    public void testProgressReportReverseMigration_NotKnown() throws Exception
    {
        ProgressReport progressReport = getProject().createProgressReport();
        getProject().populateProgressReport(progressReport, "2008-01-23", ProgressReportLongStatusQuestion.NOT_KNOWN_CODE, "some progress report details");

        verifyReverseMigration_NotKnown(progressReport, MigrationTo68.NOT_SPECIFIED);
    }

    public void testProgressReportReverseMigration_Planned() throws Exception
    {
        ProgressReport progressReport = getProject().createProgressReport();
        getProject().populateProgressReport(progressReport, "2008-01-23", ProgressReportLongStatusQuestion.PLANNED_CODE, "some progress report details");

        verifyReverseMigration_NotKnown(progressReport, ProgressReportLongStatusQuestion.PLANNED_CODE);
    }

    public void testExtendedProgressReportReverseMigration_NotKnown() throws Exception
    {
        ExtendedProgressReport progressReport = getProject().createExtendedProgressReport();
        getProject().populateExtendedProgressReport(progressReport, "2008-01-23", ProgressReportLongStatusQuestion.NOT_KNOWN_CODE, "some progress report details", "some progress report next steps", "some progress report lessons learned");

        verifyReverseMigration_NotKnown(progressReport, MigrationTo68.NOT_SPECIFIED);
    }

    public void testExtendedProgressReportReverseMigration_Planned() throws Exception
    {
        ExtendedProgressReport progressReport = getProject().createExtendedProgressReport();
        getProject().populateExtendedProgressReport(progressReport, "2008-01-23", ProgressReportLongStatusQuestion.PLANNED_CODE, "some progress report details", "some progress report next steps", "some progress report lessons learned");

        verifyReverseMigration_NotKnown(progressReport, ProgressReportLongStatusQuestion.PLANNED_CODE);
    }

    private void verifyReverseMigration_NotKnown(AbstractProgressReport progressReport, String expectedProgressStatusCode) throws Exception
    {
        String projectAsString = ProjectSaverForTesting.createSnapShot(getProject(), new VersionRange(getToVersion()));
        final RawProject projectToMigrate = RawProjectLoader.loadProject(projectAsString);
        migrateProject(projectToMigrate, new VersionRange(MigrationTo67.VERSION_TO));

        RawObject rawProgressReport = projectToMigrate.findObject(progressReport.getRef());
        assertNotNull(rawProgressReport);
        assertEquals(rawProgressReport.getData(MigrationTo68.TAG_PROGRESS_STATUS), expectedProgressStatusCode);
    }

    @Override
    protected int getFromVersion()
    {
        return MigrationTo68.VERSION_FROM;
    }

    @Override
    protected int getToVersion()
    {
        return MigrationTo68.VERSION_TO;
    }
}
