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
import org.miradi.utils.BiDirectionalHashMap;

import java.util.HashSet;
import java.util.Vector;

public class MigrationTo89 extends AbstractMigration
{
    public MigrationTo89(RawProject rawProjectToUse)
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
            final RenameAnalyticalQuestionFieldVisitor visitor = new RenameAnalyticalQuestionFieldVisitor(typeToVisit, reverseMigration);
            visitAllORefsInPool(visitor);
            final MigrationResult thisMigrationResult = visitor.getMigrationResult();
            if (migrationResult == null)
                migrationResult = thisMigrationResult;
            else
                migrationResult.merge(thisMigrationResult);
        }

        return migrationResult;    }

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
        return EAM.text("This migration renames the AssumptionIds field on Analytical Question objects.");
    }

    private Vector<Integer> getTypesToMigrate()
    {
        Vector<Integer> typesToMigrate = new Vector<Integer>();
        typesToMigrate.add(ObjectType.ANALYTICAL_QUESTION);

        return typesToMigrate;
    }

    private class RenameAnalyticalQuestionFieldVisitor extends AbstractMigrationORefVisitor
    {
        public RenameAnalyticalQuestionFieldVisitor(int typeToVisit, boolean reverseMigration)
        {
            type = typeToVisit;
            isReverseMigration = reverseMigration;
            oldToNewTagMap = createLegacyToNewTagMap();
        }

        public int getTypeToVisit()
        {
            return type;
        }

        @Override
        public MigrationResult internalVisit(ORef rawObjectRef) throws Exception
        {
            RawObject rawObject = getRawProject().findObject(rawObjectRef);
            if (rawObject != null)
            {
                if (isReverseMigration)
                    return renameFields(rawObject, oldToNewTagMap.reverseMap());
                else
                    return renameFields(rawObject, oldToNewTagMap);
            }

            return MigrationResult.createSuccess();
        }

        private MigrationResult renameFields(RawObject rawObject, BiDirectionalHashMap oldToNewTagMapToUse)
        {
            MigrationResult migrationResult = MigrationResult.createSuccess();
            HashSet<String> legacyTags = oldToNewTagMapToUse.getKeys();
            for(String legacyTag : legacyTags)
            {
                if (rawObject.containsKey(legacyTag))
                {
                    String newTag = oldToNewTagMapToUse.getValue(legacyTag);
                    String data = rawObject.get(legacyTag);
                    rawObject.remove(legacyTag);
                    rawObject.put(newTag, data);
                }
            }

            return migrationResult;
        }

        protected BiDirectionalHashMap createLegacyToNewTagMap()
        {
            BiDirectionalHashMap oldToNewTagMap = new BiDirectionalHashMap();
            oldToNewTagMap.put(LEGACY_TAG_ASSUMPTION_IDS, TAG_SUB_ASSUMPTION_IDS);

            return oldToNewTagMap;
        }

        private int type;
        private boolean isReverseMigration;
        private BiDirectionalHashMap oldToNewTagMap;
    }

    public static final int VERSION_FROM = 88;
    public static final int VERSION_TO = 89;

    public static final String LEGACY_TAG_ASSUMPTION_IDS = "AssumptionIds";
    public static final String TAG_SUB_ASSUMPTION_IDS = "SubAssumptionIds";
}