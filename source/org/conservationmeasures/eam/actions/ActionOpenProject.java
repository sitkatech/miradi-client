/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.actions;

import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.JFileChooser;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.main.MainWindowAction;

public class ActionOpenProject extends MainWindowAction
{
	public ActionOpenProject(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse, getLabel());
	}

	private static String getLabel()
	{
		return EAM.text("Action|Open Project");
	}


	public void actionPerformed(ActionEvent arg0)
	{
		JFileChooser dlg = new JFileChooser();
		if(dlg.showOpenDialog(getMainWindow()) != JFileChooser.APPROVE_OPTION)
			return;
		
		File chosen = dlg.getSelectedFile();
		getMainWindow().loadProject(chosen);
	}
}
