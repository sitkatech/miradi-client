/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
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
