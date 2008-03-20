/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/
package org.miradi.utils;

import javax.swing.table.TableModel;

import org.miradi.main.EAM;

abstract public class TableWithColumnWidthSaver extends TableWithRowHeightSaver
{
	public TableWithColumnWidthSaver(TableModel model)
	{
		super(model);
	
		addColumnWidthSaver();
		addColumnSequenceSaver();
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
	
	protected int getSavedColumnWidth(int tableColumn)
	{
		int modelColumn = convertColumnIndexToModel(tableColumn);
		return columnWidthSaver.getColumnWidth(modelColumn);
	}
	
	public boolean shouldSaveColumnWidth()
	{
		return true;
	}
	
	private ColumnWidthSaver columnWidthSaver;
	private ColumnSequenceSaver columnSequenceSaver;
}
