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
package org.miradi.questions;

import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.intellijthemes.*;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatAtomOneLightIJTheme;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatGitHubIJTheme;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatLightOwlIJTheme;
import org.miradi.main.EAM;

public class LookAndFeelThemeQuestion extends StaticChoiceQuestion
{
    public LookAndFeelThemeQuestion()
    {
        super(getLookAndFeelThemeChoices());
    }

    private static ChoiceItem[] getLookAndFeelThemeChoices()
    {
        return new ChoiceItem[] {
                new ChoiceItem(FlatLightLaf.class.getName(), EAM.text(FlatLightLaf.NAME)),
                new ChoiceItem(FlatIntelliJLaf.class.getName(), EAM.text(FlatIntelliJLaf.NAME)),
                new ChoiceItem(FlatArcIJTheme.class.getName(), EAM.text(FlatArcIJTheme.NAME)),
                new ChoiceItem(FlatCyanLightIJTheme.class.getName(), EAM.text(FlatCyanLightIJTheme.NAME)),
                new ChoiceItem(FlatGrayIJTheme.class.getName(), EAM.text(FlatGrayIJTheme.NAME)),
                new ChoiceItem(FlatLightFlatIJTheme.class.getName(), EAM.text(FlatLightFlatIJTheme.NAME)),
                new ChoiceItem(FlatSolarizedLightIJTheme.class.getName(), EAM.text(FlatSolarizedLightIJTheme.NAME)),
                new ChoiceItem(FlatAtomOneLightIJTheme.class.getName(), EAM.text(FlatAtomOneLightIJTheme.NAME)),
                new ChoiceItem(FlatGitHubIJTheme.class.getName(), EAM.text(FlatGitHubIJTheme.NAME)),
                new ChoiceItem(FlatLightOwlIJTheme.class.getName(), EAM.text(FlatLightOwlIJTheme.NAME)),
                new ChoiceItem(FlatSolarizedLightIJTheme.class.getName(), EAM.text(FlatSolarizedLightIJTheme.NAME)),
        };
    }
}

