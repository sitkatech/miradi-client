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

import org.miradi.dialogs.planning.legend.PlanningViewConfigurationComboBox;
import org.miradi.main.CommandExecutedEvent;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.BaseObject;
import org.miradi.objects.PlanningViewConfiguration;
import org.miradi.objects.ViewData;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.ChoiceQuestion;
import org.miradi.questions.PlanningViewConfigurationQuestion;

public class PlanningViewConfigurableControlPanel extends PlanningViewCustomButtonPanel
{
	public PlanningViewConfigurableControlPanel(Project projectToUse) throws Exception
	{
		super(projectToUse);
		
		rebuildConfigurationPanel();
	}
	
	protected void rebuildConfigurationPanel() throws Exception
	{
		configurationComboBox = new PlanningViewConfigurationComboBox(getProject());
		add(configurationComboBox);

		selectAppropriateConfiguredComboBoxItem();
	}
	
	private void selectAppropriateConfiguredComboBoxItem() throws Exception
	{
		ORef refToSelect = getCurrentConfigurationComboBoxChoice();
		if(refToSelect.isInvalid())
		{
			configurationComboBox.setSelectedIndex(0);
			return;
		}
		
		PlanningViewConfiguration configuration = PlanningViewConfiguration.find(getProject(), refToSelect);
		ChoiceQuestion question = new PlanningViewConfigurationQuestion(getProject());
		ChoiceItem choiceToSelect = question.findChoiceByCode(configuration.getRef().toString());
		configurationComboBox.setSelectedItemWithoutFiring(choiceToSelect);
	}
	
	private ORef getCurrentConfigurationComboBoxChoice() throws Exception
	{	
		ViewData viewData = getProject().getCurrentViewData();
		String preconfiguredChoice = viewData.getData(ViewData.TAG_PLANNING_CUSTOM_PLAN_REF);
		boolean shouldReturnDefault = preconfiguredChoice.trim().equals("");
		if (shouldReturnDefault)
			return ORef.INVALID;

		return ORef.createFromString(preconfiguredChoice);
	}
	
	public void commandExecuted(CommandExecutedEvent event)
	{
		try
		{
			updateComboFromProject(event);
			selectComboConfiguration(event);
		}
		catch(Exception e)
		{
			EAM.logException(e);
			EAM.errorDialog(EAM.text("Unexpected Error Occurred"));
		}
	}
	
	private void updateComboFromProject(CommandExecutedEvent event) throws Exception
	{
		if (event.isDeleteObjectCommand() || event.isCreateObjectCommand())
		{
			configurationComboBox.syncContentsWithProject();
			selectAppropriateConfiguredComboBoxItem();
		}
		
		if(event.isSetDataCommandWithThisTypeAndTag(PlanningViewConfiguration.getObjectType(), BaseObject.TAG_LABEL))
		{
			configurationComboBox.repaint();
		}
	}
		
	private void selectComboConfiguration(CommandExecutedEvent event) throws Exception
	{
		if (event.isSetDataCommandWithThisTypeAndTag(ViewData.getObjectType(), ViewData.TAG_PLANNING_CUSTOM_PLAN_REF))
			selectAppropriateConfiguredComboBoxItem();
	}

	private PlanningViewConfigurationComboBox configurationComboBox;
}
