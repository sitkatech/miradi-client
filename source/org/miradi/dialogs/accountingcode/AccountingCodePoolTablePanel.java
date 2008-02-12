/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.accountingcode;

import org.miradi.actions.ActionCreateAccountingCode;
import org.miradi.actions.ActionDeleteAccountingCode;
import org.miradi.actions.ActionImportAccountingCodes;
import org.miradi.actions.Actions;
import org.miradi.dialogs.base.ObjectTablePanelWithCreateAndDelete;
import org.miradi.project.Project;

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
