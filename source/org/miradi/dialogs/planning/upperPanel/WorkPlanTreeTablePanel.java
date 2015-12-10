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
import org.miradi.actions.*;
import org.miradi.dialogs.fieldComponents.PanelTitleLabel;
import org.miradi.dialogs.planning.WorkPlanRowColumnProvider;
import org.miradi.dialogs.planning.propertiesPanel.AboveBudgetColumnsBar;
import org.miradi.dialogs.planning.propertiesPanel.AbstractFixedHeightDirectlyAboveTreeTablePanel;
import org.miradi.dialogs.planning.propertiesPanel.WorkPlanAboveBudgetColumnsBar;
import org.miradi.dialogs.planning.treenodes.PlanningTreeRootNodeAlwaysExpanded;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.main.CommandExecutedEvent;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ORefSet;
import org.miradi.objects.PlanningTreeRowColumnProvider;
import org.miradi.objects.ProjectMetadata;
import org.miradi.objects.TableSettings;
import org.miradi.questions.WorkPlanVisibleRowsQuestion;
import org.miradi.schemas.ProjectMetadataSchema;
import org.miradi.views.workplan.WorkPlanDiagramFilterPanel;

import javax.swing.*;

public class WorkPlanTreeTablePanel extends PlanningTreeTablePanel
{
	protected WorkPlanTreeTablePanel(MainWindow mainWindowToUse,
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
		PlanningTreeTableModel model = new WorkPlanTreeTableModel(mainWindowToUse.getProject(), rootNode, rowColumnProvider);
		PlanningTreeTable treeTable = new PlanningTreeTableWithVisibleRootNode(mainWindowToUse, model);
		AbstractFixedHeightDirectlyAboveTreeTablePanel treeTableHeaderPanel = new AbstractFixedHeightDirectlyAboveTreeTablePanel();

		return new WorkPlanTreeTablePanel(mainWindowToUse, treeTable, model, getButtonActions(), rowColumnProvider, treeTableHeaderPanel);
	}

	@Override
	public void handleCommandEventImmediately(CommandExecutedEvent event)
	{
		super.handleCommandEventImmediately(event);

		if (event.isSetDataCommandWithThisTypeAndTag(ProjectMetadataSchema.getObjectType(), ProjectMetadata.TAG_WORK_PLAN_DIAGRAM_DATA_INCLUSION))
		{
			try
			{
				diagramFilterPanel.updateDiagramFilterChoices();
			}
			catch(CommandFailedException e)
			{
				EAM.logException(e);
			}
		}
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
		TableSettings tableSettings = TableSettings.findOrCreate(getProject(), getTabSpecificModelIdentifier());
		ORefList projectResourceFilterRefs = tableSettings.getTableSettingsMap().getRefList(TableSettings.WORK_PLAN_PROJECT_RESOURCE_FILTER_CODELIST_KEY);
		ORefSet projectResourceRefsToRetain = new ORefSet(projectResourceFilterRefs);
		
		getWorkUnitsTableModel().setResourcesFilter(projectResourceRefsToRetain);
		getBudgetDetailsTableModel().setResourcesFilter(projectResourceRefsToRetain);
		getPlanningViewMainTableModel().setResourcesFilter(projectResourceRefsToRetain);

		getMainWindow().updatePlanningDateRelatedStatus();

		if (getMainWindow().areAnyProjectResourceFiltersOn())
			filterResourceLabel.setText(EAM.text("Resource filter is on"));
		else
			filterResourceLabel.setText(" ");
	}

	@Override
	protected void updateCustomizeTableFilter() throws Exception
	{
		String workPlanBudgetMode = this.getRowColumnProvider().getWorkPlanBudgetMode();
		String textToDisplay = WorkPlanVisibleRowsQuestion.getTextForChoice(workPlanBudgetMode);
		Icon iconToDisplay = WorkPlanVisibleRowsQuestion.getIconForChoice(workPlanBudgetMode);

		customizeTableLabel.setText(textToDisplay);
		customizeTableLabel.setIcon(iconToDisplay);
	}

	@Override
	protected void addDiagramFilterPanel(JPanel diagramFilterPanelToUse) throws Exception
	{
		TableSettings tableSettings = TableSettings.findOrCreate(getProject(), getTabSpecificModelIdentifier());
		diagramFilterPanel = new WorkPlanDiagramFilterPanel(getProject(), getProject().getMetadata(), tableSettings);
		diagramFilterPanelToUse.add(diagramFilterPanel);
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

	public static String getTabSpecificModelIdentifier()
	{
		final String TAB_TAG = "Tab_Tag";
		return WorkPlanTreeTableModel.UNIQUE_TREE_TABLE_IDENTIFIER + TAB_TAG;
	}
	
	@Override
	protected GridLayoutPlus createButtonLayout()
	{
		return new GridLayoutPlus(2, 4, 1, 1);
	}

	private static Class[] getButtonActions()
	{
		return new Class[] {
				ActionExpandAllRows.class, 
				ActionTreeNodeUp.class,
				ActionPlanningCreationMenu.class,
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
