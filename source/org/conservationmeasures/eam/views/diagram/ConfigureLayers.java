/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.diagram;


import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.views.ViewDoer;

public class ConfigureLayers extends ViewDoer
{
	public boolean isAvailable()
	{
		return true;
	}

	public void doIt() throws CommandFailedException
	{
		if(!isAvailable())
			return;
		
		MainWindow window = getMainWindow();
		LayerManager manager = window.getProject().getLayerManager();
		LayerDialog dlg = new LayerDialog(EAM.mainWindow, manager);
		dlg.setVisible(true);
		if(!dlg.getResult())
			return;

		dlg.updateLayerManager(manager);
		window.updateStatusBar();
		window.getDiagramComponent().updateVisibilityOfNodes(window);
	}

}
