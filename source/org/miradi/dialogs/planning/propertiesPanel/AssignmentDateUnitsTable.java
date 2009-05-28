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
import org.miradi.dialogs.planning.TableWithExpandableColumnsInterface;
import org.miradi.dialogs.planning.RightClickActionProvider;
import org.miradi.dialogs.tablerenderers.BasicTableCellRendererFactory;
import org.miradi.dialogs.tablerenderers.DefaultFontProvider;
import org.miradi.dialogs.tablerenderers.NumericTableCellRendererFactory;
import org.miradi.main.MainWindow;
import org.miradi.utils.SingleClickAutoSelectCellEditor;

public class AssignmentDateUnitsTable extends AbstractComponentTable implements RightClickActionProvider, TableWithExpandableColumnsInterface
{
	public AssignmentDateUnitsTable(MainWindow mainWindowToUse, AssignmentDateUnitsTableModel modelToUse) throws Exception
	{
		super(mainWindowToUse, modelToUse, UNIQUE_IDENTIFIER);
		setBackground(getColumnBackGroundColor(0));	
		setAllColumnsToUseSingleClickEditors();
		setColumnSelectionAllowed(true);
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		renderer = new NumericTableCellRendererFactory(modelToUse, new DefaultFontProvider(getMainWindow()));
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
	public TableCellRenderer getCellRenderer(int row, int column)
	{
		renderer.setCellBackgroundColor(getColumnBackGroundColor(column));
		return renderer;	
	}
	
	@Override
	public Color getColumnBackGroundColor(int column)
	{
		int modelColumn = convertColumnIndexToModel(column);
		return getWorkUnitsTableModel().getCellBackgroundColor(modelColumn);
	}

	private void setAllColumnsToUseSingleClickEditors()
	{
		int colCount = getColumnCount();
		for (int i = 0; i < colCount; i++)
		{
			int modelColumn = convertColumnIndexToModel(i);
			TableColumn column = getColumnModel().getColumn(modelColumn);
			column.setCellEditor(new SingleClickAutoSelectCellEditor(new PanelTextField()));
		}
	}
	
	@Override
	public int getColumnWidth(int column)
	{
		return getColumnModel().getColumn(column).getWidth();
	}
	
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
	}

	public Vector<Action> getActionsForRightClickMenu(int tableColumn)
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
	
	public static final String UNIQUE_IDENTIFIER = "WorkUnitsTable";

	private BasicTableCellRendererFactory renderer;

}
