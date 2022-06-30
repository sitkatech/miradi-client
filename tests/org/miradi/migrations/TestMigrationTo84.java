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

import org.miradi.ids.BaseId;
import org.miradi.ids.DiagramFactorId;
import org.miradi.migrations.forward.MigrationTo84;
import org.miradi.objects.DiagramFactor;
import org.miradi.project.ObjectManager;
import org.miradi.project.ProjectSaverForTesting;
import org.miradi.schemas.DiagramFactorSchema;

public class TestMigrationTo84 extends AbstractTestMigration
{
    public TestMigrationTo84(String name)
    {
        super(name);
    }

    public void testFieldsRemovedAfterForwardMigration() throws Exception
    {
		BaseId nextId = getProject().getNormalIdAssigner().takeNextId();
        LegacyDiagramFactor diagramFactor = new LegacyDiagramFactor(getObjectManager(), new DiagramFactorId(nextId.asInt()));
        getProject().getPool(DiagramFactorSchema.getObjectType()).put(diagramFactor);
		getProject().fillObjectUsingCommand(diagramFactor, MigrationTo84.TAG_HEADER_HEIGHT, 9);

        String projectAsString = ProjectSaverForTesting.createSnapShot(getProject(), new VersionRange(MigrationTo84.VERSION_FROM));
        final RawProject projectToMigrate = RawProjectLoader.loadProject(projectAsString);
        migrateProject(projectToMigrate, new VersionRange(MigrationTo84.VERSION_TO));

        RawObject rawDiagramFactorAfter = projectToMigrate.findObject(diagramFactor.getRef());
        assertNotNull(rawDiagramFactorAfter);
        assertFalse("Field should have been removed during forward migration?", rawDiagramFactorAfter.containsKey(MigrationTo84.TAG_HEADER_HEIGHT));
    }

    @Override
    protected int getFromVersion()
    {
        return MigrationTo84.VERSION_FROM;
    }

    @Override
    protected int getToVersion()
    {
        return MigrationTo84.VERSION_TO;
    }

    private class LegacyDiagramFactor extends DiagramFactor
    {
        public LegacyDiagramFactor(ObjectManager objectManager, DiagramFactorId id) throws Exception
        {
            super(objectManager, id, new LegacyDiagramFactorSchema());
        }
    }

	private class LegacyDiagramFactorSchema extends DiagramFactorSchema
	{
		@Override
		protected void fillFieldSchemas()
		{
			super.fillFieldSchemas();

            createFieldSchemaInteger(MigrationTo84.TAG_HEADER_HEIGHT);
		}
	}
}
