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
package org.miradi.dialogfields;

import org.miradi.commands.CommandBeginTransaction;
import org.miradi.commands.CommandEndTransaction;
import org.miradi.ids.BaseId;
import org.miradi.main.EAM;
import org.miradi.objects.ResourceAssignment;
import org.miradi.objects.ProjectMetadata;
import org.miradi.project.BudgetTimePeriodChanger;
import org.miradi.project.Project;
import org.miradi.questions.BudgetTimePeriodQuestion;
import org.miradi.questions.ChoiceQuestion;

public class ObjectBudgetTimePeriodChoiceField extends ObjectRadioButtonGroupField
{
	public ObjectBudgetTimePeriodChoiceField(Project projectToUse, int objectType, BaseId objectId, String tagToUse, ChoiceQuestion questionToUse)
	{
		super(projectToUse, objectType, objectId, tagToUse, questionToUse);
	}
	
	public void setText(String code)
	{
		if(disableHandler)
			return;
		
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

	public void buttonWasPressed(String newCode)
	{
		if(disableHandler)
		{
			super.buttonWasPressed(newCode);
			return;
		}
		
		String previouslySelectedCode = getProject().getMetadata().getData(ProjectMetadata.TAG_WORKPLAN_TIME_UNIT);
		
		if(newCode.equals(previouslySelectedCode))
		{
			super.buttonWasPressed(newCode);
			return;
		}
		
		try
		{
			String conversionType = askUserForConversionType(previouslySelectedCode, newCode);
			if (conversionType == null)
			{
				return;
			}
			else if(conversionType.equals(cancelButtonLabel))
			{
				setText(previouslySelectedCode);
			}
			else
			{
				doConversion(newCode, conversionType);
			}
		}
		catch(Exception e)
		{
			EAM.panic(e);
		}
	}

	private void doConversion(String newCode, String conversionType) throws Exception
	{
		disableHandler = true;
		project.executeCommand(new CommandBeginTransaction());
		try
		{
			super.buttonWasPressed(newCode);

			if(conversionType.equals(firstQuarterButtonLabel))
				BudgetTimePeriodChanger.convertYearlyToQuarterly(project);
			
			if(conversionType.equals(combineButtonLabel))
			{
				BudgetTimePeriodChanger.convertQuarterlyToYearly(project);
			}
		}
		finally
		{
			project.executeCommand(new CommandEndTransaction());
			disableHandler = false;
		}
	}

	private String askUserForConversionType(String oldCode, String newCode) throws Exception
	{
		if(project.getPool(ResourceAssignment.getObjectType()).getRefList().size() == 0)
			return null;
		
		boolean wasByQuarter = (oldCode.equals(BudgetTimePeriodQuestion.BUDGET_BY_QUARTER_CODE));
		boolean wasByYear = (oldCode.equals(BudgetTimePeriodQuestion.BUDGET_BY_YEAR_CODE));
		boolean isNowByQuarter = (newCode.equals(BudgetTimePeriodQuestion.BUDGET_BY_QUARTER_CODE));
		boolean isNowByYear = (newCode.equals(BudgetTimePeriodQuestion.BUDGET_BY_YEAR_CODE));
		
		String conversionType = null;
		if(wasByQuarter && isNowByYear)
			conversionType = getQuarterlyToYearlyConversionType();
		else if(wasByYear && isNowByQuarter)
			conversionType = getYearlyToQuarterlyConversionType();
		else
			EAM.errorDialog("Unable to perform data conversion");
		return conversionType;
	}

	private String getQuarterlyToYearlyConversionType() throws Exception
	{
		String text = EAM.text("<html><p>If you have existing budget data, it can be migrated to the new time periods.</p>" +
		"<p>" +
		"<p>Please select how to handle existing budget data:</p>" +
		"<p>" +
		"<table>" +
		"<tr><td>&lt;" + combineButtonLabel + "&gt;<td>Consolidates quarterly data into years" +
		"<tr><td>&lt;" + cancelButtonLabel + "&gt;<td>Revert to the yearly planning with no data changes" +
		"</table>" +
		"<p>" +
		"<p><em>NOTE:</em> If you don't like the changes, you can undo this operation to restore the old values," +
		"<br>but only within this session, before you close this project." +
		"");
		String[] buttonLabels = {combineButtonLabel, cancelButtonLabel,};
		
		int result = EAM.confirmDialog(EAM.text("Change Budget Time Period"), text, buttonLabels);
		if(result < 0)
			return null;
		return buttonLabels[result];
	}

	private String getYearlyToQuarterlyConversionType() throws Exception
	{
		String text = 
		EAM.text("<html><p>If you have existing budget data, it can be migrated to the new time periods.</p>" +
				"<p>" +
				"<p>Please select how to handle existing budget data:</p>" +
				"<p>" +
				"<table>" +
				"<tr><td>&lt;") + firstQuarterButtonLabel + 
		EAM.text("&gt;<td>Put each year's value into its first quarter" +
				"<tr><td>&lt;") + 
		cancelButtonLabel + 
		EAM.text("&gt;<td>Revert to the yearly planning with no data changes" +
			"</table>" +
			"<p>" +
			"<p><em>NOTE:</em> If you don't like the changes, you can undo this operation to restore the old values," +
			"<br>but only within this session, before you close this project." +
		"");
		String[] buttonLabels = {firstQuarterButtonLabel, cancelButtonLabel,};
		
		int result = EAM.confirmDialog(EAM.text("Change Budget Time Period"), text, buttonLabels);
		if(result < 0)
			return null;
		return buttonLabels[result];
	}

	
	private static final String combineButtonLabel = EAM.text("Combine");
	private static final String firstQuarterButtonLabel = EAM.text("First Quarter");
	private static final String cancelButtonLabel = EAM.getCancelButtonText();


	
	private boolean disableHandler;
}

