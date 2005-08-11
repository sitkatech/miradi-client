/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.actions;

import java.awt.event.ActionEvent;

import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.BaseProject;
import org.conservationmeasures.eam.main.EAM;

public class ActionCut extends ProjectAction
{
	public ActionCut(BaseProject projectToUse)
	{
		super(projectToUse, getLabel(), "icons/cut.gif");
	}

	private static String getLabel()
	{
		return EAM.text("Action|Cut");
	}


	public void doAction(ActionEvent event) throws CommandFailedException
	{
		ActionCopy copy = new ActionCopy(getProject());
		copy.performCopy();
		ActionDelete delete = new ActionDelete(getProject());
		delete.performDelete();
	}

	public String getToolTipText()
	{
		return EAM.text("TT|Cut the selection to the clipboard");
	}
}
