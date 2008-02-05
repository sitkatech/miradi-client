/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.planning;

import java.awt.image.BufferedImage;

import javax.swing.JComponent;

import org.conservationmeasures.eam.dialogs.base.ObjectListManagementPanel;
import org.conservationmeasures.eam.dialogs.planning.propertiesPanel.PlanningTreePropertiesPanel;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.utils.BufferedImageFactory;
import org.conservationmeasures.eam.utils.ExportableTableInterface;

public class PlanningTreeManagementPanel extends ObjectListManagementPanel
{
	public PlanningTreeManagementPanel(MainWindow mainWindowToUse, PlanningTreeTablePanel planningTreeTablePanel, PlanningTreePropertiesPanel planningTreePropertiesPanel) throws Exception
	{
		super(mainWindowToUse.getProject(), mainWindowToUse, planningTreeTablePanel, planningTreePropertiesPanel);
		mainWindow = mainWindowToUse;
	}

	public String getPanelDescription()
	{
		return PANEL_DESCRIPTION;
	}
	
	@Override
	public boolean isImageAvailable()
	{
		return true;
	}
	
	@Override
	public BufferedImage getImage() throws Exception
	{
		PlanningTreeTablePanel panel = (PlanningTreeTablePanel) getTabContentsComponent();
		BufferedImage image = BufferedImageFactory.createImageFromComponent(panel);
		
		panel.dispose();
		return image;
	}

	@Override
	public boolean isExportableTableAvailable()
	{
		return true;
	}
	
	public ExportableTableInterface getExportableTable() throws Exception
	{
		PlanningTreeTablePanel panel = (PlanningTreeTablePanel) getTabContentsComponent();
		ExportableTableInterface table = panel.getTableForExporting();
		panel.dispose();
		
		return table;
	}

	@Override
	public JComponent getTabContentsComponent() throws Exception
	{
		return PlanningTreeTablePanel.createPlanningTreeTablePanel(mainWindow);
	}
	
	private static String PANEL_DESCRIPTION = EAM.text("Tab|Planning");
	
	private MainWindow mainWindow;
}
