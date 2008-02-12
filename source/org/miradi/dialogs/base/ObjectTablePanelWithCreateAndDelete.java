/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.base;

import org.miradi.actions.Actions;
import org.miradi.actions.EAMAction;
import org.miradi.actions.ObjectsAction;
import org.miradi.dialogs.fieldComponents.PanelButton;
import org.miradi.project.Project;

public class ObjectTablePanelWithCreateAndDelete extends ObjectTablePanel
{
	public ObjectTablePanelWithCreateAndDelete(Project projectToUse, ObjectTable tableToUse, Actions actions, Class[] buttonActionClasses)
	{
		super(projectToUse, tableToUse);

		for (int i=0; i<buttonActionClasses.length; ++i)
		{
			EAMAction action = actions.get(buttonActionClasses[i]);
			if (action.isObjectAction())
				addButton((ObjectsAction) action);
			else
				addButton(new PanelButton(action));
		}
	}
	
}
