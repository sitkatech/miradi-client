package org.conservationmeasures.eam.dialogs;

import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.project.Project;

public class ObjectivePoolTablePanel extends ObjectPoolTablePanel
{
	public ObjectivePoolTablePanel(Project projectToUse)
	{
		super(projectToUse, ObjectType.OBJECTIVE, 
				new ObjectivePoolTableModel(projectToUse)
		);
	}
}
