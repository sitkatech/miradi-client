/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
*/ 
package org.miradi.dialogs.planning.legend;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JRadioButton;

import org.miradi.commands.CommandBeginTransaction;
import org.miradi.commands.CommandEndTransaction;
import org.miradi.commands.CommandSetObjectData;
import org.miradi.main.AppPreferences;
import org.miradi.main.EAM;
import org.miradi.objects.ViewData;
import org.miradi.project.Project;

//FIXME planning - this class should extend UiRadioButton but it cant since UiRadioButton has no constructor that takes in 0 args
abstract public class PlanningViewRadioButton extends JRadioButton implements ActionListener
{
	public PlanningViewRadioButton(Project projectToUse)
	{	
		super();
		project = projectToUse;
		setBackground(AppPreferences.getControlPanelBackgroundColor());
		addActionListener(this);
	}

	public void actionPerformed(ActionEvent event)
	{
		try
		{
			saveRadioSelection();
		}
		catch (Exception e)
		{
			EAM.logException(e);
			EAM.errorDialog(EAM.text("Error Occurred While Trying to Save Settings"));
		}	
	}
	
	private void saveRadioSelection() throws Exception
	{
		project.executeCommand(new CommandBeginTransaction());
		try
		{
			saveCurrentRadioSelection();
			buttonWasPressed();
		}
		finally
		{
			project.executeCommand(new CommandEndTransaction());
		}
	}
	
	private void saveCurrentRadioSelection() throws Exception
	{
		save(ViewData.TAG_PLANNING_STYLE_CHOICE, getPropertyName());
	}

	protected void save(String tag, String newValue) throws Exception
	{
		ViewData viewData = project.getCurrentViewData();
		String existingValue = viewData.getData(tag);
		if (existingValue.equals(newValue))
			return;

		CommandSetObjectData setComboItem = new CommandSetObjectData(viewData.getRef(), tag, newValue);
		project.executeCommand(setComboItem);
	}
	
	protected void buttonWasPressed() throws Exception
	{
	}

	protected Project getProject()
	{
		return project;
	}
		
	abstract public String getPropertyName();
	
	private	Project project;
}
