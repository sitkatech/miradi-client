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
package org.miradi.actions;

import org.miradi.icons.AssumptionIcon;
import org.miradi.icons.IconManager;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;

import javax.swing.*;

public class ActionInsertAssumption extends LocationAction
{
    public ActionInsertAssumption(MainWindow mainWindow)
    {
        super(mainWindow, getLabel(), IconManager.getAssumptionIcon());
    }

    private static String getLabel()
    {
        return EAM.text("Action|Insert|Insert Assumption");
    }

    @Override
    public String getToolTipText()
    {
        return EAM.text("TT|Insert an Assumption");
    }

    @Override
    public Icon getDisabledIcon()
    {
        return AssumptionIcon.createDisabledIcon();
    }
}
