/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.workplan;

import org.conservationmeasures.eam.dialogs.ObjectPoolTableModel;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.Task;
import org.conservationmeasures.eam.project.Project;

public class ActivityPoolTableModel extends ObjectPoolTableModel
{
	public ActivityPoolTableModel(Project projectToUse)
	{
		super(projectToUse, ObjectType.TASK, Task.TAG_LABEL);
	}
}
