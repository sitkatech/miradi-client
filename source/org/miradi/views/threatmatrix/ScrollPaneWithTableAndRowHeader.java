/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.views.threatmatrix;

import java.awt.Color;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.LineBorder;

import org.miradi.main.AppPreferences;
import org.miradi.utils.MiradiScrollPane;

public class ScrollPaneWithTableAndRowHeader extends MiradiScrollPane
{
	public ScrollPaneWithTableAndRowHeader(JTable rowHeaderTableToUse, JTable table)
	{
		super(table);
		setBackground(AppPreferences.getDataPanelBackgroundColor());
		getViewport().setBackground(AppPreferences.getDataPanelBackgroundColor());
		setRowHeaderView(rowHeaderTableToUse);
		setCorner(JScrollPane.UPPER_LEFT_CORNER, rowHeaderTableToUse.getTableHeader());
		setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		setBorder(new LineBorder(Color.RED));
	}

}
