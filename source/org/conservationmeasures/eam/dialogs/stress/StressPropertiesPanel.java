/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.stress;

import org.conservationmeasures.eam.dialogfields.ObjectDataInputField;
import org.conservationmeasures.eam.dialogs.base.ObjectDataInputPanel;
import org.conservationmeasures.eam.icons.StressIcon;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.Stress;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.questions.StressRatingChoiceQuestion;
import org.conservationmeasures.eam.questions.StressScopeChoiceQuestion;
import org.conservationmeasures.eam.questions.StressSeverityChoiceQuestion;

public class StressPropertiesPanel extends ObjectDataInputPanel
{
	public StressPropertiesPanel(Project projectToUse) throws Exception
	{
		super(projectToUse, ObjectType.STRESS, BaseId.INVALID);
	
		ObjectDataInputField shortLabelField = createShortStringField(Stress.TAG_SHORT_LABEL);
		ObjectDataInputField labelField = createStringField(Stress.TAG_LABEL);
		addFieldsOnOneLine(EAM.text("Stress"), new StressIcon(), new ObjectDataInputField[]{shortLabelField, labelField});
		
		addField(createMultilineField(Stress.TAG_DETAIL));
		
		ObjectDataInputField scopeField = createRatingChoiceField(Stress.getObjectType(), new StressScopeChoiceQuestion(Stress.TAG_SCOPE));
		ObjectDataInputField severityField = createRatingChoiceField(Stress.getObjectType(), new StressSeverityChoiceQuestion(Stress.TAG_SEVERITY));		
		addFieldsOnOneLine(EAM.text("Ratings"), new ObjectDataInputField[]{scopeField, severityField});
		
		addField(createReadOnlyChoiceField(Stress.getObjectType(), Stress.PSEUDO_STRESS_RATING, new StressRatingChoiceQuestion(Stress.PSEUDO_STRESS_RATING)));
		addField(createMultilineField(Stress.TAG_COMMENTS));
		updateFieldsFromProject();
	}

	public String getPanelDescription()
	{
		return EAM.text("Title|Stress Properties");
	}
}
