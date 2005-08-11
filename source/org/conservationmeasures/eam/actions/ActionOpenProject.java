/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.actions;

import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.JFileChooser;

import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;

public class ActionOpenProject extends ProjectAction
{
	public ActionOpenProject(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse, getLabel(), "icons/open.gif");
	}

	private static String getLabel()
	{
		return EAM.text("Action|Open Project");
	}


	public void doAction(ActionEvent event) throws CommandFailedException
	{
		JFileChooser dlg = new JFileChooser();
		dlg.setDialogTitle(getLabel());
		if(dlg.showOpenDialog(getMainWindow()) != JFileChooser.APPROVE_OPTION)
			return;
		
		File chosen = dlg.getSelectedFile();
		getMainWindow().loadProject(chosen);
	}

	public String getToolTipText()
	{
		return EAM.text("TT|Open an existing project");
	}

	public boolean shouldBeEnabled()
	{
		return true;
	}
	
}
