/* 
Copyright 2005-2011, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
*/ 

package org.miradi.dialogs.viability;

import java.awt.image.BufferedImage;

import javax.swing.JComponent;

import org.miradi.dialogs.base.ObjectListManagementPanel;
import org.miradi.dialogs.planning.treenodes.PlanningTreeRootNodeAlwaysExpanded;
import org.miradi.dialogs.planning.upperPanel.PlanningTreeTablePanel;
import org.miradi.dialogs.planning.upperPanel.TreeTableModelWithRebuilder;
import org.miradi.dialogs.planning.upperPanel.ViabilityTreeTableModel;
import org.miradi.main.MainWindow;
import org.miradi.objects.PlanningTreeRowColumnProvider;
import org.miradi.utils.BufferedImageFactory;
import org.miradi.utils.TableExporter;

abstract public class AbstractViabilityManagementPanel extends ObjectListManagementPanel
{
	protected AbstractViabilityManagementPanel(MainWindow mainWindowToUse, PlanningTreeTablePanel tablePanelToUse, TargetViabilityMultiPropertiesPanel propertiesPanel) throws Exception
	{
		super(mainWindowToUse, tablePanelToUse, propertiesPanel);
		
		setTreeTablePanel(tablePanelToUse);
	}	
	
	@Override
	public boolean isImageAvailable()
	{
		return true;
	}
	
	@Override
	public BufferedImage getImage(int scale) throws Exception
	{
		JComponent panel = getPrintableComponent();
		BufferedImage image = BufferedImageFactory.createImageFromComponent(panel);
		return image;
	}

	@Override
	public boolean isExportableTableAvailable()
	{
		return true;
	}
	
	@Override
	public TableExporter getTableExporter() throws Exception
	{
		PlanningTreeTablePanel panel = createPlanningTreeTablePanelForExport();
		TableExporter table = panel.getTableForExporting();
		panel.dispose();
		
		return table;
	}

	private PlanningTreeTablePanel createPlanningTreeTablePanel(String uniqueTreeTableModelIdentifier, PlanningTreeRowColumnProvider rowColumnProvider) throws Exception
	{
		PlanningTreeRootNodeAlwaysExpanded rootNode = new PlanningTreeRootNodeAlwaysExpanded(getMainWindow().getProject());
		TreeTableModelWithRebuilder model = new ViabilityTreeTableModel(getMainWindow().getProject(), rootNode, rowColumnProvider);
		PlanningTreeTablePanel treeTablePanel = TargetViabilityTreeTablePanel.createTreeTablePanel(getMainWindow(), model, rowColumnProvider);
		
		return treeTablePanel;
	}

	@Override
	public JComponent getPrintableComponent() throws Exception
	{
		PlanningTreeTablePanel panel = createPlanningTreeTablePanelForExport();
		
		return PlanningTreeTablePanel.createReformattedPrintablePlanningTreeTablePanel(panel);
	}
	
	private PlanningTreeTablePanel createPlanningTreeTablePanelForExport() throws Exception
	{
		String uniqueTreeTableModelIdentifier = getPlanningTreeTablePanel().getTree().getTreeTableModel().getUniqueTreeTableModelIdentifier();
		PlanningTreeRowColumnProvider rowColumnProvider = getPlanningTreeTablePanel().getRowColumnProvider();
		PlanningTreeTablePanel panel = createPlanningTreeTablePanel(uniqueTreeTableModelIdentifier,	rowColumnProvider);
		panel.becomeActive();
		
		return panel;
	}
	
	private void setTreeTablePanel(PlanningTreeTablePanel planningTreeTablePanelToUse)
	{
		planningTreeTablePanel = planningTreeTablePanelToUse;
	}

	private PlanningTreeTablePanel getPlanningTreeTablePanel()
	{
		return planningTreeTablePanel;
	}
	
	private PlanningTreeTablePanel planningTreeTablePanel;
}
