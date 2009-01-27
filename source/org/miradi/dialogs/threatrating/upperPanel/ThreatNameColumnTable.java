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
package org.miradi.dialogs.threatrating.upperPanel;

import javax.swing.JTable;

import org.miradi.dialogs.tablerenderers.ChoiceItemTableCellRendererFactory;
import org.miradi.dialogs.tablerenderers.DefaultFontProvider;
import org.miradi.dialogs.tablerenderers.MultiLineObjectTableCellRendererFactory;
import org.miradi.icons.DirectThreatIcon;
import org.miradi.main.MainWindow;


public class ThreatNameColumnTable extends TableWhoseScrollPaneAlwaysExactlyFits
{
	public ThreatNameColumnTable(MainWindow mainWindowToUse, MainThreatTableModel tableModel)
	{
		super(mainWindowToUse, tableModel, UNIQUE_IDENTIFIER);
		
		getColumnModel().getColumn(ThreatNameColumnTableModel.THREAT_ICON_COLUMN_INDEX).setCellRenderer(new ChoiceItemTableCellRendererFactory(tableModel, new DefaultFontProvider(getMainWindow())));
		setColumnWidth(ThreatNameColumnTableModel.THREAT_ICON_COLUMN_INDEX, new DirectThreatIcon().getIconWidth() * 2);
		
		getColumnModel().getColumn(ThreatNameColumnTableModel.THREAT_NAME_COLUMN_INDEX).setCellRenderer(new MultiLineObjectTableCellRendererFactory(tableModel, new DefaultFontProvider(getMainWindow())));
		
		setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		getTableHeader().setReorderingAllowed(false);
	}
	
	@Override
	public boolean shouldSaveColumnSequence()
	{
		return false;
	}
		
	public static final String UNIQUE_IDENTIFIER = "ThreatsTable"; 
}
