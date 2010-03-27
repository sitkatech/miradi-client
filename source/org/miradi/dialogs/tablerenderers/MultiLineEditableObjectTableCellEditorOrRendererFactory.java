/* 
Copyright 2005-2010, Foundations of Success, Bethesda, Maryland 
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

import java.awt.Dimension;

import javax.swing.JComponent;
import javax.swing.JTable;

import org.miradi.utils.TableWithRowHeightSaver;
import org.miradi.utils.TextFieldPopupEditorComponent;

public class MultiLineEditableObjectTableCellEditorOrRendererFactory extends ObjectTableCellEditorOrRendererFactory
{
	public MultiLineEditableObjectTableCellEditorOrRendererFactory(RowColumnBaseObjectProvider providerToUse, FontForObjectProvider fontProviderToUse)
	{
		super(providerToUse, fontProviderToUse);
		
		rendererComponent = new TextFieldPopupEditorComponent();
		rendererComponent.setStopEditingListener(this);
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

	protected TextFieldPopupEditorComponent getRendererComponent()
	{
		return rendererComponent;
	}
	
	private TextFieldPopupEditorComponent rendererComponent;
	private TableWithRowHeightSaver tableBeingEdited;
}
