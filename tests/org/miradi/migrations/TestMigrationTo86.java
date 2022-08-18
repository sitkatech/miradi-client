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

package org.miradi.migrations;

import org.miradi.diagram.renderers.FactorHtmlViewer;
import org.miradi.migrations.forward.MigrationTo75;
import org.miradi.migrations.forward.MigrationTo85;
import org.miradi.migrations.forward.MigrationTo86;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.DiagramFactor;
import org.miradi.objects.DiagramLink;
import org.miradi.project.ProjectSaverForTesting;
import org.miradi.questions.DiagramFactorBackgroundQuestion;
import org.miradi.questions.DiagramFactorFontColorQuestion;
import org.miradi.questions.DiagramLinkColorQuestion;

import java.awt.*;

public class TestMigrationTo86 extends AbstractTestMigration
{
    public TestMigrationTo86(String name)
    {
        super(name);
    }

    public void testColorsAfterForwardMigration() throws Exception
    {
        DiagramFactor diagramFactorFrom = createDiagramFactor(legacyForegroundColorString, legacyBackgroundColorString);
        DiagramFactor diagramFactorTo = createDiagramFactor(legacyForegroundColorString, legacyBackgroundColorString);
        DiagramLink diagramLink = createDiagramLink(diagramFactorFrom, diagramFactorTo, legacyLinkColorString);

        String projectAsString = ProjectSaverForTesting.createSnapShot(getProject(), new VersionRange(MigrationTo86.VERSION_FROM));
        final RawProject projectToMigrate = RawProjectLoader.loadProject(projectAsString);
        migrateProject(projectToMigrate, new VersionRange(MigrationTo86.VERSION_TO));

        RawObject rawDiagramFactorFromAfter = projectToMigrate.findObject(diagramFactorFrom.getRef());
        assertNotNull(rawDiagramFactorFromAfter);
        assertEquals(rawDiagramFactorFromAfter.getData(DiagramFactor.TAG_FOREGROUND_COLOR), legacyForegroundColorString);
        assertEquals(rawDiagramFactorFromAfter.getData(DiagramFactor.TAG_BACKGROUND_COLOR), legacyBackgroundColorHexString);

        RawObject rawDiagramFactorToAfter = projectToMigrate.findObject(diagramFactorTo.getRef());
        assertNotNull(rawDiagramFactorToAfter);
        assertEquals(rawDiagramFactorToAfter.getData(DiagramFactor.TAG_FOREGROUND_COLOR), legacyForegroundColorString);
        assertEquals(rawDiagramFactorToAfter.getData(DiagramFactor.TAG_BACKGROUND_COLOR), legacyBackgroundColorHexString);

        RawObject rawDiagramLinkAfter = projectToMigrate.findObject(diagramLink.getRef());
        assertNotNull(rawDiagramLinkAfter);
        assertEquals(rawDiagramLinkAfter.getData(DiagramLink.TAG_COLOR), legacyLinkColorHexString);
    }

    public void testColorsAfterReverseMigration() throws Exception
    {
        DiagramFactor diagramFactorFrom = createDiagramFactor(userEnteredColor, legacyBackgroundColorHexString);
        DiagramFactor diagramFactorTo = createDiagramFactor(legacyForegroundColorString, userEnteredColor);
        DiagramLink diagramLink = createDiagramLink(diagramFactorFrom, diagramFactorTo, userEnteredColor);

        RawProject rawProject = reverseMigrate(new VersionRange(MigrationTo85.VERSION_TO));

        RawObject rawDiagramFactorFromAfter = rawProject.findObject(diagramFactorFrom.getRef());
        assertNotNull(rawDiagramFactorFromAfter);
        assertEquals(rawDiagramFactorFromAfter.getData(DiagramFactor.TAG_FOREGROUND_COLOR), new DiagramFactorFontColorQuestion().getReadableAlternativeDefaultCode());
        assertEquals(rawDiagramFactorFromAfter.getData(DiagramFactor.TAG_BACKGROUND_COLOR), legacyBackgroundColorString);

        RawObject rawDiagramFactorToAfter = rawProject.findObject(diagramFactorTo.getRef());
        assertNotNull(rawDiagramFactorToAfter);
        assertEquals(rawDiagramFactorToAfter.getData(DiagramFactor.TAG_FOREGROUND_COLOR), legacyForegroundColorString);
        assertEquals(rawDiagramFactorToAfter.getData(DiagramFactor.TAG_BACKGROUND_COLOR), new DiagramFactorBackgroundQuestion().getReadableAlternativeDefaultCode());

        RawObject rawDiagramLinkAfter = rawProject.findObject(diagramLink.getRef());
        assertNotNull(rawDiagramLinkAfter);
        assertEquals(rawDiagramLinkAfter.getData(DiagramLink.TAG_COLOR).toLowerCase(), new DiagramLinkColorQuestion().getReadableAlternativeDefaultCode());
    }

    private DiagramFactor createDiagramFactor(String foregroundColor, String backgroundColor) throws Exception
    {
        DiagramFactor diagramFactor = getProject().createAndPopulateDiagramFactor();

		getProject().fillObjectUsingCommand(diagramFactor, DiagramFactor.TAG_FOREGROUND_COLOR, foregroundColor);
		getProject().fillObjectUsingCommand(diagramFactor, DiagramFactor.TAG_BACKGROUND_COLOR, backgroundColor);

        return diagramFactor;
    }

    private DiagramLink createDiagramLink(DiagramFactor diagramFactorFrom, DiagramFactor diagramFactorTo, String linkColor) throws Exception
    {
        ORef diagramLinkRef = getProject().createDiagramLink(diagramFactorFrom, diagramFactorTo);
        DiagramLink diagramLink = DiagramLink.find(getProject(), diagramLinkRef);

        getProject().fillObjectUsingCommand(diagramLink, DiagramLink.TAG_COLOR, linkColor);

        return diagramLink;
    }

    @Override
    protected int getFromVersion()
    {
        return MigrationTo86.VERSION_FROM;
    }

    @Override
    protected int getToVersion()
    {
        return MigrationTo86.VERSION_TO;
    }

    private final String userEnteredColor = "#123456";
    private final String legacyLinkColorString = DiagramLinkColorQuestion.DARK_BLUE_CODE;
    private final String legacyLinkColorHexString = DiagramFactorFontColorQuestion.DARK_BLUE_HEX;
    private final String legacyForegroundColorString = DiagramFactorFontColorQuestion.DARK_GRAY_HEX;
    private final String legacyBackgroundColorString = DiagramFactorBackgroundQuestion.RED_COLOR_CODE;
    private final String legacyBackgroundColorHexString = FactorHtmlViewer.convertColorToHTMLColor(Color.RED).toUpperCase();
}
