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
package org.miradi.utils;

import javax.swing.table.TableModel;

import org.miradi.main.CommandExecutedEvent;
import org.miradi.main.CommandExecutedListener;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objects.TableSettings;

abstract public class TableWithColumnWidthAndSequenceSaver extends TableWithRowHeightSaver implements CommandExecutedListener, TableWithColumnManagement
{
	public TableWithColumnWidthAndSequenceSaver(MainWindow mainWindowToUse, TableModel model, String uniqueTableIdentifierToUse)
	{
		super(mainWindowToUse, model, uniqueTableIdentifierToUse);
	
		mainWindowToUse.getProject().addCommandExecutedListener(this);
		
		addColumnWidthSaver();
		addColumnSequenceSaver();
	}
	
	public void dispose()
	{
		getMainWindow().getProject().removeCommandExecutedListener(this);
	}

	public void commandExecuted(CommandExecutedEvent event)
	{
		try
		{
			if (event.isSetDataCommandWithThisTypeAndTag(TableSettings.getObjectType(), TableSettings.TAG_COLUMN_SEQUENCE_CODES))
				reloadColumnSequences();
			
			if (event.isSetDataCommandWithThisTypeAndTag(TableSettings.getObjectType(), TableSettings.TAG_COLUMN_WIDTHS))
				reloadColumnWidths();
			
			if (event.isSetDataCommandWithThisTypeAndTag(TableSettings.getObjectType(), TableSettings.TAG_ROW_HEIGHT))
				reloadRowHeights();
		}
		catch (Exception e)
		{
			EAM.logException(e);
		}
	}
	
	private void addColumnWidthSaver()
	{
		if (! shouldSaveColumnWidth())
			return; 
		
		columnWidthSaver = new ColumnWidthSaver(getMainWindow().getProject(), this, getUniqueTableIdentifier());
		getTableHeader().addMouseListener(columnWidthSaver);
		columnWidthSaver.restoreColumnWidths();
	}
	
	private void addColumnSequenceSaver()
	{
		if (! shouldSaveColumnSequence())
			return;
		
		try
		{
			columnSequenceSaver = new ColumnSequenceSaver(getMainWindow().getProject(), this, getUniqueTableIdentifier());
			getTableHeader().addMouseListener(columnSequenceSaver);
			columnSequenceSaver.restoreColumnSequences();
		}
		catch (Exception e)
		{
			EAM.logException(e);
			//TODO when storing column sequence is finished try throwing further up this exception
		}
	}
	
	public String getTableColumnSequenceKey(int tableColumn)
	{
		return getColumnName(tableColumn);
	}
	
	public void reloadColumnSequences() throws Exception
	{
		if(columnSequenceSaver != null)
			columnSequenceSaver.restoreColumnSequences();
		
		invalidate();
	}

	public void reloadColumnWidths()
	{
		if(columnWidthSaver != null)
			columnWidthSaver.restoreColumnWidths();
		
		invalidate();
	}
	
	protected int getSavedColumnWidth(int tableColumn) throws Exception
	{
		return columnWidthSaver.getColumnWidth(tableColumn);
	}
	
	public boolean shouldSaveColumnWidth()
	{
		return true;
	}
	
	public boolean shouldSaveColumnSequence()
	{
		return true;
	}
	
	private ColumnWidthSaver columnWidthSaver;
	private ColumnSequenceSaver columnSequenceSaver;
}
