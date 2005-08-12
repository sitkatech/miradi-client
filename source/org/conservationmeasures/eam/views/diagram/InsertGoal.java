/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.diagram;

import java.awt.Point;

import org.conservationmeasures.eam.commands.CommandInsertNode;
import org.conservationmeasures.eam.diagram.nodes.Node;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.BaseProject;
import org.conservationmeasures.eam.main.EAM;

public class InsertGoal extends InsertNode
{
	public InsertGoal(BaseProject project, Point invocationPoint)
	{
		super(project, invocationPoint);
	}

	public boolean isAvailable()
	{
		return getProject().isOpen();
	}

	public void doIt() throws CommandFailedException
	{
		doInsert(new CommandInsertNode(Node.TYPE_GOAL));
	}

	public String getInitialText()
	{
		return EAM.text("Label|New Goal");
	}

}
