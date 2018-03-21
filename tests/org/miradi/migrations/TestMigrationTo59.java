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

import org.miradi.migrations.forward.MigrationTo59;
import org.miradi.objects.HumanWelfareTarget;

public class TestMigrationTo59 extends AbstractTestMigration
{
    public TestMigrationTo59(String name)
    {
        super(name);
    }

    public void testFieldsRemovedAfterReverseMigration() throws Exception
    {
        HumanWelfareTarget hwbTarget = getProject().createAndPopulateHumanWelfareTarget();

        RawProject rawProject = reverseMigrate(new VersionRange(MigrationTo59.VERSION_TO));

        RawObject rawTarget = rawProject.findObject(hwbTarget.getRef());
        assertNotNull(rawTarget);
        assertFalse("Field should have been removed during reverse migration?", rawTarget.containsKey(MigrationTo59.TAG_TARGET_FUTURE_STATUS));
        assertFalse("Field should have been removed during reverse migration?", rawTarget.containsKey(MigrationTo59.TAG_FUTURE_STATUS_JUSTIFICATION));
    }

    @Override
    protected int getFromVersion()
    {
        return MigrationTo59.VERSION_FROM;
    }

    @Override
    protected int getToVersion()
    {
        return MigrationTo59.VERSION_TO;
    }
}


