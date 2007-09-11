/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.planning;

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
import org.conservationmeasures.eam.views.planning.PlanningView;

public class PlanningViewSingleLevelComboBox extends PlanningViewComboBox
{
	public PlanningViewSingleLevelComboBox(Project projectToUse) throws Exception
	{
		super(projectToUse, new PlanningViewSingleLevelQuestion().getChoices());
	}
	
	public CodeList getRowList() throws Exception
	{
		CodeList listToShow = new CodeList();
		listToShow.add(getSelectedItemProperty());
		CodeList masterRowList = PlanningView.getMasterRowList();
		masterRowList.subtract(listToShow);
	
		return masterRowList;
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

	public CodeList getColumnList() throws Exception
	{
		
		String propertyName = getSelectedItemProperty();
		if (propertyName.equals(Goal.OBJECT_NAME))
			return getSubtractedList(PlanningView.getGoalColumns());

		if (propertyName.equals(Objective.OBJECT_NAME))
			return getSubtractedList(PlanningView.getObjectiveColumns());
		
		if (propertyName.equals(Strategy.OBJECT_NAME))
			return getSubtractedList(PlanningView.getStrategyColumns());
		
		if (propertyName.equals(Task.ACTIVITY_NAME))
			return getSubtractedList(PlanningView.getActivityColumns());

		if (propertyName.equals(Indicator.OBJECT_NAME))
			return getSubtractedList(PlanningView.getIndicatorColumns());

		if (propertyName.equals(Task.METHOD_NAME))
			return getSubtractedList(PlanningView.getMethodColumns());

		if (propertyName.equals(Task.OBJECT_NAME))
			return getSubtractedList(PlanningView.getTaskColumns());
		
		return new CodeList();
	}
	
	//FIXME planning - have the PlanningView.getXXXColumns return list to hide not show
	private CodeList getSubtractedList(CodeList listToSubtract)
	{
		CodeList masterColumnList = PlanningView.getMasterColumnList();
		masterColumnList.subtract(listToSubtract);
		return masterColumnList;
	}

	public String getChoiceTag()
	{
		return ViewData.TAG_PLANNING_SINGLE_LEVEL_CHOICE;
	}
	
	public String getRadioTag()
	{
		return PlanningView.SINGLE_LEVEL_RADIO_CHOICE;
	}
}
