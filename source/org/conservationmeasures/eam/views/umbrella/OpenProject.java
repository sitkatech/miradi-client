/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.umbrella;

import java.io.File;

import javax.swing.JFileChooser;

import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.views.MainWindowDoer;

public class OpenProject extends MainWindowDoer
{
	public OpenProject(MainWindow mainWindow)
	{
		super(mainWindow);
	}
	
	public void doIt() throws CommandFailedException
	{
		JFileChooser dlg = new JFileChooser();
		dlg.setDialogTitle(EAM.text("Title|Open Existing Project"));
		dlg.setFileFilter(new EamProjectFileFilter());
		if(dlg.showOpenDialog(getMainWindow()) != JFileChooser.APPROVE_OPTION)
			return;
		
		File chosen = dlg.getSelectedFile();
		getMainWindow().loadProject(chosen);
	}
	
	public boolean isAvailable()
	{
		return true;
	}

}
