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

import java.util.Arrays;
import java.util.HashSet;

import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.PlanningViewConfiguration;
import org.miradi.objects.ViewData;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.PlanningViewCustomizationQuestion;
import org.miradi.utils.CodeList;
import org.miradi.views.planning.PlanningView;

public class PlanningViewCustomizationComboBox extends PlanningViewComboBox
{
	public PlanningViewCustomizationComboBox(Project projectToUse) throws Exception
	{
		super(projectToUse, new PlanningViewCustomizationQuestion(projectToUse).getChoices());
	}
	
	public CodeList getColumnListToShow() throws Exception
	{
		return getList(PlanningViewConfiguration.TAG_COL_CONFIGURATION);
	}

	public CodeList getRowListToShow() throws Exception
	{
		return getList(PlanningViewConfiguration.TAG_ROW_CONFIGURATION);
	}
	
	public void syncContentsWithProject()
	{
		addCreatedItems();
		removeDeletedItems();
	}

	private void addCreatedItems()
	{
		ChoiceItem[] choicesInProject = new PlanningViewCustomizationQuestion(getProject()).getChoices();
		HashSet choicesInList = getCurrentChoicesInList();
		for(int i = 0; i < choicesInProject.length; ++i)
		{
			ChoiceItem thisItem = choicesInProject[i];
			if(!choicesInList.contains(thisItem))
				addItem(thisItem);
		}
	}
	
	private void removeDeletedItems()
	{
		ChoiceItem[] choicesInProjectAsArray = new PlanningViewCustomizationQuestion(getProject()).getChoices();
		HashSet choicesInProject = new HashSet(Arrays.asList(choicesInProjectAsArray));
		for(int i = getItemCount() - 1; i >= 0; --i)
		{
			ChoiceItem thisItem = (ChoiceItem)getItemAt(i);
			if(!choicesInProject.contains(thisItem))
				removeItemAt(i);
		}
	}

	private HashSet getCurrentChoicesInList()
	{
		HashSet choices = new HashSet();
		for(int i = 0; i < getItemCount(); ++i)
			choices.add(getItemAt(i));
		return choices;
	}

	private CodeList getList(String tag) throws Exception
	{
		return new CodeList(findConfiguration().getData(tag)); 
	}
	
	private PlanningViewConfiguration findConfiguration() throws Exception
	{
		return (PlanningViewConfiguration) getProject().findObject(getCurrentConfigurationRef());
	}
	
	private ORef getCurrentConfigurationRef() throws Exception
	{
		ViewData viewData = getProject().getCurrentViewData();
		return viewData.getORef(ViewData.TAG_PLANNING_CUSTOM_PLAN_REF);
	}
	
	public String getPropertyName()
	{
		return PlanningView.CUSTOMIZABLE_COMBO;
	}
	
	public String getChoiceTag()
	{
		return ViewData.TAG_PLANNING_CUSTOM_PLAN_REF;
	}
	
	public String getStyleChoiceName()
	{
		return PlanningView.CUSTOMIZABLE_RADIO_CHOICE;
	}
	
	boolean comboBoxNeedsSave() throws Exception 
	{
		ViewData viewData = getProject().getViewData(PlanningView.getViewName());
		ORef existingRef = viewData.getORef(getChoiceTag());

		if(existingRef.isInvalid() && getSelectedIndex() == 0)
			return false;
		
		ChoiceItem currentChoiceItem = (ChoiceItem) getSelectedItem();
		if (currentChoiceItem == null)
			return false;
		
		if (currentChoiceItem.getCode().equals(existingRef.toString()))
			return false;
		
		EAM.logVerbose("From " + existingRef + " to " + currentChoiceItem.getCode());
		return true;
	}


}
