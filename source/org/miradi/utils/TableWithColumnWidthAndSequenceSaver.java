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

import javax.swing.table.TableModel;

import org.miradi.main.EAM;
import org.miradi.main.MainWindow;

abstract public class TableWithColumnWidthAndSequenceSaver extends TableWithRowHeightSaver
{
	public TableWithColumnWidthAndSequenceSaver(MainWindow mainWindowToUse, TableModel model, String uniqueTableIdentifierToUse)
	{
		super(mainWindowToUse, model, uniqueTableIdentifierToUse);
	
		addColumnWidthSaver();
		addColumnSequenceSaver();
	}
	
	private void addColumnWidthSaver()
	{
		if (! shouldSaveColumnWidth())
			return; 
		
		columnWidthSaver = new ColumnWidthSaver(getMainWindow().getProject(), this, (ColumnTagProvider)getModel(), getUniqueTableIdentifier());
		getTableHeader().addMouseListener(columnWidthSaver);
		columnWidthSaver.restoreColumnWidths();
	}
	
	private void addColumnSequenceSaver()
	{
		if (! shouldSaveColumnSequence())
			return;
		
		try
		{
			columnSequenceSaver = new ColumnSequenceSaver(getMainWindow().getProject(), this, (ColumnTagProvider)getModel(), getUniqueTableIdentifier());
			getTableHeader().addMouseListener(columnSequenceSaver);
			columnSequenceSaver.restoreColumnSequences();
		}
		catch (Exception e)
		{
			EAM.logException(e);
			//TODO when storing column sequence is finished try throwing further up this exception
		}
	}
	
	protected void restoreWidthsAndSequence() throws Exception
	{
		if(columnWidthSaver != null)
			columnWidthSaver.restoreColumnWidths();
		if(columnSequenceSaver != null)
			columnSequenceSaver.restoreColumnSequences();
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
