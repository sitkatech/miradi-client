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

import org.miradi.migrations.forward.MigrationTo49;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.*;
import org.miradi.schemas.*;

public class TestMigrationTo49 extends AbstractTestMigration
{
    public TestMigrationTo49(String name)
    {
        super(name);
    }

    public void testFieldsRemovedAfterReverseMigration() throws Exception
    {
        Target target = getProject().createAndPopulateTarget();
        Strategy strategy = getProject().createAndPopulateStrategy();
        Objective objective = getProject().createAndPopulateObjective(strategy);
        Goal goal = getProject().createAndPopulateGoal(target);
        getProject().createAndPopulateIntermediateResult();
        getProject().createAndPopulateBiophysicalResult();
        getProject().createAndPopulateThreatReductionResult();

        RawProject rawProject = reverseMigrate(new VersionRange(MigrationTo49.VERSION_TO));

        verifyEvidenceFieldRemoved(rawProject, StrategySchema.getObjectType());
        verifyEvidenceFieldRemoved(rawProject, ObjectiveSchema.getObjectType());
        verifyEvidenceFieldRemoved(rawProject, GoalSchema.getObjectType());
        verifyEvidenceFieldRemoved(rawProject, IntermediateResultSchema.getObjectType());
        verifyEvidenceFieldRemoved(rawProject, BiophysicalResultSchema.getObjectType());
        verifyEvidenceFieldRemoved(rawProject, ThreatReductionResultSchema.getObjectType());
    }

    private void verifyEvidenceFieldRemoved(RawProject rawProject, int objectType)
    {
        RawPool rawObjectPool = rawProject.getRawPoolForType(objectType);
        for(ORef ref : rawObjectPool.keySet())
        {
            RawObject rawObject = rawObjectPool.get(ref);
            assertFalse("Field should have been removed during reverse migration?", rawObject.containsKey(MigrationTo49.TAG_EVIDENCE_CONFIDENCE));
        }
    }

    @Override
    protected int getFromVersion()
    {
        return MigrationTo49.VERSION_FROM;
    }

    @Override
    protected int getToVersion()
    {
        return MigrationTo49.VERSION_TO;
    }
}

