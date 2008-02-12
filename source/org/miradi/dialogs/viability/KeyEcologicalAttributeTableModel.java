/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.viability;

import org.miradi.dialogs.base.ObjectPoolTableModel;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.KeyEcologicalAttribute;
import org.miradi.project.Project;

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
