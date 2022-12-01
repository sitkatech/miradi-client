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
import org.miradi.utils.CodeList;

import java.util.HashSet;
import java.util.Vector;

public class MigrationTo90 extends AbstractMigration
{
    public MigrationTo90(RawProject rawProjectToUse)
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
            final RenameSubAssumptionRelatedCodeFieldsVisitor visitor = new RenameSubAssumptionRelatedCodeFieldsVisitor(typeToVisit, reverseMigration);
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
        return EAM.text("This migration renames the sub-assumption fields on the object tree table configuration.");
    }

    private Vector<Integer> getTypesToMigrate()
    {
        Vector<Integer> typesToMigrate = new Vector<Integer>();
        typesToMigrate.add(ObjectType.OBJECT_TREE_TABLE_CONFIGURATION);

        return typesToMigrate;
    }

    private class RenameSubAssumptionRelatedCodeFieldsVisitor extends AbstractMigrationORefVisitor
    {
        public RenameSubAssumptionRelatedCodeFieldsVisitor(int typeToVisit, boolean reverseMigration)
        {
            type = typeToVisit;
            isReverseMigration = reverseMigration;
            if (isReverseMigration)
            {
                oldToNewTagMap = createLegacyToNewMapReverse();
            }
            else
            {
                oldToNewTagMap = createLegacyToNewMapForward();
            }
            codeListTagsToVisit = getCodeListTagsToVisit();
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
                {
                    migrationResult = renameFields(rawObject, codeListTagsToVisit, oldToNewTagMap.reverseMap());
                }
                else
                {
                    migrationResult = renameFields(rawObject, codeListTagsToVisit, oldToNewTagMap);
                }
            }

            return migrationResult;
        }

        private MigrationResult renameFields(RawObject rawObject, Vector<String> codeListTagsToVisit, BiDirectionalHashMap oldToNewTagMapToUse) throws Exception
        {
            MigrationResult migrationResult = MigrationResult.createSuccess();

            HashSet<String> legacyTags = oldToNewTagMapToUse.getKeys();

            for(String codeListTag : codeListTagsToVisit)
            {
                CodeList codeListToMigrate = getCodeList(rawObject, codeListTag);
                CodeList codeListMigrated = migrateCodeList(oldToNewTagMapToUse, legacyTags, codeListToMigrate);

                rawObject.setData(codeListTag, codeListMigrated.toJsonString());
            }

            return migrationResult;
        }

        private CodeList migrateCodeList(BiDirectionalHashMap oldToNewTagMapToUse, HashSet<String> legacyTags, CodeList codeListToMigrate)
        {
            CodeList codeListMigrated = new CodeList();

            for (String code : codeListToMigrate)
            {
                if (legacyTags.contains(code))
                {
                    String newTag = oldToNewTagMapToUse.getValue(code);
                    codeListMigrated.add(newTag);
                }
                else
                {
                    codeListMigrated.add(code);
                }
            }
            return codeListMigrated;
        }

        private CodeList getCodeList(RawObject rawObject, String tag) throws Exception
        {
            String data = rawObject.getData(tag);
            if (data != null)
                return new CodeList(data);
            else
                return new CodeList();
        }

        private Vector<String> getCodeListTagsToVisit()
        {
            Vector<String> codeListTagsToVisit = new Vector<String>();
            codeListTagsToVisit.add(TAG_ROW_CONFIGURATION);

            return codeListTagsToVisit;
        }

        protected BiDirectionalHashMap createLegacyToNewMapForward()
        {
            BiDirectionalHashMap oldToNewTagMap = new BiDirectionalHashMap();
            oldToNewTagMap.put(LEGACY_ASSUMPTION, SUB_ASSUMPTION);
            oldToNewTagMap.put(LEGACY_ANALYTICAL_QUESTION, ASSUMPTION);

            return oldToNewTagMap;
        }

        protected BiDirectionalHashMap createLegacyToNewMapReverse()
        {
            BiDirectionalHashMap oldToNewTagMap = new BiDirectionalHashMap();
            oldToNewTagMap.put(LEGACY_ANALYTICAL_QUESTION, ASSUMPTION);
            oldToNewTagMap.put(LEGACY_ASSUMPTION, SUB_ASSUMPTION);

            return oldToNewTagMap;
        }

        private int type;
        private boolean isReverseMigration;
        private BiDirectionalHashMap oldToNewTagMap;
        private Vector<String> codeListTagsToVisit;
    }

    public static final int VERSION_FROM = 89;
    public static final int VERSION_TO = 90;

    public static final String TAG_ROW_CONFIGURATION = "TagRowConfiguration";

    public final static String LEGACY_ANALYTICAL_QUESTION = "AnalyticalQuestion";
    public final static String ASSUMPTION = "Assumption";

    public final static String LEGACY_ASSUMPTION = "Assumption";
    public final static String SUB_ASSUMPTION = "SubAssumption";
}