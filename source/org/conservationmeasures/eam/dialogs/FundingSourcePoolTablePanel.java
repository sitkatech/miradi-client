/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.dialogs;

import org.conservationmeasures.eam.actions.ActionCreateFundingSource;
import org.conservationmeasures.eam.actions.ActionDeleteFundingSource;
import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.actions.MainWindowAction;
import org.conservationmeasures.eam.actions.ObjectsAction;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.project.Project;

public class FundingSourcePoolTablePanel extends ObjectTablePanelWithCreateAndDelete
{
	public FundingSourcePoolTablePanel(Project project, Actions actions)
	{
		super(project, ObjectType.FUNDING_SOURCE, 
				new FundingSourcePoolTable(new FundingSourcePoolTableModel(project)),
				(MainWindowAction)actions.get(ActionCreateFundingSource.class),
				(ObjectsAction)actions.get(ActionDeleteFundingSource.class));
	}
	
}