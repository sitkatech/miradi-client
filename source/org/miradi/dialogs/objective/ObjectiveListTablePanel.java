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
package org.miradi.dialogs.objective;

import org.miradi.actions.ActionCloneObjective;
import org.miradi.actions.ActionCreateObjective;
import org.miradi.actions.ActionDeleteObjective;
import org.miradi.dialogs.base.ObjectListTablePanel;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORef;
import org.miradi.views.umbrella.StaticPicker;

public class ObjectiveListTablePanel extends ObjectListTablePanel
{
	public ObjectiveListTablePanel(MainWindow mainWindowToUse, ORef nodeRef)
	{
		super(mainWindowToUse, new ObjectiveListTableModel(mainWindowToUse.getProject(), nodeRef), new StaticPicker(nodeRef), DEFAULT_SORT_COLUMN);
		
		addUnknownTypeOfButton(ActionCreateObjective.class, getParentPicker());
		addButton(ActionDeleteObjective.class);
		addButton(ActionCloneObjective.class);
	}
	
	private static final int DEFAULT_SORT_COLUMN = 0;
}
