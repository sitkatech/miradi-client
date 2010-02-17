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
package org.miradi.dialogs.planning.upperPanel;

import java.awt.BorderLayout;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;

import org.miradi.dialogs.base.EditableObjectTableModel;
import org.miradi.dialogs.base.MiradiPanel;
import org.miradi.dialogs.planning.AccountingCodeBudgetDetailsTableModel;
import org.miradi.dialogs.planning.FundingSourceBudgetDetailsTableModel;
import org.miradi.dialogs.planning.RowColumnProvider;
import org.miradi.dialogs.planning.propertiesPanel.AboveBudgetColumnsBar;
import org.miradi.dialogs.planning.propertiesPanel.AbstractFixedHeightDirectlyAboveTreeTablePanel;
import org.miradi.dialogs.planning.propertiesPanel.AbstractWorkUnitsTableModel;
import org.miradi.dialogs.planning.propertiesPanel.AccountingCodeExpenseTableModel;
import org.miradi.dialogs.planning.propertiesPanel.BudgetDetailsTableModel;
import org.miradi.dialogs.planning.propertiesPanel.ExpenseAmountsTableModel;
import org.miradi.dialogs.planning.propertiesPanel.FundingSourceExpenseTableModel;
import org.miradi.dialogs.planning.propertiesPanel.PlanningViewMainModelExporter;
import org.miradi.dialogs.planning.propertiesPanel.PlanningWorkUnitsTableModel;
import org.miradi.dialogs.planning.propertiesPanel.ProjectResourceWorkUnitsTableModel;
import org.miradi.dialogs.tablerenderers.FontForObjectTypeProvider;
import org.miradi.dialogs.tablerenderers.PlanningViewFontProvider;
import org.miradi.dialogs.treetables.AbstractTreeTablePanel;
import org.miradi.dialogs.treetables.GenericTreeTableModel;
import org.miradi.dialogs.treetables.TreeTableWithStateSaving;
import org.miradi.main.CommandExecutedEvent;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.BaseObject;
import org.miradi.objects.ExpenseAssignment;
import org.miradi.objects.Indicator;
import org.miradi.objects.Measurement;
import org.miradi.objects.ResourceAssignment;
import org.miradi.questions.CustomPlanningColumnsQuestion;
import org.miradi.utils.CodeList;
import org.miradi.utils.MultiTableCombinedAsOneExporter;
import org.miradi.utils.MultiTableRowHeightController;
import org.miradi.utils.MultiTableSelectionController;
import org.miradi.utils.MultiTableVerticalScrollController;
import org.miradi.utils.TableExporter;
import org.miradi.utils.TableWithColumnWidthAndSequenceSaver;
import org.miradi.utils.TreeTableExporter;

abstract public class PlanningTreeTablePanel extends AbstractTreeTablePanel
{
	protected PlanningTreeTablePanel(MainWindow mainWindowToUse, PlanningTreeTable treeToUse, GenericTreeTableModel modelToUse, Class[] buttonActions, RowColumnProvider rowColumnProviderToUse) throws Exception
	{
		this(mainWindowToUse, treeToUse, modelToUse, buttonActions, rowColumnProviderToUse, new AbstractFixedHeightDirectlyAboveTreeTablePanel());
	}
	
	protected PlanningTreeTablePanel(MainWindow mainWindowToUse, PlanningTreeTable treeToUse, GenericTreeTableModel modelToUse, Class[] buttonActions, RowColumnProvider rowColumnProviderToUse, JComponent filterStatusPanel) throws Exception
	{
		super(mainWindowToUse, treeToUse, buttonActions);
		
		rowColumnProvider = rowColumnProviderToUse;
		model = modelToUse;
		
		treeTableScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

		MultiTableRowHeightController rowHeightController = new MultiTableRowHeightController(getMainWindow());
		rowHeightController.addTable(treeToUse);
		
		MultiTableSelectionController selectionController = new MultiTableSelectionController(this);
		selectionController.addTable(treeToUse);
		
		MultiTableVerticalScrollController scrollController = new MultiTableVerticalScrollController();
		scrollController.addScrollPane(treeTableScrollPane);
		
		listenForColumnWidthChanges(getTree());
		
		mainModel = new PlanningViewMainTableModel(getProject(), treeToUse, rowColumnProvider);
		multiModel = new PlanningTreeMultiTableModel(treeToUse.getUniqueTableIdentifier());
		measurementModel = new PlanningViewMeasurementTableModel(getProject(), treeToUse);
		futureStatusModel = new PlanningViewFutureStatusTableModel(getProject(), treeToUse);
		workUnitsTableModel = new PlanningWorkUnitsTableModel(getProject(), treeToUse, modelToUse.getUniqueTreeTableModelIdentifier());
		resourceWorkUnitsTableModel = new ProjectResourceWorkUnitsTableModel(getProject(), treeToUse, modelToUse.getUniqueTreeTableModelIdentifier());
		expenseAmountsTableModel = new ExpenseAmountsTableModel(getProject(), treeToUse, modelToUse.getUniqueTreeTableModelIdentifier());
		budgetDetailsTableModel = new BudgetDetailsTableModel(getProject(), treeToUse, modelToUse.getUniqueTreeTableModelIdentifier());
		fundingSourceBudgetDetailsTableModel = new FundingSourceBudgetDetailsTableModel(getProject(), treeToUse, modelToUse.getUniqueTreeTableModelIdentifier());
		accoundingCodeBudgetDetailsTableModel = new AccountingCodeBudgetDetailsTableModel(getProject(), treeToUse, modelToUse.getUniqueTreeTableModelIdentifier());
		fundingSourceExpenseTableModel = new FundingSourceExpenseTableModel(getProject(), treeToUse, modelToUse.getUniqueTreeTableModelIdentifier());
		accountingCodeExpenseTableModel = new AccountingCodeExpenseTableModel(getProject(), treeToUse, modelToUse.getUniqueTreeTableModelIdentifier());
		
		FontForObjectTypeProvider fontProvider = new PlanningViewFontProvider(getMainWindow());
		mainTable = new PlanningUpperMultiTable(treeToUse, multiModel, fontProvider);
		
		mainTableScrollPane = integrateTable(treeTableScrollPane.getVerticalScrollBar(), scrollController, rowHeightController, selectionController, treeToUse, mainTable);
		mainTableScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

		// NOTE: Replace treeScrollPane that super constructor added
		removeAll();
		add(buttonBox, BorderLayout.BEFORE_FIRST_LINE);
		
		JPanel leftPanel = new MiradiPanel(new BorderLayout());
		leftPanel.add(filterStatusPanel, BorderLayout.BEFORE_FIRST_LINE);
		leftPanel.add(treeTableScrollPane, BorderLayout.CENTER);
		
		JPanel rightPanel = new MiradiPanel(new BorderLayout());
		AboveBudgetColumnsBar aboveMainTableBar = new AboveBudgetColumnsBar(mainTable);
		aboveMainTableBar.setTableScrollPane(mainTableScrollPane);
		rightPanel.add(aboveMainTableBar, BorderLayout.BEFORE_FIRST_LINE);
		rightPanel.add(mainTableScrollPane, BorderLayout.CENTER);
		
		add(leftPanel, BorderLayout.BEFORE_LINE_BEGINS);
		add(rightPanel, BorderLayout.CENTER);
		
		rebuildEntireTreeAndTable();
	
		mainTableColumnSelectionListener = new MainTableSelectionHandler();
		treeTableRowSelectionListener = new TreeTableRowSelectionHandler();
		
		enableSelectionListeners();
	}

	@Override
	public void dispose()
	{
		super.dispose();
		
		mainTable.dispose();
	}
	
	protected boolean wasTypeCreatedOrDeleted(CommandExecutedEvent event, int objectType)
	{
		if (event.isCreateCommandForThisType(objectType))
			return true;
		
		return event.isDeleteCommandForThisType(objectType);
	}

	@Override
	protected boolean wereAssignmentNodesAddedOrRemoved(CommandExecutedEvent event)
	{
		try
		{
			CodeList rowCodes = getRowColumnProvider().getRowListToShow();
			if (rowCodes.contains(ResourceAssignment.OBJECT_NAME) && event.isSetDataCommandWithThisTag(BaseObject.TAG_RESOURCE_ASSIGNMENT_IDS))
				return true;
			
			if (rowCodes.contains(ExpenseAssignment.OBJECT_NAME) && event.isSetDataCommandWithThisTag(BaseObject.TAG_EXPENSE_ASSIGNMENT_REFS))
				return true;
		}
		catch(Exception e)
		{
			EAM.logStackTrace();
		}
		
		return false;
	}

	protected void rebuildEntireTreeTable() throws Exception
	{
		int selectedRow = tree.getSelectionModel().getMinSelectionIndex();
		int selectedColumn = getMainTable().getColumnModel().getSelectionModel().getMinSelectionIndex();
		getMainTable().clearSelection();
		getMainTable().getColumnModel().getSelectionModel().clearSelection();

		// TODO: Perhaps possibly detect exactly what changed and 
		// only rebuild the columns or the rows rather than always doing both
	
		// NOTE: The following rebuild the columns but don't touch the tree
		getTreeTableModel().updateColumnsToShow();
		tree.rebuildTableCompletely();

		updateResourceFilter();
		multiModel.updateColumnsToShow();
		
		// NOTE: The following rebuild the tree but don't touch the columns
		getTreeTableModel().rebuildEntireTree();
		measurementModel.fireTableDataChanged();
		futureStatusModel.fireTableDataChanged();
		workUnitsTableModel.fireTableDataChanged();
		resourceWorkUnitsTableModel.fireTableDataChanged();
		fundingSourceExpenseTableModel.fireTableDataChanged();
		accountingCodeExpenseTableModel.fireTableDataChanged();
		expenseAmountsTableModel.fireTableDataChanged();
		budgetDetailsTableModel.fireTableDataChanged();
		fundingSourceBudgetDetailsTableModel.fireTableDataChanged();
		accoundingCodeBudgetDetailsTableModel.fireTableDataChanged();
		restoreTreeExpansionState();
		updateRightSideTablePanels();

		selectObjectAfterSwingClearsItDueToTreeStructureChange(getMainTable(), selectedRow, selectedColumn);
	}
	
	protected void updateResourceFilter() throws Exception
	{
	}

	private void updateRightSideTablePanels() throws Exception
	{
		multiModel.removeAllModels();
		multiModel.addModel(mainModel);

		mainTableScrollPane.showVerticalScrollBar();

		CodeList columnsToShow = getColumnsToShow();
		if (columnsToShow.contains(Measurement.META_COLUMN_TAG))
			multiModel.addModel(measurementModel);

		if (columnsToShow.contains(Indicator.META_COLUMN_TAG))
			multiModel.addModel(futureStatusModel);
		
		if (shouldShow(CustomPlanningColumnsQuestion.META_RESOURCE_ASSIGNMENT_COLUMN_CODE))
			multiModel.addModel(workUnitsTableModel);
		
		if (shouldShow(CustomPlanningColumnsQuestion.META_PROJECT_RESOURCE_WORK_UNITS_COLUMN_CODE))
			multiModel.addModel(resourceWorkUnitsTableModel);
		
		if (shouldShow(CustomPlanningColumnsQuestion.META_EXPENSE_ASSIGNMENT_COLUMN_CODE))
			multiModel.addModel(expenseAmountsTableModel);
		
		if (shouldShow(CustomPlanningColumnsQuestion.META_FUNDING_SOURCE_EXPENSE_COLUMN_CODE))
			multiModel.addModel(fundingSourceExpenseTableModel);
		
		if (shouldShow(CustomPlanningColumnsQuestion.META_ACCOUNTING_CODE_EXPENSE_COLUMN_CODE))
			multiModel.addModel(accountingCodeExpenseTableModel);
		
		if (shouldShow(CustomPlanningColumnsQuestion.META_FUNDING_SOURCE_BUDGET_DETAILS_COLUMN_CODE))
			multiModel.addModel(fundingSourceBudgetDetailsTableModel);
		
		if (shouldShow(CustomPlanningColumnsQuestion.META_ACCOUNTING_CODE_BUDGET_DETAILS_COLUMN_CODE))
			multiModel.addModel(accoundingCodeBudgetDetailsTableModel);
		
		if (shouldShow(CustomPlanningColumnsQuestion.META_BUDGET_DETAIL_COLUMN_CODE))
			multiModel.addModel(budgetDetailsTableModel);
		
		mainTable.updateToReflectNewColumns();
		validate();
		repaint();
	}

	private boolean shouldShow(String metaColumnCode) throws Exception
	{
		return getColumnsToShow().contains(metaColumnCode);
	}

	private CodeList getColumnsToShow() throws Exception
	{
		return getRowColumnProvider().getColumnListToShow();
	}

	public TableExporter getTableForExporting() throws Exception
	{
		MultiTableCombinedAsOneExporter multiTableExporter = new MultiTableCombinedAsOneExporter(getProject());
		multiTableExporter.addAsMasterTable(new TreeTableExporter(getTree()));
		multiTableExporter.addExportable(new PlanningViewMainModelExporter(getProject(), multiModel, getTree(), multiModel.getUniqueTableModelIdentifier()));
		
		return multiTableExporter;
	}
	
	protected TableWithColumnWidthAndSequenceSaver getMainTable()
	{
		return mainTable;
	}
	
	public RowColumnProvider getRowColumnProvider()
	{
		return rowColumnProvider;
	}
	
	protected AbstractWorkUnitsTableModel getWorkUnitsTableModel()
	{
		return workUnitsTableModel;
	}
	
	protected BudgetDetailsTableModel getBudgetDetailsTableModel()
	{
		return budgetDetailsTableModel;
	}
	
	protected PlanningViewMainTableModel getPlanningViewMainTableModel()
	{
		return (PlanningViewMainTableModel) getMainModel();
	}
	
	@Override
	protected EditableObjectTableModel getMainModel()
	{
		return mainModel;
	}
	
	@Override
	public void beginSelectionChangingProcess()
	{
		disableSectionSwitchDuringFullRebuild();
	}
	
	@Override
	public void endSelectionChangingProcess()
	{
		enableSectionSwitch();
	}
	
	private void enableSelectionListeners()
	{
		listenForColumnSelectionChanges(getMainTable());
		listenForTreeTableRowSelectionChanges(getTree());
	}
	
	private void listenForColumnSelectionChanges(JTable table)
	{
		table.getColumnModel().addColumnModelListener(mainTableColumnSelectionListener);
		table.getSelectionModel().addListSelectionListener(mainTableColumnSelectionListener);
	}

	private void listenForTreeTableRowSelectionChanges(TreeTableWithStateSaving treeToUse)
	{
		treeToUse.addSelectionChangeListener(treeTableRowSelectionListener);
	}
	
	private void selectSectionForTag(String selectedColumnTag)
	{
		if (isSideTabSwitchingDisabled())
			return;
		
		if (getPropertiesPanel() == null)
			return;
		
		getPropertiesPanel().setObjectRefs(getPicker().getSelectionHierarchy().toArray(), selectedColumnTag);
	}
	
	class TreeTableRowSelectionHandler implements ListSelectionListener
	{
		public void valueChanged(ListSelectionEvent e)
		{
			selectSectionForTag(BaseObject.TAG_LABEL);
		}
	}
	
	class MainTableSelectionHandler  implements TableColumnModelListener, ListSelectionListener
	{
		public void columnAdded(TableColumnModelEvent e)
		{
		}

		public void columnMarginChanged(ChangeEvent e)
		{
		}

		public void columnMoved(TableColumnModelEvent e)
		{
		}

		public void columnRemoved(TableColumnModelEvent e)
		{
		}

		public void columnSelectionChanged(ListSelectionEvent e)
		{
			selectSectionForColumn();
		}

		public void valueChanged(ListSelectionEvent e)
		{
			selectSectionForColumn();
		}
		
		private void selectSectionForColumn()
		{
			String selectedColumnTag = getSelectedColumnTag();
			selectSectionForTag(selectedColumnTag);
		}

		private String getSelectedColumnTag()
		{
			int selectedColumn = mainTable.getSelectedColumn();
			if (selectedColumn < 0)
				return "";
			
			int modelColumn = mainTable.convertColumnIndexToModel(selectedColumn);
			return multiModel.getTagForCell(getSafeTypeForSelectedRow(), modelColumn);
		}		
	}
	
	private int getSafeTypeForSelectedRow()
	{
		if (getTree().getSelectedObjects().length > 0)
		{
			BaseObject selectedObject = getTree().getSelectedObjects()[0];
			return selectedObject.getType();
		}
		
		return ObjectType.FAKE;
	
	}
	
	public void selectObjectAfterSwingClearsItDueToTreeStructureChange(JTable table, int fallbackRow, int fallbackColumn)
	{
		SwingUtilities.invokeLater(new Reselecter(table, fallbackRow, fallbackColumn));
	}
	
	class Reselecter implements Runnable
	{
		public Reselecter(JTable tableToUse, int rowToSelect, int columnToSelect)
		{
			table = tableToUse;
			row = rowToSelect;
			column = columnToSelect;
		}
		
		public void run()
		{
			disableSectionSwitchDuringFullRebuild();
			try
			{
				table.getSelectionModel().setSelectionInterval(row, row);
				if(column < table.getColumnCount())
					table.getColumnModel().getSelectionModel().setSelectionInterval(column, column);
			}
			finally 
			{
				enableSectionSwitch();
			}
		}
		
		private JTable table;
		private int row;
		private int column;
	}
	
	private RowColumnProvider rowColumnProvider;
	private PlanningViewMainTableModel mainModel;
	private PlanningTreeMultiTableModel multiModel;
	private PlanningUpperMultiTable mainTable;

	private PlanningViewMeasurementTableModel measurementModel;
	private PlanningViewFutureStatusTableModel futureStatusModel;
	protected AbstractWorkUnitsTableModel workUnitsTableModel;
	private ExpenseAmountsTableModel expenseAmountsTableModel;
	protected BudgetDetailsTableModel budgetDetailsTableModel;
	private FundingSourceBudgetDetailsTableModel fundingSourceBudgetDetailsTableModel;
	private AccountingCodeBudgetDetailsTableModel accoundingCodeBudgetDetailsTableModel;
	private ProjectResourceWorkUnitsTableModel resourceWorkUnitsTableModel;
	private FundingSourceExpenseTableModel fundingSourceExpenseTableModel;
	private AccountingCodeExpenseTableModel accountingCodeExpenseTableModel;

	private ScrollPaneWithHideableScrollBar mainTableScrollPane;
	
	private MainTableSelectionHandler mainTableColumnSelectionListener;
	private TreeTableRowSelectionHandler treeTableRowSelectionListener;
}
