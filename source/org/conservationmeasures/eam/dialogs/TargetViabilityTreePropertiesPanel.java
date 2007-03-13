/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs;

import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.objects.KeyEcologicalAttribute;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.questions.KeyEcologicalAttributeTypeQuestion;
import org.conservationmeasures.eam.questions.PriorityRatingQuestion;

public class TargetViabilityTreePropertiesPanel extends ObjectDataInputPanel
{
	public TargetViabilityTreePropertiesPanel(Project projectToUse, Actions actions) throws Exception
	{
		this(projectToUse, actions, new FactorId(BaseId.INVALID.asInt()));
	}
	
	public TargetViabilityTreePropertiesPanel(Project projectToUse, Actions actions, Factor factor) throws Exception
	{
		this(projectToUse, actions, factor.getId());
	}
	
	public TargetViabilityTreePropertiesPanel(Project projectToUse, Actions actions, BaseId idToShow) throws Exception
	{
		super(projectToUse, new ORef(ObjectType.FACTOR, idToShow));
		addField(createStringField(ObjectType.KEY_ECOLOGICAL_ATTRIBUTE, KeyEcologicalAttribute.TAG_LABEL));
		addField(createMultilineField(ObjectType.KEY_ECOLOGICAL_ATTRIBUTE, KeyEcologicalAttribute.TAG_DESCRIPTION));
		addField(createObjectChoiceField(ObjectType.KEY_ECOLOGICAL_ATTRIBUTE, new KeyEcologicalAttributeTypeQuestion(KeyEcologicalAttribute.TAG_KEY_ECOLOGICAL_ATTRIBUTE_TYPE)));
		addField(createStringField(ObjectType.INDICATOR, Indicator.TAG_LABEL));
		addField(createObjectStringMapTableField(ObjectType.INDICATOR,  new PriorityRatingQuestion(Indicator.TAG_INDICATOR_THRESHOLDS)));
		updateFieldsFromProject();

	}

	public String getPanelDescription()
	{
		return EAM.text("Title|KeyEcologicalAttribute Properties");
	}

}
