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

import org.miradi.migrations.forward.MigrationTo78;
import org.miradi.migrations.forward.MigrationTo79;
import org.miradi.objects.AnalyticalQuestion;
import org.miradi.objects.Assumption;
import org.miradi.objects.Factor;
import org.miradi.project.ProjectSaverForTesting;

public class TestMigrationTo79 extends AbstractTestMigration
{
    public TestMigrationTo79(String name)
    {
        super(name);
    }

    public void testAnalyticalQuestionFieldsAddedAfterForwardMigration() throws Exception
    {
        AnalyticalQuestion analyticalQuestion = getProject().createAndPopulateAnalyticalQuestion();
        testFieldsAddedAfterForwardMigration(analyticalQuestion);
    }

    public void testAnalyticalQuestionFieldsRemovedAfterReverseMigration() throws Exception
    {
        AnalyticalQuestion analyticalQuestion = getProject().createAndPopulateAnalyticalQuestion();
        testFieldsRemovedAfterReverseMigration(analyticalQuestion);
    }

    public void testAssumptionFieldsAddedAfterForwardMigration() throws Exception
    {
        Assumption assumption = getProject().createAndPopulateAssumption();
        testFieldsAddedAfterForwardMigration(assumption);
    }

    public void testAssumptionFieldsRemovedAfterReverseMigration() throws Exception
    {
        Assumption assumption = getProject().createAndPopulateAssumption();
        testFieldsRemovedAfterReverseMigration(assumption);
    }

    private void testFieldsAddedAfterForwardMigration(Factor factor) throws Exception
    {
        String commentsBefore = factor.getData(MigrationTo79.TAG_COMMENTS);

        String projectAsString = ProjectSaverForTesting.createSnapShot(getProject(), new VersionRange(MigrationTo79.VERSION_FROM));
        final RawProject projectToMigrate = RawProjectLoader.loadProject(projectAsString);
        migrateProject(projectToMigrate, new VersionRange(MigrationTo79.VERSION_TO));

        RawObject rawFactorAfter = projectToMigrate.findObject(factor.getRef());
        assertNotNull(rawFactorAfter);
        assertTrue("Field should have been added during forward migration?", rawFactorAfter.containsKey(MigrationTo79.TAG_IMPLICATIONS));

        String implications = rawFactorAfter.getData(MigrationTo79.TAG_IMPLICATIONS);
        assertEquals(commentsBefore, implications);

        String commentsAfter = rawFactorAfter.getData(MigrationTo79.TAG_COMMENTS);
        assertEquals(commentsAfter, "");
    }

    private void testFieldsRemovedAfterReverseMigration(Factor factor) throws Exception
    {
        String implicationsBefore = "Some implications";
        factor.setData(MigrationTo79.TAG_IMPLICATIONS, implicationsBefore);

        String projectAsString = ProjectSaverForTesting.createSnapShot(getProject(), new VersionRange(getToVersion()));
        final RawProject projectToMigrate = RawProjectLoader.loadProject(projectAsString);
        migrateProject(projectToMigrate, new VersionRange(MigrationTo78.VERSION_TO));

        RawObject rawFactorAfter = projectToMigrate.findObject(factor.getRef());
        assertNotNull(rawFactorAfter);
        assertFalse("Field should have been removed during reverse migration?", rawFactorAfter.containsKey(MigrationTo79.TAG_IMPLICATIONS));

        String commentsAfter = rawFactorAfter.getData(MigrationTo79.TAG_COMMENTS);
        assertEquals(commentsAfter, implicationsBefore);
    }

    @Override
    protected int getFromVersion()
    {
        return MigrationTo79.VERSION_FROM;
    }

    @Override
    protected int getToVersion()
    {
        return MigrationTo79.VERSION_TO;
    }
}

