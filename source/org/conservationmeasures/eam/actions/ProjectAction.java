/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.actions;


import javax.swing.Icon;
import javax.swing.ImageIcon;

import org.conservationmeasures.eam.main.BaseProject;

public abstract class ProjectAction extends EAMAction
{
	public ProjectAction(BaseProject projectToUse, String label)
	{
		this(projectToUse, label, "icons/blankicon.png");
	}
	
	public ProjectAction(BaseProject projectToUse, String label, String icon)
	{
		this(projectToUse, label, new ImageIcon(icon));
	}
	
	public ProjectAction(BaseProject projectToUse, String label, Icon icon)
	{
		super(label, icon);
		project = projectToUse;
	}

	public BaseProject getProject()
	{
		return project;
	}
	
	BaseProject project;
}
