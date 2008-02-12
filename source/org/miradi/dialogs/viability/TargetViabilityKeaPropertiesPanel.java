/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/
package org.miradi.dialogs.viability;

import org.miradi.actions.Actions;
import org.miradi.dialogfields.ObjectDataInputField;
import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.icons.KeyEcologicalAttributeIcon;
import org.miradi.ids.BaseId;
import org.miradi.ids.FactorId;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.KeyEcologicalAttribute;
import org.miradi.project.Project;
import org.miradi.questions.KeyEcologicalAttributeTypeQuestion;

public class TargetViabilityKeaPropertiesPanel extends ObjectDataInputPanel
{
	public TargetViabilityKeaPropertiesPanel(Project projectToUse, Actions actions) throws Exception
	{
		super(projectToUse, new ORef(ObjectType.TARGET, new FactorId(BaseId.INVALID.asInt())));		

		ObjectDataInputField shortLabelField = createShortStringField(KeyEcologicalAttribute.getObjectType(), KeyEcologicalAttribute.TAG_SHORT_LABEL);
		ObjectDataInputField labelField = createStringField(KeyEcologicalAttribute.getObjectType(), KeyEcologicalAttribute.TAG_LABEL);		
		addFieldsOnOneLine(EAM.text("KEA"), new KeyEcologicalAttributeIcon(), new ObjectDataInputField[]{shortLabelField, labelField});
		
		addField(createMultilineField(KeyEcologicalAttribute.getObjectType(), KeyEcologicalAttribute.TAG_DETAILS));
		addField(createChoiceField(KeyEcologicalAttribute.getObjectType(), KeyEcologicalAttribute.TAG_KEY_ECOLOGICAL_ATTRIBUTE_TYPE, new KeyEcologicalAttributeTypeQuestion()));
		addField(createMultilineField(KeyEcologicalAttribute.getObjectType(), KeyEcologicalAttribute.TAG_DESCRIPTION));
		
		updateFieldsFromProject();
	}
	
	public String getPanelDescription()
	{
		return EAM.text("Title|Key Ecological Attribute Properties");
	}
}
