/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.viability;

import org.conservationmeasures.eam.dialogs.base.ObjectListTable;
import org.conservationmeasures.eam.dialogs.base.ObjectTableModel;
import org.conservationmeasures.eam.dialogs.base.ObjectTablePanel;
import org.conservationmeasures.eam.project.Project;

public class KeyEcologicalAttributeListTablePanelWithoutButtons extends	ObjectTablePanel
{
	public KeyEcologicalAttributeListTablePanelWithoutButtons(Project projectToUse, ObjectTableModel modelToUse)
	{
		super(projectToUse, new ObjectListTable(modelToUse));
	}
}
