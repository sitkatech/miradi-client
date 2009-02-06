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
import java.awt.Font;

import javax.swing.JComponent;
import javax.swing.JTable;

import org.miradi.objects.BaseObject;
import org.miradi.objects.Task;

abstract public class ObjectTableCellRendererFactory extends BasicTableCellRendererFactory  implements TableCellPreferredHeightProvider
{
	public ObjectTableCellRendererFactory(RowColumnBaseObjectProvider providerToUse, FontForObjectTypeProvider fontProviderToUse)
	{
		objectProvider = providerToUse;
		fontProvider = fontProviderToUse;
		
	}

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int tableColumn)
	{
		JComponent renderer = (JComponent)super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, tableColumn);
		Font font = getCellFont(row, tableColumn);
		if(isSharedObject(row, tableColumn))
			font = font.deriveFont(Font.ITALIC);
		renderer.setFont(font);
		return renderer;
	}
	
	public Font getCellFont(int row, int column)
	{
		BaseObject object = getObjectProvider().getBaseObjectForRowColumn(row, column);
		if(object == null)
			return fontProvider.getPlainFont();
		return fontProvider.getFont(object.getType());
	}
	
	boolean isSharedObject(int row, int column)
	{
		BaseObject object = getBaseObjectForRow(row, column);
		if(object == null)
			return false;
		
		if(object.getType() != Task.getObjectType())
			return false;
		
		Task task = (Task)object;
		return task.isShared();
	}

	protected BaseObject getBaseObjectForRow(int row, int column)
	{
		BaseObject object = objectProvider.getBaseObjectForRowColumn(row, column);
		return object;
	}
	
	protected RowColumnBaseObjectProvider getObjectProvider()
	{
		return objectProvider;
	}

	private RowColumnBaseObjectProvider objectProvider;
	private FontForObjectTypeProvider fontProvider;
}
