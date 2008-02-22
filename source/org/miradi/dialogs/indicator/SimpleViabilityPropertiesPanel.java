/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.indicator;

import org.miradi.dialogfields.ObjectDataInputField;
import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.dialogs.fieldComponents.PanelFieldLabel;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.Target;
import org.miradi.project.Project;
import org.miradi.questions.StatusQuestion;

public class SimpleViabilityPropertiesPanel extends ObjectDataInputPanel
{
	public SimpleViabilityPropertiesPanel(Project projectToUse, ORef refToUse) throws Exception
	{
		super(projectToUse, refToUse);			
		
		ObjectDataInputField targetRatingField = createRatingChoiceField(Target.TAG_TARGET_STATUS, new StatusQuestion());
		PanelFieldLabel ratingFieldLabel = new PanelFieldLabel(targetRatingField.getObjectType(), targetRatingField.getTag());
		addFieldWithCustomLabel(targetRatingField, ratingFieldLabel);
		
		ObjectDataInputField justificationField = createStringField(Target.TAG_CURRENT_STATUS_JUSTIFICATION);
		PanelFieldLabel justificationFieldLabel = new PanelFieldLabel(justificationField.getObjectType(), justificationField.getTag());
		addFieldWithCustomLabel(justificationField, justificationFieldLabel);
		
		updateFieldsFromProject();
	}

	@Override
	public String getPanelDescription()
	{
		return EAM.text("Title|Simple Viability");
	}
}
