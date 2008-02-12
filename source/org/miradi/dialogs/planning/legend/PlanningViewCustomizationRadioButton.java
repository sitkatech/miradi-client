/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
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
