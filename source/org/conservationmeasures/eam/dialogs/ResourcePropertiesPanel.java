/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.ProjectResource;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.questions.BudgetCostUnitQuestion;
import org.conservationmeasures.eam.questions.ResourceRoleQuestion;

public class ResourcePropertiesPanel extends ObjectDataInputPanel
{
	public ResourcePropertiesPanel(Project projectToUse, BaseId idToEdit) throws Exception
	{
		super(projectToUse, ObjectType.PROJECT_RESOURCE, idToEdit);

		addField(createStringField(ProjectResource.TAG_INITIALS));
		addField(createStringField(ProjectResource.TAG_NAME));
		addField(createMultiCodeField(new ResourceRoleQuestion(ProjectResource.TAG_ROLE_CODES)));
		addField(createStringField(ProjectResource.TAG_ORGANIZATION));
		addField(createStringField(ProjectResource.TAG_POSITION));
		addField(createStringField(ProjectResource.TAG_PHONE_NUMBER));
		addField(createStringField(ProjectResource.TAG_EMAIL));
		addField(createRatingChoiceField(new BudgetCostUnitQuestion(ProjectResource.TAG_COST_UNIT)));
		addField(createNumericField(ProjectResource.TAG_COST_PER_UNIT));
		addField(createMultilineField(ProjectResource.TAG_COMMENTS));
		
		updateFieldsFromProject();
	}

	public String getPanelDescription()
	{
		return EAM.text("Title|Resource Properties");
	}
}
