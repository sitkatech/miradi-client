/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.viability;

import org.conservationmeasures.eam.dialogs.base.ObjectPoolTableModel;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.KeyEcologicalAttribute;
import org.conservationmeasures.eam.project.Project;

public class KeyEcologicalAttributeTableModel extends ObjectPoolTableModel
{
	public KeyEcologicalAttributeTableModel(Project projectToUse)
	{
		super(projectToUse, ObjectType.KEY_ECOLOGICAL_ATTRIBUTE, COLUMN_TAGS);
	}
	
	private static final String[] COLUMN_TAGS = new String[] {
		KeyEcologicalAttribute.TAG_LABEL,
	};
}
