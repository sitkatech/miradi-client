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

import org.miradi.migrations.forward.MigrationTo58;
import org.miradi.objects.Target;

public class TestMigrationTo58 extends AbstractTestMigration
{
    public TestMigrationTo58(String name)
    {
        super(name);
    }

    public void testFieldsRemovedAfterReverseMigration() throws Exception
    {
        Target target = getProject().createAndPopulateTarget();

        RawProject rawProject = reverseMigrate(new VersionRange(MigrationTo58.VERSION_TO));

        RawObject rawTarget = rawProject.findObject(target.getRef());
        assertNotNull(rawTarget);
        assertFalse("Field should have been removed during reverse migration?", rawTarget.containsKey(MigrationTo58.TAG_TARGET_FUTURE_STATUS));
        assertFalse("Field should have been removed during reverse migration?", rawTarget.containsKey(MigrationTo58.TAG_FUTURE_STATUS_JUSTIFICATION));
    }

    @Override
    protected int getFromVersion()
    {
        return MigrationTo58.VERSION_FROM;
    }

    @Override
    protected int getToVersion()
    {
        return MigrationTo58.VERSION_TO;
    }
}


