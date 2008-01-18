/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.planning.legend;

import org.conservationmeasures.eam.dialogs.planning.RowColumnProvider;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objects.ViewData;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.questions.ChoiceItem;
import org.conservationmeasures.eam.questions.PlanningViewCustomizationQuestion;
import org.conservationmeasures.eam.views.planning.PlanningView;

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
