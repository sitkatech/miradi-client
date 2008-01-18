/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.fundingsource;

import org.conservationmeasures.eam.actions.ActionCreateFundingSource;
import org.conservationmeasures.eam.actions.ActionDeleteFundingSource;
import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.dialogs.base.ObjectTablePanelWithCreateAndDelete;
import org.conservationmeasures.eam.project.Project;

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