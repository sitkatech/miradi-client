/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.tablerenderers;

import java.awt.Component;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JTable;

import org.miradi.objects.BaseObject;
import org.miradi.objects.Task;

public class TableCellRendererForObjects extends BasicTableCellRenderer
{
	public TableCellRendererForObjects(RowColumnBaseObjectProvider providerToUse, FontForObjectTypeProvider fontProviderToUse)
	{
		objectProvider = providerToUse;
		fontProvider = fontProviderToUse;
	}

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int tableColumn)
	{
		JLabel renderer = (JLabel)super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, tableColumn);
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
