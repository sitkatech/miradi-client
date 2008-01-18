/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.planning.legend;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.Goal;
import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.objects.Objective;
import org.conservationmeasures.eam.objects.Strategy;
import org.conservationmeasures.eam.objects.Task;
import org.conservationmeasures.eam.objects.ViewData;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.questions.ChoiceItem;
import org.conservationmeasures.eam.questions.PlanningViewSingleLevelQuestion;
import org.conservationmeasures.eam.utils.CodeList;
import org.conservationmeasures.eam.views.planning.ColumnManager;
import org.conservationmeasures.eam.views.planning.PlanningView;

public class PlanningViewSingleLevelComboBox extends PlanningViewComboBox
{
	public PlanningViewSingleLevelComboBox(Project projectToUse) throws Exception
	{
		super(projectToUse, new PlanningViewSingleLevelQuestion().getChoices());
	}
	
	public CodeList getRowListToShow() throws Exception
	{
		CodeList listToShow = new CodeList();
		listToShow.add(getSelectedItemProperty());
		return listToShow;
	}
	
	private String getSelectedItemProperty()
	{
		ChoiceItem choiceItem = (ChoiceItem) getSelectedItem();
		return choiceItem.getCode();
	}

	public String getPropertyName()
	{
		return PlanningView.SINGLE_LEVEL_COMBO;
	}

	public CodeList getColumnListToShow() throws Exception
	{
		
		String propertyName = getSelectedItemProperty();
		if (propertyName.equals(Goal.OBJECT_NAME))
			return ColumnManager.getGoalColumns();

		if (propertyName.equals(Objective.OBJECT_NAME))
			return ColumnManager.getObjectiveColumns();
		
		if (propertyName.equals(Strategy.OBJECT_NAME))
			return ColumnManager.getStrategyColumns();
		
		if (propertyName.equals(Task.ACTIVITY_NAME))
			return ColumnManager.getActivityColumns();

		if (propertyName.equals(Indicator.OBJECT_NAME))
			return ColumnManager.getIndicatorColumns();

		if (propertyName.equals(Task.METHOD_NAME))
			return ColumnManager.getMethodColumns();

		if (propertyName.equals(Task.OBJECT_NAME))
			return ColumnManager.getTaskColumns();
		
		return new CodeList();
	}
	
	public String getChoiceTag()
	{
		return ViewData.TAG_PLANNING_SINGLE_LEVEL_CHOICE;
	}
	
	public String getStyleChoiceName()
	{
		return PlanningView.SINGLE_LEVEL_RADIO_CHOICE;
	}
	
	boolean comboBoxNeedsSave() throws Exception 
	{
		ViewData viewData = getProject().getViewData(PlanningView.getViewName());
		String existingValue = viewData.getData(getChoiceTag());

		if(existingValue.length() == 0 && getSelectedIndex() == 0)
			return false;
		
		ChoiceItem currentChoiceItem = (ChoiceItem) getSelectedItem();
		if (currentChoiceItem == null)
			return false;
		
		if (currentChoiceItem.getCode().equals(existingValue))
			return false;
		
		EAM.logVerbose("From " + existingValue + " to " + currentChoiceItem.getCode());
		return true;
	}

}
