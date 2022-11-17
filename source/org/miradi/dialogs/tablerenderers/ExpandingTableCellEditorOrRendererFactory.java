/* 
Copyright 2005-2022, Foundations of Success, Bethesda, Maryland
on behalf of the Conservation Measures Partnership ("CMP").
Material developed between 2005-2013 is jointly copyright by Beneficent Technology, Inc. ("The Benetech Initiative"), Palo Alto, California.

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

import java.awt.*;

import javax.swing.JComponent;
import javax.swing.JTable;

import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.utils.AbstractPopupEditorComponent;
import org.miradi.utils.TableWithRowHeightSaver;
import org.miradi.utils.TextFieldPopupEditorComponent;

public class ExpandingTableCellEditorOrRendererFactory extends ObjectTableCellEditorOrRendererFactory
{
	public ExpandingTableCellEditorOrRendererFactory(MainWindow mainWindowToUse, RowColumnBaseObjectProvider providerToUse, FontForObjectProvider fontProviderToUse)
	{
		super(providerToUse, fontProviderToUse);
		
		rendererComponent = new TextFieldPopupEditorComponent(mainWindowToUse);
		rendererComponent.setStopEditingListener(this);
	}
	
	@Override
	public void dispose()
	{
		if (rendererComponent != null)
		{
			rendererComponent.dispose();
			rendererComponent = null;
		}
		
		super.dispose();
	}
	
	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int tableColumn)
	{
		Component editor = super.getTableCellEditorComponent(table, value, isSelected, row, tableColumn);
		((AbstractPopupEditorComponent)editor).setEditable(true);
		return editor;
	}

	@Override
	protected void updateBorderAndColors(JComponent renderer, JTable table, int row, int tableColumn, boolean isEditor, boolean isSelected)
	{
		renderer.setBorder(getCellBorder(row));
		if (isSelected)
		{
			Color fg = isEditor ? table.getSelectionForeground() : Color.WHITE;
			Color bg = isEditor ? EAM.EDITABLE_BACKGROUND_COLOR : table.getSelectionBackground();
			setColors(renderer, fg, bg);
		}
		else
		{
			Color fg = getCellForegroundColor(table, row, tableColumn);
			Color bg = isEditor ? EAM.EDITABLE_BACKGROUND_COLOR : getCellBackgroundColor();
			setColors(renderer, fg, bg);
		}
	}

	public void editingWasStoppedByComponent()
	{
		stopCellEditing();
		if(tableBeingEdited != null)
			tableBeingEdited.updateAutomaticRowHeights();
	}
	
	@Override
	public Object getCellEditorValue()
	{
		return rendererComponent.getText();
	}
	
	@Override
	public JComponent getRendererComponent(JTable table, boolean isSelected, boolean hasFocus, int row, int tableColumn, Object value)
	{
		if(value == null)
			value = "";
		String text = value.toString();
		rendererComponent.setText(text);
		return rendererComponent;
	}

	public int getPreferredHeight(JTable table, int row, int column, Object value)
	{
		int width = table.getCellRect(row, column, false).width;
		rendererComponent.setSize(new Dimension(width, Short.MAX_VALUE));
		rendererComponent.setText(value.toString());
		return rendererComponent.getPreferredSize().height;
	}

	private TextFieldPopupEditorComponent rendererComponent;
	private TableWithRowHeightSaver tableBeingEdited;
}
