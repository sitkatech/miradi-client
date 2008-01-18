/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.resource;

import org.conservationmeasures.eam.dialogs.base.ObjectPoolTableModel;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.ProjectResource;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.questions.ChoiceQuestion;
import org.conservationmeasures.eam.questions.ResourceTypeQuestion;

public class ResourcePoolTableModel extends ObjectPoolTableModel
{
	public ResourcePoolTableModel(Project project)
	{
		super(project, ObjectType.PROJECT_RESOURCE, COLUMN_TAGS);
	}

	public boolean isChoiceItemColumn(int column)
	{
		if(isResourceTypeColumn(column))
			return true;
		
		return super.isChoiceItemColumn(column);
	}

	private boolean isResourceTypeColumn(int column)
	{
		return COLUMN_TAGS[column].equals(ProjectResource.TAG_RESOURCE_TYPE);
	}
	
	public ChoiceQuestion getColumnQuestion(int column)
	{
		if(isResourceTypeColumn(column))
		{
			String fieldTag = COLUMN_TAGS[column];
			String fieldLabel = EAM.fieldLabel(ProjectResource.getObjectType(), fieldTag);
			return new ResourceTypeQuestion(fieldTag, fieldLabel);
		}
		return super.getColumnQuestion(column);
	}
	
	private static final String[] COLUMN_TAGS = new String[] {
		ProjectResource.TAG_RESOURCE_TYPE,
		ProjectResource.TAG_INITIALS,
		ProjectResource.TAG_GIVEN_NAME,
		ProjectResource.TAG_SUR_NAME,
		ProjectResource.TAG_ORGANIZATION,
		ProjectResource.TAG_POSITION,
		ProjectResource.TAG_PHONE_NUMBER,
		ProjectResource.TAG_EMAIL,
	};

}
