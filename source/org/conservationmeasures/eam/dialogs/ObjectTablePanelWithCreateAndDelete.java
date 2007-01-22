/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs;

import org.conservationmeasures.eam.actions.MainWindowAction;
import org.conservationmeasures.eam.actions.ObjectsAction;
import org.conservationmeasures.eam.project.Project;
import org.martus.swing.UiButton;

public class ObjectTablePanelWithCreateAndDelete extends ObjectTablePanel
{
	public ObjectTablePanelWithCreateAndDelete(Project projectToUse, int objectTypeToUse, ObjectTable tableToUse, MainWindowAction createAction, ObjectsAction deleteAction)
	{
		super(projectToUse, objectTypeToUse, tableToUse);

		addButton(new UiButton(createAction));
		addButton(deleteAction);
	}
	
}
