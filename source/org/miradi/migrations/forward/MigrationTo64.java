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
import org.miradi.objecthelpers.CodeToUserStringMap;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.utils.EnhancedJsonObject;

import java.util.Set;
import java.util.Vector;

public class MigrationTo64 extends AbstractMigration
{
    public MigrationTo64(RawProject rawProjectToUse)
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
            final ThreatRatingCommentsDataVisitor visitor = new ThreatRatingCommentsDataVisitor(typeToVisit, reverseMigration);
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
        return EAM.text("This migration restructures the underlying data structures for both Simple and Stress Threat Rating comments.");
    }

    private Vector<Integer> getTypesToMigrate()
    {
        Vector<Integer> typesToMigrate = new Vector<Integer>();
        typesToMigrate.add(ObjectType.THREAT_STRESS_RATING_DATA);
        typesToMigrate.add(ObjectType.THREAT_SIMPLE_RATING_DATA);

        return typesToMigrate;
    }

    private class ThreatRatingCommentsDataVisitor extends AbstractMigrationORefVisitor
    {
        public ThreatRatingCommentsDataVisitor(int typeToVisit, boolean reverseMigration)
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
                    migrationResult = mergeThreatRatingComments(rawObjectRef, rawObject);
                else
                    migrationResult = splitThreatRatingComments(rawObjectRef, rawObject);
            }

            return migrationResult;
        }

        private MigrationResult splitThreatRatingComments(ORef rawObjectRef, RawObject rawObject) throws Exception
        {
            MigrationResult migrationResult = MigrationResult.createSuccess();

            if (threatRatingCommentsShouldBeSplit(rawObject))
            {
                String commentsMapTag = getLegacyThreatRatingCommentsMapTag(rawObject.getObjectType());
                if (!commentsMapTag.isEmpty())
                {
                    String commentsMapAsString = safeGetTag(rawObject, commentsMapTag);
                    CodeToUserStringMap commentsStringMap = new CodeToUserStringMap(commentsMapAsString);
                    Set<String> codes = commentsStringMap.getCodes();
                    for(String code : codes)
                    {
                        // ignore empty (nee invalid) codes
                        if (code.isEmpty())
                            continue;

                        // ugly regex to work around lack of delimiter in comments map key
                        String[] keyRefs = code.split("(?<=})");

                        if (keyRefs.length != 2)
                            throw new Exception("splitThreatRatingComments failed for raw object: " + rawObjectRef);

                        EnhancedJsonObject threatRefAsString = new EnhancedJsonObject(keyRefs[0]);
                        ORef threatRef = new ORef(threatRefAsString);
                        if (threatRef.isValid() && threatRef.getObjectType() != ObjectType.CAUSE)
                            throw new Exception("splitThreatRatingComments failed for threat ref: " + threatRef);

                        if (threatRef.isInvalid() || getRawProject().findObject(threatRef) == null)
                            continue;

                        EnhancedJsonObject targetRefAsString = new EnhancedJsonObject(keyRefs[1]);
                        ORef targetRef = new ORef(targetRefAsString);
                        if (targetRef.isValid() && !(targetRef.getObjectType() == ObjectType.TARGET || targetRef.getObjectType() == ObjectType.HUMAN_WELFARE_TARGET))
                            throw new Exception("splitThreatRatingComments failed for target ref: " + targetRef);

                        if (targetRef.isInvalid() || getRawProject().findObject(targetRef) == null)
                            continue;

                        String comments = commentsStringMap.getUserString(code);
                        if (!comments.isEmpty())
                        {
                            ORef threatRatingDataRef = getRawProject().createObject(rawObject.getObjectType());
                            RawObject threatRatingData = getRawProject().findObject(threatRatingDataRef);
                            threatRatingData.setData(TAG_THREAT_REF, threatRef.toJson().toString());
                            threatRatingData.setData(TAG_TARGET_REF, targetRef.toJson().toString());
                            threatRatingData.setData(TAG_COMMENTS, comments);
                        }
                    }
                }

                getRawProject().deleteRawObject(rawObjectRef);
            }

            return migrationResult;
        }

        private MigrationResult mergeThreatRatingComments(ORef rawObjectRef, RawObject rawObject) throws Exception
        {
            MigrationResult migrationResult = MigrationResult.createSuccess();

            if (threatRatingCommentsShouldBeMerged(rawObject))
            {
                String commentsMapTag = getLegacyThreatRatingCommentsMapTag(rawObject.getObjectType());
                if (!commentsMapTag.isEmpty())
                {
                    String threatRefAsRawString = safeGetTag(rawObject, TAG_THREAT_REF);
                    EnhancedJsonObject threatRefAsString = new EnhancedJsonObject(threatRefAsRawString);
                    ORef threatRef = new ORef(threatRefAsString);
                    if (threatRef.isValid() && threatRef.getObjectType() != ObjectType.CAUSE)
                        throw new Exception("mergeThreatRatingComments failed for threat ref: " + threatRef);

                    String targetRefAsRawString = safeGetTag(rawObject, TAG_TARGET_REF);
                    EnhancedJsonObject targetRefAsString = new EnhancedJsonObject(targetRefAsRawString);
                    ORef targetRef = new ORef(targetRefAsString);
                    if (targetRef.isValid() && !(targetRef.getObjectType() == ObjectType.TARGET || targetRef.getObjectType() == ObjectType.HUMAN_WELFARE_TARGET))
                        throw new Exception("mergeThreatRatingComments failed for target ref: " + targetRef);

                    String comments = safeGetTag(rawObject, TAG_COMMENTS);

                    boolean threatIsValid = threatRef.isValid() && (getRawProject().findObject(threatRef) != null);
                    boolean targetIsValid = targetRef.isValid() && (getRawProject().findObject(targetRef) != null);

                    if (!comments.isEmpty() && threatIsValid && targetIsValid)
                    {
                        RawObject threatRatingDataSingleton = findOrCreateThreatRatingDataForReverseMigration(rawObject.getObjectType());
                        String commentsMapAsString = safeGetTag(threatRatingDataSingleton, commentsMapTag);
                        CodeToUserStringMap commentsStringMap = new CodeToUserStringMap(commentsMapAsString);
                        String code = createLegacyThreatRatingDataMapKey(threatRef, targetRef);
                        if (!commentsStringMap.contains(code))
                            commentsStringMap.putUserString(code, comments);
                        threatRatingDataSingleton.setData(commentsMapTag, commentsStringMap.toJsonString());
                    }
                }

                getRawProject().deleteRawObject(rawObjectRef);
            }

            return migrationResult;
        }

        private boolean threatRatingCommentsShouldBeSplit(RawObject rawObject)
        {
            if (getTypeToVisit() == ObjectType.THREAT_STRESS_RATING_DATA && rawObject.containsKey(TAG_STRESS_BASED_THREAT_RATING_COMMENTS_MAP))
                return true;

            if (getTypeToVisit() == ObjectType.THREAT_SIMPLE_RATING_DATA && rawObject.containsKey(TAG_SIMPLE_THREAT_RATING_COMMENTS_MAP))
                return true;

            return false;
        }

        private boolean threatRatingCommentsShouldBeMerged(RawObject rawObject)
        {
            if (getTypeToVisit() == ObjectType.THREAT_STRESS_RATING_DATA && !rawObject.containsKey(TAG_STRESS_BASED_THREAT_RATING_COMMENTS_MAP))
                return true;

            if (getTypeToVisit() == ObjectType.THREAT_SIMPLE_RATING_DATA && !rawObject.containsKey(TAG_SIMPLE_THREAT_RATING_COMMENTS_MAP))
                return true;

            return false;
        }

        private String getLegacyThreatRatingCommentsMapTag(int objectType)
        {
            if (objectType == ObjectType.THREAT_STRESS_RATING_DATA)
                return TAG_STRESS_BASED_THREAT_RATING_COMMENTS_MAP;

            if (objectType == ObjectType.THREAT_SIMPLE_RATING_DATA)
                return TAG_SIMPLE_THREAT_RATING_COMMENTS_MAP;

            return "";
        }

        private String safeGetTag(RawObject rawObject, String tag)
        {
            if (rawObject.hasValue(tag))
                return rawObject.getData(tag);

            return "";
        }

        private RawObject findOrCreateThreatRatingDataForReverseMigration(int objectType)
        {
            RawObject threatRatingDataSingleton = null;
            String commentsMapTag = getLegacyThreatRatingCommentsMapTag(objectType);
            RawPool threatRatingDataPool = getRawProject().getRawPoolForType(objectType);

            for (ORef threatRatingDataRef : threatRatingDataPool.getSortedReflist())
            {
                RawObject threatRatingData = getRawProject().findObject(threatRatingDataRef);
                if (threatRatingData.hasValue(commentsMapTag))
                {
                    threatRatingDataSingleton = threatRatingData;
                    break;
                }
            }

            if (threatRatingDataSingleton == null)
            {
                ORef threatRatingDataToReturnRef = getRawProject().createObject(objectType);
                threatRatingDataSingleton = getRawProject().findObject(threatRatingDataToReturnRef);
                threatRatingDataSingleton.setData(commentsMapTag, "");
            }

            return threatRatingDataSingleton;
        }

        private String createLegacyThreatRatingDataMapKey(ORef threatRef, ORef targetRef)
        {
            return threatRef.toString() + targetRef.toString();
        }

        private int type;
        private boolean isReverseMigration;
    }

    public static final int VERSION_FROM = 63;
    public static final int VERSION_TO = 64;

    public static final String TAG_THREAT_REF = "ThreatRef";
    public static final String TAG_TARGET_REF = "TargetRef";
    public static final String TAG_COMMENTS = "Comments";
    public static final String TAG_SIMPLE_THREAT_RATING_COMMENTS_MAP = "SimpleThreatRatingCommentsMap";
    public static final String TAG_STRESS_BASED_THREAT_RATING_COMMENTS_MAP = "StressBasedThreatRatingCommentsMap";
}