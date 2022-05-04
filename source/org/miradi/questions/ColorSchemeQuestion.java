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
package org.miradi.questions;

import org.miradi.main.EAM;

public class ColorSchemeQuestion extends StaticChoiceQuestion
{
    public ColorSchemeQuestion()
    {
        super(getColorSchemeChoices());
    }

    public static String LEGACY_COLOR_SCHEME = "";
    public static String NEW_45_COLOR_SCHEME = "Miradi45ColorScheme";

    private static ChoiceItem[] getColorSchemeChoices()
    {
        return new ChoiceItem[] {
                new ChoiceItem(LEGACY_COLOR_SCHEME, EAM.text("Legacy Color Scheme")),
                new ChoiceItem(NEW_45_COLOR_SCHEME, EAM.text("New Miradi 4.5 Color Scheme")),
        };
    }
}

