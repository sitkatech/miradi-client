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

import javax.swing.JLabel;
import javax.swing.JTable;

import org.miradi.objects.BaseObject;
import org.miradi.questions.ChoiceItem;

public class BudgetCostTreeTableCellRendererFactory extends NumericTableCellRendererFactory
{
	public BudgetCostTreeTableCellRendererFactory(RowColumnBaseObjectProvider providerToUse, FontForObjectTypeProvider fontProviderToUse)
	{
		super(providerToUse, fontProviderToUse);
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object rawValue, boolean isSelected, boolean hasFocus, int row, int tableColumn)
	{
		ChoiceItem choice = (ChoiceItem)rawValue;
		
		JLabel renderer = (JLabel)super.getTableCellRendererComponent(table, rawValue, isSelected, hasFocus, row, tableColumn);
		String text = annotateIfOverride(row, tableColumn, renderer, rawValue);
		renderer.setText(text);
		renderer.setIcon(choice.getIcon());
		
		return renderer;
	}
	
	private String annotateIfOverride(int row, int tableColumn, JLabel labelComponent, Object value)
	{
		if(value == null)
			return null;
		
		String baseText = value.toString();
		BaseObject object = getBaseObjectForRow(row, tableColumn);
		if(object == null)
			return baseText;
		
		return baseText;
	}
}
