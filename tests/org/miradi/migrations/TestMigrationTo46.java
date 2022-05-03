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

import org.miradi.migrations.forward.MigrationTo46;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.project.Project;

public class TestMigrationTo46 extends AbstractTestMigration
{
    public TestMigrationTo46(String name)
    {
        super(name);
    }

    public void testFieldsAddedAfterForwardMigration() throws Exception
    {
        getProject().populateEverything();

        RawProject rawProject = reverseMigrate(new VersionRange(MigrationTo46.VERSION_TO));
        migrateProject(rawProject, new VersionRange(Project.VERSION_HIGH));

        for (int objectType = ObjectType.FIRST_OBJECT_TYPE; objectType < ObjectType.OBJECT_TYPE_COUNT; ++objectType)
        {
            if (!rawProject.containsAnyObjectsOfType(objectType))
                continue;

            for (ORef objectRef : rawProject.getAllRefsForType(objectType))
            {
                RawObject rawObject = rawProject.findObject(objectRef);
                if (ObjectType.requiresUUID(objectType))
                    assertTrue("Field should have been added during forward migration?", rawObject.containsKey(MigrationTo46.TAG_UUID));
                else
                    assertFalse("Field should have not been added during forward migration?", rawObject.containsKey(MigrationTo46.TAG_UUID));
            }
        }
    }

    public void testFieldsRemovedAfterReverseMigration() throws Exception
    {
        getProject().populateEverything();

        RawProject rawProject = reverseMigrate(new VersionRange(MigrationTo46.VERSION_TO));

        for (int objectType = ObjectType.FIRST_OBJECT_TYPE; objectType < ObjectType.OBJECT_TYPE_COUNT; ++objectType)
        {
            if (!rawProject.containsAnyObjectsOfType(objectType))
                continue;

            for (ORef objectRef : rawProject.getAllRefsForType(objectType))
            {
                RawObject rawObject = rawProject.findObject(objectRef);
                assertFalse("Field should have been removed during reverse migration?", rawObject.containsKey(MigrationTo46.TAG_UUID));
            }
        }
    }

    @Override
    protected int getFromVersion()
    {
        return MigrationTo46.VERSION_FROM;
    }

    @Override
    protected int getToVersion()
    {
        return MigrationTo46.VERSION_TO;
    }
}
