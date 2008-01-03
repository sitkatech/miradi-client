/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogfields;

import java.awt.event.ActionEvent;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.questions.ChoiceQuestion;

public class ObjectBudgetTimePeriodChoiceField extends ObjectChoiceField
{
	public ObjectBudgetTimePeriodChoiceField(Project projectToUse, int objectType, BaseId objectId, ChoiceQuestion questionToUse)
	{
		super(projectToUse, objectType, objectId, questionToUse);
	}
	
	ComboChangeHandler createActionHandler()
	{
		return new BudgetTimePeriodComboChangeHandler();
	}

	class BudgetTimePeriodComboChangeHandler extends ComboChangeHandler
	{
		public void actionPerformed(ActionEvent event)
		{
			String oldValue = getOldValue();
			if(oldValue.equals(getText()))
				return;
			EAM.notifyDialog("This feature is not available yet");
			setText(oldValue);
//			super(event);
		}
	}
}
