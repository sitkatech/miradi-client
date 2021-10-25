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

import org.miradi.migrations.forward.MigrationTo68;
import org.miradi.migrations.forward.MigrationTo69;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.DiagramFactor;
import org.miradi.project.ProjectSaverForTesting;

public class TestMigrationTo69 extends AbstractTestMigration
{
    public TestMigrationTo69(String name)
    {
        super(name);
    }

    public void testDiagramFactorColorCodeForwardMigration() throws Exception
    {
        DiagramFactor diagramFactor = getProject().createDiagramFactorAndAddToDiagram(ObjectType.TEXT_BOX);
        getProject().fillObjectUsingCommand(diagramFactor, DiagramFactor.TAG_FOREGROUND_COLOR, MigrationTo69.LEGACY_RED_HEX);

        String projectAsString = ProjectSaverForTesting.createSnapShot(getProject(), new VersionRange(MigrationTo69.VERSION_FROM));
        final RawProject projectToMigrate = RawProjectLoader.loadProject(projectAsString);
        migrateProject(projectToMigrate, new VersionRange(MigrationTo69.VERSION_TO));

        RawObject rawDiagramFactor = projectToMigrate.findObject(diagramFactor.getRef());
        assertNotNull(rawDiagramFactor);
        assertEquals(rawDiagramFactor.getData(MigrationTo69.TAG_FOREGROUND_COLOR), MigrationTo69.RED_HEX);

        verifyFullCircleMigrations(new VersionRange(MigrationTo68.VERSION_TO, MigrationTo69.VERSION_TO));
    }

    public void testDiagramFactorColorCodeReverseMigration() throws Exception
    {
        DiagramFactor diagramFactor = getProject().createDiagramFactorAndAddToDiagram(ObjectType.TEXT_BOX);
        getProject().fillObjectUsingCommand(diagramFactor, DiagramFactor.TAG_FOREGROUND_COLOR, MigrationTo69.RED_HEX);

        String projectAsString = ProjectSaverForTesting.createSnapShot(getProject(), new VersionRange(getToVersion()));
        final RawProject projectToMigrate = RawProjectLoader.loadProject(projectAsString);
        migrateProject(projectToMigrate, new VersionRange(MigrationTo68.VERSION_TO));

        RawObject rawDiagramFactor = projectToMigrate.findObject(diagramFactor.getRef());
        assertNotNull(rawDiagramFactor);
        assertEquals(rawDiagramFactor.getData(MigrationTo69.TAG_FOREGROUND_COLOR), MigrationTo69.LEGACY_RED_HEX);
    }

    @Override
    protected int getFromVersion()
    {
        return MigrationTo69.VERSION_FROM;
    }

    @Override
    protected int getToVersion()
    {
        return MigrationTo69.VERSION_TO;
    }
}
