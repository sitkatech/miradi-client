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
import org.miradi.migrations.AbstractMigrationVisitor;
import org.miradi.migrations.AbstractSingleTypeMigration;
import org.miradi.migrations.MigrationResult;
import org.miradi.migrations.RawObject;
import org.miradi.migrations.RawProject;
import org.miradi.schemas.ProjectMetadataSchema;

public class MigrationTo44 extends AbstractSingleTypeMigration
{
    public MigrationTo44(RawProject rawProjectToUse)
    {
        super(rawProjectToUse);
    }

    @Override
    protected MigrationResult migrateForward() throws Exception
    {
        final ForwardProjectMetadataVisitor visitor = new ForwardProjectMetadataVisitor();

        return visitAllObjectsInPool(visitor);
    }

    @Override
    protected MigrationResult reverseMigrate() throws Exception
    {
        final ReverseProjectMetadataVisitor visitor = new ReverseProjectMetadataVisitor();

        return visitAllObjectsInPool(visitor);
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
        return EAM.text("This migration changes the default diagram factor mode to include biophysical factors / results.");
    }

    abstract private class AbstractProjectMetadataVisitor extends AbstractMigrationVisitor
    {
        public int getTypeToVisit()
        {
            return ProjectMetadataSchema.getObjectType();
        }

        @Override
        public MigrationResult internalVisit(RawObject rawObject) throws Exception
        {
            return migrate(rawObject);
        }

        abstract protected MigrationResult migrate(RawObject rawObject) throws Exception;
    }

    private class ForwardProjectMetadataVisitor extends AbstractProjectMetadataVisitor
    {
        @Override
        protected MigrationResult migrate(RawObject rawObject) throws Exception
        {
            if (!rawObject.hasValue(TAG_FACTOR_MODE) || rawObject.getData(TAG_FACTOR_MODE).equalsIgnoreCase(""))
                rawObject.setData(TAG_FACTOR_MODE, CONTRIBUTING_FACTOR_CODE);
            else
                rawObject.setData(TAG_FACTOR_MODE, "");

            return MigrationResult.createSuccess();
        }
    }

    private class ReverseProjectMetadataVisitor extends AbstractProjectMetadataVisitor
    {
        @Override
        protected MigrationResult migrate(RawObject rawObject) throws Exception
        {
            if (!rawObject.hasValue(TAG_FACTOR_MODE) || rawObject.getData(TAG_FACTOR_MODE).equalsIgnoreCase(""))
                rawObject.setData(TAG_FACTOR_MODE, BIOPHYSICAL_FACTOR_CODE);
            else
                rawObject.setData(TAG_FACTOR_MODE, "");

            return MigrationResult.createSuccess();
        }
    }

    public static final int VERSION_FROM = 43;
    public static final int VERSION_TO = 44;

    public static final String BIOPHYSICAL_FACTOR_CODE = "BiophysicalFactorMode";
    public static final String CONTRIBUTING_FACTOR_CODE = "ContributingFactorMode";

    public static final String TAG_FACTOR_MODE = "FactorMode";
}
