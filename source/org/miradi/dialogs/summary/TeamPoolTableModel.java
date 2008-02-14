/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.summary;

import org.miradi.dialogs.base.ObjectPoolTableModel;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.ProjectResource;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceQuestion;
import org.miradi.questions.ResourceRoleQuestion;

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
		ProjectResource.TAG_ROLE_CODES,
		ProjectResource.TAG_EMAIL,
		ProjectResource.TAG_PHONE_NUMBER,
	};
}
