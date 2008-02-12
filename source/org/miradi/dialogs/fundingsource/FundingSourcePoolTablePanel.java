/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.fundingsource;

import org.miradi.actions.ActionCreateFundingSource;
import org.miradi.actions.ActionDeleteFundingSource;
import org.miradi.actions.Actions;
import org.miradi.dialogs.base.ObjectTablePanelWithCreateAndDelete;
import org.miradi.project.Project;

public class FundingSourcePoolTablePanel extends ObjectTablePanelWithCreateAndDelete
{
	public FundingSourcePoolTablePanel(Project project, Actions actions)
	{
		super(project, new FundingSourcePoolTable(new FundingSourcePoolTableModel(project)), 
				actions,
				buttons);
	}
	
	static Class[] buttons = new Class[] {
		ActionCreateFundingSource.class,
		ActionDeleteFundingSource.class
	};
	
}