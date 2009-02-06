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

package org.miradi.dialogs.task;

import org.miradi.dialogs.activity.ActivityFactorVisibilityControlPanel;
import org.miradi.ids.BaseId;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;

public class ActivityPropertiesPanel extends TaskPropertiesPanel
{
	public static TaskPropertiesPanel createWithVisibilityPanel(MainWindow mainWindow) throws Exception
	{
		ActivityFactorVisibilityControlPanel visibilityPanel = new ActivityFactorVisibilityControlPanel(mainWindow);
		TaskPropertiesInputPanel inputPanelWithVisibilityPanel = new TaskPropertiesInputPanel(mainWindow, visibilityPanel, BaseId.INVALID);
		return new ActivityPropertiesPanel(mainWindow, inputPanelWithVisibilityPanel);
	}

	private ActivityPropertiesPanel(MainWindow mainWindow, TaskPropertiesInputPanel inputPanelWithVisibilityPanel) throws Exception
	{
		super(mainWindow, inputPanelWithVisibilityPanel);
	}

	public String getPanelDescription()
	{
		return EAM.text("Title|Activity Properties");
	}
		
	
}
