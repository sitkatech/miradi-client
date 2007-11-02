package org.conservationmeasures.eam.dialogs.task;

import org.conservationmeasures.eam.dialogs.base.ObjectPoolTablePanel;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.project.Project;

public class ShareableMethodPoolTablePanel extends ObjectPoolTablePanel
{
	public ShareableMethodPoolTablePanel(Project project, ORef parentRef)
	{
		super(project, ObjectType.TASK, new ShareableMethodPoolTableModel(project, parentRef));
	}
}
