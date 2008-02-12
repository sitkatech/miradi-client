/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.viability;

import org.miradi.dialogs.base.ObjectPoolTablePanel;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.project.Project;

public class KeyEcologicalAttributePoolTablePanel extends ObjectPoolTablePanel
{
	public KeyEcologicalAttributePoolTablePanel(Project projectToUse)
	{
		super(projectToUse, ObjectType.KEY_ECOLOGICAL_ATTRIBUTE, new KeyEcologicalAttributeTableModel(projectToUse));
	}
}
