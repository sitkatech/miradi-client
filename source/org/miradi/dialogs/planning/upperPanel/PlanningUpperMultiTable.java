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

import java.awt.Color;
import java.util.Vector;

import javax.swing.Action;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import org.miradi.actions.ActionCollapseAllRows;
import org.miradi.actions.ActionDeletePlanningViewTreeNode;
import org.miradi.actions.ActionExpandAllRows;
import org.miradi.actions.Actions;
import org.miradi.dialogs.fieldComponents.PanelTextField;
import org.miradi.dialogs.planning.FullTimeEmployeeDaysPerYearAction;
import org.miradi.dialogs.planning.MultiTableCollapseColumnAction;
import org.miradi.dialogs.planning.RightClickActionProvider;
import org.miradi.dialogs.planning.TableHeaderWithExpandCollapseIcons;
import org.miradi.dialogs.planning.TableWithExpandableColumnsInterface;
import org.miradi.dialogs.planning.propertiesPanel.AssignmentDateUnitsTable;
import org.miradi.dialogs.planning.propertiesPanel.MultiTableExpandColumnAction;
import org.miradi.dialogs.planning.propertiesPanel.PlanningRightClickHandler;
import org.miradi.dialogs.planning.propertiesPanel.PlanningViewAbstractTreeTableSyncedTableModel;
import org.miradi.dialogs.tablerenderers.BasicTableCellRendererEditorFactory;
import org.miradi.dialogs.tablerenderers.BudgetCostTreeTableCellRendererFactory;
import org.miradi.dialogs.tablerenderers.ChoiceItemTableCellRendererFactory;
import org.miradi.dialogs.tablerenderers.FontForObjectTypeProvider;
import org.miradi.dialogs.tablerenderers.MultiLineObjectTableCellRendererEditorFactory;
import org.miradi.dialogs.tablerenderers.NumericTableCellRendererFactory;
import org.miradi.dialogs.tablerenderers.PlanningViewFontProvider;
import org.miradi.dialogs.tablerenderers.ProgressTableCellRendererFactory;
import org.miradi.dialogs.tablerenderers.RowColumnBaseObjectProvider;
import org.miradi.dialogs.tablerenderers.WhoColumnTableCellEditorFactory;
import org.miradi.main.MainWindow;
import org.miradi.objects.BaseObject;
import org.miradi.questions.CustomPlanningColumnsQuestion;
import org.miradi.utils.DoubleClickAutoSelectCellEditor;
import org.miradi.utils.TableWithColumnWidthAndSequenceSaver;

public class PlanningUpperMultiTable extends TableWithColumnWidthAndSequenceSaver implements RowColumnBaseObjectProvider, RightClickActionProvider, TableWithExpandableColumnsInterface
{
	public PlanningUpperMultiTable(MainWindow mainWindowToUse, PlanningTreeTable masterTreeToUse, PlanningTreeMultiTableModel model)
	{
		super(mainWindowToUse, model, model.getUniqueTableModelIdentifier());
		
		setAutoResizeMode(AUTO_RESIZE_OFF);
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		setCellSelectionEnabled(true);
		setTableHeader(new TableHeaderWithExpandCollapseIcons(this));

		masterTree = masterTreeToUse;
		FontForObjectTypeProvider fontProvider = new PlanningViewFontProvider(getMainWindow());
		defaultRendererFactory = new MultiLineObjectTableCellRendererEditorFactory(this, fontProvider);
		currencyRendererFactory = new BudgetCostTreeTableCellRendererFactory(this, fontProvider);
		choiceRendererFactory = new ChoiceItemTableCellRendererFactory(this, fontProvider);
		progressRendererFactory = new ProgressTableCellRendererFactory(this, fontProvider);
		doubleRendererFactory = new NumericTableCellRendererFactory(this, fontProvider);
		whoColumnTableCellEditorFactory = new WhoColumnTableCellEditorFactory(getMainWindow(), this);
		doubleClickAutoSelectCellEditor = new DoubleClickAutoSelectCellEditor(new PanelTextField());
		
		addMouseListener(new PlanningRightClickHandler(getMainWindow(), this, this));
	}
	
	@Override
	public TableCellEditor getCellEditor(int row, int tableColumn)
	{
		int modelColumn = convertColumnIndexToModel(tableColumn);
		String columnTag = getCastedModel().getColumnTag(modelColumn);
		if (columnTag.equals(CustomPlanningColumnsQuestion.META_WHO_TOTAL))
			return whoColumnTableCellEditorFactory;
		
		return doubleClickAutoSelectCellEditor;
	}

	@Override
	public int getDefaultColumnWidth(int tableColumn, String columnTag, int columnHeaderWidth)
	{
		final int modelColumn = convertColumnIndexToModel(tableColumn);
		if (getCastedModel().isDateUnitColumn(modelColumn))
		{
			PlanningViewAbstractTreeTableSyncedTableModel castedModel = getCastedModel().getCastedModel(modelColumn);
			int subModelColumn = getCastedModel().findColumnWithinSubTable(modelColumn);
			return AssignmentDateUnitsTable.getDefaultColumnWidth(castedModel, subModelColumn, getColumnGroupCode(tableColumn), columnHeaderWidth);
		}
		
		return super.getDefaultColumnWidth(tableColumn, columnTag, columnHeaderWidth);
	}
	
	@Override
	public TableCellRenderer getCellRenderer(int row, int tableColumn)
	{
		final int modelColumn = convertColumnIndexToModel(tableColumn);

		BasicTableCellRendererEditorFactory factory = defaultRendererFactory;
		if(getCastedModel().isCurrencyColumn(modelColumn))
			factory = currencyRendererFactory;
		else if(getCastedModel().isChoiceColumn(modelColumn))
			factory = choiceRendererFactory;
		else if(getCastedModel().isProgressColumn(modelColumn))
			factory = progressRendererFactory;
		else if(getCastedModel().isDateUnitColumn(modelColumn))
			factory = doubleRendererFactory;
		
		Color background = getCastedModel().getCellBackgroundColor(row, modelColumn);
		factory.setCellBackgroundColor(background);
		return factory;
	}

	public PlanningTreeMultiTableModel getCastedModel()
	{
		return (PlanningTreeMultiTableModel)getModel();
	}

	public BaseObject getBaseObjectForRowColumn(int row, int column)
	{
		return masterTree.getBaseObjectForRowColumn(row, column);
	}

	public int getProportionShares(int row)
	{
		return masterTree.getProportionShares(row);
	}

	public boolean areBudgetValuesAllocated(int row)
	{
		return masterTree.areBudgetValuesAllocated(row);
	}
	
	@Override
	public String getColumnGroupCode(int tableColumn)
	{
		int modelColumn = convertColumnIndexToModel(tableColumn);
		return getCastedModel().getColumnGroupCode(modelColumn);
	}

	public Vector<Action> getActionsForRightClickMenu(int row, int tableColumn)
	{
		int multiModelColumn = convertColumnIndexToModel(tableColumn);
		int modelColumn = getCastedModel().findColumnWithinSubTable(multiModelColumn);
		PlanningViewAbstractTreeTableSyncedTableModel model = getCastedModel().getCastedModel(multiModelColumn);

		Vector<Action> actions = new Vector();
		actions.add(getActions().get(ActionDeletePlanningViewTreeNode.class));
		actions.add(null);
		actions.add(getActions().get(ActionExpandAllRows.class));
		actions.add(getActions().get(ActionCollapseAllRows.class));
		actions.add(null);
		if(model.isColumnExpandable(modelColumn))
			actions.add(new MultiTableExpandColumnAction(this));
		if(model.isColumnCollapsable(modelColumn))
			actions.add(new MultiTableCollapseColumnAction(this));
		if(model.isFullTimeEmployeeFractionAvailable(row, modelColumn))
			actions.add(new FullTimeEmployeeDaysPerYearAction(this));
		return actions;
	}

	public int getColumnWidth(int tableColumnIndex)
	{
		return getColumnModel().getColumn(tableColumnIndex).getWidth();
	}

	public boolean isColumnCollapsable(int tableColumnIndex)
	{
		int modelColumn = getModelColumnWithinModel(tableColumnIndex);
		PlanningViewAbstractTreeTableSyncedTableModel model = getModel(convertColumnIndexToModel(tableColumnIndex));
		return model.isColumnCollapsable(modelColumn);
	}

	public boolean isColumnExpandable(int tableColumnIndex)
	{
		int modelColumn = getModelColumnWithinModel(tableColumnIndex);
		PlanningViewAbstractTreeTableSyncedTableModel model = getModel(convertColumnIndexToModel(tableColumnIndex));
		return model.isColumnExpandable(modelColumn);
	}

	public void respondToExpandOrCollapseColumnEvent(int tableColumnIndex) throws Exception
	{
		int modelColumn = getModelColumnWithinModel(tableColumnIndex);
		PlanningViewAbstractTreeTableSyncedTableModel model = getModel(convertColumnIndexToModel(tableColumnIndex));
		model.respondToExpandOrCollapseColumnEvent(modelColumn);
		saveColumnState();
		updateToReflectNewColumns();
	}

	private PlanningViewAbstractTreeTableSyncedTableModel getModel(int modelColumnIndex)
	{
		return getCastedModel().getCastedModel(modelColumnIndex);
	}

	private int getModelColumnWithinModel(int tableColumnIndex)
	{
		int modelColumn = getCastedModel().findColumnWithinSubTable(convertColumnIndexToModel(tableColumnIndex));
		return modelColumn;
	}
	
	private Actions getActions()
	{
		return getMainWindow().getActions();
	}
	
	private PlanningTreeTable masterTree;
	private BasicTableCellRendererEditorFactory defaultRendererFactory;
	private BasicTableCellRendererEditorFactory currencyRendererFactory;
	private BasicTableCellRendererEditorFactory choiceRendererFactory;
	private BasicTableCellRendererEditorFactory progressRendererFactory;
	private BasicTableCellRendererEditorFactory doubleRendererFactory;
	private WhoColumnTableCellEditorFactory whoColumnTableCellEditorFactory;
	private DoubleClickAutoSelectCellEditor doubleClickAutoSelectCellEditor;
}
