/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.summary;

import org.conservationmeasures.eam.dialogs.base.ObjectPoolTableModel;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.ProjectResource;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.questions.ChoiceQuestion;
import org.conservationmeasures.eam.questions.ResourceRoleQuestion;

public class TeamPoolTableModel extends ObjectPoolTableModel
{
	public TeamPoolTableModel(Project project)
	{
		super(project, ObjectType.PROJECT_RESOURCE, COLUMN_TAGS);
	}

	public ORefList getLatestRefListFromProject()
	{
		return getProject().getResourcePool().getTeamMemberRefs();
	}
	
	public ChoiceQuestion getColumnQuestion(int column)
	{
		if (isRolesColumn(column))
			return getProject().getQuestion(ResourceRoleQuestion.class);
		
		return super.getColumnQuestion(column);
	}

	public boolean isCodeListColumn(int column)
	{
		if (isRolesColumn(column))
			return true;
		
		return false;
	}
	
	private boolean isRolesColumn(int column)
	{
		return getColumnTag(column).equals(ProjectResource.TAG_ROLE_CODES);
	}
		
	private static final String[] COLUMN_TAGS = new String[] {
		ProjectResource.TAG_GIVEN_NAME,
		ProjectResource.TAG_SUR_NAME,
		ProjectResource.TAG_INITIALS,
		ProjectResource.TAG_ORGANIZATION,
		ProjectResource.TAG_POSITION,
		ProjectResource.TAG_ROLE_CODES
	};
}
