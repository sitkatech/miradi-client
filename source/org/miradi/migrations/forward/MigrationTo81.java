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

package org.miradi.migrations.forward;

import org.miradi.main.EAM;
import org.miradi.migrations.*;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ObjectType;

import java.util.Vector;

public class MigrationTo81 extends AbstractMigration
{
    public MigrationTo81(RawProject rawProjectToUse)
    {
        super(rawProjectToUse);
    }

    @Override
    protected MigrationResult migrateForward() throws Exception
    {
        return migrate(false);
    }

    @Override
    protected MigrationResult reverseMigrate() throws Exception
    {
        return migrate(true);
    }

    private MigrationResult migrate(boolean reverseMigration) throws Exception
    {
        MigrationResult migrationResult = MigrationResult.createUninitializedResult();

        Vector<Integer> typesToVisit = getTypesToMigrate();

        for(Integer typeToVisit : typesToVisit)
        {
            final AnalyticalQuestionVisitor visitor = new AnalyticalQuestionVisitor(typeToVisit, reverseMigration);
            visitAllORefsInPool(visitor);
            final MigrationResult thisMigrationResult = visitor.getMigrationResult();
            if (migrationResult == null)
                migrationResult = thisMigrationResult;
            else
                migrationResult.merge(thisMigrationResult);
        }

        return migrationResult;
    }

    private Vector<Integer> getTypesToMigrate()
    {
        Vector<Integer> typesToMigrate = new Vector<Integer>();

        typesToMigrate.add(ObjectType.ANALYTICAL_QUESTION);
        typesToMigrate.add(ObjectType.SUB_ASSUMPTION);

        return typesToMigrate;
    }

    private class AnalyticalQuestionVisitor extends AbstractMigrationORefVisitor
    {
        public AnalyticalQuestionVisitor(int typeToVisit, boolean reverseMigration)
        {
            type = typeToVisit;
            isReverseMigration = reverseMigration;
        }

        public int getTypeToVisit()
        {
            return type;
        }

        @Override
        public MigrationResult internalVisit(ORef rawObjectRef) throws Exception
        {
            MigrationResult migrationResult = MigrationResult.createUninitializedResult();

            RawObject rawObject = getRawProject().findObject(rawObjectRef);
            if (rawObject != null)
            {
                migrationResult = removeFields(rawObject);
            }

            return migrationResult;
        }

        private MigrationResult removeFields(RawObject rawObject) throws Exception
        {
            MigrationResult migrationResult = MigrationResult.createSuccess();

            if (rawObject.hasValue(TAG_DIAGRAM_FACTOR_IDS))
                rawObject.remove(TAG_DIAGRAM_FACTOR_IDS);

            return migrationResult;
        }

        private int type;
        private boolean isReverseMigration;
    }

    @Override
    protected int getToVersion()
    {
        return VERSION_TO;
    }

    @Override
    protected int getFromVersion()
    {
        return VERSION_FROM;
    }

    @Override
    protected String getDescription()
    {
        return EAM.text("This migration removes unused fields from Analytical Questions and Assumptions.");
    }

    public static final int VERSION_FROM = 80;
    public static final int VERSION_TO = 81;

    public static final String TAG_DIAGRAM_FACTOR_IDS = "DiagramFactorIds";
}
