/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.summary;

import org.conservationmeasures.eam.dialogfields.ObjectDataInputField;
import org.conservationmeasures.eam.dialogs.base.ObjectDataInputPanel;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.ProjectMetadata;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.questions.CurrencyTypeQuestion;

public class SummaryPlanningSettingsPanel extends ObjectDataInputPanel
{
	public SummaryPlanningSettingsPanel(Project projectToUse)
	{
		super(projectToUse, projectToUse.getMetadata().getRef());
		
		ObjectDataInputField currency = createRatingChoiceField(new CurrencyTypeQuestion(ProjectMetadata.TAG_CURRENCY_TYPE));
		
		addField(currency);
		
		updateFieldsFromProject();
	}

	public String getPanelDescription()
	{
		return EAM.text("Planning Settings");
	}

}
