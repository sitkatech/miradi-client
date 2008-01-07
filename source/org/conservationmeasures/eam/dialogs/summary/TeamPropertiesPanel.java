/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.summary;

import org.conservationmeasures.eam.dialogs.base.ObjectDataInputPanel;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.ProjectResource;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.questions.BudgetCostUnitQuestion;
import org.conservationmeasures.eam.questions.ResourceRoleQuestion;

public class TeamPropertiesPanel extends ObjectDataInputPanel
{
	public TeamPropertiesPanel(Project projectToUse) throws Exception
	{
		super(projectToUse, ProjectResource.getObjectType(), BaseId.INVALID);

		addField(createStringField(ProjectResource.TAG_NAME));
		addFieldWithCustomLabelAndHint(createStringField(ProjectResource.TAG_INITIALS,STD_SHORT), "(for people, use their initials)");
		addField(createMultiCodeField(new ResourceRoleQuestion(ProjectResource.TAG_ROLE_CODES), 3));
		addField(createStringField(ProjectResource.TAG_ORGANIZATION));
		addField(createStringField(ProjectResource.TAG_POSITION));
		addField(createStringField(ProjectResource.TAG_PHONE_NUMBER));
		addField(createStringField(ProjectResource.TAG_EMAIL));
		addField(createRatingChoiceField(new BudgetCostUnitQuestion(ProjectResource.TAG_COST_UNIT)));
		addField(createCurrencyField(ProjectResource.TAG_COST_PER_UNIT));
		addField(createMultilineField(ProjectResource.TAG_COMMENTS));
		
		addField(createStringField(ProjectResource.TAG_CUSTOM_FIELD_1));
		addField(createStringField(ProjectResource.TAG_CUSTOM_FIELD_2));
		
		updateFieldsFromProject();
	}

	public String getPanelDescription()
	{
		return EAM.text("Title|Team Properties");
	}
}
