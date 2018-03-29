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

import org.miradi.migrations.forward.MigrationTo64;
import org.miradi.migrations.forward.MigrationTo65;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.Factor;
import org.miradi.objects.Target;
import org.miradi.project.ProjectSaverForTesting;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.ThreatRatingEvidenceConfidenceQuestion;

public class TestMigrationTo65 extends AbstractTestMigration
{
    public TestMigrationTo65(String name)
    {
        super(name);
    }

    public void testFieldsRemovedAfterReverseMigration() throws Exception
    {
        ORef targetRef = getProject().populateSimpleThreatRatingValues();
        Target target = (Target) getProject().findObject(targetRef);
        ORef threatRef = ORef.INVALID;
        for (Factor factor :target.getUpstreamDownstreamFactors())
        {
            if (factor.isDirectThreat())
            {
                threatRef = factor.getRef();
                break;
            }
        }
        assertTrue(threatRef.isValid());

        assertTrue("project is not in simple threat rating mode?", getProject().isSimpleThreatRatingMode());
        ChoiceItem evidenceConfidenceChoice = new ThreatRatingEvidenceConfidenceQuestion().findChoiceByCode(ThreatRatingEvidenceConfidenceQuestion.ROUGH_GUESS_CODE);
        getProject().populateSimpleThreatRatingEvidenceData(threatRef, targetRef, "Sample evidence notes", evidenceConfidenceChoice, ObjectType.THREAT_SIMPLE_RATING_DATA);

        String projectAsString = ProjectSaverForTesting.createSnapShot(getProject(), new VersionRange(getToVersion()));
        final RawProject projectToMigrate = RawProjectLoader.loadProject(projectAsString);
        migrateProject(projectToMigrate, new VersionRange(MigrationTo64.VERSION_TO));
        verifyEvidenceFieldsRemoved(projectToMigrate, ObjectType.THREAT_SIMPLE_RATING_DATA);
    }

    private void verifyEvidenceFieldsRemoved(RawProject rawProject, int objectType)
    {
        RawPool rawObjectPool = rawProject.getRawPoolForType(objectType);
        for(ORef ref : rawObjectPool.keySet())
        {
            RawObject rawObject = rawObjectPool.get(ref);
            assertFalse("Field should have been removed during reverse migration?", rawObject.containsKey(MigrationTo65.TAG_EVIDENCE_CONFIDENCE));
            assertFalse("Field should have been removed during reverse migration?", rawObject.containsKey(MigrationTo65.TAG_EVIDENCE_NOTES));
        }
    }

    @Override
    protected int getFromVersion()
    {
        return MigrationTo65.VERSION_FROM;
    }

    @Override
    protected int getToVersion()
    {
        return MigrationTo65.VERSION_TO;
    }
}

