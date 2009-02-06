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

import org.miradi.dialogs.base.ObjectPoolTablePanel;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ObjectType;

public class GoalPoolTablePanel extends ObjectPoolTablePanel
{
	public GoalPoolTablePanel(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse, ObjectType.GOAL, 
				new GoalPoolTableModel(mainWindowToUse.getProject()), UNIQUE_IDENTIFIER
		);
	}
	
	public static final String UNIQUE_IDENTIFIER = "GoalPoolTablePanel";
}
