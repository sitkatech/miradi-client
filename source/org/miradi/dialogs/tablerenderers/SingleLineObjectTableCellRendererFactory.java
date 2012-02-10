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
package org.miradi.dialogs.tablerenderers;

import java.awt.Component;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

import org.miradi.dialogs.fieldComponents.PanelTextArea;
import org.miradi.utils.HtmlUtilities;

public class SingleLineObjectTableCellRendererFactory extends ObjectTableCellEditorOrRendererFactory
{
	public SingleLineObjectTableCellRendererFactory(RowColumnBaseObjectProvider providerToUse, FontForObjectProvider fontProviderToUse)
	{
		super(providerToUse, fontProviderToUse);
		
		rendererComponent = new HtmlEncodedCellRenderer();
		editorComponent = new HtmlEncodedTextArea();
	}
	
	@Override
	public JComponent getRendererComponent(JTable table, boolean isSelected, boolean hasFocus, int row, int tableColumn, Object value)
	{
		JLabel renderer = (JLabel)rendererComponent.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, tableColumn);
		renderer.setVerticalAlignment(SwingConstants.TOP);
		return renderer;
	}

	public int getPreferredHeight(JTable table, int row, int column, Object value)
	{
		Component component = rendererComponent.getTableCellRendererComponent(table, value, false, false, row, column);
		return component.getPreferredSize().height;
	}
	
	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int tableColumn)
	{
		updateBorderAndColors(editorComponent, table, row, tableColumn, isSelected);
		editorComponent.setFont(getCellFont(row, tableColumn));
		editorComponent.setText(value.toString());
		
		return editorComponent;
	}
	
	@Override
	public Object getCellEditorValue()
	{
		return editorComponent.getText();
	}
	
	private class HtmlEncodedTextArea extends PanelTextArea
	{
		@Override
		public void setText(String text)
		{
			text = HtmlUtilities.convertToNonHtml(text);
			
			super.setText(text);
		}
		
		@Override
		public String getText()
		{
			String text = super.getText();
			text = HtmlUtilities.convertToHtmlText(text);
			
			return text;
		}
	}
	
	private class HtmlEncodedCellRenderer extends DefaultTableCellRenderer
	{
		@Override
		public void setText(String text)
		{
			text = HtmlUtilities.convertToNonHtml(text);
			super.setText(text);
		}
	}

	private DefaultTableCellRenderer rendererComponent;
	private HtmlEncodedTextArea editorComponent;
}
