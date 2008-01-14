/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/
package org.conservationmeasures.eam.dialogs.base;

import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.project.Project;

public class ObjectListTablePanelWithParent extends ObjectTablePanelWithCreateAndDelete
{
	public ObjectListTablePanelWithParent(Project projectToUse, int objectTypeToUse, ObjectListTableModel model, Actions actions, Class[] buttonActionClasses)
	{
		super(projectToUse, new ObjectTableWithParent(model), actions, buttonActionClasses);
	}
}
