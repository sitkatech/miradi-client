/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.actions;

import java.awt.event.ActionEvent;

import org.conservationmeasures.eam.commands.CommandInsertNode;
import org.conservationmeasures.eam.diagram.nodes.Node;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.icons.InsertInterventionIcon;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;

public class ActionInsertIntervention extends InsertNodeAction
{
	public ActionInsertIntervention(MainWindow mainWindow)
	{
		super(mainWindow, getLabel(), new InsertInterventionIcon());
	}

	private static String getLabel()
	{
		return EAM.text("Action|Insert|Intervention");
	}

	public String getToolTipText()
	{
		return EAM.text("TT|Insert an Intervention node");
	}

	public String getInitialText()
	{
		return EAM.text("Label|New Intervention");
	}

	public void doAction(ActionEvent event) throws CommandFailedException
	{
		doInsert(new CommandInsertNode(Node.TYPE_INTERVENTION));
	}

	public boolean shouldBeEnabled()
	{
		return getProject().isOpen();
	}
}

