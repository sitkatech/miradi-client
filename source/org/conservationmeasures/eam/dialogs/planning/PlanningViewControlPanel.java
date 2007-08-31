/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.planning;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.project.Project;

import com.jhlabs.awt.BasicGridLayout;

public class PlanningViewControlPanel extends JPanel
{
	public PlanningViewControlPanel(MainWindow mainWindowToUse)
	{
		mainWindow = mainWindowToUse;
		add(createScrollableLegendPanel());
	}
	
	private JScrollPane createScrollableLegendPanel()
	{
		planningCustomizationPanel = new PlanningCustomizationPanel(getProject());
		rowsLegendPanel = new PlanningViewRowsLegendPanel(getMainWindow());
		columnsLegendPanel = new PlanningViewColumsLegendPanel(getMainWindow());
		JPanel legendPanel = new JPanel(new BasicGridLayout(2, 1));
		
		legendPanel.add(rowsLegendPanel.createTitleBar(EAM.text("Control Bar")));
		legendPanel.add(planningCustomizationPanel);
		legendPanel.add(rowsLegendPanel);
		legendPanel.add(columnsLegendPanel);
			
		return new JScrollPane(legendPanel);
	}
	
	public void dispose()
	{
		rowsLegendPanel.dispose();
		columnsLegendPanel.dispose();
		planningCustomizationPanel.dispose();
	}
	
	private MainWindow getMainWindow()
	{
		return mainWindow;
	}
	
	private Project getProject()
	{
		return getMainWindow().getProject();
	}

	private MainWindow mainWindow;
	private PlanningViewRowsLegendPanel rowsLegendPanel;
	private PlanningCustomizationPanel planningCustomizationPanel;
	private PlanningViewColumsLegendPanel columnsLegendPanel;
}
