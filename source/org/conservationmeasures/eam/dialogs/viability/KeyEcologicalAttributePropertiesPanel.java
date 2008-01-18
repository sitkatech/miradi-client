/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.viability;

import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.dialogs.base.ObjectDataInputPanel;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.KeyEcologicalAttributeId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.KeyEcologicalAttribute;
import org.conservationmeasures.eam.project.Project;

public class KeyEcologicalAttributePropertiesPanel extends ObjectDataInputPanel
{
	public KeyEcologicalAttributePropertiesPanel(Project projectToUse, Actions actions) throws Exception
	{
		this(projectToUse, actions, new KeyEcologicalAttributeId(BaseId.INVALID.asInt()));
	}
	
	public KeyEcologicalAttributePropertiesPanel(Project projectToUse, Actions actions, KeyEcologicalAttribute keyEcologicalAttribute) throws Exception
	{
		this(projectToUse, actions, (KeyEcologicalAttributeId)keyEcologicalAttribute.getId());
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
