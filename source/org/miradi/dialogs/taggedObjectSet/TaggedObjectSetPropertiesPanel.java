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
package org.miradi.dialogs.taggedObjectSet;

import org.miradi.dialogfields.ObjectDataInputField;
import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.icons.TaggedObjectSetIcon;
import org.miradi.ids.BaseId;
import org.miradi.main.EAM;
import org.miradi.objects.TaggedObjectSet;
import org.miradi.project.Project;

public class TaggedObjectSetPropertiesPanel extends ObjectDataInputPanel
{
	public TaggedObjectSetPropertiesPanel(Project projectToUse) throws Exception
	{
		super(projectToUse, TaggedObjectSet.getObjectType(), BaseId.INVALID);
			
		ObjectDataInputField shortLabelField = createStringField(TaggedObjectSet.TAG_SHORT_LABEL, 10);
		ObjectDataInputField labelField = createExpandableField(TaggedObjectSet.TAG_LABEL);
		addFieldsOnOneLine(EAM.text("Tagged Object Set"), new TaggedObjectSetIcon(), new ObjectDataInputField[]{shortLabelField, labelField,});
		
		addField(createMultilineField(TaggedObjectSet.TAG_COMMENT));
		updateFieldsFromProject();
	}
	
	public String getPanelDescription()
	{
		return EAM.text("Title|Tagged Object Set Properties Panel");
	}
}
