/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.planning;

import java.awt.event.ActionEvent;

import org.conservationmeasures.eam.commands.CommandBeginTransaction;
import org.conservationmeasures.eam.commands.CommandEndTransaction;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.ViewData;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.questions.ChoiceItem;
import org.conservationmeasures.eam.utils.CodeList;
import org.conservationmeasures.eam.utils.UiComboBoxWithSaneActionFiring;
import org.conservationmeasures.eam.views.planning.PlanningView;

abstract public class PlanningViewComboBox extends UiComboBoxWithSaneActionFiring implements RowColumnProvider
{
	public PlanningViewComboBox(Project projectToUse, ChoiceItem[] choices) throws Exception
	{
		super(choices);
		
		project = projectToUse;
		addActionListener(this);
		setSelectionFromProjectSetting();
	}
	
	public void actionPerformed(ActionEvent event)
	{		
		try
		{
			if (isSameSelection())
				return;
			
			saveState();
		}
		catch (Exception e)
		{
			EAM.logException(e);
		}
	}

	private void saveState() throws Exception
	{
		project.executeCommand(new CommandBeginTransaction());
		try
		{
			saveSelectedItem(ViewData.TAG_PLANNING_STYLE_CHOICE, getRadioTag());
			
			saveSubtractedList(PlanningView.getMasterRowList(), getRowList(), ViewData.TAG_PLANNING_HIDDEN_ROW_TYPES);
			saveSubtractedList(PlanningView.getMasterColumnList(), getColumnList(), ViewData.TAG_PLANNING_HIDDEN_COL_TYPES);

			ChoiceItem selectedItem = (ChoiceItem) getSelectedItem();
			saveSelectedItem(getChoiceTag(), selectedItem.getCode().toString());
		}
		finally
		{
			project.executeCommand(new CommandEndTransaction());
		}
	}

	private void saveSubtractedList(CodeList masterList, CodeList list, String hiddenTypeTag) throws Exception
	{
		masterList.subtract(list);
		saveSelectedItem(hiddenTypeTag, masterList.toString());
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

	private boolean isSameSelection() throws Exception
	{
		ViewData viewData = getProject().getCurrentViewData();
		String existingValue = viewData.getData(getChoiceTag());
		ChoiceItem currentChoiceItem = (ChoiceItem) getSelectedItem();
		
		if (currentChoiceItem.getCode().equals(existingValue))
			return true;
		
		return false;
	}

	protected Project getProject()
	{
		return project;
	}
	
	abstract public void setSelectionFromProjectSetting() throws Exception;
	abstract public String getChoiceTag();
	abstract public String getRadioTag();
	
	private Project project;
	
}
