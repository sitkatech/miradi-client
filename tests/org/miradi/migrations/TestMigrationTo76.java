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

import org.miradi.migrations.forward.MigrationTo76;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.AnalyticalQuestion;

public class TestMigrationTo76 extends AbstractTestMigration
{
    public TestMigrationTo76(String name)
    {
        super(name);
    }

    public void testPoolRemovedAfterReverseMigration() throws Exception
    {
        AnalyticalQuestion analyticalQuestion = getProject().createAndPopulateAnalyticalQuestion();

        RawProject rawProject = reverseMigrate(new VersionRange(MigrationTo76.VERSION_TO));

        assertFalse("Pool should have been removed during reverse migration", rawProject.containsAnyObjectsOfType(ObjectType.ANALYTICAL_QUESTION));
    }

    @Override
    protected int getFromVersion()
    {
        return MigrationTo76.VERSION_FROM;
    }

    @Override
    protected int getToVersion()
    {
        return MigrationTo76.VERSION_TO;
    }
}