/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.viability;

import org.miradi.dialogs.base.ObjectListTableModel;
import org.miradi.ids.FactorId;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.Factor;
import org.miradi.objects.KeyEcologicalAttribute;
import org.miradi.project.Project;

public class KeyEcologicalAttributeListTableModel extends ObjectListTableModel
{
	public KeyEcologicalAttributeListTableModel(Project projectToUse, FactorId nodeId)
	{
		super(projectToUse, ObjectType.TARGET, nodeId, Factor.TAG_KEY_ECOLOGICAL_ATTRIBUTE_IDS, ObjectType.KEY_ECOLOGICAL_ATTRIBUTE, KeyEcologicalAttribute.TAG_LABEL);
	}
}
