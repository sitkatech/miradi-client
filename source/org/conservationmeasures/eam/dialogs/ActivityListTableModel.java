package org.conservationmeasures.eam.dialogs;

import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.Strategy;
import org.conservationmeasures.eam.objects.Task;
import org.conservationmeasures.eam.project.Project;

public class ActivityListTableModel extends ObjectListTableModel
{
	public ActivityListTableModel(Project projectToUse, FactorId nodeId)
	{
		super(projectToUse, ObjectType.MODEL_NODE, nodeId, Strategy.TAG_ACTIVITY_IDS, ObjectType.TASK, COLUMN_TAGS);
	}

	private static String[] COLUMN_TAGS = {
		Task.TAG_LABEL,
	};
}
