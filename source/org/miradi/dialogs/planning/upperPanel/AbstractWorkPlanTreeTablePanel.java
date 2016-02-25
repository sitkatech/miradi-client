/* 
Copyright 2005-2015, Foundations of Success, Bethesda, Maryland
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
package org.miradi.dialogs.planning.upperPanel;

import com.jhlabs.awt.GridLayoutPlus;
import org.miradi.dialogs.fieldComponents.PanelTitleLabel;
import org.miradi.dialogs.planning.propertiesPanel.AboveBudgetColumnsBar;
import org.miradi.dialogs.planning.propertiesPanel.AbstractFixedHeightDirectlyAboveTreeTablePanel;
import org.miradi.dialogs.planning.propertiesPanel.WorkPlanAboveBudgetColumnsBar;
import org.miradi.icons.IconManager;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ORefSet;
import org.miradi.objects.PlanningTreeRowColumnProvider;
import org.miradi.objects.TableSettings;
import org.miradi.views.workplan.WorkPlanDiagramFilterPanel;

import javax.swing.*;

abstract class AbstractWorkPlanTreeTablePanel extends PlanningTreeTablePanel
{
	protected AbstractWorkPlanTreeTablePanel(MainWindow mainWindowToUse,
											 PlanningTreeTable treeToUse,
											 PlanningTreeTableModel modelToUse,
											 Class[] buttonActions,
											 PlanningTreeRowColumnProvider rowColumnProvider,
											 AbstractFixedHeightDirectlyAboveTreeTablePanel treeTableHeaderPanel) throws Exception
	{
		super(mainWindowToUse, treeToUse, modelToUse, buttonActions, rowColumnProvider, treeTableHeaderPanel);
	}

	@Override
	public AboveBudgetColumnsBar createAboveColumnBar()
	{
		WorkPlanAboveBudgetColumnsBar aboveMainTableBar = new WorkPlanAboveBudgetColumnsBar(getProject(), getMainTableInterface());
		aboveMainTableBar.setTableScrollPane(mainTableScrollPane);

		return aboveMainTableBar;
	}

	@Override
	protected void updateResourceFilter() throws Exception
	{
		TableSettings tableSettings = TableSettings.findOrCreate(getProject(), getIdentifier());
		ORefList projectResourceFilterRefs = tableSettings.getTableSettingsMap().getRefList(TableSettings.WORK_PLAN_PROJECT_RESOURCE_FILTER_CODELIST_KEY);
		ORefSet projectResourceRefsToRetain = new ORefSet(projectResourceFilterRefs);
		
		getWorkUnitsTableModel().setResourcesFilter(projectResourceRefsToRetain);
		getBudgetDetailsTableModel().setResourcesFilter(projectResourceRefsToRetain);
		getPlanningViewMainTableModel().setResourcesFilter(projectResourceRefsToRetain);

		if (getMainWindow().areAnyProjectResourceFiltersOn())
		{
			filterResourceLabel.setText(EAM.text("Resource filter is on"));
			filterResourceLabel.setIcon(IconManager.getWarningIcon());
		}
		else
		{
			filterResourceLabel.setText(" ");
			filterResourceLabel.setIcon(null);
		}
	}

	@Override
	protected void addFilterPanel(JPanel filterPanel) throws Exception
	{
		TableSettings tableSettings = TableSettings.findOrCreate(getProject(), getIdentifier());
		diagramFilterPanel = new WorkPlanDiagramFilterPanel(getProject(), getProject().getMetadata(), tableSettings, getRowColumnProvider());
		filterPanel.add(diagramFilterPanel);
	}

	@Override
	protected GridLayoutPlus createFilterStatusLayout()
	{
		return new GridLayoutPlus(2, 1, 0, 10, 0, 8);
	}

	@Override
	protected void addFilterStatusPanel(JPanel filterStatusPanel)
	{
		createFilterStatusLabels();
		filterStatusPanel.add(customizeTableLabel);
		filterStatusPanel.add(filterResourceLabel);
	}

	@Override
	protected GridLayoutPlus createButtonLayout()
	{
		return new GridLayoutPlus(2, 4, 1, 1);
	}

	@Override
	protected void updateDiagramFilter() throws Exception
	{
		diagramFilterPanel.updateDiagramFilterChoices();
	}

	@Override
	protected void updateCustomizeTableFilter() throws Exception
	{
		String workPlanBudgetMode = getProject().getTimePeriodCostsMapsCache().getWorkPlanBudgetMode();

		customizeTableLabel.setText(getTextForCustomizeTableFilter(workPlanBudgetMode));
		customizeTableLabel.setIcon(getIconForCustomizeTableFilter(workPlanBudgetMode));
	}

	protected String getIdentifier()
	{
		return getTabSpecificModelIdentifier();
	}

	public static String getTabSpecificModelIdentifier()
	{
		final String TAB_TAG = "Tab_Tag";
		return WorkPlanTreeTableModel.UNIQUE_TREE_TABLE_IDENTIFIER + TAB_TAG;
	}

	@Override
	public void updateStatusBar()
	{
		getMainWindow().updatePlanningDateRelatedStatus();
	}

	abstract protected String getTextForCustomizeTableFilter(String workPlanBudgetMode);

	abstract protected Icon getIconForCustomizeTableFilter(String workPlanBudgetMode);

	private void createFilterStatusLabels()
	{
		customizeTableLabel = new PanelTitleLabel(" ");
		filterResourceLabel = new PanelTitleLabel(" ");
	}

	private WorkPlanDiagramFilterPanel diagramFilterPanel;
	private PanelTitleLabel customizeTableLabel;
	private PanelTitleLabel filterResourceLabel;
}
