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

import org.miradi.dialogs.base.EditableObjectTableModel;
import org.miradi.dialogs.planning.TableWithExpandableColumnsInterface;
import org.miradi.dialogs.planning.propertiesPanel.*;
import org.miradi.dialogs.treetables.AbstractTreeTablePanel;
import org.miradi.dialogs.treetables.GenericTreeTableModel;
import org.miradi.layout.TwoColumnPanel;
import org.miradi.main.CommandExecutedEvent;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Indicator;
import org.miradi.objects.Measurement;
import org.miradi.objects.PlanningTreeRowColumnProvider;
import org.miradi.questions.WorkPlanColumnConfigurationQuestion;
import org.miradi.schemas.ExpenseAssignmentSchema;
import org.miradi.schemas.ResourceAssignmentSchema;
import org.miradi.utils.*;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;

abstract public class PlanningTreeTablePanel extends AbstractTreeTablePanel
{
	protected PlanningTreeTablePanel(MainWindow mainWindowToUse, PlanningTreeTable treeToUse, GenericTreeTableModel modelToUse, Class[] buttonActions, PlanningTreeRowColumnProvider rowColumnProviderToUse) throws Exception
	{
		this(mainWindowToUse, treeToUse, modelToUse, buttonActions, rowColumnProviderToUse, new AbstractFixedHeightDirectlyAboveTreeTablePanel());
	}
	
	protected PlanningTreeTablePanel(MainWindow mainWindowToUse, PlanningTreeTable treeToUse, GenericTreeTableModel modelToUse, Class[] buttonActions, PlanningTreeRowColumnProvider rowColumnProviderToUse, JComponent treeTableHeaderPanelToUse) throws Exception
	{
		super(mainWindowToUse, treeToUse, buttonActions);
		
		rowColumnProvider = rowColumnProviderToUse;
		treeTableHeaderPanel = treeTableHeaderPanelToUse;
		treeTableScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		
		createModels();
		mainTable = createUpperMultiTable(treeToUse, multiTableModel);
		mainTableScrollPane = integrateTable(getTreeTableScrollPane().getVerticalScrollBar(), mainTable);
		mainTableScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

		createTreeAndTablePanel();
		rebuildEntireTreeAndTable();
	
		mainTableColumnSelectionListener = new MainTableSelectionHandler();

		enableSelectionListeners();
	}

	protected PlanningUpperMultiTable createUpperMultiTable(PlanningTreeTable treeToUse, PlanningTreeMultiTableModel multiModelToUse)
	{
		return new PlanningUpperMultiTable(getMainWindow(), treeToUse, multiModelToUse);
	}

	@Override
	protected void addAboveTreeHeaderPanel(JPanel leftPanel)
	{
		leftPanel.add(treeTableHeaderPanel, BorderLayout.BEFORE_FIRST_LINE);
	}

	@Override
	protected void addAboveColumnBar(JPanel rightPanel)
	{
		AboveBudgetColumnsBar aboveMainTableBar = createAboveColumnBar();
		rightPanel.add(aboveMainTableBar, BorderLayout.BEFORE_FIRST_LINE);
	}

	public AboveBudgetColumnsBar createAboveColumnBar()
	{
		AboveBudgetColumnsBar aboveMainTableBar = new AboveBudgetColumnsBar(getProject(), mainTable);
		aboveMainTableBar.setTableScrollPane(mainTableScrollPane);
		
		return aboveMainTableBar;
	}

	private void createModels() throws Exception
	{
		mainModel = createMainTableModel(rowColumnProvider);
		multiTableModel = new PlanningTreeMultiTableModel(getTree().getUniqueTableIdentifier());
		measurementModel = new PlanningViewMeasurementTableModel(getProject(), getTree());
		futureStatusModel = new PlanningViewFutureStatusTableModel(getProject(), getTree());
		workUnitsTableModel = new WorkPlanWorkUnitsTableModel(getProject(), getRowColumnProvider(), getTree(), getTreeTableModel().getUniqueTreeTableModelIdentifier());
		resourceWorkUnitsTableModel = new ProjectResourceWorkUnitsTableModel(getProject(), getRowColumnProvider(), getTree(), getTreeTableModel().getUniqueTreeTableModelIdentifier());
		resourceBudgetDetailsTableModel = new ProjectResourceBudgetDetailsTableModel(getProject(), getRowColumnProvider(), getTree(), getTreeTableModel().getUniqueTreeTableModelIdentifier());
		expenseAmountsTableModel = new WorkPlanExpenseAmountsTableModel(getProject(), getRowColumnProvider(), getTree(), getTreeTableModel().getUniqueTreeTableModelIdentifier());
		budgetDetailsTableModel = new WorkPlanBudgetDetailsTableModel(getProject(), getRowColumnProvider(), getTree(), getTreeTableModel().getUniqueTreeTableModelIdentifier());
		fundingSourceBudgetDetailsTableModel = new FundingSourceBudgetDetailsTableModel(getProject(), getRowColumnProvider(), getTree(), getTreeTableModel().getUniqueTreeTableModelIdentifier());
		accountingCodeBudgetDetailsTableModel = new AccountingCodeBudgetDetailsTableModel(getProject(), getRowColumnProvider(), getTree(), getTreeTableModel().getUniqueTreeTableModelIdentifier());
		fundingSourceExpenseTableModel = new FundingSourceExpenseTableModel(getProject(), getRowColumnProvider(), getTree(), getTreeTableModel().getUniqueTreeTableModelIdentifier());
		accountingCodeExpenseTableModel = new AccountingCodeExpenseTableModel(getProject(), getRowColumnProvider(), getTree(), getTreeTableModel().getUniqueTreeTableModelIdentifier());
		rollupReportsWorkUnitsModel = new AnalysisWorkUnitsModel(getProject(), getRowColumnProvider(), getTree(), getTreeTableModel().getUniqueTreeTableModelIdentifier());
		rollupReportsExpenseModel = new AnalysisExpenseTableModel(getProject(), getRowColumnProvider(), getTree(), getTreeTableModel().getUniqueTreeTableModelIdentifier());
		rollupReportsBudgetDetailsModel = new AnalysisBudgetDetailsTableModel(getProject(), getRowColumnProvider(), getTree(), getTreeTableModel().getUniqueTreeTableModelIdentifier());
		budgetCategoryOneWorkUnitsModel = new BudgetCategoryOneWorkUnitsTableModel(getProject(), getRowColumnProvider(), getTree(), getTreeTableModel().getUniqueTreeTableModelIdentifier());
		budgetCategoryOneExpenseModel = new BudgetCategoryOneExpenseTableModel(getProject(), getRowColumnProvider(), getTree(), getTreeTableModel().getUniqueTreeTableModelIdentifier());
		budgetCategoryOneBudgetDetailsModel = new BudgetCategoryOneBudgetDetailsTableModel(getProject(), getRowColumnProvider(), getTree(), getTreeTableModel().getUniqueTreeTableModelIdentifier());
		budgetCategoryTwoWorkUnitsModel = new BudgetCategoryTwoWorkUnitsTableModel(getProject(), getRowColumnProvider(), getTree(), getTreeTableModel().getUniqueTreeTableModelIdentifier());
		budgetCategoryTwoExpenseModel = new BudgetCategoryTwoExpenseTableModel(getProject(), getRowColumnProvider(), getTree(), getTreeTableModel().getUniqueTreeTableModelIdentifier());
		budgetCategoryTwoBudgetDetailsModel = new BudgetCategoryOneBudgetDetailsTableModel(getProject(), getRowColumnProvider(), getTree(), getTreeTableModel().getUniqueTreeTableModelIdentifier());
	}

	protected PlanningViewAbstractTreeTableSyncedTableModel createMainTableModel(final PlanningTreeRowColumnProvider rowColumnProviderToUse) throws Exception
	{
		return new PlanningViewMainTableModel(getProject(), getTree(), rowColumnProviderToUse);
	}

	protected boolean wasTypeCreatedOrDeleted(CommandExecutedEvent event, int objectType)
	{
		if (event.isCreateCommandForThisType(objectType))
			return true;
		
		return event.isDeleteCommandForThisType(objectType);
	}

	@Override
	protected boolean wereAssignmentNodesAddedOrRemoved(CommandExecutedEvent event) throws Exception
	{
		CodeList rowCodes = getRowColumnProvider().getRowCodesToShow();
		if (rowCodes.contains(ResourceAssignmentSchema.OBJECT_NAME) && event.isSetDataCommandWithThisTag(BaseObject.TAG_RESOURCE_ASSIGNMENT_IDS))
			return true;

		if (rowCodes.contains(ExpenseAssignmentSchema.OBJECT_NAME) && event.isSetDataCommandWithThisTag(BaseObject.TAG_EXPENSE_ASSIGNMENT_REFS))
			return true;
		
		return false;
	}

	@Override
	protected void rebuildEntireTreeTable() throws Exception
	{
		ORefList selectionHierarchy = getTree().getSelectionHierarchy();
		int selectedRow = tree.getSelectionModel().getMinSelectionIndex();
		int selectedColumn = getMainTable().getColumnModel().getSelectionModel().getMinSelectionIndex();
		getMainTable().clearSelection();
		getMainTable().getColumnModel().getSelectionModel().clearSelection();

		// TODO: Perhaps possibly detect exactly what changed and 
		// only rebuild the columns or the rows rather than always doing both
	
		// NOTE: The following rebuild the columns but don't touch the tree
		tree.rebuildTableCompletely();

		updateResourceFilter();
		updateDiagramFilter();
		updateCustomizeTableFilter();
		multiTableModel.updateColumnsToShow();
		
		// NOTE: The following rebuild the tree but don't touch the columns
		getTreeTableModel().rebuildEntireTree();
		measurementModel.fireTableDataChanged();
		futureStatusModel.fireTableDataChanged();
		workUnitsTableModel.fireTableDataChanged();
		resourceWorkUnitsTableModel.fireTableDataChanged();
		resourceBudgetDetailsTableModel.fireTableDataChanged();
		fundingSourceExpenseTableModel.fireTableDataChanged();
		accountingCodeExpenseTableModel.fireTableDataChanged();
		expenseAmountsTableModel.fireTableDataChanged();
		budgetDetailsTableModel.fireTableDataChanged();
		fundingSourceBudgetDetailsTableModel.fireTableDataChanged();
		accountingCodeBudgetDetailsTableModel.fireTableDataChanged();
		rollupReportsWorkUnitsModel.fireTableDataChanged();
		rollupReportsExpenseModel.fireTableDataChanged();
		rollupReportsBudgetDetailsModel.fireTableDataChanged();
		restoreTreeExpansionState();
		updateRightSideTablePanels();

		selectObjectAfterSwingClearsItDueToTreeStructureChange(getMainTable(), selectionHierarchy, selectedRow, selectedColumn);

		updateStatusBar();
	}
	
	protected void updateResourceFilter() throws Exception
	{
	}

	protected void updateDiagramFilter() throws Exception
	{
	}

	protected void updateCustomizeTableFilter() throws Exception
	{
	}

	public void updateStatusBar()
	{
	}

	private void updateRightSideTablePanels() throws Exception
	{
		multiTableModel.removeAllModels();
		multiTableModel.addModel(mainModel);

		mainTableScrollPane.showVerticalScrollBar();

		CodeList columnsToShow = getRowColumnProvider().getColumnCodesToShow();
		
		if (columnsToShow.contains(Measurement.META_COLUMN_TAG))
			multiTableModel.addModel(measurementModel);

		if (columnsToShow.contains(Indicator.META_COLUMN_TAG))
			multiTableModel.addModel(futureStatusModel);
		
		if (columnsToShow.contains(WorkPlanColumnConfigurationQuestion.META_RESOURCE_ASSIGNMENT_COLUMN_CODE))
			multiTableModel.addModel(workUnitsTableModel);
		
		if (columnsToShow.contains(WorkPlanColumnConfigurationQuestion.META_PROJECT_RESOURCE_WORK_UNITS_COLUMN_CODE))
			multiTableModel.addModel(resourceWorkUnitsTableModel);
		
		if (columnsToShow.contains(WorkPlanColumnConfigurationQuestion.META_PROJECT_RESOURCE_BUDGET_DETAILS_COLUMN_CODE))
			multiTableModel.addModel(resourceBudgetDetailsTableModel);
		
		if (columnsToShow.contains(WorkPlanColumnConfigurationQuestion.META_EXPENSE_ASSIGNMENT_COLUMN_CODE))
			multiTableModel.addModel(expenseAmountsTableModel);
		
		if (columnsToShow.contains(WorkPlanColumnConfigurationQuestion.META_FUNDING_SOURCE_EXPENSE_COLUMN_CODE))
			multiTableModel.addModel(fundingSourceExpenseTableModel);
		
		if (columnsToShow.contains(WorkPlanColumnConfigurationQuestion.META_ACCOUNTING_CODE_EXPENSE_COLUMN_CODE))
			multiTableModel.addModel(accountingCodeExpenseTableModel);
		
		if (columnsToShow.contains(WorkPlanColumnConfigurationQuestion.META_FUNDING_SOURCE_BUDGET_DETAILS_COLUMN_CODE))
			multiTableModel.addModel(fundingSourceBudgetDetailsTableModel);
		
		if (columnsToShow.contains(WorkPlanColumnConfigurationQuestion.META_ACCOUNTING_CODE_BUDGET_DETAILS_COLUMN_CODE))
			multiTableModel.addModel(accountingCodeBudgetDetailsTableModel);
		
		if (columnsToShow.contains(WorkPlanColumnConfigurationQuestion.META_BUDGET_DETAIL_COLUMN_CODE))
			multiTableModel.addModel(budgetDetailsTableModel);
		
		if (columnsToShow.contains(WorkPlanColumnConfigurationQuestion.META_ANALYSIS_WORK_UNITS_COLUMN_CODE))
			multiTableModel.addModel(rollupReportsWorkUnitsModel);
		
		if (columnsToShow.contains(WorkPlanColumnConfigurationQuestion.META_ANALYSIS_EXPENSES_CODE))
			multiTableModel.addModel(rollupReportsExpenseModel);
		
		if (columnsToShow.contains(WorkPlanColumnConfigurationQuestion.META_ANALYSIS_BUDGET_DETAILS_COLUMN_CODE))
			multiTableModel.addModel(rollupReportsBudgetDetailsModel);
		
		if (columnsToShow.contains(WorkPlanColumnConfigurationQuestion.META_BUDGET_CATEGORY_ONE_WORK_UNITS_COLUMN_CODE))
			multiTableModel.addModel(budgetCategoryOneWorkUnitsModel);
		
		if (columnsToShow.contains(WorkPlanColumnConfigurationQuestion.META_BUDGET_CATEGORY_ONE_EXPENSE_COLUMN_CODE))
			multiTableModel.addModel(budgetCategoryOneExpenseModel);
		
		if (columnsToShow.contains(WorkPlanColumnConfigurationQuestion.META_BUDGET_CATEGORY_ONE_BUDGET_DETAILS_COLUMN_CODE))
			multiTableModel.addModel(budgetCategoryOneBudgetDetailsModel);
		
		if (columnsToShow.contains(WorkPlanColumnConfigurationQuestion.META_BUDGET_CATEGORY_TWO_WORK_UNITS_COLUMN_CODE))
			multiTableModel.addModel(budgetCategoryTwoWorkUnitsModel);
		
		if (columnsToShow.contains(WorkPlanColumnConfigurationQuestion.META_BUDGET_CATEGORY_TWO_EXPENSE_COLUMN_CODE))
			multiTableModel.addModel(budgetCategoryTwoExpenseModel);
		
		if (columnsToShow.contains(WorkPlanColumnConfigurationQuestion.META_BUDGET_CATEGORY_TWO_BUDGET_DETAILS_COLUMN_CODE))
			multiTableModel.addModel(budgetCategoryTwoBudgetDetailsModel);
		
		mainTable.updateToReflectNewColumns();
		validate();
		repaint();
	}

	public TableExporter getTableForExporting() throws Exception
	{
		MultiTableCombinedAsOneExporter multiTableExporter = new MultiTableCombinedAsOneExporter(getProject());
		multiTableExporter.addAsMasterTable(new TreeTableExporter(getTree()));
		multiTableExporter.addExportable(new PlanningViewMainModelExporter(getProject(), multiTableModel, getTree(), multiTableModel.getUniqueTableModelIdentifier()));
		
		return multiTableExporter;
	}
	
	@Override
	protected TableWithColumnWidthAndSequenceSaver getMainTable()
	{
		return mainTable;
	}

	protected TableWithExpandableColumnsInterface getMainTableInterface()
	{
		return mainTable;
	}

	public PlanningTreeRowColumnProvider getRowColumnProvider()
	{
		return rowColumnProvider;
	}
	
	protected AbstractWorkUnitsTableModel getWorkUnitsTableModel()
	{
		return workUnitsTableModel;
	}
	
	protected WorkPlanBudgetDetailsTableModel getBudgetDetailsTableModel()
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

	@Override
	protected boolean shouldSetFocusOnFirstField()
	{
		return !isCellEditable();
	}

	private boolean isCellEditable()
	{
		int selectedRow = tree.getSelectionModel().getMinSelectionIndex();
		if(selectedRow < 0 || selectedRow >= getMainTable().getRowCount())
			return false;

		int selectedColumn = getMainTable().getColumnModel().getSelectionModel().getMinSelectionIndex();
		if(selectedColumn < 0 || selectedColumn >= getMainTable().getColumnCount())
			return false;

		return getMainTable().isCellEditable(selectedRow, selectedColumn);
	}

	private void enableSelectionListeners()
	{
		listenForColumnSelectionChanges(getMainTable());
	}
	
	private void listenForColumnSelectionChanges(JTable table)
	{
		table.getColumnModel().addColumnModelListener(mainTableColumnSelectionListener);
		table.getSelectionModel().addListSelectionListener(mainTableColumnSelectionListener);
	}

	private void selectSectionForTag(String selectedColumnTag)
	{
		if (isSideTabSwitchingDisabled())
			return;
		
		if (getPropertiesPanel() == null)
			return;
		
		getPropertiesPanel().setObjectRefs(getPicker().getSelectionHierarchy().toArray(), selectedColumnTag);
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
	
	public static JComponent createReformattedPrintablePlanningTreeTablePanel(PlanningTreeTablePanel wholePanel)
	{
		TwoColumnPanel reformatted = new TwoColumnPanel();
		reformatted.add(new FillerLabel());
		reformatted.add(wholePanel.createAboveColumnBar());
		
		reformatted.add(wholePanel.getTree().getTableHeader());
		reformatted.add(wholePanel.getMainTable().getTableHeader());
		wholePanel.getTree().updateAutomaticRowHeights();
		reformatted.add(wholePanel.getTree());
		reformatted.add(wholePanel.getMainTable());
		
		wholePanel.dispose();
		return reformatted;
	}
	
	private void selectObjectAfterSwingClearsItDueToTreeStructureChange(TableWithColumnWidthAndSequenceSaver table, ORefList selectionHierarchy, int fallbackRow, int fallbackColumn)
	{
		SwingUtilities.invokeLater(new Reselecter(table, selectionHierarchy, fallbackRow, fallbackColumn));
	}
	
	private class Reselecter implements Runnable
	{
		public Reselecter(TableWithColumnWidthAndSequenceSaver tableToUse, ORefList selectionHierarchyToUse, int rowToSelect, int columnToSelect)
		{
			table = tableToUse;
			selectionHierarchy = selectionHierarchyToUse;
			row = rowToSelect;
			column = columnToSelect;
		}
		
		public void run()
		{
			disableSectionSwitchDuringFullRebuild();
			try
			{
				if (selectionHierarchy.hasRefs())
				{
					getTree().selectObject(selectionHierarchy, row);
				}
				else
				{
					table.getSelectionModel().setSelectionInterval(row, row);
					if(column < table.getColumnCount())
						table.getColumnModel().getSelectionModel().setSelectionInterval(column, column);
				}
				table.ensureSelectedRowVisible();
				getMainWindow().updateActionsAndStatusBar();
			}
			finally 
			{
				enableSectionSwitch();
			}
		}
		
		private TableWithColumnWidthAndSequenceSaver table;
		private ORefList selectionHierarchy;
		private int row;
		private int column;
	}

	private class MainTableSelectionHandler  implements TableColumnModelListener, ListSelectionListener
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

		// defer handling column / value changes until later to allow JTreeTable ListSelectionHandler to execute first
		public void columnSelectionChanged(ListSelectionEvent e)
		{
			if (!e.getValueIsAdjusting())
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						selectSectionForColumn();
					}
				});
		}

		public void valueChanged(ListSelectionEvent e)
		{
			if (!e.getValueIsAdjusting())
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						selectSectionForColumn();
					}
				});
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
			return multiTableModel.getTagForCell(getSafeTypeForSelectedRow(), modelColumn);
		}		
	}
	
	private PlanningTreeRowColumnProvider rowColumnProvider;
	private PlanningViewAbstractTreeTableSyncedTableModel mainModel;
	private PlanningTreeMultiTableModel multiTableModel;
	private PlanningUpperMultiTable mainTable;

	private PlanningViewMeasurementTableModel measurementModel;
	private PlanningViewFutureStatusTableModel futureStatusModel;
	private AbstractWorkUnitsTableModel workUnitsTableModel;
	private WorkPlanExpenseAmountsTableModel expenseAmountsTableModel;
	private WorkPlanBudgetDetailsTableModel budgetDetailsTableModel;
	private FundingSourceBudgetDetailsTableModel fundingSourceBudgetDetailsTableModel;
	private AccountingCodeBudgetDetailsTableModel accountingCodeBudgetDetailsTableModel;
	private ProjectResourceWorkUnitsTableModel resourceWorkUnitsTableModel;
	private ProjectResourceBudgetDetailsTableModel resourceBudgetDetailsTableModel;
	private FundingSourceExpenseTableModel fundingSourceExpenseTableModel;
	private AccountingCodeExpenseTableModel accountingCodeExpenseTableModel;
	private AnalysisWorkUnitsModel rollupReportsWorkUnitsModel;
	private AnalysisExpenseTableModel rollupReportsExpenseModel;
	private AnalysisBudgetDetailsTableModel rollupReportsBudgetDetailsModel;
	private BudgetCategoryOneWorkUnitsTableModel budgetCategoryOneWorkUnitsModel;
	private BudgetCategoryOneExpenseTableModel budgetCategoryOneExpenseModel;
	private BudgetCategoryOneBudgetDetailsTableModel budgetCategoryOneBudgetDetailsModel;
	private BudgetCategoryTwoWorkUnitsTableModel budgetCategoryTwoWorkUnitsModel;
	private BudgetCategoryTwoExpenseTableModel budgetCategoryTwoExpenseModel;
	private BudgetCategoryOneBudgetDetailsTableModel budgetCategoryTwoBudgetDetailsModel;
	
	private MainTableSelectionHandler mainTableColumnSelectionListener;
	private JComponent treeTableHeaderPanel;
}
