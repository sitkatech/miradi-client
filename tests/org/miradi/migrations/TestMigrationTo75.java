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

import org.miradi.migrations.forward.MigrationTo75;
import org.miradi.objects.Strategy;
import org.miradi.objects.Task;

public class TestMigrationTo75 extends AbstractTestMigration
{
    public TestMigrationTo75(String name)
    {
        super(name);
    }

    public void testFieldsRemovedAfterReverseMigration() throws Exception
    {
        Strategy strategy = getProject().createAndPopulateStrategy();
        Task task = getProject().createAndPopulateTask(strategy, "Some activity Label");

        RawProject rawProject = reverseMigrate(new VersionRange(MigrationTo75.VERSION_TO));

        RawObject rawStrategy = rawProject.findObject(strategy.getRef());
        assertNotNull(rawStrategy);
        assertFalse("Field should have been removed during reverse migration?", rawStrategy.containsKey(MigrationTo75.TAG_OUTPUT_REFS));

        RawObject rawTask = rawProject.findObject(task.getRef());
        assertNotNull(rawTask);
        assertFalse("Field should have been removed during reverse migration?", rawTask.containsKey(MigrationTo75.TAG_OUTPUT_REFS));
    }

    @Override
    protected int getFromVersion()
    {
        return MigrationTo75.VERSION_FROM;
    }

    @Override
    protected int getToVersion()
    {
        return MigrationTo75.VERSION_TO;
    }
}

