/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.noproject;

import javax.swing.JToolBar;

import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.noproject.wizard.NoProjectWizardPanel;
import org.conservationmeasures.eam.views.umbrella.UmbrellaView;

public class NoProjectView extends UmbrellaView
{
	public NoProjectView(MainWindow mainWindow) throws Exception
	{
		super(mainWindow);
		wizardPanel = new NoProjectWizardPanel(this);
	}
	
	public JToolBar createToolBar()
	{
		return new NoProjectToolBar(getActions());
	}

	public void becomeActive() throws Exception
	{
		super.becomeActive();
		wizardPanel.refresh();
		add(wizardPanel);
	}

	public void becomeInactive() throws Exception
	{
		// nothing to do...would clear all view data
		super.becomeInactive();
	}
	
	public void refreshText() throws Exception
	{
		wizardPanel.refresh();
	}

	public String cardName()
	{
		return getViewName();
	}
	
	static public String getViewName()
	{
		return Project.NO_PROJECT_VIEW_NAME;
	}

}

