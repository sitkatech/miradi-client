/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs;

import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.actions.EAMAction;
import org.conservationmeasures.eam.actions.ObjectsAction;
import org.conservationmeasures.eam.dialogs.fieldComponents.PanelButton;
import org.conservationmeasures.eam.project.Project;

public class ObjectTablePanelWithCreateAndDelete extends ObjectTablePanel
{
	public ObjectTablePanelWithCreateAndDelete(Project projectToUse, int objectTypeToUse, ObjectTable tableToUse, Actions actions, Class[] buttons)
	{
		super(projectToUse, objectTypeToUse, tableToUse);

		for (int i=0; i<buttons.length; ++i)
		{
			EAMAction action = actions.get(buttons[i]);
			if (action.isObjectAction())
				addButton((ObjectsAction) action);
			else
				addButton(new PanelButton(action));
		}
	}
	
}
