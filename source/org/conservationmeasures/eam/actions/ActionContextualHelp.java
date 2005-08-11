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

public class ActionContextualHelp extends ProjectAction
{
	public ActionContextualHelp(BaseProject projectToUse)
	{
		super(projectToUse, getLabel());
	}

	private static String getLabel()
	{
		return EAM.text("Action|ContextSensitive|Help");
	}

	public void doAction(ActionEvent event) throws CommandFailedException
	{
		EAM.logWarning("Context-sensitive help action invoked!");
	}
}