/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.summary;

import org.conservationmeasures.eam.dialogs.ObjectPoolTableModel;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.objectdata.CodeListData;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.ProjectResource;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.ratings.ResourceRoleQuestion;
import org.conservationmeasures.eam.utils.CodeList;

public class TeamModel extends ObjectPoolTableModel
{
	public TeamModel(Project projectToUse)
	{
		super(projectToUse, ObjectType.PROJECT_RESOURCE, COLUMN_TAGS);
	}
	
	public IdList getLatestIdListFromProject()
	{
		IdList filteredResources = new IdList();
		
		IdList resources = super.getLatestIdListFromProject();
		for (int i=0; i<resources.size(); ++i)
		{
			BaseId baseId = resources.get(i);
			CodeList listData = extractCodeList(baseId);
			
			if (listData.contains(ResourceRoleQuestion.TeamMemberRoleCode))
				filteredResources.add(baseId);
		}
		return filteredResources;
	}

	private CodeList extractCodeList(BaseId baseId)
	{
		ProjectResource resource = (ProjectResource)getProject().findObject(ObjectType.PROJECT_RESOURCE, baseId);
		String codes = resource.getData(ProjectResource.TAG_ROLE_CODES);
		CodeList listData = new CodeListData(codes).getCodeList();
		return listData;
	}
	
	private static final String[] COLUMN_TAGS = new String[] {
		ProjectResource.TAG_NAME,
	};
}