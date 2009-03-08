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

import org.miradi.dialogs.base.ObjectListTableModel;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.Strategy;
import org.miradi.objects.Task;
import org.miradi.project.Project;

public class ActivityListTableModel extends ObjectListTableModel
{
	public ActivityListTableModel(Project projectToUse, ORefList selectedHierarchy)
	{
		super(projectToUse, selectedHierarchy, Strategy.TAG_ACTIVITY_IDS, ObjectType.TASK, COLUMN_TAGS);
	}

	private static String[] COLUMN_TAGS = {
		Task.TAG_SHORT_LABEL,
		Task.TAG_LABEL,
// TODO: Before we can add this column, we need to set up an appropriate 
// renderer that will right-align and indicate estimate vs. roll-up
//		Task.PSEUDO_TAG_BUDGET_TOTAL,
	};
}
