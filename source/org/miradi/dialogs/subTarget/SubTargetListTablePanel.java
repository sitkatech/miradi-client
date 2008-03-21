/* 
Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
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
package org.miradi.dialogs.subTarget;

import org.miradi.actions.ActionCreateSubTarget;
import org.miradi.actions.ActionDeleteSubTarget;
import org.miradi.actions.Actions;
import org.miradi.dialogs.base.ObjectListTablePanel;
import org.miradi.objecthelpers.ORef;
import org.miradi.project.Project;

public class SubTargetListTablePanel extends ObjectListTablePanel
{
	public SubTargetListTablePanel(Project projectToUse, Actions actions, ORef nodeRef)
	{
		super(projectToUse, new SubTargetListTableModel(projectToUse, nodeRef), actions, 
				buttonActionClasses);
	}
	
	static Class[] buttonActionClasses = new Class[] {
		ActionCreateSubTarget.class,
		ActionDeleteSubTarget.class,
	};
}
