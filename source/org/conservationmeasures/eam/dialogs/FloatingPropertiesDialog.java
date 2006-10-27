/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.dialogs;

import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objects.EAMObject;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.umbrella.UmbrellaView;

abstract public class FloatingPropertiesDialog extends ModelessDialogWithClose
{
	public FloatingPropertiesDialog(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse);
		mainWindow = mainWindowToUse;
	}

	abstract public EAMObject getObject();

	public MainWindow getMainWindow()
	{
		return mainWindow;
	}
	
	public Project getProject()
	{
		return getMainWindow().getProject();
	}
	
	public UmbrellaView getView()
	{
		return getMainWindow().getCurrentView();
	}

	MainWindow mainWindow;

}
