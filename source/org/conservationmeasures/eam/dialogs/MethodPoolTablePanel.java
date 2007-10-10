package org.conservationmeasures.eam.dialogs;

import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.project.Project;

public class MethodPoolTablePanel extends ObjectPoolTablePanel
{
	public MethodPoolTablePanel(Project project, ORef parentRef)
	{
		super(project, ObjectType.TASK, new ShareableMethodPoolTableModel(project, parentRef));
	}
}
