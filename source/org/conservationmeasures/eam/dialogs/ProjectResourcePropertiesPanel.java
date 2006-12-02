/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.dialogs;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.ProjectResource;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.ratings.BudgetCostUnitQuestion;
import org.conservationmeasures.eam.ratings.ResourceRoleQuestion;

public class ProjectResourcePropertiesPanel extends ObjectDataInputPanel
{
	public ProjectResourcePropertiesPanel(Project projectToUse, BaseId idToEdit) throws Exception
	{
		super(projectToUse, ObjectType.PROJECT_RESOURCE, idToEdit);

		addField(createStringField(ProjectResource.TAG_INITIALS));
		addField(createStringField(ProjectResource.TAG_NAME));
		addField(createMultiCodeField(new ResourceRoleQuestion(ProjectResource.TAG_ROLE_CODES)));
		addField(createStringField(ProjectResource.TAG_ORGANIZATION));
		addField(createStringField(ProjectResource.TAG_POSITION));
		addField(createStringField(ProjectResource.TAG_PHONE_NUMBER));
		addField(createStringField(ProjectResource.TAG_EMAIL));
		addField(createRatingField(new BudgetCostUnitQuestion(ProjectResource.TAG_COST_UNIT)));
		addField(createNumericField(ProjectResource.TAG_COST_PER_UNIT));
		
		updateFieldsFromProject();
	}

	public String getPanelDescription()
	{
		return EAM.text("Title|Resource Properties");
	}
}
