/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.tablerenderers;

import java.awt.Component;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JTable;

import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.Task;

public class TableCellRendererForObjects extends BasicTableCellRenderer
{
	public TableCellRendererForObjects(RowBaseObjectProvider providerToUse, FontForObjectTypeProvider fontProviderToUse)
	{
		objectProvider = providerToUse;
		fontProvider = fontProviderToUse;
	}

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int tableColumn)
	{
		JLabel renderer = (JLabel)super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, tableColumn);
		Font font = getCellFont(row);
		if(isSharedObject(row))
			font = font.deriveFont(Font.ITALIC);
		renderer.setFont(font);
		return renderer;
	}
	
	public Font getCellFont(int row)
	{
		BaseObject object = getObjectProvider().getBaseObjectForRow(row);
		if(object == null)
			return fontProvider.getPlainFont();
		return fontProvider.getFont(object.getType());
	}
	
	boolean isSharedObject(int row)
	{
		BaseObject object = getBaseObjectForRow(row);
		if(object == null)
			return false;
		
		if(object.getType() != Task.getObjectType())
			return false;
		
		Task task = (Task)object;
		return task.isShared();
	}

	protected BaseObject getBaseObjectForRow(int row)
	{
		BaseObject object = objectProvider.getBaseObjectForRow(row);
		return object;
	}
	
	protected RowBaseObjectProvider getObjectProvider()
	{
		return objectProvider;
	}

	private RowBaseObjectProvider objectProvider;
	private FontForObjectTypeProvider fontProvider;
}
