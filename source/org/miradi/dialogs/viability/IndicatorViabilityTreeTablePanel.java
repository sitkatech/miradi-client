/* 
Copyright 2005-2018, Foundations of Success, Bethesda, Maryland
on behalf of the Conservation Measures Partnership ("CMP").
Material developed between 2005-2013 is jointly copyright by Beneficent Technology, Inc. ("The Benetech Initiative"), Palo Alto, California.

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

import org.miradi.dialogs.planning.propertiesPanel.PlanningViewAbstractTreeTableSyncedTableModel;
import org.miradi.dialogs.planning.upperPanel.PlanningTreeMultiTableModel;
import org.miradi.dialogs.planning.upperPanel.PlanningTreeTable;
import org.miradi.dialogs.planning.upperPanel.PlanningTreeTableModel;
import org.miradi.dialogs.planning.upperPanel.PlanningTreeTablePanel;
import org.miradi.dialogs.planning.upperPanel.PlanningUpperMultiTable;
import org.miradi.dialogs.planning.upperPanel.IndicatorViabilityTableModel;
import org.miradi.dialogs.planning.upperPanel.ViabilityTreeTable;
import org.miradi.dialogs.treetables.GenericTreeTableModel;
import org.miradi.main.MainWindow;
import org.miradi.objects.PlanningTreeRowColumnProvider;

public class IndicatorViabilityTreeTablePanel extends PlanningTreeTablePanel
{
	protected IndicatorViabilityTreeTablePanel(MainWindow mainWindowToUse, PlanningTreeTable treeToUse, GenericTreeTableModel modelToUse,
											   Class[] buttonActions, PlanningTreeRowColumnProvider rowColumnProviderToUse) throws Exception
	{
		super(mainWindowToUse, treeToUse, modelToUse, buttonActions, rowColumnProviderToUse);
	}

	public static PlanningTreeTablePanel createTreeTablePanel(MainWindow mainWindowToUse, PlanningTreeTableModel model, Class[] buttonActions, PlanningTreeRowColumnProvider rowColumnProvider) throws Exception
	{
		ViabilityTreeTable treeTable = new ViabilityTreeTable(mainWindowToUse, model);

		return new IndicatorViabilityTreeTablePanel(mainWindowToUse, treeTable, model, buttonActions, rowColumnProvider);
	}

	@Override
	protected PlanningViewAbstractTreeTableSyncedTableModel createMainTableModel(final PlanningTreeRowColumnProvider rowColumnProviderToUse) throws Exception
	{
		return new IndicatorViabilityTableModel(getProject(), getTree(), rowColumnProviderToUse);
	}

	@Override
	protected PlanningUpperMultiTable createUpperMultiTable(PlanningTreeTable treeToUse, PlanningTreeMultiTableModel multiModelToUse)
	{
		return new PlanningUpperMultiTable(getMainWindow(), treeToUse, multiModelToUse);
	}

	@Override
	protected boolean shouldSetFocusOnFirstField()
	{
		return false;
	}
}
