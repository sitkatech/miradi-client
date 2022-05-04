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

import org.miradi.migrations.forward.MigrationTo51;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.Indicator;
import org.miradi.objects.Strategy;
import org.miradi.project.Project;
import org.miradi.questions.RatingSourceQuestion;
import org.miradi.questions.ViabilityRatingEvidenceConfidence;

public class TestMigrationTo51 extends AbstractTestMigration
{
    public TestMigrationTo51(String name)
    {
        super(name);
    }

    public void testIndicatorFieldsRenamedAfterMigration() throws Exception
    {
        Strategy strategy = getProject().createAndPopulateStrategy();
        Indicator indicator = getProject().createAndPopulateIndicator(strategy);

        testObjectFieldsRenamedAfterMigration(indicator.getRef(), ViabilityRatingEvidenceConfidence.EXPERT_KNOWLEDGE_CODE, RatingSourceQuestion.EXPERT_KNOWLEDGE_CODE);
        testObjectFieldsRenamedAfterMigration(indicator.getRef(), ViabilityRatingEvidenceConfidence.ROUGH_GUESS_CODE, RatingSourceQuestion.ROUGH_GUESS_CODE);
    }

    private void testObjectFieldsRenamedAfterMigration(ORef objectRef, String forwardSampleData, String reverseSampleData) throws Exception
    {
        getProject().fillObjectUsingCommand(objectRef, MigrationTo51.TAG_VIABILITY_RATINGS_EVIDENCE_CONFIDENCE, forwardSampleData);

        RawProject reverseMigratedProject = reverseMigrate(new VersionRange(MigrationTo51.VERSION_TO));
        assertEquals("Data should match during field rename on reverse migration?", reverseSampleData, reverseMigratedProject.getData(objectRef, MigrationTo51.LEGACY_TAG_RATING_SOURCE));

        migrateProject(reverseMigratedProject, new VersionRange(Project.VERSION_HIGH));
        assertEquals("Data should match during field rename on forward migration?", forwardSampleData, reverseMigratedProject.getData(objectRef, MigrationTo51.TAG_VIABILITY_RATINGS_EVIDENCE_CONFIDENCE));

        verifyFullCircleMigrations(new VersionRange(50, 51));
    }

    @Override
    protected int getFromVersion()
    {
        return MigrationTo51.VERSION_FROM;
    }

    @Override
    protected int getToVersion()
    {
        return MigrationTo51.VERSION_TO;
    }
}
