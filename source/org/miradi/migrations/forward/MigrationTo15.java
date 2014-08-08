/* 
Copyright 2005-2013, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

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

import org.miradi.ids.IdList;
import org.miradi.main.EAM;
import org.miradi.migrations.*;
import org.miradi.objectdata.ORefData;
import org.miradi.objecthelpers.*;

import java.util.Vector;

public class MigrationTo15 extends AbstractMigration
{
	public MigrationTo15(RawProject rawProjectToUse)
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
            final ChangeDefaultRelevancyForActivityVisitor visitor = new ChangeDefaultRelevancyForActivityVisitor(typeToVisit, reverseMigration);
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
		return EAM.text("This migration changes the default relevancy rules for Activities on Strategies related to Goals / Objectives.");
	}

    private Vector<Integer> getTypesToMigrate()
    {
        Vector<Integer> typesToMigrate = new Vector<Integer>();
        typesToMigrate.add(ObjectType.OBJECTIVE);
        typesToMigrate.add(ObjectType.GOAL);

        return typesToMigrate;
    }

    private class ChangeDefaultRelevancyForActivityVisitor extends AbstractMigrationORefVisitor
    {
        public ChangeDefaultRelevancyForActivityVisitor(int typeToVisit, boolean reverseMigration)
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
            migrateRawObject(rawObjectRef);
            return MigrationResult.createSuccess();
        }

        private void migrateRawObject(ORef rawObjectRef) throws Exception
        {
            RawObject rawGoalOrObjective = getRawProject().findObject(rawObjectRef);
            if (rawGoalOrObjective != null)
            {
                String relevancySetAsJsonString = rawGoalOrObjective.getData(DESIRE_TAG_RELEVANT_STRATEGY_ACTIVITY_SET);

                if (relevancySetAsJsonString == null)
                    relevancySetAsJsonString = "";

                RelevancyOverrideSet relevancySet = new RelevancyOverrideSet(relevancySetAsJsonString);

                ORefSet relatedStrategyRefs = getNonDraftStrategiesRelatedToGoalOrObjective(rawObjectRef);

                for(ORef strategyRef : relatedStrategyRefs)
                {
                    ORefList activityRefs = getRawObjectIdRefs(strategyRef, STRATEGY_TAG_ACTIVITY_IDS, ObjectType.TASK);
                    for (ORef activityRef : activityRefs)
                    {
                        if (isReverseMigration)
                        {
                            // revert to old relevancy rules
                            if (relevancySet.contains(activityRef))
                                relevancySet.remove(activityRef);
                            else
                                relevancySet.add(new RelevancyOverride(activityRef, true));
                        }
                        else
                        {
                            // migrate from old relevancy rules
                            // override meant relevant which is new default so can be removed
                            // lack of override meant irrelevant which we must now make explicit
                            if (relevancySet.contains(activityRef))
                                relevancySet.remove(activityRef);
                            else
                                relevancySet.add(new RelevancyOverride(activityRef, false));
                        }
                    }
                }

                rawGoalOrObjective.setData(DESIRE_TAG_RELEVANT_STRATEGY_ACTIVITY_SET, relevancySet.toString());
            }
        }

        private ORefSet getNonDraftStrategiesRelatedToGoalOrObjective(ORef rawGoalOrObjectiveRef) throws Exception
        {
            ORefSet relatedStrategyRefs = new ORefSet();

            ORef parentFactor = getParentFactor(rawGoalOrObjectiveRef);
            if (parentFactor != null)
            {
                if (isNonDraftStrategy(parentFactor))
                    relatedStrategyRefs.add(parentFactor);

                getRawProject().ensurePoolExists(ObjectType.FACTOR_LINK);
                ORefList factorLinksToCheck = getRawProject().getAllRefsForType(ObjectType.FACTOR_LINK);
                for (ORef factorLinkToCheck : factorLinksToCheck)
                {
                    RawObject rawFactorLink = getRawProject().findObject(factorLinkToCheck);
                    if (rawFactorLink != null)
                    {
                        ORef fromRef = getFactorLinkFromRef(rawFactorLink);
                        ORef toRef = getFactorLinkToRef(rawFactorLink);
                        boolean isBidirectionalLink = isFactorLinkBidirectional(rawFactorLink);
                        if (isBidirectionalLink)
                        {
                            if (fromRef.equals(parentFactor) && isNonDraftStrategy(toRef))
                                relatedStrategyRefs.add(toRef);
                        }
                        else
                        {
                            if (toRef.equals(parentFactor) && isNonDraftStrategy(fromRef))
                                relatedStrategyRefs.add(fromRef);
                        }
                    }
                }
            }

            return relatedStrategyRefs;
        }

        private boolean isFactorLinkBidirectional(RawObject rawFactorLink)
        {
            boolean isBidirectional = false;

            if (rawFactorLink.hasValue(FACTOR_LINK_TAG_BIDIRECTIONAL_LINK))
            {
                String bidirectionalLink = rawFactorLink.getData(FACTOR_LINK_TAG_BIDIRECTIONAL_LINK);
                isBidirectional = bidirectionalLink.equals(BOOLEAN_TRUE);
            }

            return isBidirectional;
        }

        private ORef getFactorLinkFromRef(RawObject rawFactorLink)
        {
            return getFactorLinkRef(rawFactorLink, FACTOR_LINK_TAG_FROM_REF);
        }

        private ORef getFactorLinkToRef(RawObject rawFactorLink)
        {
            return getFactorLinkRef(rawFactorLink, FACTOR_LINK_TAG_TO_REF);
        }

        private ORef getFactorLinkRef(RawObject rawFactorLink, String directionTag)
        {
            ORef linkRef = null;

            if (rawFactorLink.hasValue(directionTag))
            {
                ORefData linkRefData = new ORefData(rawFactorLink.getData(directionTag));
                linkRef = linkRefData.getRef();
            }

            return linkRef;
        }

        private ORef getParentFactor(ORef rawGoalOrObjectiveRef) throws Exception
        {
            int[] typesThatCanOwnGoalOrObjective = getTypesThatCanOwnGoalOrObjective(rawGoalOrObjectiveRef.getObjectType());

            for(int typeToCheck : typesThatCanOwnGoalOrObjective)
            {
                getRawProject().ensurePoolExists(typeToCheck);
                ORefList possibleParentFactorRefs = getRawProject().getAllRefsForType(typeToCheck);
                for(ORef possibleParentFactor : possibleParentFactorRefs)
                {
                    if (isParentFactor(possibleParentFactor, rawGoalOrObjectiveRef))
                    {
                        return possibleParentFactor;
                    }
                }
            }

            return null;
        }

        private int[] getTypesThatCanOwnGoalOrObjective(int rawObjectType)
        {
            if (rawObjectType == ObjectType.GOAL)
                return new int[] {
                        ObjectType.TARGET,
                        ObjectType.HUMAN_WELFARE_TARGET,
                };
            else if (rawObjectType == ObjectType.OBJECTIVE)
                return new int[] {
                        ObjectType.STRATEGY,
                        ObjectType.CAUSE,
                        ObjectType.INTERMEDIATE_RESULT,
                        ObjectType.THREAT_REDUCTION_RESULT,
                };
            else
                return new int[]{};
        }

        private boolean isParentFactor(ORef possibleParentFactor, ORef rawGoalOrObjectiveRef) throws Exception
        {
            if (rawGoalOrObjectiveRef.getObjectType() == ObjectType.GOAL)
            {
                ORefList goalRefs = getRawObjectIdRefs(possibleParentFactor, FACTOR_TAG_GOAL_IDS, rawGoalOrObjectiveRef.getObjectType());
                return goalRefs.contains(rawGoalOrObjectiveRef);
            }
            else if (rawGoalOrObjectiveRef.getObjectType() == ObjectType.OBJECTIVE)
            {
                ORefList objectiveRefs = getRawObjectIdRefs(possibleParentFactor, FACTOR_TAG_OBJECTIVE_IDS, rawGoalOrObjectiveRef.getObjectType());
                return objectiveRefs.contains(rawGoalOrObjectiveRef);
            }
            else
            {
                return false;
            }
        }

        private ORefList getRawObjectIdRefs(ORef rawObjectRef, String tag, int objectTypeForIds) throws Exception
        {
            ORefList idRefs = new ORefList();
            RawObject rawObject = getRawProject().findObject(rawObjectRef);
            if (rawObject != null)
            {
                if (rawObject.hasValue(tag)) {
                    IdList idList = new IdList(objectTypeForIds, rawObject.getData(tag));
                    idRefs = new ORefList(idList);
                }
            }
            return idRefs;
        }

        private boolean isNonDraftStrategy(ORef objectRef)
        {
            return objectRef.getObjectType() == ObjectType.STRATEGY && !isStatusDraft(objectRef);
        }

        private boolean isStatusDraft(ORef strategyRef)
        {
            boolean isDraft = false;

            RawObject rawStrategy = getRawProject().findObject(strategyRef);
            if (rawStrategy != null)
            {
                if (rawStrategy.hasValue(STRATEGY_TAG_STATUS))
                {
                    String strategyStatus = rawStrategy.getData(STRATEGY_TAG_STATUS);
                    isDraft = strategyStatus.equals(STRATEGY_STATUS_DRAFT_CODE);
                }
            }

            return isDraft;
        }

        private int type;
        private boolean isReverseMigration;

        private static final String DESIRE_TAG_RELEVANT_STRATEGY_ACTIVITY_SET = "RelevantStrategySet";

        private static final String STRATEGY_TAG_ACTIVITY_IDS = "ActivityIds";
        private static final String STRATEGY_TAG_STATUS = "Status";
        private static final String STRATEGY_STATUS_DRAFT_CODE = "Draft";

        private static final String FACTOR_TAG_GOAL_IDS = "GoalIds";
        private static final String FACTOR_TAG_OBJECTIVE_IDS = "ObjectiveIds";

        private static final String FACTOR_LINK_TAG_BIDIRECTIONAL_LINK = "BidirectionalLink";
        private static final String FACTOR_LINK_TAG_FROM_REF = "FromRef";
        private static final String FACTOR_LINK_TAG_TO_REF = "ToRef";

        private static final String BOOLEAN_TRUE = "1";
    }

    public static final int VERSION_FROM = 14;
    public static final int VERSION_TO = 15;

}
