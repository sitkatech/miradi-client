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

import java.util.Comparator;

import javax.swing.table.TableModel;

import org.miradi.dialogs.base.AbstractObjectTableModel;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;

abstract public class SortableRowTable extends TableWithColumnWidthAndSequenceSaver
{
	public SortableRowTable(MainWindow mainWindowToUse, TableModel model, String uniqueTableIdentifierToUse)
	{
		super(mainWindowToUse, model, uniqueTableIdentifierToUse);
		
		rowSortController = new MultiTableRowSortController(getProject());
		
		//FIXME urgent: bubble up exception
		try
		{
			rowSortController.addTableToSort(this);
		}
		catch(Exception e)
		{
			EAM.logException(e);
		}
	}
	
	@Override
	public void dispose()
	{
		rowSortController.dispose();
		
		super.dispose();
	}
	
	public void sort(int sortByTableColumn) 
	{
		//FIXME urgent: this method is no longer used since sort column and direction are stored by a different mechanism.
		//Look at both callers of this method and verify that they can be removed.  
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
	
	private MultiTableRowSortController rowSortController;
}
