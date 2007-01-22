/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.umbrella;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.Collections;
import java.util.Vector;

import javax.swing.JTable;

import org.conservationmeasures.eam.objects.EAMObject;
import org.martus.swing.UiTable.UiTableHeader;

public class LegacyObjectPoolTablePanelSortListener  extends MouseAdapter
{	
	public void mouseClicked(MouseEvent e) 
	{
		UiTableHeader tableHeader = (UiTableHeader)e.getSource();
		JTable table = tableHeader.getTable();
		int clickedColumn = tableHeader.columnAtPoint(e.getPoint());
		int sortColumn = tableHeader.getTable().convertColumnIndexToModel(clickedColumn);
		
		LegacyObjectPoolTableModel annotationTableModel = (LegacyObjectPoolTableModel)table.getModel();
		sortTable(sortColumn, annotationTableModel);

		table.revalidate();
		table.repaint();
	}


	private void sortTable(int sortColumn, LegacyObjectPoolTableModel annotationTableModel)
	{
		EAMObject[] originalRows = annotationTableModel.getEAMObjectRows();
		EAMObject[] rowsToSort = new EAMObject[originalRows.length];
		System.arraycopy(rowsToSort, 0, originalRows, 0, rowsToSort.length);
		Arrays.sort(rowsToSort,  new EAMObjectComparator(annotationTableModel, sortColumn));
		
		if ( toggle(sortColumn) )  
		{
			Vector list = new Vector(Arrays.asList(rowsToSort));
			Collections.reverse(list);
			rowsToSort = (EAMObject[])list.toArray(new EAMObject[0]);
		}
		
		annotationTableModel.setEAMObjectRows(rowsToSort);
	}
	
	public  boolean toggle(int sortColumn) {
		sortToggle = !sortToggle;
		return sortToggle;
	}

	boolean sortToggle;

}
