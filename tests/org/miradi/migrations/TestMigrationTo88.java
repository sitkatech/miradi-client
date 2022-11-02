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

import org.miradi.migrations.forward.MigrationTo88;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.Cause;
import org.miradi.schemas.CauseSchema;

public class TestMigrationTo88 extends AbstractTestMigration
{
    public TestMigrationTo88(String name)
    {
        super(name);
    }

    public void testFieldsRemovedAfterReverseMigration() throws Exception
    {
        Cause threat = getProject().createAndPopulateThreat();

        RawProject rawProject = reverseMigrate(new VersionRange(MigrationTo88.VERSION_TO));
        RawPool rawThreatPool = rawProject.getRawPoolForType(CauseSchema.getObjectType());
        for(ORef ref : rawThreatPool.keySet())
        {
            RawObject rawThreat = rawThreatPool.get(ref);
            assertFalse("Field should have been removed during reverse migration?", rawThreat.containsKey(MigrationTo88.TAG_STANDARD_CLASSIFICATION_V20_CODE));
        }
    }

    @Override
    protected int getFromVersion()
    {
        return MigrationTo88.VERSION_FROM;
    }

    @Override
    protected int getToVersion()
    {
        return MigrationTo88.VERSION_TO;
    }
}
