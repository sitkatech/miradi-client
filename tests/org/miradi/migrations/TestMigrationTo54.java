/*
Copyright 2005-2018, Foundations of Success, Bethesda, Maryland
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

import org.miradi.migrations.forward.MigrationTo54;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.BiophysicalResult;
import org.miradi.objects.ResultReport;
import org.miradi.questions.ResultReportLongStatusQuestion;
import org.miradi.utils.MiradiMultiCalendar;

public class TestMigrationTo54 extends AbstractTestMigration
{
    public TestMigrationTo54(String name)
    {
        super(name);
    }

    public void testPoolRemovedAfterReverseMigration() throws Exception
    {
        ResultReport resultReport = createLatestResultReport();
        BiophysicalResult biophysicalResult = getProject().createAndPopulateBiophysicalResult();
        getProject().addResultReport(biophysicalResult, resultReport);

        RawProject rawProject = reverseMigrate(new VersionRange(MigrationTo54.VERSION_TO));

        assertFalse("Pool should have been removed during reverse migration", rawProject.containsAnyObjectsOfType(ObjectType.RESULT_REPORT));
    }

    private ResultReport createLatestResultReport() throws Exception
    {
        MiradiMultiCalendar calendar = new MiradiMultiCalendar();
        String currentDate = calendar.toIsoDateString();
        String latestDetails = "latest result report details";

        ResultReport resultReport = getProject().createResultReport();
        getProject().populateResultReport(resultReport, currentDate, ResultReportLongStatusQuestion.ACHIEVED_CODE, latestDetails);

        return resultReport;
    }

    @Override
    protected int getFromVersion()
    {
        return MigrationTo54.VERSION_FROM;
    }

    @Override
    protected int getToVersion()
    {
        return MigrationTo54.VERSION_TO;
    }
}
