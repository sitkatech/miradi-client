/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/
package org.conservationmeasures.eam.utils;

import java.awt.Rectangle;
import java.util.Vector;

import javax.swing.table.TableModel;

import org.conservationmeasures.eam.dialogs.fieldComponents.PanelTable;
import org.conservationmeasures.eam.main.EAM;

abstract public class TableWithColumnWidthSaver extends PanelTable implements TableWithRowHeightManagement
{
	public TableWithColumnWidthSaver(TableModel model)
	{
		super(model);
		rowHeightListeners = new Vector<RowHeightListener>();
		
		addColumnWidthSaver();
		addColumnSequenceSaver();
		addRowHeightSaver();
	}
	
	private void addColumnWidthSaver()
	{
		if (! shouldSaveColumnWidth())
			return; 
		
		columnWidthSaver = new ColumnWidthSaver(this, (ColumnTagProvider)getModel(), getUniqueTableIdentifier());
		getTableHeader().addMouseListener(columnWidthSaver);
		columnWidthSaver.restoreColumnWidths();
	}
	
	private void addColumnSequenceSaver()
	{
		try
		{
			columnSequenceSaver = new ColumnSequenceSaver(this, (ColumnTagProvider)getModel(), getUniqueTableIdentifier());
			getTableHeader().addMouseListener(columnSequenceSaver);
			columnSequenceSaver.restoreColumnSequences();
		}
		catch (Exception e)
		{
			EAM.logException(e);
			//TODO when storing column sequence is finished try throwing further up this exception
		}
	}
	
	private void addRowHeightSaver()
	{
		rowHeightSaver = new TableRowHeightSaver();
		rowHeightSaver.manage(this, getUniqueTableIdentifier());
	}
	
	public void addRowHeightListener(RowHeightListener listener)
	{
		rowHeightListeners.add(listener);
	}
	
	protected int getSavedColumnWidth(int column)
	{
		return columnWidthSaver.getColumnWidth(column);
	}
	
	public boolean shouldSaveColumnWidth()
	{
		return true;
	}
	
	public void setRowHeight(int rowHeight)
	{
		super.setRowHeight(rowHeight);
		if(rowHeightSaver != null)
			rowHeightSaver.saveRowHeight();
		
		if(rowHeightListeners != null)
		{
			for(RowHeightListener listener : rowHeightListeners)
			{
				listener.rowHeightChanged(rowHeight);
			}
		}
	}
	
	public Rectangle getCellRect(int row, int column, boolean includeSpacing)
	{
		Rectangle cellRect = super.getCellRect(row, column, includeSpacing);
		if(!includeSpacing)
		{
			cellRect.height -= TableRowHeightSaver.ROW_RESIZE_MARGIN;
		}
		return cellRect;
	}

	abstract public String getUniqueTableIdentifier();
	
	private ColumnWidthSaver columnWidthSaver;
	private ColumnSequenceSaver columnSequenceSaver;
	private TableRowHeightSaver rowHeightSaver;
	private Vector<RowHeightListener> rowHeightListeners;
}
