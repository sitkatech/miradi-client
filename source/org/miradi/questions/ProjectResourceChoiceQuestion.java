/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.questions;

import org.miradi.objects.ProjectResource;
import org.miradi.project.Project;

public class ProjectResourceChoiceQuestion extends ObjectQuestion
{
	public ProjectResourceChoiceQuestion(Project project)
	{
		super(project, getAllProjectResources(project));
	}
	
	private static ProjectResource[] getAllProjectResources(Project project)
	{
		return project.getResourcePool().getAllProjectResources();
	}
}
