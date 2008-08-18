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
package org.miradi.dialogs.tablerenderers;

import java.awt.Color;

import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.text.html.StyleSheet;

import org.martus.swing.HyperlinkHandler;
import org.miradi.diagram.renderers.FactorHtmlViewer;
import org.miradi.dialogs.fieldComponents.HtmlFormViewer;
import org.miradi.main.MainWindow;

public class MultiLineObjectTableCellRendererFactory extends
		ObjectTableCellRendererFactory implements TableCellPreferredHeightProvider
{
	public MultiLineObjectTableCellRendererFactory(MainWindow mainWindowToUse, RowColumnBaseObjectProvider providerToUse, FontForObjectTypeProvider fontProviderToUse)
	{
		super(providerToUse, fontProviderToUse);
		
		rendererComponent = new TableCellHtmlRendererComponent(mainWindowToUse, null);
	}
	
	public JComponent getRendererComponent(JTable table, boolean isSelected, boolean hasFocus, int row, int tableColumn, Object value)
	{
		String html = getAsHtmlText(value);
		return getRendererComponent(table, isSelected, hasFocus, row, tableColumn, html);
	}
	
	public HtmlFormViewer getRendererComponent(JTable table, boolean isSelected, boolean hasFocus, int row, int tableColumn, String html)
	{
		rendererComponent.setText(html);
		return rendererComponent;
	}

	public int getPreferredHeight(JTable table, int row, int column, Object value)
	{
		HtmlFormViewer viewer = (HtmlFormViewer)getRendererComponent(table, false, false, row, column, value);

		int columnWidth = table.getCellRect(row, column, false).width;
		int preferredHeight = viewer.getPreferredHeight(columnWidth);
		return preferredHeight;
	}
	
	class TableCellHtmlRendererComponent extends HtmlFormViewer
	{
		public TableCellHtmlRendererComponent(MainWindow mainWindowToUse, HyperlinkHandler hyperLinkHandler)
		{
			super(mainWindowToUse, "", hyperLinkHandler);
		}

		@Override
		protected void customizeStyleSheet(StyleSheet style)
		{
			super.customizeStyleSheet(style);
			Color color = getCellBackgroundColor();
			style.addRule(makeSureRuleHasRightPrefix("body {background-color:"+FactorHtmlViewer.convertColorToHTMLColor(color)+";}"));
		}
	}

	private TableCellHtmlRendererComponent rendererComponent;

}
