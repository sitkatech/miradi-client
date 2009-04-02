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
package org.miradi.dialogs.planning;

import org.miradi.commands.CommandSetObjectData;
import org.miradi.dialogs.planning.legend.PlanningViewSingleLevelComboBox;
import org.miradi.main.CommandExecutedEvent;
import org.miradi.main.CommandExecutedListener;
import org.miradi.main.EAM;
import org.miradi.objects.Goal;
import org.miradi.objects.ViewData;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.PlanningViewSingleLevelQuestion;

public class PlanningViewObjectsOnlyDropDownPanel extends PlanningViewCustomButtonPanel implements CommandExecutedListener
{
	public PlanningViewObjectsOnlyDropDownPanel(Project projectToUse) throws Exception
	{
		super(projectToUse);
		
		rebuildCustomizationPanel();
	}
	
	protected void rebuildCustomizationPanel() throws Exception
	{
		singleLevelCombo = new PlanningViewSingleLevelComboBox(getProject());
		add(singleLevelCombo);

		selectAppropriateSingleLevelComboBoxItem();
	}
	
	private void selectAppropriateSingleLevelComboBoxItem() throws Exception
	{
		ViewData viewData = getProject().getCurrentViewData();
		String preconfiguredChoice = getCurrentSingleLevelChoice(viewData);
		setComboBoxSelection(preconfiguredChoice);
	}
	
	private String getCurrentSingleLevelChoice(ViewData viewData)
	{	
		String singleLevelChoice = viewData.getData(ViewData.TAG_PLANNING_SINGLE_LEVEL_CHOICE);
		boolean shouldReturnDefault = singleLevelChoice.trim().equals("");
		if (shouldReturnDefault)
			return Goal.OBJECT_NAME;

		return singleLevelChoice;
	}
	
	public void commandExecuted(CommandExecutedEvent event)
	{
		try
		{
			if (event.isSetDataCommandWithThisTypeAndTag(ViewData.getObjectType(), ViewData.TAG_PLANNING_SINGLE_LEVEL_CHOICE))
				setSingleLevelComboSelection(event);
		}
		catch(Exception e)
		{
			EAM.logException(e);
			EAM.errorDialog(EAM.text("Unexpected Error Occurred"));
		}
	}
	
	private void setSingleLevelComboSelection(CommandExecutedEvent event)
	{
		CommandSetObjectData setCommand = (CommandSetObjectData) event.getCommand();
		String code = setCommand.getDataValue();
		setComboBoxSelection(code);
	}
	
	private void setComboBoxSelection(String code)
	{		
		ChoiceItem currentSelection = (ChoiceItem)singleLevelCombo.getSelectedItem();
		PlanningViewSingleLevelQuestion question = new PlanningViewSingleLevelQuestion();
		ChoiceItem choiceItemToSelect = question.findChoiceByCode(code);
		if (currentSelection.equals(choiceItemToSelect))
			return;
		
		singleLevelCombo.setSelectedItemWithoutFiring(choiceItemToSelect);
	}
	
	private PlanningViewSingleLevelComboBox singleLevelCombo;
}
