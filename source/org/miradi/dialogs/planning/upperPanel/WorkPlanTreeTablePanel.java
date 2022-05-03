/* 
Copyright 2005-2022, Foundations of Success, Bethesda, Maryland
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
import org.miradi.actions.*;
import org.miradi.dialogs.fieldComponents.PanelTitleLabel;
import org.miradi.dialogs.planning.WorkPlanRowColumnProvider;
import org.miradi.dialogs.planning.propertiesPanel.AboveBudgetColumnsBar;
import org.miradi.dialogs.planning.propertiesPanel.AbstractFixedHeightDirectlyAboveTreeTablePanel;
import org.miradi.dialogs.planning.propertiesPanel.PlanningViewAbstractTreeTableSyncedTableModel;
import org.miradi.dialogs.planning.propertiesPanel.WorkPlanAboveBudgetColumnsBar;
import org.miradi.dialogs.planning.treenodes.PlanningTreeRootNodeAlwaysExpanded;
import org.miradi.icons.IconManager;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ORefSet;
import org.miradi.objects.PlanningTreeRowColumnProvider;
import org.miradi.objects.TableSettings;
import org.miradi.questions.WorkPlanVisibleRowsQuestion;
import org.miradi.views.workplan.WorkPlanDiagramFilterPanel;

import javax.swing.*;

public class WorkPlanTreeTablePanel extends PlanningTreeTablePanel
{
	private WorkPlanTreeTablePanel(MainWindow mainWindowToUse,
								   PlanningTreeTable treeToUse,
								   PlanningTreeTableModel modelToUse,
								   Class[] buttonActions,
								   PlanningTreeRowColumnProvider rowColumnProvider,
								   AbstractFixedHeightDirectlyAboveTreeTablePanel treeTableHeaderPanel) throws Exception
	{
		super(mainWindowToUse, treeToUse, modelToUse, buttonActions, rowColumnProvider, treeTableHeaderPanel);
	}

	public static PlanningTreeTablePanel createPlanningTreeTablePanel(MainWindow mainWindowToUse) throws Exception
	{
		WorkPlanRowColumnProvider rowColumnProvider = new WorkPlanRowColumnProvider(mainWindowToUse.getProject());
		PlanningTreeRootNodeAlwaysExpanded rootNode = new PlanningTreeRootNodeAlwaysExpanded(mainWindowToUse.getProject());
		WorkPlanTreeTableModel model = new WorkPlanTreeTableModel(mainWindowToUse.getProject(), rootNode, rowColumnProvider);
		WorkPlanningTreeTableWithVisibleRootNode treeTable = new WorkPlanningTreeTableWithVisibleRootNode(mainWindowToUse, model);
		AbstractFixedHeightDirectlyAboveTreeTablePanel treeTableHeaderPanel = new AbstractFixedHeightDirectlyAboveTreeTablePanel();

		return new WorkPlanTreeTablePanel(mainWindowToUse, treeTable, model, getButtonActions(), rowColumnProvider, treeTableHeaderPanel);
	}

	@Override
	protected PlanningUpperMultiTable createUpperMultiTable(PlanningTreeTable treeToUse, PlanningTreeMultiTableModel multiModelToUse)
	{
		return new WorkPlanUpperMultiTable(getMainWindow(), treeToUse, multiModelToUse);
	}

	@Override
	protected PlanningViewAbstractTreeTableSyncedTableModel createMainTableModel(final PlanningTreeRowColumnProvider rowColumnProviderToUse) throws Exception
	{
		return new WorkPlanViewMainTableModel(getProject(), getTree(), rowColumnProviderToUse);
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
		ORefSet projectResourceRefsToRetain = getResourceFilter();
		
		getWorkUnitsTableModel().setResourcesFilter(projectResourceRefsToRetain);
		getBudgetDetailsTableModel().setResourcesFilter(projectResourceRefsToRetain);
		getPlanningViewMainTableModel().setResourcesFilter(projectResourceRefsToRetain);

		PlanningUpperMultiTable mainTable = (PlanningUpperMultiTable) getMainTable();
		mainTable.setResourcesFilter(projectResourceRefsToRetain);

		if (getMainWindow().areAnyProjectResourceFiltersOn())
		{
			filterResourceLabel.setText(EAM.text("People filter is on"));
			filterResourceLabel.setIcon(IconManager.getWarningIcon());
		}
		else
		{
			filterResourceLabel.setText(" ");
			filterResourceLabel.setIcon(null);
		}
	}

	private ORefSet getResourceFilter()
	{
		try
		{
			TableSettings tableSettings = TableSettings.findOrCreate(getProject(), getIdentifier());
			ORefList projectResourceFilterRefs = tableSettings.getTableSettingsMap().getRefList(TableSettings.WORK_PLAN_PROJECT_RESOURCE_FILTER_CODELIST_KEY);
			return new ORefSet(projectResourceFilterRefs);
		}
		catch (Exception e)
		{
			EAM.logException(e);
			return new ORefSet();
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

	private String getTextForCustomizeTableFilter(String workPlanBudgetMode)
	{
		return WorkPlanVisibleRowsQuestion.getTextForChoice(workPlanBudgetMode);
	}

	private Icon getIconForCustomizeTableFilter(String workPlanBudgetMode)
	{
		return WorkPlanVisibleRowsQuestion.getIconForChoice(workPlanBudgetMode);
	}

	private static Class[] getButtonActions()
	{
		return new Class[] {
				ActionExpandToMenu.class,
				ActionTreeNodeUp.class,
				ActionWorkPlanningCreationMenu.class,
				ActionWorkPlanBudgetCustomizeTableEditor.class,

				ActionCollapseAllRows.class,
				ActionTreeNodeDown.class,
				ActionDeletePlanningViewTreeNode.class,
				ActionFilterWorkPlanByProjectResource.class,
		};
	}

	private void createFilterStatusLabels()
	{
		customizeTableLabel = new PanelTitleLabel(" ");
		filterResourceLabel = new PanelTitleLabel(" ");
	}

	private WorkPlanDiagramFilterPanel diagramFilterPanel;
	private PanelTitleLabel customizeTableLabel;
	private PanelTitleLabel filterResourceLabel;
}
