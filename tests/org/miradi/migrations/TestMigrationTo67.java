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

package org.miradi.migrations;

import org.miradi.migrations.forward.MigrationTo67;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.DiagramFactor;

import static org.miradi.questions.DiagramFactorBackgroundQuestion.*;

public class TestMigrationTo67 extends AbstractTestMigration
{
    public TestMigrationTo67(String name)
    {
        super(name);
    }

    public void testBackgroundColorChangedByReverseMigration() throws Exception
    {
        verifyBackgroundColor(TARGET_LIGHT_GREEN_COLOR_CODE, LIGHT_GREEN_COLOR_CODE);
        verifyBackgroundColor(HWB_TARGET_LIGHT_TAN_COLOR_CODE, BROWN_COLOR_CODE);
        verifyBackgroundColor(BIOPHYSICAL_FACTOR_LIGHT_OLIVE_COLOR_CODE, TAN_COLOR_CODE);
        verifyBackgroundColor(THREAT_LIGHT_PINK_COLOR_CODE, PINK_COLOR_CODE);
        verifyBackgroundColor(CONTRIBUTING_FACTOR_LIGHT_ORANGE_COLOR_CODE, ORANGE_COLOR_CODE);
        verifyBackgroundColor(STRATEGY_ACTIVITY_LIGHT_YELLOW_COLOR_CODE, LIGHT_YELLOW_COLOR_CODE);
        verifyBackgroundColor(BIOPHYSICAL_RESULT_LIGHT_LAVENDER_COLOR_CODE, DARK_BLUE_COLOR_CODE);
        verifyBackgroundColor(THREAT_REDUCTION_RESULT_LIGHT_PURPLE_COLOR_CODE, LIGHT_PURPLE_COLOR_CODE);
        verifyBackgroundColor(INTERMEDIATE_RESULT_LIGHT_BLUE_COLOR_CODE, LIGHT_BLUE_COLOR_CODE);
        verifyBackgroundColor(OBJECTIVE_GOAL_BLUE_COLOR_CODE, LIGHT_BLUE_COLOR_CODE);
        verifyBackgroundColor(INDICATOR_PURPLE_COLOR_CODE, DARK_PURPLE_COLOR_CODE);
    }

    private void verifyBackgroundColor(String backgroundColorCode, String expectedBackgroundColorCode) throws Exception
    {
        DiagramFactor diagramFactor = getProject().createDiagramFactorAndAddToDiagram(ObjectType.TEXT_BOX);

        getProject().fillObjectUsingCommand(diagramFactor, DiagramFactor.TAG_BACKGROUND_COLOR, backgroundColorCode);
        RawProject rawProject = reverseMigrate(new VersionRange(MigrationTo67.VERSION_TO));
        RawPool rawDiagramFactorPool = rawProject.getRawPoolForType(ObjectType.DIAGRAM_FACTOR);
        RawObject rawDiagramFactor = rawDiagramFactorPool.findObject(diagramFactor.getRef());

        assertEquals(rawDiagramFactor.getData(DiagramFactor.TAG_BACKGROUND_COLOR), expectedBackgroundColorCode);
    }

    @Override
    protected int getFromVersion()
    {
        return MigrationTo67.VERSION_FROM;
    }

    @Override
    protected int getToVersion()
    {
        return MigrationTo67.VERSION_TO;
    }
}

