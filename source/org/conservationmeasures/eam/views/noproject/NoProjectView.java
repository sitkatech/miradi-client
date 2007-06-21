/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.noproject;

import javax.swing.JToolBar;

import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.umbrella.UmbrellaView;
import org.conservationmeasures.eam.wizard.WizardPanel;

public class NoProjectView extends UmbrellaView
{
	public NoProjectView(MainWindow mainWindow) throws Exception
	{
		super(mainWindow);
		wizardPanel = new WizardPanel(mainWindow, this);
	}
	
	public JToolBar createToolBar()
	{
		return new NoProjectToolBar(getActions());
	}

	
	public WizardPanel createWizardPanel() throws Exception
	{
		return wizardPanel;
	}

	
	public void becomeActive() throws Exception
	{
		super.becomeActive();
		getMainWindow().setDividerLocation(Integer.MAX_VALUE);
	}

	public void becomeInactive() throws Exception
	{
		getMainWindow().setDividerLocation(getMainWindow().getHeight()/2);
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

