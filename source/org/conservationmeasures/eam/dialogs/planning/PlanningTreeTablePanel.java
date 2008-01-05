/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.planning;

import java.awt.BorderLayout;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.Arrays;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;

import org.conservationmeasures.eam.actions.ActionDeletePlanningViewTreeNode;
import org.conservationmeasures.eam.actions.ActionShareMethod;
import org.conservationmeasures.eam.actions.ActionTreeCreateActivity;
import org.conservationmeasures.eam.actions.ActionTreeCreateMethod;
import org.conservationmeasures.eam.actions.ActionTreeCreateTask;
import org.conservationmeasures.eam.actions.ActionTreeNodeDown;
import org.conservationmeasures.eam.actions.ActionTreeNodeUp;
import org.conservationmeasures.eam.commands.CommandCreateObject;
import org.conservationmeasures.eam.commands.CommandDeleteObject;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.dialogs.planning.propertiesPanel.PlanningViewBudgetAnnualTotalTableModel;
import org.conservationmeasures.eam.dialogs.planning.propertiesPanel.PlanningViewBudgetAnnualTotalsTable;
import org.conservationmeasures.eam.dialogs.planning.propertiesPanel.PlanningViewFutureStatusTable;
import org.conservationmeasures.eam.dialogs.planning.propertiesPanel.PlanningViewFutureStatusTableModel;
import org.conservationmeasures.eam.dialogs.planning.propertiesPanel.PlanningViewMeasurementTable;
import org.conservationmeasures.eam.dialogs.planning.propertiesPanel.PlanningViewMeasurementTableModel;
import org.conservationmeasures.eam.dialogs.tablerenderers.PlanningViewFontProvider;
import org.conservationmeasures.eam.dialogs.treetables.TreeTableNode;
import org.conservationmeasures.eam.dialogs.treetables.TreeTablePanel;
import org.conservationmeasures.eam.dialogs.treetables.TreeTableWithStateSaving;
import org.conservationmeasures.eam.main.CommandExecutedEvent;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objects.Assignment;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.objects.Measurement;
import org.conservationmeasures.eam.objects.Strategy;
import org.conservationmeasures.eam.objects.Task;
import org.conservationmeasures.eam.utils.CodeList;
import org.conservationmeasures.eam.utils.FastScrollPane;
import org.conservationmeasures.eam.utils.MultiTableRowHeightController;
import org.conservationmeasures.eam.utils.MultipleTableSelectionController;
import org.conservationmeasures.eam.views.planning.ColumnManager;
import org.conservationmeasures.eam.views.planning.PlanningView;

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
		
		fontProvider = new PlanningViewFontProvider();
		
		mainPanel = new JPanel(new BasicGridLayout(1, 4));
		turnOffVerticalHorizontalScrolling(treeTableScrollPane);
		mainPanel.add(treeTableScrollPane, BorderLayout.CENTER);
		mainScrollPane = new FastScrollPane(mainPanel);
		treeTableScrollPane.addMouseWheelListener(this);
		listenForColumnWidthChanges(getTree());
		add(mainScrollPane);
		
		selectionController = new MultipleTableSelectionController();
		
		createRightSideTables(treeToUse);
		annualTotalsScrollPane = new FastScrollPane(annualTotalsTable);
		rebuildSyncedTable(treeToUse, annualTotalsScrollPane, annualTotalsTable);
		
		measurementScrollPane = new FastScrollPane(measurementTable);
		rebuildSyncedTable(treeToUse, measurementScrollPane, measurementTable);
		
		futureStatusScrollPane = new FastScrollPane(futureStatusTable);
		rebuildSyncedTable(treeToUse, futureStatusScrollPane, futureStatusTable);
		
		rebuildEntireTreeTable();
	}

	private void createRightSideTables(PlanningTreeTable treeTableToUse) throws Exception
	{
		TreeTableModelAdapter treeTableModelAdapter = treeTableToUse.getTreeTableAdapter();
		
		annualTotalsModel = new PlanningViewBudgetAnnualTotalTableModel(getProject(), treeTableModelAdapter);
		annualTotalsTable = new PlanningViewBudgetAnnualTotalsTable(annualTotalsModel, fontProvider);
		new ModelUpdater(treeTableModelAdapter, annualTotalsModel);
		listenForColumnWidthChanges(annualTotalsTable);
		rowHeightController.addTable(annualTotalsTable);
		
		measurementModel = new PlanningViewMeasurementTableModel(getProject(), treeTableModelAdapter);
		measurementTable = new PlanningViewMeasurementTable(measurementModel, fontProvider);
		new ModelUpdater(treeTableModelAdapter, measurementModel);
		listenForColumnWidthChanges(measurementTable);
		rowHeightController.addTable(measurementTable);
		
		futureStatusModel = new PlanningViewFutureStatusTableModel(getProject(), treeTableModelAdapter);
		futureStatusTable = new PlanningViewFutureStatusTable(futureStatusModel, fontProvider);
		new ModelUpdater(treeTableModelAdapter, futureStatusModel);
		listenForColumnWidthChanges(futureStatusTable);
		rowHeightController.addTable(futureStatusTable);
	}
	
	private void listenForColumnWidthChanges(JTable table)
	{
		table.getColumnModel().addColumnModelListener(this);
	}
	
	private void turnOffVerticalHorizontalScrolling(FastScrollPane scrollPaneToUse)
	{
		scrollPaneToUse.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPaneToUse.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
	}
	
	private void rebuildSyncedTable(PlanningTreeTable treeTableToUse, FastScrollPane scrollPaneToUse, JTable tableToUse)
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
			ActionTreeCreateMethod.class,
			ActionShareMethod.class,
			ActionTreeCreateTask.class,
			ActionDeletePlanningViewTreeNode.class,
			ActionTreeNodeUp.class,
			ActionTreeNodeDown.class,
		};
	}
	
	public void commandExecuted(CommandExecutedEvent event)
	{
		try
		{		
			if (doesCommandForceRebuild(event))
				rebuildEntireTreeTable();
			
			if(isSelectedObjectModification(event))
				validate();
		

			// NOTE: The following is required to resize the table when 
			// it grows due to a node being expanded (MRD-1123)
			getTopLevelAncestor().repaint();
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
		
		if(isCreate(event) || isDeleteCommand(event))
			return true;
		
		return false;
	}
	
	private boolean isDeleteCommand(CommandExecutedEvent event)
	{
		if (! event.isDeleteObjectCommand())
			return false;
		
		CommandDeleteObject deleteCommand = (CommandDeleteObject) event.getCommand();
		if (deleteCommand.getObjectType() != Task.getObjectType())
			return false;
		
		return true;
	}

	private boolean isCreate(CommandExecutedEvent event)
	{
		if (! event.isCreateObjectCommand())
			return false;

		CommandCreateObject createCommand = (CommandCreateObject) event.getCommand();
		if (createCommand.getObjectType() != Task.getObjectType())
			return false;
		
		return true;
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
	
	private boolean isSelectedObjectModification(CommandExecutedEvent event)
	{
		if (! event.isSetDataCommand())
			return false;
		
		TreeTableNode node = getSelectedTreeNode();
		if (node == null)
			return false;
		
		BaseObject selectedObject = node.getObject(); 
		if (selectedObject == null)
			return false;
		
		CommandSetObjectData setCommand = (CommandSetObjectData) event.getCommand();
		int setType = setCommand.getObjectType();
		if(setType == Assignment.getObjectType())
			return true;
		
		String setField = setCommand.getFieldTag();
		
		String[] fieldTags = selectedObject.getFieldTags();
		Vector fields = new Vector(Arrays.asList(fieldTags));

		boolean sameType = (selectedObject.getType() == setType);
		boolean containsField = (fields.contains(setField));
		return (sameType && containsField);
	}

	private void rebuildEntireTreeTable() throws Exception
	{
		ORef selectedRef = ORef.INVALID;
		BaseObject[] selected = tree.getSelectedObjects();
		if(selected.length == 1)
			selectedRef = selected[0].getRef();

		// TODO: Perhaps possibly detect exactly what changed and 
		// only rebuild the columns or the rows rather than always doing both
	
		// NOTE: The following rebuild the columns but don't touch the tree
		getPlanningModel().rebuildCodeList();
		tree.rebuildTableCompletely();
		
		// NOTE: The following rebuild the tree but don't touch the columns
		getPlanningModel().rebuildEntireTree();
		annualTotalsModel.fireTableDataChanged();
		measurementModel.fireTableDataChanged();
		futureStatusModel.fireTableDataChanged();
		restoreTreeExpansionState();
		updateRightSideTablePanels();

		selectObjectAfterSwingClearsItDueToTreeStructureChange(selectedRef);
	}
	
	private void updateRightSideTablePanels() throws Exception
	{
		mainPanel.removeAll();
		mainPanel.add(treeTableScrollPane);
		CodeList columnsToShow = new CodeList(ColumnManager.getVisibleColumnCodes(getProject().getCurrentViewData()));
		if (columnsToShow.contains(Task.PSEUDO_TAG_TASK_BUDGET_DETAIL))
		{
			mainPanel.add(annualTotalsScrollPane);
		}
		if (columnsToShow.contains(Measurement.META_COLUMN_TAG))
		{
			mainPanel.add(measurementScrollPane);
		}
		if (columnsToShow.contains(Indicator.META_COLUMN_TAG))
		{
			mainPanel.add(futureStatusScrollPane);
		}
		
		validate();
		repaint();
	}

	private void selectObjectAfterSwingClearsItDueToTreeStructureChange(ORef selectedRef)
	{
		SwingUtilities.invokeLater(new Reselecter(tree, selectedRef));
	}
	
	class Reselecter implements Runnable
	{
		public Reselecter(TreeTableWithStateSaving treeTableToUse, ORef refToSelect)
		{
			treeTable = treeTableToUse;
			ref = refToSelect;
		}
		
		public void run()
		{
			treeTable.selectObject(ref);
		}
		
		TreeTableWithStateSaving treeTable;
		ORef ref;
	}

	private PlanningTreeTableModel getPlanningModel()
	{
		return (PlanningTreeTableModel)getModel();
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
	private FastScrollPane annualTotalsScrollPane;
	private FastScrollPane measurementScrollPane;
	private FastScrollPane futureStatusScrollPane;
	private FastScrollPane mainScrollPane;
	
	private MultiTableRowHeightController rowHeightController;
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