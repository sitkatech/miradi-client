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
package org.miradi.views.threatmatrix;

import javax.swing.JScrollPane;
import javax.swing.JTable;

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
	}

}
