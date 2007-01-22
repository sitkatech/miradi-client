/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs;

import org.conservationmeasures.eam.actions.ActionCreateAccountingCode;
import org.conservationmeasures.eam.actions.ActionDeleteAccountingCode;
import org.conservationmeasures.eam.actions.ActionImportAccountingCodes;
import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.actions.MainWindowAction;
import org.conservationmeasures.eam.actions.ObjectsAction;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.project.Project;

public class AccountingCodePoolTablePanel extends ObjectTablePanelWithCreateAndDelete
{
	public AccountingCodePoolTablePanel(Project project, Actions actions)
	{
		super(project, ObjectType.ACCOUNTING_CODE, 
			new AccountingCodePoolTable(new AccountingCodePoolTableModel(project)),
			(MainWindowAction)actions.get(ActionCreateAccountingCode.class),
			(ObjectsAction)actions.get(ActionDeleteAccountingCode.class));
		
		ObjectsAction importAction = (ObjectsAction)actions.get(ActionImportAccountingCodes.class);
		addButton(importAction);
	}
}
