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

import javax.swing.Icon;

import org.miradi.dialogs.base.ObjectListManagementPanel;
import org.miradi.dialogs.planning.AbstractPlanningTreeRowColumnProvider;
import org.miradi.dialogs.planning.treenodes.PlanningTreeRootNode;
import org.miradi.dialogs.planning.upperPanel.PlanningTreeTablePanel;
import org.miradi.dialogs.planning.upperPanel.TargetViabilityTreeTableModel;
import org.miradi.dialogs.planning.upperPanel.TreeTableModelWithRebuilder;
import org.miradi.dialogs.treetables.TreeTablePanel;
import org.miradi.icons.IconManager;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;

//FIXME urgent - Make new target viability tree table work
public class TargetViabilityManagementPanelNew extends ObjectListManagementPanel
{
	private TargetViabilityManagementPanelNew(MainWindow mainWindowToUse, TreeTablePanel tablePanelToUse, TargetViabilityMultiPropertiesPanel propertiesPanel) throws Exception
	{
		super(mainWindowToUse, tablePanelToUse, propertiesPanel);
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
}
