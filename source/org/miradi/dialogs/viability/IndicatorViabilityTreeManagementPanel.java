/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
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

import org.miradi.actions.ActionCloneIndicator;
import org.miradi.actions.ActionCreateIndicator;
import org.miradi.actions.ActionCreateIndicatorMeasurement;
import org.miradi.actions.ActionDeleteIndicator;
import org.miradi.actions.ActionDeleteIndicatorMeasurement;
import org.miradi.actions.jump.ActionJumpMonitoringWizardDefineIndicatorsStep;
import org.miradi.dialogs.planning.treenodes.PlanningTreeBaseObjectNode;
import org.miradi.dialogs.planning.upperPanel.PlanningTreeTablePanel;
import org.miradi.dialogs.planning.upperPanel.ViabilityTreeTableModel;
import org.miradi.dialogs.planning.upperPanel.TreeTableModelWithRebuilder;
import org.miradi.icons.IconManager;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORef;

public class IndicatorViabilityTreeManagementPanel extends TargetViabilityManagementPanel
{
	private IndicatorViabilityTreeManagementPanel(MainWindow mainWindowToUse, PlanningTreeTablePanel tablePanelToUse, TargetViabilityMultiPropertiesPanel propertiesPanel) throws Exception
	{
		super(mainWindowToUse, tablePanelToUse, propertiesPanel);
	}
	
	public static TargetViabilityManagementPanel createIndicatorViabilityManagementPanel(MainWindow mainWindowToUse, ORef parentRefToUse) throws Exception
	{
		PlanningTreeBaseObjectNode rootNode = new PlanningTreeBaseObjectNode(mainWindowToUse.getProject(), null, parentRefToUse);
		IndicatorViabilityRowColumnProvider rowColumnProvider = new IndicatorViabilityRowColumnProvider(mainWindowToUse.getProject());
		
		TreeTableModelWithRebuilder model = new ViabilityTreeTableModel(mainWindowToUse.getProject(), rootNode, rowColumnProvider);
		PlanningTreeTablePanel treeTablePanel = IndicatorViabilityTreeTablePanel.createTreeTablePanel(mainWindowToUse, model, buttonActions, rowColumnProvider);
		TargetViabilityMultiPropertiesPanel propertiesPanel = new TargetViabilityMultiPropertiesPanel(mainWindowToUse);
		
		return new IndicatorViabilityTreeManagementPanel(mainWindowToUse, treeTablePanel, propertiesPanel);
	}
	
	@Override
	public String getSplitterDescription()
	{
		return PANEL_DESCRIPTION_INDICATORS + SPLITTER_TAG;
	}
	
	@Override
	public String getPanelDescription()
	{
		return PANEL_DESCRIPTION_INDICATORS;
	}
	
	@Override
	public Icon getIcon()
	{
		return IconManager.getIndicatorIcon();
	}
	
	@Override
	public Class getJumpActionClass()
	{
		return ActionJumpMonitoringWizardDefineIndicatorsStep.class;
	}
	
	private static final Class[] buttonActions = new Class[] {
		ActionCreateIndicator.class,
		ActionCreateIndicatorMeasurement.class,
		ActionCloneIndicator.class,
		ActionDeleteIndicator.class,
		ActionDeleteIndicatorMeasurement.class, 
	};

	private static String PANEL_DESCRIPTION_INDICATORS = EAM.text("Tab|Indicators"); 
}
