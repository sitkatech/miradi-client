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

package org.miradi.migrations.forward;

import org.miradi.main.EAM;
import org.miradi.migrations.*;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ObjectType;

import java.util.Vector;

public class MigrationTo68 extends AbstractMigration
{
    public MigrationTo68(RawProject rawProjectToUse)
    {
        super(rawProjectToUse);
    }

    @Override
    protected MigrationResult migrateForward() throws Exception
    {
        return MigrationResult.createSuccess();
    }

    @Override
    protected MigrationResult reverseMigrate() throws Exception
    {
        return migrate();
    }

    private MigrationResult migrate() throws Exception
    {
        MigrationResult migrationResult = MigrationResult.createUninitializedResult();

        Vector<Integer> typesToVisit = getTypesToMigrate();

        for(Integer typeToVisit : typesToVisit)
        {
            final ProgressReportVisitor visitor = new ProgressReportVisitor(typeToVisit);
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
        return EAM.text("This migration removes the Not Known choice for Progress Report Statuses.");
    }

    private Vector<Integer> getTypesToMigrate()
    {
        Vector<Integer> typesToMigrate = new Vector<Integer>();
        typesToMigrate.add(ObjectType.PROGRESS_REPORT);
        typesToMigrate.add(ObjectType.EXTENDED_PROGRESS_REPORT);

        return typesToMigrate;
    }

    private class ProgressReportVisitor extends AbstractMigrationORefVisitor
    {
        public ProgressReportVisitor(int typeToVisit)
        {
            type = typeToVisit;
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
                migrationResult = removeNotKnownChoiceItem(rawObject);
            }

            return migrationResult;
        }

        private MigrationResult removeNotKnownChoiceItem(RawObject rawObject)
        {
            MigrationResult migrationResult = MigrationResult.createSuccess();

            if (rawObject.hasValue(TAG_PROGRESS_STATUS))
                if (rawObject.getData(TAG_PROGRESS_STATUS).equals(NOT_KNOWN_CODE))
                    rawObject.setData(TAG_PROGRESS_STATUS, NOT_SPECIFIED);

            return migrationResult;
        }

        private int type;
    }

    public static final int VERSION_FROM = 67;
    public static final int VERSION_TO = 68;

    public static final String NOT_SPECIFIED = "";
    public static final String NOT_KNOWN_CODE = "NotKnown";

    public static final String TAG_PROGRESS_STATUS = "ProgressStatus";
}