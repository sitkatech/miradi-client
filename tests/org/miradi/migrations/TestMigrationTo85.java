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

import org.miradi.migrations.forward.MigrationTo84;
import org.miradi.migrations.forward.MigrationTo85;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.DiagramFactor;
import org.miradi.project.ProjectSaverForTesting;
import org.miradi.questions.TextBoxZOrderQuestion;
import org.miradi.schemas.DiagramFactorSchema;
import org.miradi.schemas.DiagramLinkSchema;

public class TestMigrationTo85 extends AbstractTestMigration
{
    public TestMigrationTo85(String name)
    {
        super(name);
    }

    public void testFieldsRemovedAfterForwardMigration() throws Exception
    {
        DiagramFactor diagramFactor = getProject().createAndPopulateDiagramFactor();
        getProject().fillObjectUsingCommand(diagramFactor, MigrationTo85.TAG_TEXT_BOX_Z_ORDER_CODE, TextBoxZOrderQuestion.DEFAULT_Z_ORDER);

        String projectAsString = ProjectSaverForTesting.createSnapShot(getProject(), new VersionRange(MigrationTo85.VERSION_FROM));
        final RawProject projectToMigrate = RawProjectLoader.loadProject(projectAsString);
        migrateProject(projectToMigrate, new VersionRange(MigrationTo85.VERSION_TO));

        RawObject rawDiagramFactorAfter = projectToMigrate.findObject(diagramFactor.getRef());
        assertNotNull(rawDiagramFactorAfter);
        assertFalse("Field should have been removed during forward migration?", rawDiagramFactorAfter.containsKey(MigrationTo85.TAG_TEXT_BOX_Z_ORDER_CODE));
    }

    public void testFieldsRemovedAfterReverseMigration() throws Exception
    {
        DiagramFactor diagramFactorFrom = getProject().createAndPopulateDiagramFactor();
        DiagramFactor diagramFactorTo = getProject().createAndPopulateDiagramFactor();
        ORef diagramLink = getProject().createDiagramLink(diagramFactorFrom, diagramFactorTo);

        String projectAsString = ProjectSaverForTesting.createSnapShot(getProject(), new VersionRange(getToVersion()));
        final RawProject projectToMigrate = RawProjectLoader.loadProject(projectAsString);
        migrateProject(projectToMigrate, new VersionRange(MigrationTo84.VERSION_TO));

        verifyDiagramFactorFieldsRemoved(projectToMigrate, DiagramFactorSchema.getObjectType());
        verifyDiagramFactorFieldsRemoved(projectToMigrate, DiagramLinkSchema.getObjectType());
    }

    private void verifyDiagramFactorFieldsRemoved(RawProject rawProject, int objectType)
    {
        RawPool rawObjectPool = rawProject.getRawPoolForType(objectType);
        for(ORef ref : rawObjectPool.keySet())
        {
            RawObject rawObject = rawObjectPool.get(ref);
            assertFalse("Field should have been removed during reverse migration?", rawObject.containsKey(MigrationTo85.TAG_Z_INDEX));
        }
    }

    @Override
    protected int getFromVersion()
    {
        return MigrationTo85.VERSION_FROM;
    }

    @Override
    protected int getToVersion()
    {
        return MigrationTo85.VERSION_TO;
    }
}
