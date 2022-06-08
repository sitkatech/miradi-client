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

import org.miradi.migrations.forward.MigrationTo82;
import org.miradi.objects.Strategy;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceQuestion;
import org.miradi.questions.StaticQuestionManager;
import org.miradi.questions.StrategyClassificationQuestionV11;

public class TestMigrationTo82 extends AbstractTestMigration
{
    public TestMigrationTo82(String name)
    {
        super(name);
    }

    public void testStrategyFieldRenamedAfterMigration() throws Exception
    {
        Strategy strategy = getProject().createAndPopulateStrategy();

        ChoiceQuestion classificationQuestionV11 = StaticQuestionManager.getQuestion(StrategyClassificationQuestionV11.class);
        String expectedValue = classificationQuestionV11.getCode(1);

        getProject().fillObjectUsingCommand(strategy.getRef(), MigrationTo82.TAG_TAXONOMY_CODE, expectedValue);

        RawProject reverseMigratedProject = reverseMigrate(new VersionRange(MigrationTo82.VERSION_TO));
        assertEquals("Data should match during field rename on reverse migration?", expectedValue, reverseMigratedProject.getData(strategy.getRef(), MigrationTo82.LEGACY_TAG_TAXONOMY_CODE));

        migrateProject(reverseMigratedProject, new VersionRange(Project.VERSION_HIGH));
        assertEquals("Data should match during field rename on forward migration?", expectedValue, reverseMigratedProject.getData(strategy.getRef(), MigrationTo82.TAG_TAXONOMY_CODE));

        verifyFullCircleMigrations(new VersionRange(81, 82));
    }

    @Override
    protected int getFromVersion()
    {
        return MigrationTo82.VERSION_FROM;
    }

    @Override
    protected int getToVersion()
    {
        return MigrationTo82.VERSION_TO;
    }
}
