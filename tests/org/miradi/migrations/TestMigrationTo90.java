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

import org.miradi.migrations.forward.MigrationTo90;
import org.miradi.objecthelpers.ORef;
import org.miradi.project.Project;
import org.miradi.project.ProjectSaverForTesting;
import org.miradi.utils.CodeList;


public class TestMigrationTo90 extends AbstractTestMigration
{
    public TestMigrationTo90(String name)
    {
        super(name);
    }

    public void testObjectTreeTableConfigFieldsRenamedAfterMigration() throws Exception
    {
        ORef objectTreeTableConfigRef = getProject().createAndPopulateObjectTreeTableConfiguration().getRef();

        CodeList rowCodes = new CodeList();
        rowCodes.add(MigrationTo90.ASSUMPTION);
        rowCodes.add(MigrationTo90.SUB_ASSUMPTION);
        getProject().fillObjectUsingCommand(objectTreeTableConfigRef, MigrationTo90.TAG_ROW_CONFIGURATION, rowCodes.toJsonString());

        String projectAsString = ProjectSaverForTesting.createSnapShot(getProject(), new VersionRange(getToVersion()));
        final RawProject projectToMigrate = RawProjectLoader.loadProject(projectAsString);
        migrateProject(projectToMigrate, new VersionRange(getFromVersion()));

        CodeList codeListAfterReverseMigration = new CodeList(projectToMigrate.getData(objectTreeTableConfigRef, MigrationTo90.TAG_ROW_CONFIGURATION));

        assertEquals("Reverse migration should not have changed the number of codes", rowCodes.size(), codeListAfterReverseMigration.size());

        assertTrue("Reverse migration should have reinstated legacy code", codeListAfterReverseMigration.contains(MigrationTo90.LEGACY_ANALYTICAL_QUESTION));
        assertTrue("Reverse migration should have reinstated legacy code", codeListAfterReverseMigration.contains(MigrationTo90.LEGACY_ASSUMPTION));
        assertFalse("Reverse migration should not have retained new code", codeListAfterReverseMigration.contains(MigrationTo90.SUB_ASSUMPTION));

        migrateProject(projectToMigrate, new VersionRange(Project.VERSION_HIGH));

        CodeList codeListAfterForwardMigration = new CodeList(projectToMigrate.getData(objectTreeTableConfigRef, MigrationTo90.TAG_ROW_CONFIGURATION));

        assertEquals("Forward migration should not have changed the number of codes", rowCodes.size(), codeListAfterForwardMigration.size());

        assertFalse("Forward migration should have removed legacy code", codeListAfterForwardMigration.contains(MigrationTo90.LEGACY_ANALYTICAL_QUESTION));
        assertTrue("Forward migration should have reinstated new code", codeListAfterForwardMigration.contains(MigrationTo90.ASSUMPTION));
        assertTrue("Forward migration should have reinstated new code", codeListAfterForwardMigration.contains(MigrationTo90.SUB_ASSUMPTION));

        verifyFullCircleMigrations(new VersionRange(getFromVersion(), getToVersion()));
    }

    @Override
    protected int getFromVersion()
    {
        return MigrationTo90.VERSION_FROM;
    }

    @Override
    protected int getToVersion()
    {
        return MigrationTo90.VERSION_TO;
    }
}
