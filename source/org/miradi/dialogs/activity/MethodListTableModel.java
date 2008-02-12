/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.activity;

import org.miradi.dialogs.base.ObjectListTableModel;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.Indicator;
import org.miradi.objects.Task;
import org.miradi.project.Project;

public class MethodListTableModel extends ObjectListTableModel
{
	public MethodListTableModel(Project projectToUse, ORef nodeRef)
	{
		super(projectToUse, nodeRef, Indicator.TAG_TASK_IDS, ObjectType.TASK, COLUMN_TAGS);
	}

	private static String[] COLUMN_TAGS = {
		Task.TAG_LABEL,
	};

}
