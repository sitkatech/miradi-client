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
		boolean oldValue = disableHandler;
		disableHandler = true;
		try
		{
			super.setText(code);
		}
		finally
		{
			disableHandler = oldValue;
		}
	}

	class BudgetTimePeriodComboChangeHandler extends ComboChangeHandler
	{
		public void actionPerformed(ActionEvent event)
		{
			if(disableHandler)
				return;
			
			String oldValue = getOldValue();
			String newValue = getText();
			if(oldValue.equals(newValue))
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
					"</table>" +
					"<p>" +
					"<p><em>NOTE:</em> If you don't like the changes, you can undo this operation to restore the old values," +
					"<br>but only within this session, before you close this project." +
					"");
			String[] buttonLabels = {discardButtonLabel, distributeButtonLabel, cancelButtonLabel,};
			
			boolean worked = true;
			
			int result = EAM.confirmDialog(EAM.text("Change Budget Time Period"), text, buttonLabels);
			switch(result)
			{
				case 0:
					EAM.notifyDialog("Changing Budget Time Periods is not yet supported.");
					worked = false;
					break;
				case 1:
					worked = distributeDateRangeEffortLists(oldValue, newValue);
					break;
				default:
					worked = false;
					break;
			}
			
			if(worked)
				super.actionPerformed(event);
			else
				setText(oldValue);
		}

		private boolean distributeDateRangeEffortLists(String oldValue, String newValue)
		{
			EAM.notifyDialog("Changing Budget Time Periods is not yet supported.");
			return false;
//			try
//			{
//				BudgetTimePeriodChanger.distributeAllDateRangeEffortLists(getProject(), oldValue, newValue);
//				return true;
//			}
//			catch(UnknownConversionException e)
//			{
//				EAM.logError("Attempted to convert " + oldValue + " to " + newValue);
//				EAM.errorDialog("Unable to convert the data in that way.");
//				return false;
//			}
//			catch(Exception e)
//			{
//				EAM.panic(e);
//				return false;
//			}
		}
	}
	
	private boolean disableHandler;
}
