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
import org.conservationmeasures.eam.icons.InsertGoalIcon;
import org.conservationmeasures.eam.main.BaseProject;
import org.conservationmeasures.eam.main.EAM;

public class ActionInsertGoal extends InsertNodeAction
{
	public ActionInsertGoal(BaseProject projectToUse)
	{
		super(projectToUse, getLabel(), new InsertGoalIcon());
	}

	private static String getLabel()
	{
		return EAM.text("Action|Insert|Goal");
	}

	public String getToolTipText()
	{
		return EAM.text("TT|Insert a Goal node");
	}

	public String getInitialText()
	{
		return EAM.text("Label|New Goal");
	}

	public void doAction(ActionEvent event) throws CommandFailedException
	{
		doInsert(new CommandInsertNode(Node.TYPE_GOAL));
	}

	public boolean shouldBeEnabled()
	{
		return getProject().isOpen();
	}
}

