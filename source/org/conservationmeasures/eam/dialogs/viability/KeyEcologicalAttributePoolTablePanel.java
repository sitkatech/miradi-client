/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.viability;

import org.conservationmeasures.eam.dialogs.base.ObjectPoolTablePanel;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.project.Project;

public class KeyEcologicalAttributePoolTablePanel extends ObjectPoolTablePanel
{
	public KeyEcologicalAttributePoolTablePanel(Project projectToUse)
	{
		super(projectToUse, ObjectType.KEY_ECOLOGICAL_ATTRIBUTE, new KeyEcologicalAttributeTableModel(projectToUse));
	}
}
