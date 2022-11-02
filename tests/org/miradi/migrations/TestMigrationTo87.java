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

import org.miradi.migrations.forward.MigrationTo87;
import org.miradi.objects.Cause;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceQuestion;
import org.miradi.questions.StaticQuestionManager;
import org.miradi.questions.ThreatClassificationQuestionV11;

public class TestMigrationTo87 extends AbstractTestMigration
{
    public TestMigrationTo87(String name)
    {
        super(name);
    }

    public void testCauseFieldRenamedAfterMigration() throws Exception
    {
        Cause threat = getProject().createAndPopulateThreat();

        ChoiceQuestion classificationQuestionV11 = StaticQuestionManager.getQuestion(ThreatClassificationQuestionV11.class);
        String expectedValue = classificationQuestionV11.getAllCodes().lastElement();

        getProject().fillObjectUsingCommand(threat.getRef(), MigrationTo87.TAG_TAXONOMY_CODE, expectedValue);

        RawProject reverseMigratedProject = reverseMigrate(new VersionRange(MigrationTo87.VERSION_TO));
        assertEquals("Data should match during field rename on reverse migration?", expectedValue, reverseMigratedProject.getData(threat.getRef(), MigrationTo87.LEGACY_TAG_TAXONOMY_CODE));

        migrateProject(reverseMigratedProject, new VersionRange(Project.VERSION_HIGH));
        assertEquals("Data should match during field rename on forward migration?", expectedValue, reverseMigratedProject.getData(threat.getRef(), MigrationTo87.TAG_TAXONOMY_CODE));

        verifyFullCircleMigrations(new VersionRange(86, 87));
    }

    @Override
    protected int getFromVersion()
    {
        return MigrationTo87.VERSION_FROM;
    }

    @Override
    protected int getToVersion()
    {
        return MigrationTo87.VERSION_TO;
    }
}
