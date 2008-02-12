/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.viability;

import org.miradi.dialogs.base.ObjectListTable;
import org.miradi.dialogs.base.ObjectTableModel;
import org.miradi.dialogs.base.ObjectTablePanel;
import org.miradi.project.Project;

public class KeyEcologicalAttributeListTablePanelWithoutButtons extends	ObjectTablePanel
{
	public KeyEcologicalAttributeListTablePanelWithoutButtons(Project projectToUse, ObjectTableModel modelToUse)
	{
		super(projectToUse, new ObjectListTable(modelToUse));
	}
}
