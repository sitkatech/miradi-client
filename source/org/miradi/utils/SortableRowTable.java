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

import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;

import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;

import org.miradi.dialogs.base.AbstractObjectTableModel;
import org.miradi.main.MainWindow;

abstract public class SortableRowTable extends TableWithColumnWidthAndSequenceSaver implements SortableTable
{
	public SortableRowTable(MainWindow mainWindowToUse, TableModel model, String uniqueTableIdentifierToUse)
	{
		super(mainWindowToUse, model, uniqueTableIdentifierToUse);
		
		currentSortColumn = -1;
		
		enableClickToSortColumnHeaders();
	}

	private void enableClickToSortColumnHeaders()
	{
		JTableHeader columnHeader = getTableHeader();
		columnHeader.setReorderingAllowed(true);
		ColumnSortListener sortListener = new ColumnSortListener(this);
		columnHeader.addMouseListener(sortListener);
	}
	
	public void sort(int sortByTableColumn) 
	{
		Comparator comparator = getComparator(sortByTableColumn);
		Vector rows = new Vector();
		for(int i = 0; i < getRowCount(); ++i)
			rows.add(new Integer(i));

		Vector unsortedRows = (Vector)rows.clone();
		Collections.sort(rows, comparator);
		
		if (sortByTableColumn == currentSortColumn && rows.equals(unsortedRows))
			Collections.reverse(rows);

		getAbstractObjectTableModel().setNewRowOrder(((Integer[])rows.toArray(new Integer[0])));
		
		// TODO: Should memorize sort order for each table
		currentSortColumn = sortByTableColumn;
		
		revalidate();
		repaint();
	}

	protected Comparator getComparator(int sortByTableColumn)
	{
		int sortByModelColumn = convertColumnIndexToModel(sortByTableColumn);
		return getAbstractObjectTableModel().createComparator(sortByModelColumn);
	}
	
	private AbstractObjectTableModel getAbstractObjectTableModel()
	{
		return (AbstractObjectTableModel) getModel();
	}
	
	private int currentSortColumn;
}
