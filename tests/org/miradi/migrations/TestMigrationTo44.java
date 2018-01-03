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

import org.miradi.migrations.forward.MigrationTo43;
import org.miradi.migrations.forward.MigrationTo44;
import org.miradi.objects.ProjectMetadata;
import org.miradi.project.Project;
import org.miradi.project.ProjectSaverForTesting;
import org.miradi.questions.FactorModeQuestion;
import org.miradi.utils.StringUtilities;

public class TestMigrationTo44 extends AbstractTestMigration
{
    public TestMigrationTo44(String name)
    {
        super(name);
    }

    // old default --> new contributing factor code
    public void testFieldDefaultCodeUpdatedAfterForwardMigration() throws Exception
    {
        ProjectMetadata metadata = getProject().getMetadata();
        getProject().fillObjectUsingCommand(metadata, MigrationTo44.TAG_FACTOR_MODE, "");

        String projectAsString = ProjectSaverForTesting.createSnapShot(getProject(), new VersionRange(MigrationTo43.VERSION_TO));
        final RawProject projectToMigrate = RawProjectLoader.loadProject(projectAsString);
        migrateProject(projectToMigrate, new VersionRange(Project.VERSION_HIGH));

        RawObject rawMetadata = projectToMigrate.findObject(metadata.getRef());
        assertNotNull(rawMetadata);
        assertEquals(rawMetadata.getData(MigrationTo44.TAG_FACTOR_MODE), FactorModeQuestion.CONTRIBUTING_FACTOR_CODE);

        verifyFullCircleMigrations(new VersionRange(MigrationTo43.VERSION_TO, MigrationTo44.VERSION_TO));
    }

    // old biophysical factor code --> new default (empty string)
    public void testFieldBiophysicalFactorCodeUpdatedAfterForwardMigration() throws Exception
    {
        ProjectMetadata metadata = getProject().getMetadata();
        getProject().fillObjectUsingCommand(metadata, MigrationTo44.TAG_FACTOR_MODE, MigrationTo44.BIOPHYSICAL_FACTOR_CODE);

        String projectAsString = ProjectSaverForTesting.createSnapShot(getProject(), new VersionRange(MigrationTo43.VERSION_TO));
        final RawProject projectToMigrate = RawProjectLoader.loadProject(projectAsString);
        migrateProject(projectToMigrate, new VersionRange(Project.VERSION_HIGH));

        RawObject rawMetadata = projectToMigrate.findObject(metadata.getRef());
        assertNotNull(rawMetadata);
        assertTrue(StringUtilities.isNullOrEmpty(rawMetadata.getData(MigrationTo44.TAG_FACTOR_MODE)));

        verifyFullCircleMigrations(new VersionRange(MigrationTo43.VERSION_TO, MigrationTo44.VERSION_TO));
    }

    // new default (empty string) --> old biophysical factor code
    public void testFieldDefaultCodeUpdatedAfterReverseMigration() throws Exception
    {
        ProjectMetadata metadata = getProject().getMetadata();
        getProject().fillObjectUsingCommand(metadata, MigrationTo44.TAG_FACTOR_MODE, FactorModeQuestion.DEFAULT_CODE);

        String projectAsString = ProjectSaverForTesting.createSnapShot(getProject(), new VersionRange(getToVersion()));
        final RawProject projectToMigrate = RawProjectLoader.loadProject(projectAsString);
        migrateProject(projectToMigrate, new VersionRange(MigrationTo43.VERSION_TO));

        RawObject rawMetadata = projectToMigrate.findObject(metadata.getRef());
        assertNotNull(rawMetadata);
        assertEquals(rawMetadata.getData(MigrationTo44.TAG_FACTOR_MODE), MigrationTo44.BIOPHYSICAL_FACTOR_CODE);
    }

    // new contributing factor code --> old default (empty string)
    public void testFieldContributingFactorModeUpdatedAfterReverseMigration() throws Exception
    {
        ProjectMetadata metadata = getProject().getMetadata();
        getProject().fillObjectUsingCommand(metadata, MigrationTo44.TAG_FACTOR_MODE, FactorModeQuestion.CONTRIBUTING_FACTOR_CODE);

        String projectAsString = ProjectSaverForTesting.createSnapShot(getProject(), new VersionRange(getToVersion()));
        final RawProject projectToMigrate = RawProjectLoader.loadProject(projectAsString);
        migrateProject(projectToMigrate, new VersionRange(MigrationTo43.VERSION_TO));

        RawObject rawMetadata = projectToMigrate.findObject(metadata.getRef());
        assertNotNull(rawMetadata);
        assertTrue(StringUtilities.isNullOrEmpty(rawMetadata.getData(MigrationTo44.TAG_FACTOR_MODE)));
    }

    @Override
    protected int getFromVersion()
    {
        return MigrationTo44.VERSION_FROM;
    }

    @Override
    protected int getToVersion()
    {
        return MigrationTo44.VERSION_TO;
    }
}
