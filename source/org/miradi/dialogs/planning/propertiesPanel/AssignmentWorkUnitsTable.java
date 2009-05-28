package org.miradi.dialogs.planning.propertiesPanel;

import java.util.Vector;

import javax.swing.Action;

import org.miradi.actions.ActionAssignResource;
import org.miradi.actions.ActionRemoveAssignment;
import org.miradi.main.MainWindow;

public class AssignmentWorkUnitsTable extends AssignmentDateUnitsTable
{
	public AssignmentWorkUnitsTable(MainWindow mainWindowToUse, AssignmentDateUnitsTableModel modelToUse) throws Exception
	{
		super(mainWindowToUse, modelToUse);
	}

	@Override
	public Vector<Action> getActionsForRightClickMenu(int tableColumn)
	{
		Vector<Action> actions = new Vector<Action>();
		actions.add(getActions().get(ActionAssignResource.class));
		actions.add(getActions().get(ActionRemoveAssignment.class));
		actions.add(null);
		actions.addAll(super.getActionsForRightClickMenu(tableColumn));
		return actions;
	}
}
