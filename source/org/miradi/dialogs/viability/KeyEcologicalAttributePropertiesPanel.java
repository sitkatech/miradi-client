/* 
Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
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

import org.miradi.actions.Actions;
import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.ids.BaseId;
import org.miradi.ids.KeyEcologicalAttributeId;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.KeyEcologicalAttribute;
import org.miradi.project.Project;

public class KeyEcologicalAttributePropertiesPanel extends ObjectDataInputPanel
{
	public KeyEcologicalAttributePropertiesPanel(Project projectToUse, Actions actions) throws Exception
	{
		this(projectToUse, actions, new KeyEcologicalAttributeId(BaseId.INVALID.asInt()));
	}
	
	public KeyEcologicalAttributePropertiesPanel(Project projectToUse, Actions actions, KeyEcologicalAttributeId idToShow) throws Exception
	{
		super(projectToUse, ObjectType.KEY_ECOLOGICAL_ATTRIBUTE, idToShow);

		addField(createStringField(KeyEcologicalAttribute.TAG_LABEL));
		updateFieldsFromProject();
	}

	public String getPanelDescription()
	{
		return EAM.text("Title|KeyEcologicalAttribute Properties");
	}

}
