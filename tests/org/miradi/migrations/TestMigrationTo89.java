/*
Copyright 2005-2022, Foundations of Success, Bethesda, Maryland
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

import org.miradi.migrations.forward.MigrationTo89;
import org.miradi.objects.AnalyticalQuestion;
import org.miradi.objects.SubAssumption;
import org.miradi.project.Project;
import org.miradi.project.ProjectSaverForTesting;

public class TestMigrationTo89 extends AbstractTestMigration
{
    public TestMigrationTo89(String name)
    {
        super(name);
    }

    public void testAnalyticalQuestionFieldsRenamedAfterMigration() throws Exception
    {
        AnalyticalQuestion analyticalQuestion = getProject().createAndPopulateAnalyticalQuestion();
        SubAssumption subAssumption = getProject().addSubAssumption(analyticalQuestion);

        String expectedValue = analyticalQuestion.getData(MigrationTo89.TAG_SUB_ASSUMPTION_IDS);

        String projectAsString = ProjectSaverForTesting.createSnapShot(getProject(), new VersionRange(getToVersion()));
        final RawProject projectToMigrate = RawProjectLoader.loadProject(projectAsString);
        migrateProject(projectToMigrate, new VersionRange(getFromVersion()));
        assertEquals("Data should match during field rename on reverse migration?", expectedValue, projectToMigrate.getData(analyticalQuestion.getRef(), MigrationTo89.LEGACY_TAG_ASSUMPTION_IDS));

        migrateProject(projectToMigrate, new VersionRange(Project.VERSION_HIGH));
        assertEquals("Data should match during field rename on forward migration?", expectedValue, projectToMigrate.getData(analyticalQuestion.getRef(), MigrationTo89.TAG_SUB_ASSUMPTION_IDS));

        verifyFullCircleMigrations(new VersionRange(getFromVersion(), getToVersion()));
    }

    @Override
    protected int getFromVersion()
    {
        return MigrationTo89.VERSION_FROM;
    }

    @Override
    protected int getToVersion()
    {
        return MigrationTo89.VERSION_TO;
    }
}
