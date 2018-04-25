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
import org.miradi.migrations.AbstractMigrationVisitor;
import org.miradi.migrations.AbstractSingleTypeMigration;
import org.miradi.migrations.MigrationResult;
import org.miradi.migrations.RawObject;
import org.miradi.migrations.RawProject;
import org.miradi.schemas.DiagramFactorSchema;

import java.util.HashMap;

public class MigrationTo67 extends AbstractSingleTypeMigration
{
    public MigrationTo67(RawProject rawProjectToUse)
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
        final ReverseDiagramFactorVisitor visitor = new ReverseDiagramFactorVisitor();

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
        return EAM.text("This migration downgrades any background color choices not supported in the earlier version of Miradi.");
    }

    abstract private class AbstractDiagramFactorVisitor extends AbstractMigrationVisitor
    {
        public AbstractDiagramFactorVisitor()
        {
            oldToNewColorCodeMap = createNewToOldColorCodeMap();
        }

        public int getTypeToVisit()
        {
            return DiagramFactorSchema.getObjectType();
        }

        @Override
        public MigrationResult internalVisit(RawObject rawObject) throws Exception
        {
            return migrate(rawObject);
        }

        abstract protected MigrationResult migrate(RawObject rawObject) throws Exception;

        protected String safeGetTag(RawObject rawObject, String tag)
        {
            if (rawObject.hasValue(tag))
                return rawObject.getData(tag);

            return "";
        }

        protected HashMap<String, String> createNewToOldColorCodeMap()
        {
            HashMap<String, String> newToOldColorCodeMap = new HashMap<String, String>();

            newToOldColorCodeMap.put(TARGET_LIGHT_GREEN_COLOR_CODE, LIGHT_GREEN_COLOR_CODE);
            newToOldColorCodeMap.put(HWB_TARGET_LIGHT_TAN_COLOR_CODE, BROWN_COLOR_CODE);
            newToOldColorCodeMap.put(BIOPHYSICAL_FACTOR_LIGHT_OLIVE_COLOR_CODE, TAN_COLOR_CODE);
            newToOldColorCodeMap.put(THREAT_LIGHT_PINK_COLOR_CODE, PINK_COLOR_CODE);
            newToOldColorCodeMap.put(CONTRIBUTING_FACTOR_LIGHT_ORANGE_COLOR_CODE, ORANGE_COLOR_CODE);
            newToOldColorCodeMap.put(STRATEGY_ACTIVITY_LIGHT_YELLOW_COLOR_CODE, LIGHT_YELLOW_COLOR_CODE);
            newToOldColorCodeMap.put(BIOPHYSICAL_RESULT_LIGHT_LAVENDER_COLOR_CODE, DARK_BLUE_COLOR_CODE);
            newToOldColorCodeMap.put(THREAT_REDUCTION_RESULT_LIGHT_PURPLE_COLOR_CODE, LIGHT_PURPLE_COLOR_CODE);
            newToOldColorCodeMap.put(INTERMEDIATE_RESULT_LIGHT_BLUE_COLOR_CODE, LIGHT_BLUE_COLOR_CODE);
            newToOldColorCodeMap.put(OBJECTIVE_GOAL_BLUE_COLOR_CODE, LIGHT_BLUE_COLOR_CODE);
            newToOldColorCodeMap.put(INDICATOR_PURPLE_COLOR_CODE, DARK_PURPLE_COLOR_CODE);

            return newToOldColorCodeMap;
        }

        protected HashMap<String, String> oldToNewColorCodeMap;
    }

    private class ReverseDiagramFactorVisitor extends AbstractDiagramFactorVisitor
    {
        @Override
        protected MigrationResult migrate(RawObject rawObject) throws Exception
        {
            String bgColor = safeGetTag(rawObject, TAG_BACKGROUND_COLOR);
            if (bgColor.length() > 0 && oldToNewColorCodeMap.containsKey(bgColor))
                rawObject.setData(TAG_BACKGROUND_COLOR, oldToNewColorCodeMap.get(bgColor));

            return MigrationResult.createSuccess();
        }
    }

    public static final int VERSION_FROM = 66;
    public static final int VERSION_TO = 67;

    public static final String TAG_BACKGROUND_COLOR = "BackgroundColor";

    public static final String TARGET_LIGHT_GREEN_COLOR_CODE = "TargetLightGreen";
    public static final String HWB_TARGET_LIGHT_TAN_COLOR_CODE = "HWBTargetLightTan";
    public static final String BIOPHYSICAL_FACTOR_LIGHT_OLIVE_COLOR_CODE = "BiophysicalFactorLightOlive";
    public static final String THREAT_LIGHT_PINK_COLOR_CODE = "ThreatLightPink";
    public static final String CONTRIBUTING_FACTOR_LIGHT_ORANGE_COLOR_CODE = "ContributingFactorLightOrange";
    public static final String STRATEGY_ACTIVITY_LIGHT_YELLOW_COLOR_CODE = "StrategyActivityLightYellow";
    public static final String BIOPHYSICAL_RESULT_LIGHT_LAVENDER_COLOR_CODE = "BiophysicalResultLightLavender";
    public static final String THREAT_REDUCTION_RESULT_LIGHT_PURPLE_COLOR_CODE = "ThreatReductionResultLightPurple";
    public static final String INTERMEDIATE_RESULT_LIGHT_BLUE_COLOR_CODE = "IntermediateResultLightBlue";
    public static final String OBJECTIVE_GOAL_BLUE_COLOR_CODE = "ObjectiveGoalBlue";
    public static final String INDICATOR_PURPLE_COLOR_CODE = "IndicatorPurple";

    public static final String PINK_COLOR_CODE = "Pink";
    public static final String ORANGE_COLOR_CODE = "Orange";
    public static final String LIGHT_YELLOW_COLOR_CODE = "LightYellow";
    public static final String LIGHT_GREEN_COLOR_CODE = "LightGreen";
    public static final String LIGHT_BLUE_COLOR_CODE = "LightBlue";
    public static final String LIGHT_PURPLE_COLOR_CODE = "LightPurple";
    public static final String TAN_COLOR_CODE = "Tan";

    public static final String DARK_BLUE_COLOR_CODE = "DarkBlue";
    public static final String DARK_PURPLE_COLOR_CODE = "DarkPurple";
    public static final String BROWN_COLOR_CODE = "Brown";
}

