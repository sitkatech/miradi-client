/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.threatmatrix;

import javax.swing.JScrollPane;
import javax.swing.JTable;

import org.conservationmeasures.eam.utils.FastScrollPane;

public class ScrollPaneWithTableAndRowHeader extends FastScrollPane
{
	public ScrollPaneWithTableAndRowHeader(JTable rowHeaderTableToUse, JTable table)
	{
		super(table);
		setRowHeaderView(rowHeaderTableToUse);
		setCorner(JScrollPane.UPPER_LEFT_CORNER, rowHeaderTableToUse.getTableHeader());
		setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
	}

}
