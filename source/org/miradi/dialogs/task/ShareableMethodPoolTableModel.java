/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
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
		//FIXME include associated factor
	};
	
	public ORefList getLatestRefListFromProject()
	{
		return new ORefList(getRowObjectType(), getLatestIdListFromProject());
	}
	
	private IdList getLatestIdListFromProject()
	{
		ORefList filteredTaskRefs = new ORefList();
		Indicator  parentIndicator = (Indicator) getProject().findObject(parentRef);
		ORefList parentsMethodRefs = parentIndicator.getMethods();
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
