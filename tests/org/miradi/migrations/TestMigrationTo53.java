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

import org.miradi.migrations.forward.MigrationTo53;
import org.miradi.objects.Measurement;
import org.miradi.project.Project;

public class TestMigrationTo53 extends AbstractTestMigration
{
    public TestMigrationTo53(String name)
    {
        super(name);
    }

    public void testIndicatorFieldsRenamedAfterMigration() throws Exception
    {
        Measurement measurement = getProject().createAndPopulateMeasurement();
        String expectedValue = measurement.getData(MigrationTo53.TAG_EVIDENCE_CONFIDENCE);

        RawProject reverseMigratedProject = reverseMigrate(new VersionRange(MigrationTo53.VERSION_TO));
        assertEquals("Data should match during field rename on reverse migration?", expectedValue, reverseMigratedProject.getData(measurement.getRef(), MigrationTo53.LEGACY_TAG_STATUS_CONFIDENCE));

        migrateProject(reverseMigratedProject, new VersionRange(Project.VERSION_HIGH));
        assertEquals("Data should match during field rename on forward migration?", expectedValue, reverseMigratedProject.getData(measurement.getRef(), MigrationTo53.TAG_EVIDENCE_CONFIDENCE));

        verifyFullCircleMigrations(new VersionRange(52, 53));
    }

    @Override
    protected int getFromVersion()
    {
        return MigrationTo53.VERSION_FROM;
    }

    @Override
    protected int getToVersion()
    {
        return MigrationTo53.VERSION_TO;
    }
}
