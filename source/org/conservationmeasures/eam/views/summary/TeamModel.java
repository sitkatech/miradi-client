/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.summary;

import org.conservationmeasures.eam.dialogs.base.ObjectPoolTableModel;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.ProjectResource;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.questions.ResourceRoleQuestion;
import org.conservationmeasures.eam.utils.CodeList;

public class TeamModel extends ObjectPoolTableModel
{

	public TeamModel(Project projectToUse)
	{
		super(projectToUse, ObjectType.PROJECT_RESOURCE, COLUMN_TAGS);
	}
	
	public ORefList getLatestRefListFromProject()
	{
		return new ORefList(getRowObjectType(), getLatestIdListFromProject());
	}
	
	private IdList getLatestIdListFromProject()
	{
		IdList filteredResources = new IdList();
		
		ORefList resourceRefs = super.getLatestRefListFromProject();
		for (int i=0; i<resourceRefs.size(); ++i)
		{
			BaseId baseId = resourceRefs.get(i).getObjectId();
			CodeList listData = extractCodeList(baseId);
			
			if (listData.contains(ResourceRoleQuestion.TeamMemberRoleCode))
				filteredResources.add(baseId);
		}
		return filteredResources;
	}

	private CodeList extractCodeList(BaseId baseId)
	{
		try 
		{
			String codes = getProject().getObjectData(ObjectType.PROJECT_RESOURCE, baseId, ProjectResource.TAG_ROLE_CODES);
			return new CodeList(codes);
		}
		catch (Exception e)  
		{
			EAM.logException(e);
			return new CodeList();
		}
	}
	
	private static final String[] COLUMN_TAGS = new String[] {
		ProjectResource.TAG_NAME, ProjectResource.TAG_INITIALS
	};
}