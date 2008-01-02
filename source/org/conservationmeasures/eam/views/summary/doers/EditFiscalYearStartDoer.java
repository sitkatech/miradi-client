/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.summary.doers;

import java.util.Vector;

import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.commands.CommandBeginTransaction;
import org.conservationmeasures.eam.commands.CommandEndTransaction;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.dialogs.EditFiscalYearDialog;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objectpools.ObjectPool;
import org.conservationmeasures.eam.objects.Assignment;
import org.conservationmeasures.eam.objects.ProjectMetadata;
import org.conservationmeasures.eam.views.ViewDoer;
import org.martus.swing.Utilities;

public class EditFiscalYearStartDoer extends ViewDoer
{
	public boolean isAvailable()
	{
		return false;
	}

	public void doIt() throws CommandFailedException
	{
		ProjectMetadata metadata = getProject().getMetadata();
		String oldCode = metadata.getData(ProjectMetadata.TAG_FISCAL_YEAR_START);
		EditFiscalYearDialog dialog = new EditFiscalYearDialog(oldCode);
		Utilities.centerDlg(dialog);
		dialog.setVisible(true);
		if(!dialog.getResult())
			return;

		String newCode = dialog.getFiscalYearChoiceCode();
		if(newCode.equals(oldCode))
			return;
		
		getProject().executeCommand(new CommandBeginTransaction());
		try
		{
			CommandSetObjectData cmd = new CommandSetObjectData(metadata.getRef(), metadata.TAG_FISCAL_YEAR_START, newCode);
			getProject().executeCommand(cmd);
			int oldMonth = getSkewedMonthFromCode(oldCode);
			int newMonth = getSkewedMonthFromCode(newCode);
			updateExistingBudgetData(getMonthDelta(oldMonth, newMonth));
		}
		catch(Exception e)
		{
			throw new CommandFailedException(e);
		}
		finally
		{
			getProject().executeCommand(new CommandEndTransaction());
		}
	}
	
	static int getMonthDelta(int oldMonth, int newMonth)
	{
		int monthDelta = newMonth - oldMonth;
		return monthDelta;
	}

	protected static int getSkewedMonthFromCode(String fiscalYearStartCode)
	{
		if(fiscalYearStartCode.length() == 0)
			fiscalYearStartCode = "1";
		int month = Integer.parseInt(fiscalYearStartCode);
		if(month > 6)
			month -= 12;
		return month;
	}

	private void updateExistingBudgetData(int monthDelta) throws Exception
	{
		ObjectPool pool = getProject().getPool(Assignment.getObjectType());
		ORefList assignmentRefs = pool.getRefList();
		for(int i = 0; i < assignmentRefs.size(); ++i)
		{
			Assignment assignment = Assignment.find(getProject(), assignmentRefs.get(i));
			Vector<Command> commands = assignment.getCommandsToShiftEffort(monthDelta);
			getProject().executeCommandsWithoutTransaction(commands);
		}
	}

}
