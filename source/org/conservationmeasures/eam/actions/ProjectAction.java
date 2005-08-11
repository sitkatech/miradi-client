/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.ImageIcon;

import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.BaseProject;
import org.conservationmeasures.eam.main.EAM;

public abstract class ProjectAction extends AbstractAction
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
	
	public void actionPerformed(ActionEvent event)
	{
		try
		{
			doAction(event);
		}
		catch (CommandFailedException e)
		{
			EAM.logException(e);
			EAM.errorDialog(EAM.text("An internal error prevented this operation"));
		}
		
	}
	
	public ImageIcon getIcon()
	{
		return (ImageIcon)getValue("icon");
	}
	
	public abstract void doAction(ActionEvent event) throws CommandFailedException;
	public String getToolTipText()
	{
		return "";
	}
	
	public boolean shouldBeEnabled()
	{
		return false;
	}
	
	public void updateEnabledState()
	{
		setEnabled(shouldBeEnabled());
	}
	
	BaseProject project;
}
