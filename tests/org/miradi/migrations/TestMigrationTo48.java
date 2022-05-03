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

import org.miradi.migrations.forward.MigrationTo48;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.*;
import org.miradi.schemas.*;

public class TestMigrationTo48 extends AbstractTestMigration
{
    public TestMigrationTo48(String name)
    {
        super(name);
    }

    public void testFieldsRemovedAfterReverseMigration() throws Exception
    {
        Target target = getProject().createAndPopulateTarget();
        HumanWelfareTarget humanWelfareTarget = getProject().createAndPopulateHumanWelfareTarget();
        Strategy strategy = getProject().createAndPopulateStrategy();
        Objective objective = getProject().createAndPopulateObjective(strategy);
        Goal goal = getProject().createAndPopulateGoal(target);
        Indicator indicator = getProject().createAndPopulateIndicator(strategy);
        Measurement measurement = getProject().createAndPopulateMeasurement();
        BiophysicalFactor biophysicalFactor = getProject().createAndPopulateBiophysicalFactor();
        BiophysicalResult biophysicalResult = getProject().createAndPopulateBiophysicalResult();
        Cause threat = getProject().createAndPopulateThreat();
        Stress stress = getProject().createAndPopulateStress();
        getProject().createAndPopulateThreatReductionResult();
        getProject().createAndPopulateIntermediateResult();

        RawProject rawProject = reverseMigrate(new VersionRange(MigrationTo48.VERSION_TO));

        verifyEvidenceFieldRemoved(rawProject, TargetSchema.getObjectType());
        verifyEvidenceFieldRemoved(rawProject, HumanWelfareTargetSchema.getObjectType());
        verifyEvidenceFieldRemoved(rawProject, StrategySchema.getObjectType());
        verifyEvidenceFieldRemoved(rawProject, TaskSchema.getObjectType());
        verifyEvidenceFieldRemoved(rawProject, ObjectiveSchema.getObjectType());
        verifyEvidenceFieldRemoved(rawProject, GoalSchema.getObjectType());
        verifyEvidenceFieldRemoved(rawProject, IndicatorSchema.getObjectType());
        verifyEvidenceFieldRemoved(rawProject, MeasurementSchema.getObjectType());
        verifyEvidenceFieldRemoved(rawProject, BiophysicalFactorSchema.getObjectType());
        verifyEvidenceFieldRemoved(rawProject, BiophysicalResultSchema.getObjectType());
        verifyEvidenceFieldRemoved(rawProject, CauseSchema.getObjectType());
        verifyEvidenceFieldRemoved(rawProject, StressSchema.getObjectType());
        verifyEvidenceFieldRemoved(rawProject, ThreatReductionResultSchema.getObjectType());
        verifyEvidenceFieldRemoved(rawProject, IntermediateResultSchema.getObjectType());
    }

    private void verifyEvidenceFieldRemoved(RawProject rawProject, int objectType)
    {
        RawPool rawObjectPool = rawProject.getRawPoolForType(objectType);
        for(ORef ref : rawObjectPool.keySet())
        {
            RawObject rawObject = rawObjectPool.get(ref);
            assertFalse("Field should have been removed during reverse migration?", rawObject.containsKey(MigrationTo48.TAG_EVIDENCE_NOTES));
        }
    }

    @Override
    protected int getFromVersion()
    {
        return MigrationTo48.VERSION_FROM;
    }

    @Override
    protected int getToVersion()
    {
        return MigrationTo48.VERSION_TO;
    }
}

