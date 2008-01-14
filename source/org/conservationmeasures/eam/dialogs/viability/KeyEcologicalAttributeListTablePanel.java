/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.viability;

import org.conservationmeasures.eam.actions.ActionCreateKeyEcologicalAttribute;
import org.conservationmeasures.eam.actions.ActionDeleteKeyEcologicalAttribute;
import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.dialogs.base.ObjectListTablePanel;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.project.Project;

public class KeyEcologicalAttributeListTablePanel extends ObjectListTablePanel
{
	public KeyEcologicalAttributeListTablePanel(Project projectToUse, Actions actions, FactorId nodeId)
	{
		super(projectToUse, new KeyEcologicalAttributeListTableModel(projectToUse, nodeId), 
				actions, 
				buttonActionClasses);
	}
	
	static Class[] buttonActionClasses = new Class[] {
		ActionCreateKeyEcologicalAttribute.class,
		ActionDeleteKeyEcologicalAttribute.class
	};

}
