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

import org.miradi.main.EAM;
import org.miradi.objects.Cause;
import org.miradi.objects.Goal;
import org.miradi.objects.Indicator;
import org.miradi.objects.Objective;
import org.miradi.objects.Strategy;
import org.miradi.objects.Target;
import org.miradi.objects.Task;
import org.miradi.objects.ViewData;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.PlanningViewSingleLevelQuestion;
import org.miradi.utils.CodeList;
import org.miradi.views.planning.ColumnManager;
import org.miradi.views.planning.PlanningView;

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
		
		if (propertyName.equals(Target.OBJECT_NAME))
			return ColumnManager.getTargetColumns();
		
		if (propertyName.equals(Cause.OBJECT_NAME_THREAT))
			return ColumnManager.getDirectThreatsColumns();
		
		return new CodeList();
	}
	
	public String getChoiceTag()
	{
		return ViewData.TAG_PLANNING_SINGLE_LEVEL_CHOICE;
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
