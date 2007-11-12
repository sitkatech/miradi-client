/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.tablerenderers;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.questions.ChoiceItem;

public class ChoiceItemTableCellRenderer extends TableCellRendererForObjects
{
	public ChoiceItemTableCellRenderer(RowBaseObjectProvider providerToUse, FontForObjectTypeProvider fontProviderToUse)
	{
		super(providerToUse, fontProviderToUse);
		emptyChoiceItem = new ChoiceItem("", "", Color.WHITE);
	}

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int tableColumn)
	{
		JLabel renderer = (JLabel)super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, tableColumn);
		ChoiceItem choice = getChoiceItem(value);
		renderer.setBackground(choice.getColor());
		renderer.setText(choice.getLabel());
		return renderer;
	}
	
	ChoiceItem getChoiceItem(Object value)
	{
		if(value == null || value.equals(""))
			return emptyChoiceItem;
		if(value instanceof String)
		{
			EAM.logError("Expected ChoiceItem, not: " + value);
			return emptyChoiceItem;
		}
		return (ChoiceItem)value;
	}

	ChoiceItem emptyChoiceItem;
}
