/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.targetviability;

import javax.swing.JPanel;

import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.TabbedView;
import org.conservationmeasures.eam.wizard.WizardPanel;
import org.martus.swing.UiScrollPane;

import com.jhlabs.awt.GridLayoutPlus;

public class TargetViabilityView extends TabbedView
{
	public TargetViabilityView(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse);
		setToolBar(new TargetViabilityToolBar(mainWindowToUse.getActions()));
		wizardPanel = new WizardPanel(mainWindowToUse, this);
	}
	
	public String cardName()
	{
		return getViewName();
	}
	
	static public String getViewName()
	{
		return Project.TARGET_VIABILITY_NAME;
	}

	public void createTabs() throws Exception
	{
		JPanel panel = new JPanel(new GridLayoutPlus(0,1));
		addTab(getViewName(), new UiScrollPane(panel));
	}

	public void deleteTabs() throws Exception
	{
	}

	public WizardPanel createWizardPanel() throws Exception
	{
		return wizardPanel;
	}
}