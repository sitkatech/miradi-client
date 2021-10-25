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

package org.miradi.migrations.forward;

import org.miradi.main.EAM;
import org.miradi.migrations.*;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ObjectType;

import java.util.UUID;
import java.util.Vector;

public class MigrationTo46 extends AbstractMigration
{
    public MigrationTo46(RawProject rawProjectToUse)
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
            final UUIDVisitor visitor = new UUIDVisitor(typeToVisit, reverseMigration);
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
        return EAM.text("This migration adds a UUID field to all objects that require it.");
    }

    private Vector<Integer> getTypesToMigrate()
    {
        Vector<Integer> typesToMigrate = new Vector<Integer>();

        for (int objectType = ObjectType.FIRST_OBJECT_TYPE; objectType < ObjectType.OBJECT_TYPE_COUNT; ++objectType)
        {
            if (ObjectType.requiresUUID(objectType))
                typesToMigrate.add(objectType);
        }

        return typesToMigrate;
    }

    private class UUIDVisitor extends AbstractMigrationORefVisitor
    {
        public UUIDVisitor(int typeToVisit, boolean reverseMigration)
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
                    migrationResult = removeFields(rawObject);
                else
                    migrationResult = addFields(rawObject);
            }

            return migrationResult;
        }

        private MigrationResult addFields(RawObject rawObject) throws Exception
        {
            MigrationResult migrationResult = MigrationResult.createSuccess();

            rawObject.setData(TAG_UUID, UUID.randomUUID().toString());

            return migrationResult;
        }

        private MigrationResult removeFields(RawObject rawObject) throws Exception
        {
            MigrationResult migrationResult = MigrationResult.createSuccess();

            if (rawObject.hasValue(TAG_UUID))
                rawObject.remove(TAG_UUID);

            return migrationResult;
        }

        private int type;
        private boolean isReverseMigration;
    }

    public static final int VERSION_FROM = 45;
    public static final int VERSION_TO = 46;

    public static final String TAG_UUID = "UUID";
}