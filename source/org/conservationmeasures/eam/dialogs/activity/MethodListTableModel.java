/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.activity;

import org.conservationmeasures.eam.dialogs.base.ObjectListTableModel;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.objects.Task;
import org.conservationmeasures.eam.project.Project;

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
