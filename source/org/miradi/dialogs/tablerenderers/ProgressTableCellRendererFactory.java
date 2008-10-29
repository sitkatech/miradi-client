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

import java.awt.Component;

import javax.swing.JTable;

import org.miradi.objects.BaseObject;
import org.miradi.objects.Strategy;

public class ProgressTableCellRendererFactory extends SingleLineObjectTableCellRendererFactory
{
	public ProgressTableCellRendererFactory(RowColumnBaseObjectProvider providerToUse, FontForObjectTypeProvider fontProviderToUse)
	{
		super(providerToUse, fontProviderToUse);
		
		choiceItemRendererFactory = new ChoiceItemTableCellRendererFactory(providerToUse, fontProviderToUse);
		singleLineRendererFactory = new SingleLineObjectTableCellRendererFactory(providerToUse, fontProviderToUse);		
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int tableColumn)
	{
		BaseObject baseObject = getBaseObjectForRow(row, tableColumn);
		if (isChoiceItemRow(baseObject))
			return choiceItemRendererFactory.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, tableColumn);
		
		return singleLineRendererFactory.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, tableColumn);	
	}

	private boolean isChoiceItemRow(BaseObject baseObject)
	{
		return Strategy.is(baseObject);
	}
	
	private ChoiceItemTableCellRendererFactory choiceItemRendererFactory;
	private SingleLineObjectTableCellRendererFactory singleLineRendererFactory;
}
