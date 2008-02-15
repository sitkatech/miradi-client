/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.stress;

import org.miradi.dialogfields.ObjectDataInputField;
import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.icons.StressIcon;
import org.miradi.ids.BaseId;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.Stress;
import org.miradi.project.Project;
import org.miradi.questions.StressRatingChoiceQuestion;
import org.miradi.questions.StressScopeChoiceQuestion;
import org.miradi.questions.StressSeverityChoiceQuestion;

public class StressPropertiesPanel extends ObjectDataInputPanel
{
	public StressPropertiesPanel(Project projectToUse) throws Exception
	{
		super(projectToUse, ObjectType.STRESS, BaseId.INVALID);
	
		ObjectDataInputField shortLabelField = createShortStringField(Stress.TAG_SHORT_LABEL);
		ObjectDataInputField labelField = createExpandableField(Stress.TAG_LABEL);
		addFieldsOnOneLine(EAM.text("Stress"), new StressIcon(), new ObjectDataInputField[]{shortLabelField, labelField});
		
		addField(createMultilineField(Stress.TAG_DETAIL));
		
		ObjectDataInputField scopeField = createRatingChoiceField(Stress.getObjectType(), Stress.TAG_SCOPE, new StressScopeChoiceQuestion());
		ObjectDataInputField severityField = createRatingChoiceField(Stress.getObjectType(), Stress.TAG_SEVERITY, new StressSeverityChoiceQuestion());		
		addFieldsOnOneLine(EAM.text("Ratings"), new ObjectDataInputField[]{scopeField, severityField});
		
		addField(createReadOnlyChoiceField(Stress.getObjectType(), Stress.PSEUDO_STRESS_RATING, new StressRatingChoiceQuestion()));
		addField(createMultilineField(Stress.TAG_COMMENTS));
		updateFieldsFromProject();
	}

	public String getPanelDescription()
	{
		return EAM.text("Title|Stress Properties");
	}
}
