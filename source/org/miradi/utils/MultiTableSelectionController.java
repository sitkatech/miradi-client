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

import java.util.Vector;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class MultiTableSelectionController implements ListSelectionListener
{
	public MultiTableSelectionController(MultiTableSelectionChangingListener selectionChangingListenerToUse)
	{
		selectionChangingListener = selectionChangingListenerToUse;
		startPropagatingSelections();
	}
	
	public void addTable(JTable tableToAdd)
	{
		tables.add(tableToAdd);
		tableToAdd.getSelectionModel().addListSelectionListener(this);
	}

	public void valueChanged(ListSelectionEvent event)
	{
		if (!shouldPropagateSelections())
			return;
		
		selectionChangingListener.beginSelectionChangingProcess();
		stopPropagatingSelections();
		try
		{
			adjustSelectionOfAllTables(event);
		}
		finally
		{
			selectionChangingListener.endSelectionChangingProcess();
			startPropagatingSelections();
		}
	}

	private void adjustSelectionOfAllTables(ListSelectionEvent event)
	{
		ListSelectionModel source = (ListSelectionModel) event.getSource();
		int selectedRow  = source.getMinSelectionIndex();
		for (int i = 0; i < tables.size(); ++i)
		{
			JTable table = tables.get(i);

			ListSelectionModel selectionModel = table.getSelectionModel();
			if(selectedRow == -1)
				selectionModel.clearSelection();
			else
				selectionModel.setSelectionInterval(selectedRow, selectedRow);
		}
	}

	private boolean shouldPropagateSelections()
	{
		return propagateSelectionsFlag;
	}
	
	private void startPropagatingSelections()
	{
		propagateSelectionsFlag = true;
	}
	
	private void stopPropagatingSelections()
	{
		propagateSelectionsFlag = false;
	}

	private Vector<JTable> tables = new Vector<JTable>();
	private MultiTableSelectionChangingListener selectionChangingListener;
	private boolean propagateSelectionsFlag;
}
