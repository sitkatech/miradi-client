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

import org.miradi.migrations.forward.MigrationTo79;
import org.miradi.migrations.forward.MigrationTo80;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.AnalyticalQuestion;
import org.miradi.objects.Assumption;
import org.miradi.project.ProjectSaverForTesting;
import org.miradi.schemas.AnalyticalQuestionSchema;
import org.miradi.schemas.AssumptionSchema;

public class TestMigrationTo80 extends AbstractTestMigration
{
    public TestMigrationTo80(String name)
    {
        super(name);
    }

    public void testFieldsRemovedAfterReverseMigration() throws Exception
    {
        AnalyticalQuestion analyticalQuestion = getProject().createAndPopulateAnalyticalQuestion();
        Assumption assumption = getProject().createAndPopulateAssumption();

        String projectAsString = ProjectSaverForTesting.createSnapShot(getProject(), new VersionRange(getToVersion()));
        final RawProject projectToMigrate = RawProjectLoader.loadProject(projectAsString);
        migrateProject(projectToMigrate, new VersionRange(MigrationTo79.VERSION_TO));

        verifyEvidenceFieldsRemoved(projectToMigrate, AnalyticalQuestionSchema.getObjectType());
        verifyEvidenceFieldsRemoved(projectToMigrate, AssumptionSchema.getObjectType());
    }

    private void verifyEvidenceFieldsRemoved(RawProject rawProject, int objectType)
    {
        RawPool rawObjectPool = rawProject.getRawPoolForType(objectType);
        for(ORef ref : rawObjectPool.keySet())
        {
            RawObject rawObject = rawObjectPool.get(ref);
            assertFalse("Field should have been removed during reverse migration?", rawObject.containsKey(MigrationTo80.TAG_EVIDENCE_NOTES));
            assertFalse("Field should have been removed during reverse migration?", rawObject.containsKey(MigrationTo80.TAG_EVIDENCE_CONFIDENCE));
        }
    }

    @Override
    protected int getFromVersion()
    {
        return MigrationTo80.VERSION_FROM;
    }

    @Override
    protected int getToVersion()
    {
        return MigrationTo80.VERSION_TO;
    }
}

