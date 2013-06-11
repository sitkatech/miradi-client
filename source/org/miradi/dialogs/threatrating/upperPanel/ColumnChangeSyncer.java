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
package org.miradi.dialogs.threatrating.upperPanel;

import javax.swing.JTable;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

public class ColumnChangeSyncer implements TableColumnModelListener
{

	public ColumnChangeSyncer(JTable otherTableToUse)
	{
		otherTable = otherTableToUse; 
	}

	public void columnMarginChanged(ChangeEvent event)
	{
		TableColumnModel model = (TableColumnModel) event.getSource();
		syncPreferredColumnWidths(model);
	}

	public void syncPreferredColumnWidths(TableColumnModel model)
	{
		int columnCount = model.getColumnCount();
		for (int i = 0; i < columnCount; ++i)
		{
			TableColumn tableColumn = model.getColumn(i);
			TableColumn columnToAdjust = otherTable.getColumnModel().getColumn(i);
			columnToAdjust.setPreferredWidth(tableColumn.getPreferredWidth());
		}
	}

	public void columnAdded(TableColumnModelEvent event)
	{
	}
	
	public void columnMoved(TableColumnModelEvent event)
	{
		otherTable.getColumnModel().moveColumn(event.getFromIndex(), event.getToIndex());
	}

	public void columnRemoved(TableColumnModelEvent event)
	{
	}

	public void columnSelectionChanged(ListSelectionEvent events)
	{
	}
	
	private JTable otherTable;
}
