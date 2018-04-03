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

import org.miradi.migrations.forward.MigrationTo65;
import org.miradi.migrations.forward.MigrationTo66;
import org.miradi.objectdata.BooleanData;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.AbstractThreatRatingData;
import org.miradi.objects.Factor;
import org.miradi.objects.Target;
import org.miradi.project.ProjectSaverForTesting;

public class TestMigrationTo66 extends AbstractTestMigration
{
    public TestMigrationTo66(String name)
    {
        super(name);
    }

    public void testFieldRemovedAfterReverseMigration() throws Exception
    {
        ORef targetRef = getProject().populateSimpleThreatRatingValues();
        Target target = (Target) getProject().findObject(targetRef);
        ORef threatRef = ORef.INVALID;
        for (Factor factor :target.getUpstreamDownstreamFactors())
        {
            if (factor.isDirectThreat())
            {
                threatRef = factor.getRef();
                break;
            }
        }
        assertTrue(threatRef.isValid());

        assertTrue("project is not in simple threat rating mode?", getProject().isSimpleThreatRatingMode());
        getProject().populateThreatRatingDataField(threatRef, targetRef, ObjectType.THREAT_SIMPLE_RATING_DATA, AbstractThreatRatingData.TAG_IS_THREAT_RATING_NOT_APPLICABLE, BooleanData.BOOLEAN_TRUE);

        String projectAsString = ProjectSaverForTesting.createSnapShot(getProject(), new VersionRange(getToVersion()));
        final RawProject projectToMigrate = RawProjectLoader.loadProject(projectAsString);
        migrateProject(projectToMigrate, new VersionRange(MigrationTo65.VERSION_TO));
        verifyNotApplicableFieldRemoved(projectToMigrate, ObjectType.THREAT_SIMPLE_RATING_DATA);
    }

    private void verifyNotApplicableFieldRemoved(RawProject rawProject, int objectType)
    {
        RawPool rawObjectPool = rawProject.getRawPoolForType(objectType);
        for(ORef ref : rawObjectPool.keySet())
        {
            RawObject rawObject = rawObjectPool.get(ref);
            assertFalse("Field should have been removed during reverse migration?", rawObject.containsKey(MigrationTo66.TAG_IS_THREAT_RATING_NOT_APPLICABLE));
        }
    }

    @Override
    protected int getFromVersion()
    {
        return MigrationTo66.VERSION_FROM;
    }

    @Override
    protected int getToVersion()
    {
        return MigrationTo66.VERSION_TO;
    }
}

