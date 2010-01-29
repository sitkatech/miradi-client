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

import java.util.EventObject;

import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;

import org.miradi.dialogfields.FieldSaver;
import org.miradi.main.CommandExecutedEvent;
import org.miradi.main.CommandExecutedListener;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.BaseObject;
import org.miradi.objects.TableSettings;
import org.miradi.project.Project;

abstract public class TableWithColumnWidthAndSequenceSaver extends TableWithRowHeightSaver implements CommandExecutedListener, TableWithColumnManagement, ColumnWidthProvider
{
	public TableWithColumnWidthAndSequenceSaver(MainWindow mainWindowToUse, TableModel model, String uniqueTableIdentifierToUse)
	{
		super(mainWindowToUse, model, uniqueTableIdentifierToUse);
	
		mainWindowToUse.getProject().addCommandExecutedListener(this);
		safelyHandleAddingRenderersAndEditors();
		addColumnWidthSaver();
		addColumnSequenceSaver();
	}

	private void safelyHandleAddingRenderersAndEditors()
	{
		try
		{
			rebuildColumnEditorsAndRenderers();
		}
		catch(Exception e)
		{
			EAM.logException(e);
			EAM.unexpectedErrorDialog(e);
		}
	}
	
	protected void rebuildColumnEditorsAndRenderers() throws Exception
	{
	}

	@Override
	public void setTableHeader(JTableHeader tableHeader)
	{
		super.setTableHeader(tableHeader);
		
		tableHeader.addMouseListener(columnWidthSaver);
		tableHeader.addMouseListener(columnSequenceSaver);
	}
	
	public void dispose()
	{
		FieldSaver.setEditingTable(null);
		getProject().removeCommandExecutedListener(this);
	}
	
	@Override
	public boolean editCellAt(int row, int column, EventObject e)
	{
		FieldSaver.setEditingTable(this);		
		return super.editCellAt(row, column, e);
	}

	public void commandExecuted(CommandExecutedEvent event)
	{
		try
		{
			ORef tableSettingRef = getTableSettingsRef();
			if (event.isSetDataCommandFor(tableSettingRef))
			{
				if (event.isSetDataCommandWithThisTag(TableSettings.TAG_COLUMN_SEQUENCE_CODES))
					reloadColumnSequences();

				if (event.isSetDataCommandWithThisTag(TableSettings.TAG_COLUMN_WIDTHS))
					reloadColumnWidths();

				if (event.isSetDataCommandWithThisTag(TableSettings.TAG_ROW_HEIGHT))
					reloadRowHeights();
			}
		}
		catch (Exception e)
		{
			EAM.logException(e);
		}
	}

	private ORef getTableSettingsRef()
	{
		TableSettings tableSettings = TableSettings.find(getProject(), getUniqueTableIdentifier());
		if (tableSettings == null)
			return ORef.INVALID;
		
		return tableSettings.getRef();
	}
	
	private void addColumnWidthSaver()
	{
		if (! shouldSaveColumnWidth())
			return; 
		
		columnWidthSaver = new ColumnWidthSaver(getProject(), this, this, getUniqueTableIdentifier());
		getTableHeader().addMouseListener(columnWidthSaver);
		columnWidthSaver.restoreColumnWidths();
	}
	
	private void addColumnSequenceSaver()
	{
		if (! shouldSaveColumnSequence())
			return;
		
		try
		{
			columnSequenceSaver = new ColumnSequenceSaver(getProject(), this, getUniqueTableIdentifier());
			getTableHeader().addMouseListener(columnSequenceSaver);
			columnSequenceSaver.restoreColumnSequences();
		}
		catch (Exception e)
		{
			EAM.logException(e);
			//TODO when storing column sequence is finished try throwing further up this exception
		}
	}
	
	public String getColumnGroupCode(int tableColumn)
	{
		return getColumnName(tableColumn);
	}
	
	protected void saveColumnState() throws Exception
	{
		if(columnSequenceSaver != null)
			columnSequenceSaver.saveColumnSequence();
		if(columnWidthSaver != null)
			columnWidthSaver.saveColumnWidths();
	}

	public void updateToReflectNewColumns() throws Exception
	{
		reloadColumnSequences();
		reloadColumnWidths();
	}
	
	protected void reloadColumnSequences() throws Exception
	{
		if(columnSequenceSaver != null)
			columnSequenceSaver.restoreColumnSequences();
		
		invalidate();
	}

	protected void reloadColumnWidths()
	{
		if(columnWidthSaver != null)
			columnWidthSaver.restoreColumnWidths();
		
		invalidate();
	}
	
	public boolean shouldSaveColumnWidth()
	{
		return true;
	}
	
	public boolean shouldSaveColumnSequence()
	{
		return true;
	}
	
	public int getDefaultColumnWidth(int tableColumn, String columnTag, int columnHeaderWidth)
	{
		if (columnTag.equals(BaseObject.TAG_LABEL))
			return ColumnWidthSaver.DEFAULT_WIDE_COLUMN_WIDTH;
		
		else if (columnHeaderWidth < ColumnWidthSaver.DEFAULT_NARROW_COLUMN_WIDTH)
			return ColumnWidthSaver.DEFAULT_NARROW_COLUMN_WIDTH;
		
		return columnHeaderWidth;
	}
	
	private Project getProject()
	{
		return getMainWindow().getProject();
	}
		
	private ColumnWidthSaver columnWidthSaver;
	private ColumnSequenceSaver columnSequenceSaver;
}
