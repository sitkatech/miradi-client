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

import org.miradi.migrations.forward.MigrationTo78;
import org.miradi.objects.AnalyticalQuestion;
import org.miradi.objects.Assumption;

public class TestMigrationTo78 extends AbstractTestMigration
{
    public TestMigrationTo78(String name)
    {
        super(name);
    }

    public void testFieldsRemovedAfterReverseMigration() throws Exception
    {
        AnalyticalQuestion analyticalQuestion = getProject().createAndPopulateAnalyticalQuestion();
        Assumption assumption = getProject().addAssumption(analyticalQuestion);

        RawProject rawProject = reverseMigrate(new VersionRange(MigrationTo78.VERSION_TO));

        RawObject rawAnalyticalQuestion = rawProject.findObject(analyticalQuestion.getRef());
        assertNotNull(rawAnalyticalQuestion);
        assertFalse("Field should have been removed during reverse migration?", rawAnalyticalQuestion.containsKey(MigrationTo78.TAG_ASSUMPTION_REFS));
    }

    @Override
    protected int getFromVersion()
    {
        return MigrationTo78.VERSION_FROM;
    }

    @Override
    protected int getToVersion()
    {
        return MigrationTo78.VERSION_TO;
    }
}

