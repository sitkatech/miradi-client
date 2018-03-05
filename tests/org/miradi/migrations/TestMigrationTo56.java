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

import org.miradi.migrations.forward.MigrationTo56;
import org.miradi.objects.ThreatReductionResult;

public class TestMigrationTo56 extends AbstractTestMigration
{
    public TestMigrationTo56(String name)
    {
        super(name);
    }

    public void testFieldsRemovedAfterReverseMigration() throws Exception
    {
        ThreatReductionResult threatReductionResult = getProject().createAndPopulateThreatReductionResult();

        RawProject rawProject = reverseMigrate(new VersionRange(MigrationTo56.VERSION_TO));

        RawObject rawThreatReductionResult = rawProject.findObject(threatReductionResult.getRef());
        assertNotNull(rawThreatReductionResult);
        assertFalse("Field should have been removed during reverse migration?", rawThreatReductionResult.containsKey(MigrationTo56.TAG_RESULT_REPORT_REFS));
    }

    @Override
    protected int getFromVersion()
    {
        return MigrationTo56.VERSION_FROM;
    }

    @Override
    protected int getToVersion()
    {
        return MigrationTo56.VERSION_TO;
    }
}

