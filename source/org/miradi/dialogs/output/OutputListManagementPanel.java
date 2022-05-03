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
package org.miradi.dialogs.output;

import org.miradi.dialogs.base.ObjectListManagementPanel;
import org.miradi.icons.OutputIcon;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORefList;

import javax.swing.*;

public class OutputListManagementPanel extends ObjectListManagementPanel
{
    public static OutputListManagementPanel create(MainWindow mainWindow, ORefList selectionHierarchy) throws Exception
	{
		OutputListTablePanel tablePanel = new OutputListTablePanel(mainWindow, selectionHierarchy);
		return new OutputListManagementPanel(mainWindow, tablePanel);
	}

    public OutputListManagementPanel(MainWindow mainWindowToUse, OutputListTablePanel objectListPanel) throws Exception
    {
        super(mainWindowToUse, objectListPanel, new OutputPropertiesPanel(mainWindowToUse));
    }

    @Override
    public String getSplitterDescription()
    {
        return getPanelDescription() + SPLITTER_TAG;
    }

    @Override
    public String getPanelDescription()
    {
        return PANEL_DESCRIPTION;
    }

    @Override
    public Icon getIcon()
    {
        return new OutputIcon();
    }

    private static String PANEL_DESCRIPTION = EAM.text("Tab|Outputs");
}
