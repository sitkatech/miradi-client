/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

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
package org.miradi.dialogs.diagram;

import org.miradi.actions.Actions;
import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.BaseObject;

public class ForecastEstimateSubPanel extends ObjectDataInputPanel
{
	public ForecastEstimateSubPanel(MainWindow mainWindow, Actions actions, ORef initialRef)
	{
		super(mainWindow.getProject(), initialRef);
		
		addField(createCurrencyField(initialRef.getObjectType(), BaseObject.TAG_BUDGET_COST_OVERRIDE));
		addField(createDateRangeChooserField(initialRef.getObjectType(), BaseObject.TAG_WHEN_OVERRIDE));
		addField(createEditableObjectListField(mainWindow, initialRef.getObjectType(),  BaseObject.TAG_WHO_OVERRIDE_REFS));
	}

	public String getPanelDescription()
	{
		return EAM.text("Estimate Panel");
	}
}
