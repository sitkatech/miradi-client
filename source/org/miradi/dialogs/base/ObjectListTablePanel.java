/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.base;

import org.miradi.actions.Actions;
import org.miradi.project.Project;

abstract public class ObjectListTablePanel extends ObjectTablePanelWithCreateAndDelete
{
	public ObjectListTablePanel(Project projectToUse, ObjectTableModel model, Actions actions, Class[] buttonActionClasses)
	{
		super(projectToUse, new ObjectListTable(model), actions,buttonActionClasses);
	}
}
