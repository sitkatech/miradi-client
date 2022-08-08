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
import org.miradi.utils.StringUtilities;

import java.util.Vector;

public class MigrationTo86 extends AbstractMigration
{
    public MigrationTo86(RawProject rawProjectToUse)
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
            {
                if (isReverseMigration)
                    migrationResult = changeColorFields(rawObject, false);
                else
                    migrationResult = changeColorFields(rawObject, true);
            }

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
                return defaultLinkColor;

            return color;
        }

        private String changeForegroundColor(String color, boolean reverseMigration)
        {
            if (reverseMigration)
                return defaultForegroundColor;

            return color;
        }

        private String changeBackgroundColor(String color, boolean reverseMigration)
        {
            if (reverseMigration)
                return defaultBackgroundColor;

            return color;
        }

        private int type;
        private boolean isReverseMigration;
    }

    // TODO: MS-2448 - need forward / backward maps for the following...
    // vocabulary_diagram_link_color = 'black'|'darkGray'|'red'|'DarkOrange'|'DarkYellow'|'darkGreen'|'darkBlue'|'DarkPurple'|'brown'|'lightGray'|'White'|'pink'|'orange'|'yellow'|'lightGreen'|'lightBlue'|'LightPurple'|'tan'
    // vocabulary_diagram_factor_background_color = 'LightGray'|'TargetLightGreen'|'HWBTargetLightTan'|'BiophysicalFactorLightOlive'|'ThreatLightPink'|'ContributingFactorLightOrange'|'StrategyActivityLightYellow'|'BiophysicalResultLightLavender'|'ThreatReductionResultLightPurple'|'IntermediateResultLightBlue'|'ObjectiveGoalBlue'|'IndicatorPurple'|'White'|'Pink'|'Orange'|'LightYellow'|'LightGreen'|'LightBlue'|'LightPurple'|'Tan'|'Black'|'DarkGray'|'Red'|'DarkOrange'|'DarkYellow'|'DarkGreen'|'DarkBlue'|'DarkPurple'|'Brown'
    // vocabulary_diagram_factor_foreground_color = '#000000'|'#4E4848'|'#D31913'|'#FF6600'|'#FFCC00'|'#007F00'|'#0000CC'|'#9900FF'|'#C85A17'|'#6D7B8D'|'#FFFFFF'|'#FF00FF'|'#FF8040'|'#FFFFCC'|'#5FFB17'|'#00CCFF'|'#CC99FF'|'#EDE275'

    public static final int VERSION_FROM = 85;
    public static final int VERSION_TO = 86;

    private static final String defaultLinkColor = "black";
    private static final String defaultForegroundColor = "#000000";
    private static final String defaultBackgroundColor = "LightGray";

    public static final String TAG_COLOR = "Color";
    public static final String TAG_FOREGROUND_COLOR = "FontColor";
    public static final String TAG_BACKGROUND_COLOR = "BackgroundColor";
}
