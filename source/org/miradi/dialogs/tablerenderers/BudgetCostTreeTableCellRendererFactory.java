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

import org.miradi.dialogs.planning.upperPanel.PlanningUpperMultiTable;
import org.miradi.main.EAM;
import org.miradi.objects.BaseObject;
import org.miradi.project.CurrencyFormat;
import org.miradi.questions.ChoiceItem;

public class BudgetCostTreeTableCellRendererFactory extends NumericTableCellRendererFactory
{
	public BudgetCostTreeTableCellRendererFactory(PlanningUpperMultiTable providerToUse, FontForObjectTypeProvider fontProviderToUse, CurrencyFormat currencyFormatterToUse)
	{
		super(providerToUse, fontProviderToUse);
		currencyFormatter = currencyFormatterToUse;
	}

	public Component getTableCellRendererComponent(JTable table, Object rawValue, boolean isSelected, boolean hasFocus, int row, int tableColumn)
	{
		ChoiceItem choice = (ChoiceItem)rawValue;
		String value = formatCurrency(choice.getLabel());
		
		JLabel renderer = (JLabel)super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, tableColumn);
		String text = annotateIfOverride(row, tableColumn, renderer, value);
		renderer.setText(text);
		renderer.setIcon(choice.getIcon());
		
		return renderer;
	}
	
	public String formatCurrency(String costAsString)
	{
		try
		{
			double cost = toDouble(costAsString);
			if(cost == 0.0)
				return "";
			
			return currencyFormatter.format(cost);
		}
		catch(Exception e)
		{
			EAM.logException(e);
			return EAM.text("(Error)");
		}
	}
	
	private double toDouble(String valueAsString) throws Exception
	{
		if(valueAsString == null || valueAsString.length() == 0)
			return 0.0;
		
		return Double.parseDouble(valueAsString);
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
	
	private CurrencyFormat currencyFormatter;
}
