/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs;

import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.KeyEcologicalAttributeId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.objects.KeyEcologicalAttribute;
import org.conservationmeasures.eam.project.Project;

public class TargetViabilityTreePropertiesPanel extends ObjectDataInputPanelNew
{
	public TargetViabilityTreePropertiesPanel(Project projectToUse, Actions actions) throws Exception
	{
		this(projectToUse, actions, new KeyEcologicalAttributeId(BaseId.INVALID.asInt()));
	}
	
	public TargetViabilityTreePropertiesPanel(Project projectToUse, Actions actions, KeyEcologicalAttribute keyEcologicalAttribute) throws Exception
	{
		this(projectToUse, actions, (KeyEcologicalAttributeId)keyEcologicalAttribute.getId());
	}
	
	public TargetViabilityTreePropertiesPanel(Project projectToUse, Actions actions, KeyEcologicalAttributeId idToShow) throws Exception
	{
		super(projectToUse, new ORef(ObjectType.KEY_ECOLOGICAL_ATTRIBUTE, idToShow));
		addField(createStringField(ObjectType.KEY_ECOLOGICAL_ATTRIBUTE, KeyEcologicalAttribute.TAG_LABEL));
		addField(createStringField(ObjectType.INDICATOR, Indicator.TAG_LABEL));
		updateFieldsFromProject();

	}

	public String getPanelDescription()
	{
		return EAM.text("Title|KeyEcologicalAttribute Properties");
	}

}
