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
package org.miradi.dialogs.activity;

import org.miradi.actions.ActionActivityMoveDown;
import org.miradi.actions.ActionActivityMoveUp;
import org.miradi.actions.ActionCreateActivity;
import org.miradi.actions.ActionDeleteActivity;
import org.miradi.actions.ActionShareActivity;
import org.miradi.dialogs.base.ObjectListTablePanel;
import org.miradi.main.CommandExecutedEvent;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.Strategy;
import org.miradi.views.umbrella.StaticPicker;

public class ActivityListTablePanel extends ObjectListTablePanel
{
	public ActivityListTablePanel(MainWindow mainWindowToUse, ORefList selectedHierarchy)
	{
		super(mainWindowToUse, new ActivityListTableModel(mainWindowToUse.getProject(), selectedHierarchy), new StaticPicker(selectedHierarchy));
		
		addObjectActionButton(ActionCreateActivity.class, getParentPicker());
		addUnknownTypeOfButton(ActionDeleteActivity.class);
		addUnknownTypeOfButton(ActionShareActivity.class);
		addUnknownTypeOfButton(ActionActivityMoveUp.class);
		addUnknownTypeOfButton(ActionActivityMoveDown.class);
	}
	
	@Override
	public void commandExecuted(CommandExecutedEvent event)
	{
		super.commandExecuted(event);
		
		if (event.isSetDataCommandWithThisTypeAndTag(Strategy.getObjectType(), Strategy.TAG_ACTIVITY_IDS))
		{
			ORef selectedObjectRef = getSelectedObject().getRef();
			getTable().getObjectTableModel().resetRows();
			getTable().setSelectedRow(selectedObjectRef);
		}
	}
}
