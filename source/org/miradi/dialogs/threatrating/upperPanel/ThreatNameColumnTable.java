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

import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JTable;

import org.miradi.dialogs.tablerenderers.ChoiceItemTableCellRendererFactory;
import org.miradi.dialogs.tablerenderers.DefaultFontProvider;
import org.miradi.dialogs.tablerenderers.FontForObjectTypeProvider;
import org.miradi.dialogs.tablerenderers.MultiLineObjectTableCellRendererFactory;
import org.miradi.dialogs.tablerenderers.RowColumnBaseObjectProvider;
import org.miradi.icons.DirectThreatIcon;
import org.miradi.main.MainWindow;


public class ThreatNameColumnTable extends TableWhoseScrollPaneAlwaysExactlyFits
{
	public ThreatNameColumnTable(MainWindow mainWindowToUse, MainThreatTableModel tableModel)
	{
		super(mainWindowToUse, tableModel, UNIQUE_IDENTIFIER);
		
		iconCellRendererFactory = new BorderlessChoiceItemCellRendererFactory(tableModel, new DefaultFontProvider(getMainWindow()));
		getColumnModel().getColumn(ThreatNameColumnTableModel.THREAT_ICON_COLUMN_INDEX).setCellRenderer(iconCellRendererFactory);
		setColumnWidth(ThreatNameColumnTableModel.THREAT_ICON_COLUMN_INDEX, new DirectThreatIcon().getIconWidth() * 2);
		
		textCellRendererFactory = new BorderlessMultilineCellRendererFactory(tableModel, new DefaultFontProvider(getMainWindow()));
		getColumnModel().getColumn(ThreatNameColumnTableModel.THREAT_NAME_COLUMN_INDEX).setCellRenderer(textCellRendererFactory);
		
		setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		getTableHeader().setReorderingAllowed(false);
		setShowVerticalLines(false);
	}

	@Override
	public boolean shouldSaveColumnSequence()
	{
		return false;
	}

	public static final String UNIQUE_IDENTIFIER = "ThreatsTable"; 

	private BorderlessChoiceItemCellRendererFactory iconCellRendererFactory;
	private BorderlessMultilineCellRendererFactory textCellRendererFactory;
}

class BorderlessChoiceItemCellRendererFactory extends ChoiceItemTableCellRendererFactory
{
	public BorderlessChoiceItemCellRendererFactory(
			RowColumnBaseObjectProvider providerToUse,
			FontForObjectTypeProvider fontProviderToUse)
	{
		super(providerToUse, fontProviderToUse);
	}
	
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int tableColumn)
	{
		final Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus,
				row, tableColumn);
		((JComponent)component).setBorder(BorderFactory.createEmptyBorder());
		return component;
	}
}

class BorderlessMultilineCellRendererFactory extends MultiLineObjectTableCellRendererFactory
{
	public BorderlessMultilineCellRendererFactory(
			RowColumnBaseObjectProvider providerToUse,
			FontForObjectTypeProvider fontProviderToUse)
	{
		super(providerToUse, fontProviderToUse);
	}
	
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int tableColumn)
	{
		final Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus,
				row, tableColumn);
		((JComponent)component).setBorder(BorderFactory.createEmptyBorder());
		return component;
	}
	
}

