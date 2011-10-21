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

import javax.swing.Icon;
import javax.swing.JComponent;

import org.miradi.dialogs.base.ObjectListManagementPanel;
import org.miradi.dialogs.planning.AbstractPlanningTreeRowColumnProvider;
import org.miradi.dialogs.planning.treenodes.PlanningTreeRootNode;
import org.miradi.dialogs.planning.upperPanel.PlanningTreeTablePanel;
import org.miradi.dialogs.planning.upperPanel.TargetViabilityTreeTableModel;
import org.miradi.dialogs.planning.upperPanel.TreeTableModelWithRebuilder;
import org.miradi.icons.IconManager;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objects.PlanningTreeRowColumnProvider;
import org.miradi.utils.BufferedImageFactory;
import org.miradi.utils.TableExporter;

//FIXME urgent - Make new target viability tree table work
public class TargetViabilityManagementPanelNew extends ObjectListManagementPanel
{
	private TargetViabilityManagementPanelNew(MainWindow mainWindowToUse, PlanningTreeTablePanel tablePanelToUse, TargetViabilityMultiPropertiesPanel propertiesPanel) throws Exception
	{
		super(mainWindowToUse, tablePanelToUse, propertiesPanel);
		
		setTreeTablePanel(tablePanelToUse);
	}
	
	public static TargetViabilityManagementPanelNew createManagementPanel(MainWindow mainWindowToUse) throws Exception
	{
		PlanningTreeRootNode rootNode = new PlanningTreeRootNode(mainWindowToUse.getProject());
		AbstractPlanningTreeRowColumnProvider rowColumnProvider = new TargetViabilityRowColumnProvider(mainWindowToUse.getProject());
		
		TreeTableModelWithRebuilder model = new TargetViabilityTreeTableModel(mainWindowToUse.getProject(), rootNode, rowColumnProvider);
		PlanningTreeTablePanel treeTablePanel = TargetViabilityTreeTablePanel.createTreeTablePanel(mainWindowToUse, model, rowColumnProvider);
		TargetViabilityMultiPropertiesPanel propertiesPanel = new TargetViabilityMultiPropertiesPanel(mainWindowToUse);
		
		return new TargetViabilityManagementPanelNew(mainWindowToUse, treeTablePanel, propertiesPanel);
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
		PlanningTreeRootNode rootNode = new PlanningTreeRootNode(getMainWindow().getProject());
		TreeTableModelWithRebuilder model = new TargetViabilityTreeTableModel(getMainWindow().getProject(), rootNode, rowColumnProvider);
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

	@Override
	public Icon getIcon()
	{
		return IconManager.getKeyEcologicalAttributeIcon();
	}
	
	@Override
	public String getPanelDescription()
	{
		return PANEL_DESCRIPTION_VIABILITY;
	}
	
	@Override
	public String getSplitterDescription()
	{
		return PANEL_DESCRIPTION_VIABILITY + SPLITTER_TAG;
	}

	private static String PANEL_DESCRIPTION_VIABILITY = EAM.text("Tab|Viability");
	
	private PlanningTreeTablePanel planningTreeTablePanel;
}
