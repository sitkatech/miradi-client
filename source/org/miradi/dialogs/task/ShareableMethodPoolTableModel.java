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

import org.miradi.dialogs.base.ObjectPoolTableModel;
import org.miradi.ids.IdList;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.Indicator;
import org.miradi.objects.Task;
import org.miradi.project.Project;

public class ShareableMethodPoolTableModel extends ObjectPoolTableModel
{
	public ShareableMethodPoolTableModel(Project projectToUse, ORef parentRefToUse)
	{
		super(projectToUse, ObjectType.TASK, COLUMN_TAGS);
		parentRef = parentRefToUse;
	}
	
	private static final String[] COLUMN_TAGS = new String[] {
		Task.TAG_LABEL,
	};
	
	public ORefList getLatestRefListFromProject()
	{
		return new ORefList(getRowObjectType(), getLatestIdListFromProject());
	}
	
	private IdList getLatestIdListFromProject()
	{
		ORefList filteredTaskRefs = new ORefList();
		Indicator  parentIndicator = (Indicator) getProject().findObject(parentRef);
		ORefList parentsMethodRefs = parentIndicator.getMethodRefs();
		ORefList taskRefs = super.getLatestRefListFromProject();
		for (int i = 0; i < taskRefs.size(); ++i)
		{
			Task task = (Task) project.findObject(taskRefs.get(i));
			if (! task.isMethod())
				continue;
			
			if (parentsMethodRefs.contains(task.getRef()))
				continue;
			
			filteredTaskRefs.add(taskRefs.get(i));
		}
				
		return filteredTaskRefs.convertToIdList(Task.getObjectType());
	}

	private ORef parentRef;
}
