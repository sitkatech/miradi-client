/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.activity;

import org.miradi.dialogs.base.ObjectPoolTableModel;
import org.miradi.ids.IdList;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.Strategy;
import org.miradi.objects.Task;
import org.miradi.project.Project;

public class ShareableActivityPoolTableModel extends ObjectPoolTableModel
{
	public ShareableActivityPoolTableModel(Project projectToUse, ORef parentRefToUse)
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
		Strategy  parentStrategy = (Strategy) getProject().findObject(parentRef);
		ORefList parentActivityRefs = parentStrategy.getActivities();
		ORefList taskRefs = super.getLatestRefListFromProject();
		for (int i = 0; i < taskRefs.size(); ++i)
		{
			Task task = (Task) project.findObject(taskRefs.get(i));
			if (! task.isActivity())
				continue;
			
			if (parentActivityRefs.contains(task.getRef()))
				continue;
			
			filteredTaskRefs.add(taskRefs.get(i));
		}
				
		return filteredTaskRefs.convertToIdList(Task.getObjectType());
	}

	private ORef parentRef;
}
