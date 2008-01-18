/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.planning.legend;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JRadioButton;

import org.conservationmeasures.eam.commands.CommandBeginTransaction;
import org.conservationmeasures.eam.commands.CommandEndTransaction;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.main.AppPreferences;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.ViewData;
import org.conservationmeasures.eam.project.Project;

//FIXME planning - this class should extend UiRadioButton but it cant since UiRadioButton has no constructor that takes in 0 args
abstract public class PlanningViewRadioButton extends JRadioButton implements ActionListener
{
	public PlanningViewRadioButton(Project projectToUse)
	{	
		super();
		project = projectToUse;
		setBackground(AppPreferences.CONTROL_PANEL_BACKGROUND);
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
