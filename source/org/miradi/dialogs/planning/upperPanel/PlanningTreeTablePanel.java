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
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;

import org.miradi.actions.ActionDeletePlanningViewTreeNode;
import org.miradi.actions.ActionTreeCreateActivity;
import org.miradi.actions.ActionTreeCreateMethod;
import org.miradi.actions.ActionTreeCreateTask;
import org.miradi.actions.ActionTreeNodeDown;
import org.miradi.actions.ActionTreeNodeUp;
import org.miradi.commands.CommandSetObjectData;
import org.miradi.dialogs.tablerenderers.PlanningViewFontProvider;
import org.miradi.dialogs.treetables.TreeTablePanel;
import org.miradi.main.AppPreferences;
import org.miradi.main.CommandExecutedEvent;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.Assignment;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Indicator;
import org.miradi.objects.Measurement;
import org.miradi.objects.Strategy;
import org.miradi.objects.Task;
import org.miradi.utils.CodeList;
import org.miradi.utils.ExportableTableInterface;
import org.miradi.utils.MiradiScrollPane;
import org.miradi.utils.MultiTableCombinedAsOneExporter;
import org.miradi.utils.MultiTableRowHeightController;
import org.miradi.utils.MultipleTableSelectionController;
import org.miradi.views.planning.ColumnManager;
import org.miradi.views.planning.PlanningView;

import com.java.sun.jtreetable.TreeTableModelAdapter;
import com.jhlabs.awt.BasicGridLayout;

public class PlanningTreeTablePanel extends TreeTablePanel implements MouseWheelListener, TableColumnModelListener
{
	public static PlanningTreeTablePanel createPlanningTreeTablePanel(MainWindow mainWindowToUse) throws Exception
	{ 
		PlanningTreeTableModel model = new PlanningTreeTableModel(mainWindowToUse.getProject());
		PlanningTreeTable treeTable = new PlanningTreeTable(mainWindowToUse.getProject(), model, new PlanningViewFontProvider());	
		
		return new PlanningTreeTablePanel(mainWindowToUse, treeTable, model);
	}
	
	private PlanningTreeTablePanel(MainWindow mainWindowToUse, PlanningTreeTable treeToUse, PlanningTreeTableModel modelToUse) throws Exception
	{
		super(mainWindowToUse, treeToUse, getButtonActions());
		model = modelToUse;
		
		rowHeightController = new MultiTableRowHeightController();
		rowHeightController.addTable(treeToUse);
		
		multiTableExporter = new MultiTableCombinedAsOneExporter();		
		fontProvider = new PlanningViewFontProvider();
		
		mainPanel = new JPanel(new BasicGridLayout(1, 4));
		mainPanel.setBackground(AppPreferences.getDataPanelBackgroundColor());
		turnOffVerticalHorizontalScrolling(treeTableScrollPane);
		mainPanel.add(treeTableScrollPane, BorderLayout.CENTER);
		mainScrollPane = new MiradiScrollPane(mainPanel);
		treeTableScrollPane.addMouseWheelListener(this);
		listenForColumnWidthChanges(getTree());
		add(mainScrollPane);
		
		selectionController = new MultipleTableSelectionController();
		
		createRightSideTables(treeToUse);
		annualTotalsScrollPane = new MiradiScrollPane(annualTotalsTable);
		rebuildSyncedTable(treeToUse, annualTotalsScrollPane, annualTotalsTable);
		
		measurementScrollPane = new MiradiScrollPane(measurementTable);
		rebuildSyncedTable(treeToUse, measurementScrollPane, measurementTable);
		
		futureStatusScrollPane = new MiradiScrollPane(futureStatusTable);
		rebuildSyncedTable(treeToUse, futureStatusScrollPane, futureStatusTable);
		
		rebuildEntireTreeTable();
	}

	private void createRightSideTables(PlanningTreeTable treeTableToUse) throws Exception
	{
		TreeTableModelAdapter treeTableModelAdapter = treeTableToUse.getTreeTableAdapter();
		
		annualTotalsModel = new PlanningViewBudgetAnnualTotalTableModel(getProject(), treeTableToUse);
		annualTotalsTable = new PlanningViewBudgetAnnualTotalsTable(annualTotalsModel, fontProvider);
		new ModelUpdater(treeTableModelAdapter, annualTotalsModel);
		listenForColumnWidthChanges(annualTotalsTable);
		rowHeightController.addTable(annualTotalsTable);
		
		measurementModel = new PlanningViewMeasurementTableModel(getProject(), treeTableToUse);
		measurementTable = new PlanningViewMeasurementTable(measurementModel, fontProvider);
		new ModelUpdater(treeTableModelAdapter, measurementModel);
		listenForColumnWidthChanges(measurementTable);
		rowHeightController.addTable(measurementTable);
		
		futureStatusModel = new PlanningViewFutureStatusTableModel(getProject(), treeTableToUse);
		futureStatusTable = new PlanningViewFutureStatusTable(futureStatusModel, fontProvider);
		new ModelUpdater(treeTableModelAdapter, futureStatusModel);
		listenForColumnWidthChanges(futureStatusTable);
		rowHeightController.addTable(futureStatusTable);
	}
	
	private void listenForColumnWidthChanges(JTable table)
	{
		table.getColumnModel().addColumnModelListener(this);
	}
	
	private void turnOffVerticalHorizontalScrolling(MiradiScrollPane scrollPaneToUse)
	{
		scrollPaneToUse.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPaneToUse.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
	}
	
	private void rebuildSyncedTable(PlanningTreeTable treeTableToUse, MiradiScrollPane scrollPaneToUse, JTable tableToUse)
	{
		scrollPaneToUse.addMouseWheelListener(this);
		tableToUse.setRowHeight(treeTableToUse.getRowHeight());	
		turnOffVerticalHorizontalScrolling(scrollPaneToUse);
		
		selectionController.addTable(tableToUse);
		selectionController.addTable(treeTableToUse);
	}

	private static Class[] getButtonActions()
	{
		return new Class[] {
			ActionTreeCreateActivity.class,
			ActionTreeCreateTask.class,
			ActionTreeNodeUp.class,
			
			ActionTreeCreateMethod.class,
			ActionDeletePlanningViewTreeNode.class,
			ActionTreeNodeDown.class,
		};
	}
	
	public void commandExecuted(CommandExecutedEvent event)
	{
		try
		{		
			if (doesCommandForceRebuild(event))
				rebuildEntireTreeTable();
			
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
		
		if(isTaskMove(event))
			return true;
		
		if(isCreateCommand(event, Task.getObjectType()) || isDeleteCommand(event, Task.getObjectType()))
			return true;
		
		return false;
	}
	
	//TODO this should use that getTasksTag (or something like that) method
	//from email :Please put a todo in isTaskMove that it should use that 
	//getTasksTag method (or whatever it's called) that I mentioned the 
	//other day. I know that one is my code not yours.
	private boolean isTaskMove(CommandExecutedEvent event)
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
		multiTableExporter.addExportable(getTree());
		mainPanel.removeAll();
		mainPanel.add(treeTableScrollPane);
		CodeList columnsToShow = new CodeList(ColumnManager.getVisibleColumnCodes(getProject().getCurrentViewData()));
		if (columnsToShow.contains(Task.PSEUDO_TAG_TASK_BUDGET_DETAIL))
		{
			mainPanel.add(annualTotalsScrollPane);
			multiTableExporter.addExportable(annualTotalsTable);
		}
		if (columnsToShow.contains(Measurement.META_COLUMN_TAG))
		{
			mainPanel.add(measurementScrollPane);
			multiTableExporter.addExportable(measurementTable);
		}
		if (columnsToShow.contains(Indicator.META_COLUMN_TAG))
		{
			mainPanel.add(futureStatusScrollPane);
			multiTableExporter.addExportable(futureStatusTable);
		}
		
		validate();
		repaint();
	}

	private PlanningTreeTableModel getPlanningModel()
	{
		return (PlanningTreeTableModel)getModel();
	}
	
	public ExportableTableInterface getTableForExporting()
	{
		return multiTableExporter;
	}
	
	private void resizeTablesToExactlyFitAllColumns() 
	{
		validate();
	}
	
	
	public void mouseWheelMoved(MouseWheelEvent e)
	{
		mainScrollPane.processMouseWheelEvent(e);
	}
	
	// Begin TableColumnModelListener
	public void columnAdded(TableColumnModelEvent e)
	{
		// NOTE: We only care about margin changed (column resize) events
	}

	public void columnMarginChanged(ChangeEvent e)
	{
		resizeTablesToExactlyFitAllColumns();
	}

	public void columnMoved(TableColumnModelEvent e)
	{
		// NOTE: We only care about margin changed (column resize) events
	}

	public void columnRemoved(TableColumnModelEvent e)
	{
		// NOTE: We only care about margin changed (column resize) events
	}

	public void columnSelectionChanged(ListSelectionEvent e)
	{
		// NOTE: We only care about margin changed (column resize) events
	}

	private PlanningViewFontProvider fontProvider;
	private JPanel mainPanel;
	private MultipleTableSelectionController selectionController;
	private PlanningViewBudgetAnnualTotalsTable annualTotalsTable;
	private PlanningViewBudgetAnnualTotalTableModel annualTotalsModel;
	private PlanningViewMeasurementTable measurementTable;
	private PlanningViewMeasurementTableModel measurementModel;
	private PlanningViewFutureStatusTable futureStatusTable;
	private PlanningViewFutureStatusTableModel futureStatusModel;
	private MiradiScrollPane annualTotalsScrollPane;
	private MiradiScrollPane measurementScrollPane;
	private MiradiScrollPane futureStatusScrollPane;
	private MiradiScrollPane mainScrollPane;
	
	private MultiTableRowHeightController rowHeightController;
	private MultiTableCombinedAsOneExporter multiTableExporter;
}

class ModelUpdater implements TableModelListener
{
	public ModelUpdater(TreeTableModelAdapter adapter, AbstractTableModel modelToUpdateToUse)
	{
		modelToUpdate = modelToUpdateToUse;
		adapter.addTableModelListener(this);
	}
	
	public void tableChanged(TableModelEvent e)
	{
		modelToUpdate.fireTableDataChanged();
	}
	
	private AbstractTableModel modelToUpdate;
}