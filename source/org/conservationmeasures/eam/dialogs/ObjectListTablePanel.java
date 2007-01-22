/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs;

import org.conservationmeasures.eam.actions.MainWindowAction;
import org.conservationmeasures.eam.actions.ObjectsAction;
import org.conservationmeasures.eam.project.Project;

abstract public class ObjectListTablePanel extends ObjectTablePanelWithCreateAndDelete
{
	public ObjectListTablePanel(Project projectToUse, int objectTypeToUse, ObjectTableModel model, MainWindowAction createAction, ObjectsAction deleteAction)
	{
		super(projectToUse, objectTypeToUse, new ObjectListTable(model), createAction, deleteAction);
	}

	public void dispose()
	{
		super.dispose();
	}


}
