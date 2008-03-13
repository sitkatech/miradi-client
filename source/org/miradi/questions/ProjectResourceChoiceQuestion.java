/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.questions;

import org.miradi.objects.BaseObject;
import org.miradi.objects.ProjectResource;
import org.miradi.project.Project;

public class ProjectResourceChoiceQuestion extends ObjectQuestion
{
	public ProjectResourceChoiceQuestion(Project project)
	{
		super(getAllProjectResources(project));
	}
	
	private static ProjectResource[] getAllProjectResources(Project project)
	{
		return project.getResourcePool().getAllProjectResources();
	}
	
	@Override
	protected String getStringToDisplay(BaseObject thisObject)
	{
		ProjectResource resource = (ProjectResource)thisObject;
		return resource.getFullName();
	}
}
