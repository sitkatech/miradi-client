/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
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
