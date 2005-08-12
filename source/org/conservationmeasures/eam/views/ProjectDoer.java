/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views;

import org.conservationmeasures.eam.main.BaseProject;

abstract public class ProjectDoer extends Doer
{
	public ProjectDoer(BaseProject projectToUse)
	{
		project = projectToUse;
	}
	
	public BaseProject getProject()
	{
		return project;
	}

	BaseProject project;
}
