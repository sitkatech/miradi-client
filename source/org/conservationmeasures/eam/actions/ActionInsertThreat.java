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
import org.conservationmeasures.eam.icons.InsertThreatIcon;
import org.conservationmeasures.eam.main.BaseProject;
import org.conservationmeasures.eam.main.EAM;

public class ActionInsertThreat extends InsertNodeAction
{
	public ActionInsertThreat(BaseProject projectToUse)
	{
		super(projectToUse, getLabel(), new InsertThreatIcon());
	}

	private static String getLabel()
	{
		return EAM.text("Action|Insert|Threat");
	}

	public String getInitialText()
	{
		return EAM.text("Label|New Threat");
	}

	public String getToolTipText()
	{
		return EAM.text("TT|Insert a Threat node");
	}

	public void doAction(ActionEvent event) throws CommandFailedException
	{
		doInsert(new CommandInsertNode(Node.TYPE_THREAT));
	}

	public boolean shouldBeEnabled()
	{
		return getProject().isOpen();
	}	
}

