/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.planning.legend;

import java.awt.event.ActionEvent;

import org.conservationmeasures.eam.commands.CommandBeginTransaction;
import org.conservationmeasures.eam.commands.CommandEndTransaction;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.dialogs.planning.RowColumnProvider;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.ViewData;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.questions.ChoiceItem;
import org.conservationmeasures.eam.utils.UiComboBoxWithSaneActionFiring;

abstract public class PlanningViewComboBox extends UiComboBoxWithSaneActionFiring implements RowColumnProvider
{
	public PlanningViewComboBox(Project projectToUse, ChoiceItem[] choices) throws Exception
	{
		super(choices);
		
		project = projectToUse;
		addActionListener(this);
	}
	
	public void actionPerformed(ActionEvent event)
	{		
		try
		{
			if (!needsSave())
				return;
			
			saveState();
		}
		catch (Exception e)
		{
			EAM.logException(e);
			EAM.errorDialog("Error: " + e.getMessage());
		}
	}

	private void saveState() throws Exception
	{
		project.executeCommand(new CommandBeginTransaction());
		try
		{
			ChoiceItem selectedItem = (ChoiceItem) getSelectedItem();
			saveSelectedItem(getChoiceTag(), selectedItem.getCode());
			saveRadioSelection();
		}
		finally
		{
			project.executeCommand(new CommandEndTransaction());
		}
	}

	private void saveSelectedItem(String tag, String newValue) throws Exception
	{	
		ViewData viewData = getProject().getCurrentViewData();
		String existingValue = viewData.getData(tag);
		if (existingValue.equals(newValue))
			return;

		CommandSetObjectData setComboItem = new CommandSetObjectData(viewData.getRef(), tag, newValue);
		getProject().executeCommand(setComboItem);
	}
	
	private void saveRadioSelection() throws Exception
	{
		ViewData viewData = getProject().getCurrentViewData();
		String existingStyleChoice = viewData.getData(ViewData.TAG_PLANNING_STYLE_CHOICE);
		if (existingStyleChoice.equals(getRadioChoicTag()))
			return;

		CommandSetObjectData setSelectedRadio = new CommandSetObjectData(viewData.getRef(), ViewData.TAG_PLANNING_STYLE_CHOICE, getRadioChoicTag());
		getProject().executeCommand(setSelectedRadio);
	}
	
	protected Project getProject()
	{
		return project;
	}

	abstract public String getRadioChoicTag();
	abstract public String getChoiceTag();
	abstract boolean needsSave() throws Exception;
	
	private Project project;
	
}
