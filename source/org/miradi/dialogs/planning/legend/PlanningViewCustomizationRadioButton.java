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

import org.miradi.dialogs.planning.RowColumnProvider;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.ViewData;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.PlanningViewCustomizationQuestion;
import org.miradi.views.planning.PlanningView;

public class PlanningViewCustomizationRadioButton extends PlanningViewRadioButton
{
	public PlanningViewCustomizationRadioButton(Project projectToUse, RowColumnProvider rowColumnProvider)
	{
		super(projectToUse);
	}
	
	public String getPropertyName()
	{
		return PlanningView.CUSTOMIZABLE_RADIO_CHOICE;
	}
	
	protected void buttonWasPressed() throws Exception
	{
		save(ViewData.TAG_PLANNING_CUSTOM_PLAN_REF, getCustomizationComboRefToSelect().toString());
	}
	
	private ORef getCustomizationComboRefToSelect() throws Exception
	{
		ViewData viewData = getProject().getCurrentViewData();
		ORef customRef = viewData.getORef(ViewData.TAG_PLANNING_CUSTOM_PLAN_REF);
		if (!customRef.isInvalid())
			return customRef;
		
		PlanningViewCustomizationQuestion question = new PlanningViewCustomizationQuestion(getProject());
		ChoiceItem[] choices = question.getChoices();
		if (choices.length == 0)
			return ORef.INVALID;
		
		return ORef.createFromString(choices[0].getCode());
	}
}
