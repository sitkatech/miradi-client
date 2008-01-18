/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views;

import org.conservationmeasures.eam.project.Project;

abstract public class ProjectDoer extends Doer
{
	public void setProject(Project projectToUse)
	{
		project = projectToUse;
	}

	public Project getProject()
	{
		return project;
	}

	Project project;
}
