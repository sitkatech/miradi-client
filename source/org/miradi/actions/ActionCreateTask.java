/* 
Copyright 2005-2018, Foundations of Success, Bethesda, Maryland
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

import org.miradi.icons.TaskIcon;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;

public class ActionCreateTask extends ObjectsAction
{
	public ActionCreateTask(MainWindow mainWindowToUse)
	{
		this(mainWindowToUse, getLabel());
	}

	public ActionCreateTask(MainWindow mainWindowToUse, String labelToUse)
	{
		super(mainWindowToUse, labelToUse, new TaskIcon());
	}

	private static String getLabel()
	{
		return EAM.text("Action|Manage|Create Task");
	}

	@Override
	public String getToolTipText()
	{
		return EAM.text("TT|Create a child Task for the selected item");
	}
}
