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
	
	public void setText(String code)
	{
		disableHandler = true;
		try
		{
			super.setText(code);
		}
		finally
		{
			disableHandler = false;
		}
	}

	class BudgetTimePeriodComboChangeHandler extends ComboChangeHandler
	{
		public void actionPerformed(ActionEvent event)
		{
			if(disableHandler)
				return;
			
			String oldValue = getOldValue();
			if(oldValue.equals(getText()))
				return;
			
			String discardButtonLabel = EAM.text("Discard");
			String distributeButtonLabel = EAM.text("Distribute Evenly");
			String cancelButtonLabel = EAM.getCancelButtonText();
			String text = EAM.text("<html><p>If you have existing budget data, it can be migrated to the new time periods.</p>" +
					"<p>" +
					"<p>Please select how to handle existing budget data:</p>" +
					"<p>" +
					"<table>" +
					"<tr><td>&lt;" + discardButtonLabel + "&gt;<td>Deletes all existing budget entries" +
					"<tr><td>&lt;" + distributeButtonLabel + "&gt;<td>Consolidates or spreads out existing entries" +
					"<tr><td>&lt;" + cancelButtonLabel + "&gt;<td>Revert to the existing time period with no data changes" +
					"<p>" +
					"");
			String[] buttonLabels = {discardButtonLabel, distributeButtonLabel, cancelButtonLabel,};
			
			int result = EAM.confirmDialog(EAM.text("Change Budget Time Period"), text, buttonLabels);
			System.out.println(result);
			if(result != 2)
				EAM.notifyDialog("Changing Budget Time Periods is not yet supported.");
			setText(oldValue);
//			super(event);
		}
	}
	
	private boolean disableHandler;
}
