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

import org.miradi.migrations.forward.MigrationTo80;
import org.miradi.migrations.forward.MigrationTo81;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.Assumption;
import org.miradi.objects.SubAssumption;
import org.miradi.project.ProjectSaverForTesting;
import org.miradi.schemas.AssumptionSchema;
import org.miradi.schemas.SubAssumptionSchema;

public class TestMigrationTo81 extends AbstractTestMigration
{
    public TestMigrationTo81(String name)
    {
        super(name);
    }

    public void testFieldsRemovedAfterReverseMigration() throws Exception
    {
        Assumption assumption = getProject().createAndPopulateAssumption();
        SubAssumption subAssumption = getProject().createAndPopulateSubAssumption();

        String projectAsString = ProjectSaverForTesting.createSnapShot(getProject(), new VersionRange(getToVersion()));
        final RawProject projectToMigrate = RawProjectLoader.loadProject(projectAsString);
        migrateProject(projectToMigrate, new VersionRange(MigrationTo80.VERSION_TO));

        verifyDiagramFactorFieldsRemoved(projectToMigrate, AssumptionSchema.getObjectType());
        verifyDiagramFactorFieldsRemoved(projectToMigrate, SubAssumptionSchema.getObjectType());
    }

    private void verifyDiagramFactorFieldsRemoved(RawProject rawProject, int objectType)
    {
        RawPool rawObjectPool = rawProject.getRawPoolForType(objectType);
        for(ORef ref : rawObjectPool.keySet())
        {
            RawObject rawObject = rawObjectPool.get(ref);
            assertFalse("Field should have been removed during reverse migration?", rawObject.containsKey(MigrationTo81.TAG_DIAGRAM_FACTOR_IDS));
        }
    }

    @Override
    protected int getFromVersion()
    {
        return MigrationTo81.VERSION_FROM;
    }

    @Override
    protected int getToVersion()
    {
        return MigrationTo81.VERSION_TO;
    }
}

