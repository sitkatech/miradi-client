/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.planning;

import java.util.Arrays;
import java.util.HashSet;

import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objects.PlanningViewConfiguration;
import org.conservationmeasures.eam.objects.ViewData;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.questions.ChoiceItem;
import org.conservationmeasures.eam.questions.PlanningViewCustomizationQuestion;
import org.conservationmeasures.eam.utils.CodeList;
import org.conservationmeasures.eam.views.planning.PlanningView;

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
			if(!choicesInList.contains(choicesInProject[i]))
				addItem(choicesInProject[i]);
		}
	}
	
	private void removeDeletedItems()
	{
		ChoiceItem[] choicesInProjectAsArray = new PlanningViewCustomizationQuestion(getProject()).getChoices();
		HashSet choicesInProject = new HashSet(Arrays.asList(choicesInProjectAsArray));
		for(int i = getItemCount() - 1; i >= 0; --i)
		{
			if(!choicesInProject.contains(getItemAt(i)))
				removeItem(i);
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
	
	public String getChosenRadioTag()
	{
		return PlanningView.CUSTOMIZABLE_RADIO_CHOICE;
	}
}
