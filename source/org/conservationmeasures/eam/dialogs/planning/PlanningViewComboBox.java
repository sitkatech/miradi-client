/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.planning;

import java.awt.event.ActionEvent;

import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.main.CommandExecutedEvent;
import org.conservationmeasures.eam.main.CommandExecutedListener;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.ViewData;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.questions.ChoiceItem;
import org.conservationmeasures.eam.utils.CodeList;
import org.conservationmeasures.eam.utils.UiComboBoxWithSaneActionFiring;
import org.conservationmeasures.eam.views.planning.PlanningView;

abstract public class PlanningViewComboBox extends UiComboBoxWithSaneActionFiring implements CommandExecutedListener, RowColumnProvider
{
	public PlanningViewComboBox(Project projectToUse, ChoiceItem[] choices) throws Exception
	{
		super(choices);
		
		project = projectToUse;
		addActionListener(this);
		project.addCommandExecutedListener(this);
		setSelectionFromProjectSetting();
	}
	
	public void dispose()
	{
		project.removeCommandExecutedListener(this);
	}
	
	public void actionPerformed(ActionEvent event)
	{
		try
		{
			CodeList masterRowList = PlanningView.getMasterRowList();
			masterRowList.subtract(getRowList());
			saveSelectedItem(ViewData.TAG_PLANNING_HIDDEN_ROW_TYPES, masterRowList.toString());
			
			CodeList masterColumnList = PlanningView.getMasterColumnList();
			masterRowList.subtract(getColumnList());
			saveSelectedItem(ViewData.TAG_PLANNING_HIDDEN_COL_TYPES, masterColumnList.toString());
			
			ChoiceItem selectedItem = (ChoiceItem) getSelectedItem();
			saveSelectedItem(getChoiceTag(), selectedItem.getCode().toString());
		}
		catch (Exception e)
		{
			EAM.logException(e);
		}
	}

	public void commandExecuted(CommandExecutedEvent event)
	{
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
	
	protected Project getProject()
	{
		return project;
	}
	
	abstract public void setSelectionFromProjectSetting() throws Exception;
	abstract public String getChoiceTag();
	
	private Project project;
	
}
