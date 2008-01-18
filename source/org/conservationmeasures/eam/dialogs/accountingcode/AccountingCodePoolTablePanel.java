/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.accountingcode;

import org.conservationmeasures.eam.actions.ActionCreateAccountingCode;
import org.conservationmeasures.eam.actions.ActionDeleteAccountingCode;
import org.conservationmeasures.eam.actions.ActionImportAccountingCodes;
import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.dialogs.base.ObjectTablePanelWithCreateAndDelete;
import org.conservationmeasures.eam.project.Project;

public class AccountingCodePoolTablePanel extends ObjectTablePanelWithCreateAndDelete
{
	public AccountingCodePoolTablePanel(Project project, Actions actions)
	{
		super(project, new AccountingCodePoolTable(new AccountingCodePoolTableModel(project)), 
			actions,
			buttons);
	}
	
	
	static Class[] buttons = new Class[] {
		ActionCreateAccountingCode.class,
		ActionDeleteAccountingCode.class,
		ActionImportAccountingCodes.class
	};
}
