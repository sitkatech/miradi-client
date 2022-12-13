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
package org.miradi.dialogs.subassumption;

import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.dialogs.base.ObjectDataInputPanelWithSections;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.schemas.SubAssumptionSchema;

public abstract class AbstractSubAssumptionPropertiesPanel extends ObjectDataInputPanelWithSections
{
    protected AbstractSubAssumptionPropertiesPanel(MainWindow mainWindow) throws Exception
    {
        super(mainWindow.getProject(), SubAssumptionSchema.getObjectType());

        SubAssumptionFactorVisibilityControlPanel subAssumptionVisibilityButtonPanel = null;
        if (addFactorVisibilityControlPanel())
            subAssumptionVisibilityButtonPanel = new SubAssumptionFactorVisibilityControlPanel(mainWindow);

        addSubPanelWithTitledBorder(createDetailsPanel(mainWindow, subAssumptionVisibilityButtonPanel));
        updateFieldsFromProject();
    }

    protected ObjectDataInputPanel createDetailsPanel(MainWindow mainWindow, SubAssumptionFactorVisibilityControlPanel subAssumptionVisibilityButtonPanel) throws Exception
    {
        return new SubAssumptionDetailsPanel(getProject(), mainWindow, subAssumptionVisibilityButtonPanel);
    }

    protected boolean addFactorVisibilityControlPanel()
    {
        return true;
    }

	@Override
	public String getPanelDescription()
	{
		return EAM.text("Title|Subassumption Properties");
	}
}
