/* 
Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
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
package org.miradi.dialogs.planning.upperPanel;

import java.awt.BorderLayout;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.miradi.actions.ActionCollapseAllNodes;
import org.miradi.actions.ActionDeletePlanningViewTreeNode;
import org.miradi.actions.ActionExpandAllNodes;
import org.miradi.actions.ActionTreeCreateActivity;
import org.miradi.actions.ActionTreeCreateIndicator;
import org.miradi.actions.ActionTreeCreateMethod;
import org.miradi.actions.ActionTreeCreateObjective;
import org.miradi.actions.ActionTreeCreateTask;
import org.miradi.actions.ActionTreeNodeDown;
import org.miradi.actions.ActionTreeNodeUp;
import org.miradi.actions.ActionTreeShareActivity;
import org.miradi.actions.ActionTreeShareMethod;
import org.miradi.commands.CommandSetObjectData;
import org.miradi.dialogs.planning.propertiesPanel.PlanningViewMainModelExporter;
import org.miradi.dialogs.tablerenderers.FontForObjectTypeProvider;
import org.miradi.dialogs.tablerenderers.PlanningViewFontProvider;
import org.miradi.dialogs.treetables.TreeTablePanelWithSixButtonColumns;
import org.miradi.main.CommandExecutedEvent;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.Assignment;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Factor;
import org.miradi.objects.Indicator;
import org.miradi.objects.KeyEcologicalAttribute;
import org.miradi.objects.Measurement;
import org.miradi.objects.Objective;
import org.miradi.objects.Strategy;
import org.miradi.objects.Target;
import org.miradi.objects.Task;
import org.miradi.utils.AbstractTableExporter;
import org.miradi.utils.CodeList;
import org.miradi.utils.MultiTableCombinedAsOneExporter;
import org.miradi.utils.MultiTableRowHeightController;
import org.miradi.utils.MultiTableVerticalScrollController;
import org.miradi.utils.MultipleTableSelectionController;
import org.miradi.utils.TreeTableExporter;
import org.miradi.views.planning.ColumnManager;
import org.miradi.views.planning.PlanningView;

public class PlanningTreeTablePanel extends TreeTablePanelWithSixButtonColumns
{
	public static PlanningTreeTablePanel createPlanningTreeTablePanelWithoutButtons(MainWindow mainWindowToUse) throws Exception
	{
		Class[] noButtons = new Class[0];
		return createPlanningTreeTablePanel(mainWindowToUse, noButtons);
	}
	
	public static PlanningTreeTablePanel createPlanningTreeTablePanel(MainWindow mainWindowToUse) throws Exception
	{
		return createPlanningTreeTablePanel(mainWindowToUse, getButtonActions());
	}
	
	public static PlanningTreeTablePanel createPlanningTreeTablePanel(MainWindow mainWindowToUse, Class[] buttonActions) throws Exception
	{
		PlanningTreeTableModel model = new PlanningTreeTableModel(mainWindowToUse.getProject());
		PlanningTreeTable treeTable = new PlanningTreeTable(mainWindowToUse, model);	
		
		return new PlanningTreeTablePanel(mainWindowToUse, treeTable, model, buttonActions);
	}
	
	private PlanningTreeTablePanel(MainWindow mainWindowToUse, PlanningTreeTable treeToUse, PlanningTreeTableModel modelToUse, Class[] buttonActions) throws Exception
	{
		super(mainWindowToUse, treeToUse, buttonActions);
		model = modelToUse;
		
		treeTableScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

		MultiTableRowHeightController rowHeightController = new MultiTableRowHeightController(getMainWindow());
		rowHeightController.addTable(treeToUse);
		
		MultipleTableSelectionController selectionController = new MultipleTableSelectionController();
		selectionController.addTable(treeToUse);
		
		MultiTableVerticalScrollController scrollController = new MultiTableVerticalScrollController();
		scrollController.addScrollPane(treeTableScrollPane);
		
		multiTableExporter = new MultiTableCombinedAsOneExporter();		
		
		listenForColumnWidthChanges(getTree());
		
		mainModel = new PlanningViewMainTableModel(getProject(), treeToUse);
		multiModel = new PlanningTreeMultiTableModel();

		annualTotalsModel = new PlanningViewBudgetAnnualTotalTableModel(getProject(), treeToUse);
		
		measurementModel = new PlanningViewMeasurementTableModel(getProject(), treeToUse);
		
		futureStatusModel = new PlanningViewFutureStatusTableModel(getProject(), treeToUse);
		
		FontForObjectTypeProvider fontProvider = new PlanningViewFontProvider(getMainWindow());
		mainTable = new PlanningUpperMultiTable(treeToUse, multiModel, fontProvider);
		
		mainTableScrollPane = integrateTable(treeTableScrollPane.getVerticalScrollBar(), scrollController, rowHeightController, selectionController, treeToUse, mainTable);
		mainTableScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

		// NOTE: Replace treeScrollPane that super constructor put in CENTER
		add(treeTableScrollPane, BorderLayout.BEFORE_LINE_BEGINS);
		add(mainTableScrollPane, BorderLayout.CENTER);
		
		rebuildEntireTreeTable();
	}
	
	@Override
	public void dispose()
	{
		super.dispose();
		
		mainTable.dispose();
	}
	
	private static Class[] getButtonActions()
	{
		return new Class[] {
			ActionTreeCreateIndicator.class,
			ActionTreeCreateActivity.class,
			ActionTreeCreateMethod.class,
			ActionTreeCreateTask.class,			
			ActionTreeNodeUp.class,
			ActionExpandAllNodes.class,
			
			ActionTreeCreateObjective.class,
			ActionTreeShareActivity.class,
			ActionTreeShareMethod.class,
			ActionDeletePlanningViewTreeNode.class,
			ActionTreeNodeDown.class,
			ActionCollapseAllNodes.class,
		};
	}
	
	public void commandExecuted(CommandExecutedEvent event)
	{
		try
		{		
			if (doesCommandForceRebuild(event) || doesCommandAffectRowHeight(event))
				rebuildEntireTreeTable();
			else if(isTreeExpansionCommand(event))
				restoreTreeExpansionState();
			
			if(isSelectedObjectModification(event, Assignment.getObjectType()))
				validate();
		
			repaintToGrowIfTreeIsTaller();
		}
		catch(Exception e)
		{
			EAM.logException(e);
			EAM.errorDialog("Error occurred: " + e.getMessage());
		}
		
	}

	private boolean doesCommandForceRebuild(CommandExecutedEvent event)
	{
		if(PlanningView.isRowOrColumnChangingCommand(event))
			return true;
		
		if(didAffectTaskInTree(event))
			return true;
		
		if (didAffectIndicatorInTree(event))
			return true;
		
		if (didAffectRelevancyInTree(event))
			return true;
		
		if (isTargetModeChange(event))
			return true;
		
		return false;
	}
	
	private boolean isTargetModeChange (CommandExecutedEvent event)
	{
		return event.isSetDataCommandWithThisTypeAndTag(Target.getObjectType(), Target.TAG_VIABILITY_MODE);
	}
	
	private boolean didAffectRelevancyInTree(CommandExecutedEvent event)
	{
		if (! event.isSetDataCommand())
			return false;
		
		CommandSetObjectData setCommand = (CommandSetObjectData) event.getCommand();
		ORef ref = setCommand.getObjectORef();
		String tag = setCommand.getFieldTag();

		if(Objective.is(ref))
		{
			if(tag.equals(Objective.TAG_RELEVANT_STRATEGY_ACTIVITY_SET))
				return true;
			
			if(tag.equals(Objective.TAG_RELEVANT_INDICATOR_SET))
				return true;
		}

		return false;
	}

	private boolean didAffectIndicatorInTree(CommandExecutedEvent event)
	{
		if (! event.isSetDataCommand())
			return false;
		
		CommandSetObjectData setCommand = (CommandSetObjectData) event.getCommand();
		int type = setCommand.getObjectType();
		String tag = setCommand.getFieldTag();
		if(Factor.isFactor(type))
			return isValidFactorTag(tag);
		
		if(type == KeyEcologicalAttribute.getObjectType() && tag.equals(KeyEcologicalAttribute.TAG_INDICATOR_IDS))
			return true;
				
		return false;
	}
	
	private boolean isValidFactorTag(String relevancyTag)
	{
		if (relevancyTag.equals(Factor.TAG_INDICATOR_IDS))
				return true;
		
		if (relevancyTag.equals(Factor.TAG_OBJECTIVE_IDS))
			return true;
		
		return false;
	}

	//TODO this should use that getTasksTag (or something like that) method
	//from email :Please put a todo in isTaskMove that it should use that 
	//getTasksTag method (or whatever it's called) that I mentioned the 
	//other day. I know that one is my code not yours.
	private boolean didAffectTaskInTree(CommandExecutedEvent event)
	{
		if (! event.isSetDataCommand())
			return false;
		
		CommandSetObjectData setCommand = (CommandSetObjectData) event.getCommand();
		int type = setCommand.getObjectType();
		String tag = setCommand.getFieldTag();
		if(type == Task.getObjectType() && tag.equals(Task.TAG_SUBTASK_IDS))
			return true;
		if(type == Strategy.getObjectType() && tag.equals(Strategy.TAG_ACTIVITY_IDS))
			return true;
		if(type == Indicator.getObjectType() && tag.equals(Indicator.TAG_TASK_IDS))
			return true;
		if(type == Task.getObjectType() && tag.equals(Task.TAG_ASSIGNMENT_IDS))
			return true;
		
		return false;
	}
	
	private void rebuildEntireTreeTable() throws Exception
	{
		ORef selectedRef = ORef.INVALID;
		BaseObject[] selected = tree.getSelectedObjects();
		if(selected.length == 1)
			selectedRef = selected[0].getRef();
		int selectedRow = tree.getSelectionModel().getAnchorSelectionIndex();

		// TODO: Perhaps possibly detect exactly what changed and 
		// only rebuild the columns or the rows rather than always doing both
	
		// NOTE: The following rebuild the columns but don't touch the tree
		getPlanningModel().updateColumnsToShow();
		tree.rebuildTableCompletely();

		mainModel.updateColumnsToShow();
		
		// NOTE: The following rebuild the tree but don't touch the columns
		getPlanningModel().rebuildEntireTree();
		annualTotalsModel.fireTableDataChanged();
		measurementModel.fireTableDataChanged();
		futureStatusModel.fireTableDataChanged();
		restoreTreeExpansionState();
		updateRightSideTablePanels();

		tree.selectObjectAfterSwingClearsItDueToTreeStructureChange(selectedRef, selectedRow);
	}
	
	private void updateRightSideTablePanels() throws Exception
	{
		multiTableExporter.clear();
		multiModel.removeAllModels();
		multiModel.addModel(mainModel);

		multiTableExporter.addAsMasterTable(new TreeTableExporter(getTree()));
		multiTableExporter.addExportable(new PlanningViewMainModelExporter(multiModel, getTree()));
		mainTableScrollPane.showVerticalScrollBar();

		CodeList columnsToShow = new CodeList(ColumnManager.getVisibleColumnCodes(getProject().getCurrentViewData()));
		if (columnsToShow.contains(Task.PSEUDO_TAG_TASK_BUDGET_DETAIL))
			multiModel.addModel(annualTotalsModel);

		if (columnsToShow.contains(Measurement.META_COLUMN_TAG))
			multiModel.addModel(measurementModel);

		if (columnsToShow.contains(Indicator.META_COLUMN_TAG))
			multiModel.addModel(futureStatusModel);
		
		mainTable.reloadColumnSequences();
		mainTable.reloadColumnWidths();
		validate();
		repaint();
	}
	
	private PlanningTreeTableModel getPlanningModel()
	{
		return (PlanningTreeTableModel)getModel();
	}
	
	public AbstractTableExporter getTableForExporting()
	{
		return multiTableExporter;
	}
	
	public static JComponent createPrintablePlanningTreeTablePanel(MainWindow mainWindow) throws Exception
	{
		PlanningTreeTablePanel wholePanel = createPlanningTreeTablePanelWithoutButtons(mainWindow);

		JPanel reformatted = new JPanel(new BorderLayout());
		
		reformatted.add(wholePanel.getTree(), BorderLayout.BEFORE_LINE_BEGINS);
		reformatted.add(wholePanel.mainTable, BorderLayout.CENTER);
		
		wholePanel.dispose();
		return reformatted;
	}

	private PlanningViewMainTableModel mainModel;
	private PlanningTreeMultiTableModel multiModel;
	private PlanningUpperMultiTable mainTable;

	private PlanningViewBudgetAnnualTotalTableModel annualTotalsModel;
	private PlanningViewMeasurementTableModel measurementModel;
	private PlanningViewFutureStatusTableModel futureStatusModel;

	private ScrollPaneWithHideableScrollBar mainTableScrollPane;
	
	private MultiTableCombinedAsOneExporter multiTableExporter;
}
