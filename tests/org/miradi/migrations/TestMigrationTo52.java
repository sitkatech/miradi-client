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

import org.miradi.migrations.forward.MigrationTo51;
import org.miradi.migrations.forward.MigrationTo52;
import org.miradi.objects.Measurement;
import org.miradi.project.ProjectSaverForTesting;
import org.miradi.questions.PrecisionTypeQuestion;

public class TestMigrationTo52 extends AbstractTestMigration
{
    public TestMigrationTo52(String name)
    {
        super(name);
    }

    public void testMeasurementSourceForwardMigration() throws Exception
    {
        Measurement measurement = getProject().createMeasurement();
        getProject().fillObjectUsingCommand(measurement, MigrationTo52.TAG_STATUS_CONFIDENCE, MigrationTo52.SAMPLING_BASED);
        getProject().fillObjectUsingCommand(measurement, Measurement.TAG_SAMPLE_PRECISION, "3");
        getProject().fillObjectUsingCommand(measurement, Measurement.TAG_SAMPLE_PRECISION_TYPE, PrecisionTypeQuestion.SD_CODE);
        getProject().fillObjectUsingCommand(measurement, Measurement.TAG_SAMPLE_SIZE, "10");

        String projectAsString = ProjectSaverForTesting.createSnapShot(getProject(), new VersionRange(MigrationTo52.VERSION_FROM));
        final RawProject projectToMigrate = RawProjectLoader.loadProject(projectAsString);
        migrateProject(projectToMigrate, new VersionRange(MigrationTo52.VERSION_TO));

        RawObject rawMeasurement = projectToMigrate.findObject(measurement.getRef());
        assertNotNull(rawMeasurement);
        assertEquals(rawMeasurement.getData(MigrationTo52.TAG_STATUS_CONFIDENCE), MigrationTo52.INTENSIVE_ASSESSMENT_CODE);
        assertEquals(rawMeasurement.getData(Measurement.TAG_SAMPLE_PRECISION), "3");
        assertEquals(rawMeasurement.getData(Measurement.TAG_SAMPLE_PRECISION_TYPE), PrecisionTypeQuestion.SD_CODE);
        assertEquals(rawMeasurement.getData(Measurement.TAG_SAMPLE_SIZE), "10");

        verifyFullCircleMigrations(new VersionRange(MigrationTo51.VERSION_TO, MigrationTo52.VERSION_TO));
    }

    public void testMeasurementSourceReverseMigration_IntensiveAssessment() throws Exception
    {
        Measurement measurement = getProject().createMeasurement();
        getProject().fillObjectUsingCommand(measurement, MigrationTo52.TAG_STATUS_CONFIDENCE, MigrationTo52.INTENSIVE_ASSESSMENT_CODE);

        String projectAsString = ProjectSaverForTesting.createSnapShot(getProject(), new VersionRange(MigrationTo51.VERSION_TO));
        final RawProject projectToMigrate = RawProjectLoader.loadProject(projectAsString);

        migrateProject(projectToMigrate, new VersionRange(MigrationTo52.VERSION_TO));

        RawObject rawMeasurement = projectToMigrate.findObject(measurement.getRef());
        assertNotNull(rawMeasurement);
        assertEquals(rawMeasurement.getData(MigrationTo52.TAG_STATUS_CONFIDENCE), MigrationTo52.INTENSIVE_ASSESSMENT_CODE);
    }

    public void testMeasurementSourceReverseMigration_SamplingBased() throws Exception
    {
        Measurement measurement = getProject().createAndPopulateMeasurement();
        getProject().fillObjectUsingCommand(measurement, MigrationTo52.TAG_STATUS_CONFIDENCE, MigrationTo52.INTENSIVE_ASSESSMENT_CODE);
        getProject().fillObjectUsingCommand(measurement, Measurement.TAG_SAMPLE_PRECISION, "3");
        getProject().fillObjectUsingCommand(measurement, Measurement.TAG_SAMPLE_PRECISION_TYPE, PrecisionTypeQuestion.SD_CODE);
        getProject().fillObjectUsingCommand(measurement, Measurement.TAG_SAMPLE_SIZE, "10");

        String projectAsString = ProjectSaverForTesting.createSnapShot(getProject(), new VersionRange(MigrationTo51.VERSION_TO));
        final RawProject projectToMigrate = RawProjectLoader.loadProject(projectAsString);

        migrateProject(projectToMigrate, new VersionRange(MigrationTo52.VERSION_TO));

        RawObject rawMeasurement = projectToMigrate.findObject(measurement.getRef());
        assertNotNull(rawMeasurement);
        assertEquals(rawMeasurement.getData(MigrationTo52.TAG_STATUS_CONFIDENCE), MigrationTo52.INTENSIVE_ASSESSMENT_CODE);
    }

    @Override
    protected int getFromVersion()
    {
        return MigrationTo52.VERSION_FROM;
    }

    @Override
    protected int getToVersion()
    {
        return MigrationTo52.VERSION_TO;
    }
}
