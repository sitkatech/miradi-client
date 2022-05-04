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

package org.miradi.dialogs.task;

import org.miradi.dialogs.activity.ActivityFactorVisibilityControlPanel;
import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;

public class ActivityPropertiesPanel extends TaskPropertiesPanel
{
	public ActivityPropertiesPanel(MainWindow mainWindow) throws Exception
	{
		super(mainWindow);
	}
	
	@Override
	protected ObjectDataInputPanel createDetailsPanel(MainWindow mainWindow, ActivityFactorVisibilityControlPanel activityVisibilityButtonPanel) throws Exception
	{
		return new ActivityDetailsPanelWithRelevancyOverrideFields(getProject(), mainWindow, activityVisibilityButtonPanel);
	}
	
	@Override
	protected boolean shouldHaveVisibilityPanel()
	{
		return true;
	}

	@Override
	public String getPanelDescription()
	{
		return EAM.text("Title|Activity Properties");
	}
}
