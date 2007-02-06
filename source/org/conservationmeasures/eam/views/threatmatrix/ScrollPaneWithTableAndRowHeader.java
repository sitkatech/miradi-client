/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.threatmatrix;

import javax.swing.JScrollPane;
import javax.swing.JTable;

public class ScrollPaneWithTableAndRowHeader extends JScrollPane
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
