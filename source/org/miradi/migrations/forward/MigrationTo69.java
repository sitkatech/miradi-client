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

public class MigrationTo69 extends AbstractMigration
{
    public MigrationTo69(RawProject rawProjectToUse)
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
            final DiagramFactorVisitor visitor = new DiagramFactorVisitor(typeToVisit, reverseMigration);
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
        return EAM.text("This migration modifies the 'red' color choice for Diagram Factor font color styling.");
    }

    private Vector<Integer> getTypesToMigrate()
    {
        Vector<Integer> typesToMigrate = new Vector<Integer>();
        typesToMigrate.add(ObjectType.DIAGRAM_FACTOR);

        return typesToMigrate;
    }

    private class DiagramFactorVisitor extends AbstractMigrationORefVisitor
    {
        public DiagramFactorVisitor(int typeToVisit, boolean reverseMigration)
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
                    migrationResult = revertForegroundColorChoice(rawObject);
                else
                    migrationResult = changeForegroundColorChoice(rawObject);
            }

            return migrationResult;
        }

        private MigrationResult changeForegroundColorChoice(RawObject rawObject) throws Exception
        {
            MigrationResult migrationResult = MigrationResult.createSuccess();

            if (rawObject.hasValue(TAG_FOREGROUND_COLOR))
                if (rawObject.getData(TAG_FOREGROUND_COLOR).equals(LEGACY_RED_HEX))
                    rawObject.setData(TAG_FOREGROUND_COLOR, RED_HEX);

            return migrationResult;
        }

        private MigrationResult revertForegroundColorChoice(RawObject rawObject) throws Exception
        {
            MigrationResult migrationResult = MigrationResult.createSuccess();

            if (rawObject.hasValue(TAG_FOREGROUND_COLOR))
                if (rawObject.getData(TAG_FOREGROUND_COLOR).equals(RED_HEX))
                    rawObject.setData(TAG_FOREGROUND_COLOR, LEGACY_RED_HEX);

            return migrationResult;
        }

        private int type;
        private boolean isReverseMigration;
    }

    public static final int VERSION_FROM = 68;
    public static final int VERSION_TO = 69;

    public static final String TAG_FOREGROUND_COLOR = "FontColor";
    public static final String LEGACY_RED_HEX = "#FF0000";
    public static final String RED_HEX = "#D31913";
}