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
package org.miradi.dialogs.goal;

import org.miradi.actions.ActionCloneGoal;
import org.miradi.actions.ActionCreateGoal;
import org.miradi.actions.ActionDeleteGoal;
import org.miradi.dialogs.base.ObjectListTablePanel;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORef;

public class GoalListTablePanel extends ObjectListTablePanel
{
	public GoalListTablePanel(MainWindow mainWindowToUse, ORef nodeRef)
	{
		super(mainWindowToUse, new GoalListTableModel(mainWindowToUse.getProject(), nodeRef), 
				buttonActionClasses);
	}
	
	static Class[] buttonActionClasses = new Class[] {
		ActionCreateGoal.class,
		ActionDeleteGoal.class,
		ActionCloneGoal.class
	};
}
