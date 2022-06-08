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

import org.miradi.migrations.forward.MigrationTo83;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.Strategy;
import org.miradi.schemas.StrategySchema;

public class TestMigrationTo83 extends AbstractTestMigration
{
    public TestMigrationTo83(String name)
    {
        super(name);
    }

    public void testFieldsRemovedAfterReverseMigration() throws Exception
    {
        Strategy strategy = getProject().createAndPopulateStrategy();

        RawProject rawProject = reverseMigrate(new VersionRange(MigrationTo83.VERSION_TO));
        RawPool rawStrategyPool = rawProject.getRawPoolForType(StrategySchema.getObjectType());
        for(ORef ref : rawStrategyPool.keySet())
        {
            RawObject rawStrategy = rawStrategyPool.get(ref);
            assertFalse("Field should have been removed during reverse migration?", rawStrategy.containsKey(MigrationTo83.TAG_STANDARD_CLASSIFICATION_V20_CODE));
        }
    }

    @Override
    protected int getFromVersion()
    {
        return MigrationTo83.VERSION_FROM;
    }

    @Override
    protected int getToVersion()
    {
        return MigrationTo83.VERSION_TO;
    }
}
