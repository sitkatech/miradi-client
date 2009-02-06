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
package org.miradi.dialogs.stress;

import org.miradi.actions.ActionHideStressBubble;
import org.miradi.actions.ActionShowStressBubble;
import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.ids.BaseId;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.utils.ObjectsActionButton;

public class StressFactorVisibilityControlPanel extends ObjectDataInputPanel
{
	public StressFactorVisibilityControlPanel(MainWindow mainWindow) throws Exception
	{
		super(mainWindow.getProject(), ObjectType.STRESS, BaseId.INVALID);
		
		ObjectsActionButton showStressBubble = createObjectsActionButton(mainWindow.getActions().getObjectsAction(ActionShowStressBubble.class), getPicker());
		add(showStressBubble);
		
		ObjectsActionButton hideStressBubble = createObjectsActionButton(mainWindow.getActions().getObjectsAction(ActionHideStressBubble.class), getPicker());
		add(hideStressBubble);
		
		updateFieldsFromProject();
	}

	public String getPanelDescription()
	{
		return EAM.text("Title|Stress Visibility");
	}
}
