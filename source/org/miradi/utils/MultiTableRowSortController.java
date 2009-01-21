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

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.swing.JTable;
import javax.swing.table.JTableHeader;

import org.miradi.dialogs.threatrating.upperPanel.AbstractThreatTargetTableModel;
import org.miradi.objects.Factor;

public class MultiTableRowSortController
{
	public MultiTableRowSortController()
	{
		tablesToSort = new Vector();
	}
	
	public void addTableToSort(TableWithRowHeightSaver tableToSort)
	{
		JTableHeader columnHeader = tableToSort.getTableHeader();
		ColumnSortListener sortListener = new ColumnSortListener();
		columnHeader.addMouseListener(sortListener);
		tablesToSort.add(tableToSort);
	}
	
	class ColumnSortListener extends MouseAdapter
	{
		public void mouseClicked(MouseEvent event) 
		{
			JTableHeader tableHeader = ((JTableHeader)event.getSource());
			int clickedTableColumn = tableHeader.columnAtPoint(event.getPoint());
			if (clickedTableColumn >= 0)
				sortByTableColumn(tableHeader.getTable(), clickedTableColumn);
		}

		private void sortByTableColumn(JTable tableClickedOn, int sortByTableColumn)
		{
			AbstractThreatTargetTableModel model = (AbstractThreatTargetTableModel)tableClickedOn.getModel();
			Factor[] sortedThreats = model.getThreatsSortedBy(tableClickedOn.convertColumnIndexToModel(sortByTableColumn));
			
			for (int index = 0; index < tablesToSort.size(); ++index)
			{
				TableWithRowHeightSaver table = tablesToSort.get(index);
				AbstractThreatTargetTableModel modelToSetThreats = (AbstractThreatTargetTableModel)table.getModel();
				modelToSetThreats.setThreats(sortedThreats);
				table.updateAutomaticRowHeights();
				table.revalidate();
				table.repaint();
			}
		}
	}
	
	private Vector<TableWithRowHeightSaver> tablesToSort;
}
