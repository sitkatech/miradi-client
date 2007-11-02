package org.conservationmeasures.eam.dialogs.base;

import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.project.Project;

public class ObjectListTablePanelWithParent extends ObjectTablePanelWithCreateAndDelete
{
	public ObjectListTablePanelWithParent(Project projectToUse, int objectTypeToUse, ObjectListTableModel model, Actions actions, Class[] buttonActionClasses)
	{
		super(projectToUse, objectTypeToUse, new ObjectTableWithParent(model), actions, buttonActionClasses);
	}
}
