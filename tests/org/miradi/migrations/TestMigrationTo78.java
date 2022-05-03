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

import org.miradi.migrations.forward.MigrationTo77;
import org.miradi.migrations.forward.MigrationTo78;
import org.miradi.objects.Output;
import org.miradi.project.ProjectSaverForTesting;

public class TestMigrationTo78 extends AbstractTestMigration
{
    public TestMigrationTo78(String name)
    {
        super(name);
    }

    public void testFieldsRemovedAfterReverseMigration() throws Exception
    {
        Output output = getProject().createAndPopulateOutput();

        String projectAsString = ProjectSaverForTesting.createSnapShot(getProject(), new VersionRange(getToVersion()));
        final RawProject projectToMigrate = RawProjectLoader.loadProject(projectAsString);
        migrateProject(projectToMigrate, new VersionRange(MigrationTo77.VERSION_TO));

        RawObject rawOutput = projectToMigrate.findObject(output.getRef());
        assertNotNull(rawOutput);
        assertFalse("Field should have been removed during reverse migration?", rawOutput.containsKey(MigrationTo78.TAG_GOAL_IDS));
        assertFalse("Field should have been removed during reverse migration?", rawOutput.containsKey(MigrationTo78.TAG_OBJECTIVE_IDS));
        assertFalse("Field should have been removed during reverse migration?", rawOutput.containsKey(MigrationTo78.TAG_INDICATOR_IDS));
    }

    @Override
    protected int getFromVersion()
    {
        return MigrationTo78.VERSION_FROM;
    }

    @Override
    protected int getToVersion()
    {
        return MigrationTo78.VERSION_TO;
    }
}

