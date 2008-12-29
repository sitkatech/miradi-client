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
package org.miradi.utils;

import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

import org.miradi.dialogs.fieldComponents.PanelTable;
import org.miradi.dialogs.tablerenderers.BooleanTableCellRendererFactoryWithPreferredHeight;
import org.miradi.dialogs.tablerenderers.DefaultTableCellRendererWithPreferredHeightFactory;
import org.miradi.dialogs.tablerenderers.TableCellPreferredHeightProvider;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;

abstract public class TableWithRowHeightSaver extends PanelTable implements TableWithRowHeightManagement
{
	public TableWithRowHeightSaver(MainWindow mainWindowToUse, TableModel model, String uniqueTableIdentifierToUse)
	{
		super(mainWindowToUse, model);
		
		uniqueTableIdentifier = uniqueTableIdentifierToUse;
		verifyUniqueTableIdentifier();
		
		defaultCellRendererFactory = new DefaultTableCellRendererWithPreferredHeightFactory();
		booleanRendererFactory = new BooleanTableCellRendererFactoryWithPreferredHeight();
		
		rowHeightManager = new TableRowHeightManager(getMainWindow().getProject(), getRowHeight());
		rowHeightManager.manage(getMainWindow(), this, uniqueTableIdentifier);
		
		rowHeightController = new SingleTableRowHeightController(getMainWindow(), this);
		
		getTableHeader().addMouseListener(new ColumnChangeHandler(this));
	}

	private void verifyUniqueTableIdentifier()
	{
		if (uniqueTableIdentifier.length() == 0)
			EAM.logError("A unique table identifier has been passed in with 0 length");
	}
	
	public void reloadRowHeights()
	{
		if (shouldSaveRowHeight())
			rowHeightManager.restoreRowHeight();
	}

	public void saveRowHeight(int newRowHeight)
	{
		rowHeightManager.saveRowHeightIgnoreExceptions();
	}
	
	public void setRowHeight(int rowHeight)
	{
		super.setRowHeight(rowHeight);
		if(rowHeightManager == null)
			return;
		
		rowHeightManager.rowHeightChanged(rowHeight);
	}
	
	public void setRowHeight(int row, int rowHeight)
	{
		super.setRowHeight(row, rowHeight);
		if(rowHeightManager == null)
			return;
		
		rowHeightManager.rowHeightChanged(row, rowHeight);
	}
	
	public boolean allowUserToSetRowHeight()
	{
		return true;
	}
	
	public boolean shouldSaveRowHeight()
	{
		return true;
	}
	
	public void setMultiTableRowHeightController(MultiTableRowHeightController listener)
	{
		rowHeightController.setMultiTableRowHeightController(listener);
		rowHeightManager.setMultiTableRowHeightController(listener);
	}
	
	public void updateAutomaticRowHeights()
	{
		rowHeightController.updateAutomaticRowHeights();
	}
	
	public void setVariableRowHeight()
	{
		// NOTE: No action required for plain tables
	}
	
	public int getPreferredRowHeight(int row)
	{
		return getPreferredRowHeight(this, row);
	}
	
	public static int getPreferredRowHeight(JTable table, int row)
	{
		return getPreferredRowHeight(table, row, 0);
	}
	
	public static int getPreferredRowHeight(JTable table, int row, int firstColumn)
	{
		int maxPreferredHeight = 1;
		for(int column = firstColumn; column < table.getColumnCount(); ++column)
		{
			int thisHeight = getPreferredCellHeight(table, row, column);
			maxPreferredHeight = Math.max(maxPreferredHeight, thisHeight);
		}
		
		final int ESTIMATED_CELL_PADDING_HEIGHT = 4;
		maxPreferredHeight += ESTIMATED_CELL_PADDING_HEIGHT;
		return maxPreferredHeight;
	}

	private static int getPreferredCellHeight(JTable table, int row, int column)
	{
		try
		{
			TableCellRenderer rawRenderer = table.getCellRenderer(row, column);
			TableCellPreferredHeightProvider provider = (TableCellPreferredHeightProvider)rawRenderer;
			int thisHeight = provider.getPreferredHeight(table, row, column, table.getValueAt(row, column));
			return thisHeight;
		}
		catch(RuntimeException e)
		{
			EAM.logWarning("Error in table " + table.getClass().getName() + " column " + column);
			EAM.logException(e);
			return 1;
		}
	}
	
	@Override
	public TableCellRenderer getCellRenderer(int row, int tableColumn)
	{
		TableCellRenderer renderer = getColumnModel().getColumn(tableColumn).getCellRenderer();
		if(renderer != null)
			return renderer;
		
		int modelColumn = convertColumnIndexToModel(tableColumn);
		boolean isCheckbox = getModel().getColumnClass(modelColumn).equals(Boolean.class);
		if(isCheckbox)
		{
			TableCellRenderer r = super.getCellRenderer(row, tableColumn);
			r.getTableCellRendererComponent(this, false, false, false, row, tableColumn);
			return booleanRendererFactory;
		}
		
		return defaultCellRendererFactory;
	}
	
	public JTable asTable()
	{
		return this;
	}
	
	public String getUniqueTableIdentifier()
	{
		return uniqueTableIdentifier;
	}
	
	private String uniqueTableIdentifier;
	private TableRowHeightManager rowHeightManager;
	private SingleTableRowHeightController rowHeightController;
	private DefaultTableCellRendererWithPreferredHeightFactory defaultCellRendererFactory;
	private BooleanTableCellRendererFactoryWithPreferredHeight booleanRendererFactory;
}
