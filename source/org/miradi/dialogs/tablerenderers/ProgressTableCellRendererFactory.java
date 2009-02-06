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
import javax.swing.JTable;

import org.miradi.objects.BaseObject;
import org.miradi.objects.Indicator;
import org.miradi.objects.Strategy;
import org.miradi.objects.Task;

public class ProgressTableCellRendererFactory extends ObjectTableCellRendererFactory
{
	public ProgressTableCellRendererFactory(RowColumnBaseObjectProvider providerToUse, FontForObjectTypeProvider fontProviderToUse)
	{
		super(providerToUse, fontProviderToUse);
		
		choiceItemRendererFactory = new ChoiceItemTableCellRendererFactory(providerToUse, fontProviderToUse);
		singleLineRendererFactory = new SingleLineObjectTableCellRendererFactory(providerToUse, fontProviderToUse);		
	}

	@Override
	public JComponent getRendererComponent(JTable table, boolean isSelected, boolean hasFocus, int row, int tableColumn, Object value)
	{	
		return getRendererFactory(row, tableColumn).getRendererComponent(table, isSelected, hasFocus, row, tableColumn, value);
	}
	
	public int getPreferredHeight(JTable table, int row, int column, Object value)
	{
		return getRendererFactory(row, column).getPreferredHeight(table, row, column, value);
	}	
	
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int tableColumn)
	{
		return getRendererFactory(row, tableColumn).getTableCellRendererComponent(table, value, isSelected, hasFocus, row, tableColumn);
	}
	
	private ObjectTableCellRendererFactory getRendererFactory(int row, int column)
	{
		BaseObject baseObject = getBaseObjectForRow(row, column);
		if (baseObject != null && isChoiceItemRow(baseObject))
			return choiceItemRendererFactory;
		
		return singleLineRendererFactory;
	}

	private boolean isChoiceItemRow(BaseObject baseObject)
	{
		if (Strategy.is(baseObject))
			return true;
		
		if (Indicator.is(baseObject))
			return true;
		
		if (Task.isActivity(baseObject))
			return true;
		
		return false;
	}
	
	private ChoiceItemTableCellRendererFactory choiceItemRendererFactory;
	private SingleLineObjectTableCellRendererFactory singleLineRendererFactory;
}
