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
import org.miradi.utils.StringUtilities;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

public class MigrationTo86 extends AbstractMigration
{
    public MigrationTo86(RawProject rawProjectToUse)
    {
        super(rawProjectToUse);

        linkColorMap = createLinkColorMap();
        backgroundColorMap = createBackgroundColorMap();
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
            final DiagramColorVisitor visitor = new DiagramColorVisitor(typeToVisit, reverseMigration);
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
        return EAM.text("This migration replaces the diagram link and factor colors with their hex color codes.");
    }

    private Vector<Integer> getTypesToMigrate()
    {
        Vector<Integer> typesToMigrate = new Vector<Integer>();

        typesToMigrate.add(ObjectType.DIAGRAM_FACTOR);
        typesToMigrate.add(ObjectType.DIAGRAM_LINK);

        return typesToMigrate;
    }

    private BiDirectionalHashMap createLinkColorMap()
    {
        BiDirectionalHashMap map = new BiDirectionalHashMap();
        map.put("", "#000000");
        map.put("darkGray", "#4E4848");
        map.put("red", "#D31913");
        map.put("DarkOrange", "#FF6600");
        map.put("DarkYellow", "#FFCC00");
        map.put("darkGreen", "#007F00");
        map.put("darkBlue", "#0000CC");
        map.put("DarkPurple", "#9900FF");
        map.put("brown", "#C85A17");
        map.put("lightGray", "#6D7B8D");
        map.put("White", "#FFFFFF");
        map.put("pink", "#FF00FF");
        map.put("orange", "#FF8040");
        map.put("yellow", "#FFFFCC");
        map.put("lightGreen", "#5FFB17");
        map.put("lightBlue", "#00CCFF");
        map.put("LightPurple", "#CC99FF");
        map.put("tan", "#EDE275");

        return map;
    }

    private BiDirectionalHashMap createBackgroundColorMap()
    {
        BiDirectionalHashMap map = new BiDirectionalHashMap();
        map.put("", "#E6E6E6");
        map.put("TargetLightGreen", "#DAEDDA");
        map.put("HWBTargetLightTan", "#D8CBC0");
        map.put("BiophysicalFactorLightOlive", "#CECCB6");
        map.put("ThreatLightPink", "#D8B2B2");
        map.put("ContributingFactorLightOrange", "#EFD18E");
        map.put("StrategyActivityLightYellow", "#FDF9CE");
        map.put("BiophysicalResultLightLavender", "#BFBFE8");
        map.put("ThreatReductionResultLightPurple", "#D0B4DB");
        map.put("IntermediateResultLightBlue", "#B3DDE0");
        map.put("ObjectiveGoalBlue", "#CAE8EA");
        map.put("IndicatorPurple", "#AA6EAE");
        map.put("White", "#FFFFFF");
        map.put("Pink", "#FF00FF");
        map.put("Orange", "#FF8040");
        map.put("LightYellow", "#FFFFCD");
        map.put("LightGreen", "#5FFB17");
        map.put("LightBlue", "#00CCFF");
        map.put("LightPurple", "#CC99FF");
        map.put("Tan", "#EDE275");
        map.put("Black", "#000000");
        map.put("DarkGray", "#4E4848");
        map.put("Red", "#FF0000");
        map.put("DarkOrange", "#FF6600");
        map.put("DarkYellow", "#FFCC00");
        map.put("DarkGreen", "#008000");
        map.put("DarkBlue", "#0000CC");
        map.put("DarkPurple", "#9900FF");
        map.put("Brown", "#C85A17");

        return map;
    }

    private class DiagramColorVisitor extends AbstractMigrationORefVisitor
    {
        public DiagramColorVisitor(int typeToVisit, boolean reverseMigration)
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
                migrationResult = changeColorFields(rawObject, isReverseMigration);

            return migrationResult;
        }

        private MigrationResult changeColorFields(RawObject rawObject, boolean reverseMigration) throws Exception
        {
            MigrationResult migrationResult = MigrationResult.createSuccess();

            // link color
            String linkColor = safeGetTag(rawObject, TAG_COLOR);
            if (!StringUtilities.isNullOrEmpty(linkColor))
                rawObject.setData(TAG_COLOR, changeLinkColor(linkColor, reverseMigration));

            // foreground (font) color
            String foregroundColor = safeGetTag(rawObject, TAG_FOREGROUND_COLOR);
            if (!StringUtilities.isNullOrEmpty(foregroundColor))
                rawObject.setData(TAG_FOREGROUND_COLOR, changeForegroundColor(foregroundColor, reverseMigration));

            // background (shape) color
            String backgroundColor = safeGetTag(rawObject, TAG_BACKGROUND_COLOR);
            if (!StringUtilities.isNullOrEmpty(backgroundColor))
                rawObject.setData(TAG_BACKGROUND_COLOR, changeBackgroundColor(backgroundColor, reverseMigration));

            return migrationResult;
        }

        private String safeGetTag(RawObject rawObject, String tag)
        {
            if (rawObject.hasValue(tag))
                return rawObject.getData(tag);

            return "";
        }

        private String changeLinkColor(String color, boolean reverseMigration)
        {
            if (reverseMigration)
            {
                if (linkColorMap.containsValue(color.toUpperCase()))
                    return linkColorMap.getKey(color.toUpperCase());

                if (linkColorMap.containsKey(color))
                    return color;

                return DefaultLinkColor;
            }

            return linkColorMap.getValue(color);
        }

        private String changeForegroundColor(String color, boolean reverseMigration)
        {
            if (reverseMigration)
                if (!foregroundColorSet.contains(color.toUpperCase()))
                    return DefaultForegroundColor;

            return color;
        }

        private String changeBackgroundColor(String color, boolean reverseMigration)
        {
            if (reverseMigration)
            {
                if (backgroundColorMap.containsValue(color.toUpperCase()))
                    return backgroundColorMap.getKey(color.toUpperCase());

                if (backgroundColorMap.containsKey(color))
                    return color;

                return DefaultBackgroundColor;
            }

            return backgroundColorMap.getValue(color);
        }

        private int type;
        private boolean isReverseMigration;
    }

    public static final int VERSION_FROM = 85;
    public static final int VERSION_TO = 86;

    private static BiDirectionalHashMap linkColorMap;
    private static BiDirectionalHashMap backgroundColorMap;
    private static final Set<String> foregroundColorSet = new HashSet<String>(Arrays.asList("#4E4848", "#D31913", "#FF6600", "#FFCC00", "#007F00", "#0000CC", "#9900FF", "#C85A17", "#6D7B8D", "#FFFFFF", "#FF00FF", "#FF8040", "#FFFFCC", "#5FFB17", "#00CCFF", "#CC99FF", "#EDE275"));

    public static final String DefaultLinkColor = "";
    public static final String DefaultForegroundColor = "";
    public static final String DefaultBackgroundColor = "";

    public static final String TAG_COLOR = "Color";
    public static final String TAG_FOREGROUND_COLOR = "FontColor";
    public static final String TAG_BACKGROUND_COLOR = "BackgroundColor";
}
