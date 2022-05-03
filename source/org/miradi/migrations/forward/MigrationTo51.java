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

public class MigrationTo51 extends AbstractMigration
{
    public MigrationTo51(RawProject rawProjectToUse)
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
            final RenameRatingSourceFieldVisitor visitor = new RenameRatingSourceFieldVisitor(typeToVisit, reverseMigration);
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
        return EAM.text("This migration renames the Rating Source field on Indicator objects.");
    }

    private Vector<Integer> getTypesToMigrate()
    {
        Vector<Integer> typesToMigrate = new Vector<Integer>();
        typesToMigrate.add(ObjectType.INDICATOR);

        return typesToMigrate;
    }

    private class RenameRatingSourceFieldVisitor extends AbstractMigrationORefVisitor
    {
        public RenameRatingSourceFieldVisitor(int typeToVisit, boolean reverseMigration)
        {
            type = typeToVisit;
            isReverseMigration = reverseMigration;
            oldToNewTagMap = createLegacyToNewTagMap();
            oldToNewValueMap = createLegacyToNewValueMap();
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
                    return renameFields(rawObject, oldToNewTagMap.reverseMap(), oldToNewValueMap.reverseMap());
                else
                    return renameFields(rawObject, oldToNewTagMap, oldToNewValueMap);
            }

            return MigrationResult.createSuccess();
        }

        private MigrationResult renameFields(RawObject rawObject, BiDirectionalHashMap oldToNewTagMapToUse, BiDirectionalHashMap oldToNewValueMapToUse)
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

                    if (oldToNewValueMapToUse.containsKey(data))
                        rawObject.put(newTag, oldToNewValueMapToUse.getValue(data));
                    else
                        rawObject.put(newTag, data);
                }
            }

            return migrationResult;
        }

        protected BiDirectionalHashMap createLegacyToNewTagMap()
        {
            BiDirectionalHashMap oldToNewTagMap = new BiDirectionalHashMap();
            oldToNewTagMap.put(LEGACY_TAG_RATING_SOURCE, TAG_VIABILITY_RATINGS_EVIDENCE_CONFIDENCE);

            return oldToNewTagMap;
        }

        protected BiDirectionalHashMap createLegacyToNewValueMap()
        {
            BiDirectionalHashMap oldToNewValueMap = new BiDirectionalHashMap();
            oldToNewValueMap.put(LEGACY_EXPERT_KNOWLEDGE_CODE, EXPERT_KNOWLEDGE_CODE);

            return oldToNewValueMap;
        }

        private int type;
        private boolean isReverseMigration;
        private BiDirectionalHashMap oldToNewTagMap;
        private BiDirectionalHashMap oldToNewValueMap;
    }

    public static final int VERSION_FROM = 50;
    public static final int VERSION_TO = 51;

    public static final String LEGACY_TAG_RATING_SOURCE = "RatingSource";
    public static final String TAG_VIABILITY_RATINGS_EVIDENCE_CONFIDENCE = "ViabilityRatingsEvidenceConfidence";

    public static final String LEGACY_EXPERT_KNOWLEDGE_CODE = "ExpertKnowlege";
    public static final String EXPERT_KNOWLEDGE_CODE = "ExpertKnowledge";
}