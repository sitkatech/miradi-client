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
package org.miradi.dialogs.planning.propertiesPanel;

import java.awt.Color;
import java.util.Vector;

import javax.swing.Action;
import javax.swing.JLabel;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import org.miradi.actions.Actions;
import org.miradi.dialogs.fieldComponents.PanelTextField;
import org.miradi.dialogs.fieldComponents.PanelTitleLabel;
import org.miradi.dialogs.planning.RightClickActionProvider;
import org.miradi.dialogs.planning.TableHeaderWithExpandCollapseIcons;
import org.miradi.dialogs.planning.TableWithExpandableColumnsInterface;
import org.miradi.dialogs.tablerenderers.BasicTableCellEditorOrRendererFactory;
import org.miradi.dialogs.tablerenderers.BudgetCostCellRendererWithStrikeThroughFactory;
import org.miradi.dialogs.tablerenderers.DefaultFontProvider;
import org.miradi.dialogs.tablerenderers.FontForObjectProvider;
import org.miradi.dialogs.tablerenderers.NumericTableCellRendererWithStrikeThroughFactory;
import org.miradi.dialogs.tablerenderers.PlanningViewFontProvider;
import org.miradi.dialogs.tablerenderers.RowColumnBaseObjectProvider;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.DateUnit;
import org.miradi.objects.BaseObject;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.WorkPlanColumnConfigurationQuestion;
import org.miradi.utils.DoubleClickAutoSelectCellEditor;

abstract public class AssignmentDateUnitsTable extends AbstractComponentTable implements RightClickActionProvider, TableWithExpandableColumnsInterface, RowColumnBaseObjectProvider
{
	public AssignmentDateUnitsTable(MainWindow mainWindowToUse, AssignmentDateUnitsTableModel modelToUse) throws Exception
	{
		super(mainWindowToUse, modelToUse, UNIQUE_IDENTIFIER);
		
		setBackground(getColumnBackGroundColor(0));	
		setAllColumnsToUseDoubleClickEditors();
		setColumnSelectionAllowed(true);
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		FontForObjectProvider fontProvider = new PlanningViewFontProvider(getMainWindow());
		currencyRendererFactory = new BudgetCostCellRendererWithStrikeThroughFactory(getWorkUnitsTableModel(), fontProvider);
		numericRendererFactory = new NumericTableCellRendererWithStrikeThroughFactory(getWorkUnitsTableModel(), new DefaultFontProvider(getMainWindow()));
		setTableHeader(new TableHeaderWithExpandCollapseIcons(this));

		addRightClickHandler();
	}

	private void addRightClickHandler()
	{
		addMouseListener(new PlanningRightClickHandler(getMainWindow(), this, this));
	}
	
	protected Actions getActions()
	{
		return getMainWindow().getActions();
	}
	
	protected AssignmentDateUnitsTableModel getWorkUnitsTableModel()
	{
		return (AssignmentDateUnitsTableModel) getModel();
	}
	
	@Override
	public TableCellRenderer getCellRenderer(int row, int tableColumn)
	{
		final int modelColumn = convertColumnIndexToModel(tableColumn);
		BasicTableCellEditorOrRendererFactory renderer = numericRendererFactory;
		if(getWorkUnitsTableModel().isCurrencyColumn(modelColumn))
			renderer = currencyRendererFactory;

		renderer.setCellBackgroundColor(getColumnBackGroundColor(tableColumn));
		return renderer;	
	}
	
	@Override
	public Color getColumnBackGroundColor(int column)
	{
		int modelColumn = convertColumnIndexToModel(column);
		return getWorkUnitsTableModel().getCellBackgroundColor(modelColumn);
	}

	private void setAllColumnsToUseDoubleClickEditors()
	{
		int colCount = getColumnCount();
		for (int i = 0; i < colCount; i++)
		{
			int modelColumn = convertColumnIndexToModel(i);
			TableColumn column = getColumnModel().getColumn(modelColumn);
			column.setCellEditor(new DoubleClickAutoSelectCellEditor(new PanelTextField()));
		}
	}
	
	@Override
	public int getColumnWidth(int column)
	{
		return getColumnModel().getColumn(column).getWidth();
	}
	
	@Override
	abstract public String getColumnGroupCode(int tableColumn);
	
	public int getColumnAlignment()
	{
		return JLabel.RIGHT;
	}
	
	public boolean isColumnExpandable(int tableColumn)
	{
		int modelColumn = convertColumnIndexToModel(tableColumn);
		return getWorkUnitsTableModel().isColumnExpandable(modelColumn);
	}
	
	public boolean isColumnCollapsable(int tableColumn)
	{
		int modelColumn = convertColumnIndexToModel(tableColumn);
		return getWorkUnitsTableModel().isColumnCollapsable(modelColumn);
	}
	
	public boolean isDayColumnSelected()
	{ 
		return getWorkUnitsTableModel().isDayColumn(getSelectedModelColumn());
	}

	public boolean isSelectedDateUnitColumnExpanded()
	{
		return getWorkUnitsTableModel().isDateUnitColumnExpanded(getSelectedModelColumn());
	}

	public void respondToExpandOrCollapseColumnEvent(int tableColumnIndex) throws Exception
	{
		int modelColumn = convertColumnIndexToModel(tableColumnIndex);
		getWorkUnitsTableModel().respondToExpandOrCollapseColumnEvent(modelColumn);
		saveColumnState();
		updateToReflectNewColumns();
	}

	public Vector<Action> getActionsForRightClickMenu(int row, int tableColumn)
	{
		int modelColumn = convertColumnIndexToModel(tableColumn);
		AssignmentDateUnitsTableModel model = getWorkUnitsTableModel();
		
		Vector<Action> rightClickActions = new Vector();

		if(model.isColumnExpandable(modelColumn))
			rightClickActions.add(new ExpandColumnAction(this, model));
		if(model.isColumnCollapsable(modelColumn))
			rightClickActions.add(new CollapseColumnAction(this, model));
		
		return rightClickActions;		
	}

	private int getSelectedModelColumn()
	{
		int selectedTableColumn = getSelectedColumn();
		
		return convertColumnIndexToModel(selectedTableColumn);
	}
	
	public BaseObject getBaseObjectForRowColumn(int row, int column)
	{
		return getWorkUnitsTableModel().getBaseObjectForRow(row);
	}
	
	public int getProportionShares(int row)
	{
		return getWorkUnitsTableModel().getProportionShares(row);
	}
	
	public boolean areBudgetValuesAllocated(int row)
	{
		return getWorkUnitsTableModel().areBudgetValuesAllocated(row);
	}
	
	@Override
	public int getDefaultColumnWidth(int tableColumn, String columnTag,	int columnHeaderWidth)
	{
		int modelColumn = convertColumnIndexToModel(tableColumn);
		
		return getDefaultColumnWidth(getWorkUnitsTableModel(), modelColumn, getColumnGroupCode(tableColumn), columnHeaderWidth);
	}
	
	public static int getDefaultColumnWidth(PlanningViewAbstractTreeTableSyncedTableModel model, int modelColumn, String columnGroupTag, int defaultColumnWidth)
	{
		DateUnit dateUnitForColumn = model.getDateUnit(modelColumn);
		if (model.isColumnExpandable(modelColumn) && dateUnitForColumn.isProjectTotal())
		{
			WorkPlanColumnConfigurationQuestion question = new WorkPlanColumnConfigurationQuestion();
			String budgetColumnGroupCode = model.getColumnGroupCode(modelColumn);
			String normalizedBudgetGroupColumnCode = question.getNormalizedBudgetGroupColumnCode(budgetColumnGroupCode);
			ChoiceItem choiceItem = question.findChoiceByCode(normalizedBudgetGroupColumnCode);
			
			return getPreferredLabelWidth(choiceItem);
		}
		
		return defaultColumnWidth;
	}
	
	private static int getPreferredLabelWidth(ChoiceItem choiceItem)
	{
		PanelTitleLabel label = new PanelTitleLabel(choiceItem.getLabel());
		
		return label.getPreferredSize().width;
	}
		
	public static final String UNIQUE_IDENTIFIER = "WorkUnitsTable";

	private BasicTableCellEditorOrRendererFactory currencyRendererFactory;
	private BasicTableCellEditorOrRendererFactory numericRendererFactory;
}
