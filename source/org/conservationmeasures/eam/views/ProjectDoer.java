/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
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
