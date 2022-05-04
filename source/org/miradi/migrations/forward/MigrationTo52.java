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

public class MigrationTo52 extends AbstractMigration
{
    public MigrationTo52(RawProject rawProjectToUse)
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
            final MeasurementVisitor visitor = new MeasurementVisitor(typeToVisit, reverseMigration);
            visitAllORefsInPool(visitor);
            final MigrationResult thisMigrationResult = visitor.getMigrationResult();
            if (migrationResult == null)
                migrationResult = thisMigrationResult;
            else
                migrationResult.merge(thisMigrationResult);
        }

        return migrationResult;
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
        return EAM.text("This migration removes the Sampling Based choice for Measurement Source.");
    }

    private Vector<Integer> getTypesToMigrate()
    {
        Vector<Integer> typesToMigrate = new Vector<Integer>();
        typesToMigrate.add(ObjectType.MEASUREMENT);

        return typesToMigrate;
    }

    private class MeasurementVisitor extends AbstractMigrationORefVisitor
    {
        public MeasurementVisitor(int typeToVisit, boolean reverseMigration)
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
                if (isReverseMigration)
                    migrationResult = addSamplingBasedChoiceItem(rawObject);
                else
                    migrationResult = removeSamplingBasedChoiceItem(rawObject);
            }

            return migrationResult;
        }

        private MigrationResult removeSamplingBasedChoiceItem(RawObject rawObject) throws Exception
        {
            MigrationResult migrationResult = MigrationResult.createSuccess();

            if (rawObject.hasValue(TAG_STATUS_CONFIDENCE))
                if (rawObject.getData(TAG_STATUS_CONFIDENCE).equals(SAMPLING_BASED))
                    rawObject.setData(TAG_STATUS_CONFIDENCE, INTENSIVE_ASSESSMENT_CODE);

            return migrationResult;
        }

        private MigrationResult addSamplingBasedChoiceItem(RawObject rawObject) throws Exception
        {
            MigrationResult migrationResult = MigrationResult.createSuccess();

            if (rawObject.hasValue(TAG_STATUS_CONFIDENCE))
                if (rawObject.getData(TAG_STATUS_CONFIDENCE).equals(INTENSIVE_ASSESSMENT_CODE))
                    if (anySampleFieldPopulated(rawObject))
                        rawObject.setData(TAG_STATUS_CONFIDENCE, SAMPLING_BASED);

            return migrationResult;
        }

        private boolean anySampleFieldPopulated(RawObject rawObject)
        {
            if (rawObject.hasValue(TAG_SAMPLE_SIZE))
                if (!rawObject.getData(TAG_SAMPLE_SIZE).isEmpty())
                    return true;

            if (rawObject.hasValue(TAG_SAMPLE_PRECISION))
                if (!rawObject.getData(TAG_SAMPLE_PRECISION).isEmpty())
                    return true;

            if (rawObject.hasValue(TAG_SAMPLE_PRECISION_TYPE))
                if (!rawObject.getData(TAG_SAMPLE_PRECISION_TYPE).isEmpty())
                    return true;

            return false;
        }

        private int type;
        private boolean isReverseMigration;
    }

    public static final int VERSION_FROM = 51;
    public static final int VERSION_TO = 52;

    public static final String INTENSIVE_ASSESSMENT_CODE = "IntensiveAssessment";
	public static final String SAMPLING_BASED = "SamplingBased";

    public static final String TAG_STATUS_CONFIDENCE = "StatusConfidence";
    public static final String TAG_SAMPLE_SIZE = "SampleSize";
    public static final String TAG_SAMPLE_PRECISION = "SamplePrecision";
    public static final String TAG_SAMPLE_PRECISION_TYPE = "SamplePrecisionType";
}