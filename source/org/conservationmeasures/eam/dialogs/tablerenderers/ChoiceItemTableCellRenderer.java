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

import org.conservationmeasures.eam.icons.ColoredIcon;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.questions.ChoiceItem;

public class ChoiceItemTableCellRenderer extends TableCellRendererForObjects
{
	public ChoiceItemTableCellRenderer(RowColumnBaseObjectProvider providerToUse, FontForObjectTypeProvider fontProviderToUse)
	{
		super(providerToUse, fontProviderToUse);
		emptyChoiceItem = new ChoiceItem("", "", Color.WHITE);
		icon = new ColoredIcon();
	}

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int tableColumn)
	{
		JLabel renderer = (JLabel)super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, tableColumn);
		ChoiceItem choice = getChoiceItem(value);
		if(!isSelected)
			renderer.setBackground(getBackgroundColor(choice));
		renderer.setText(choice.getLabel());
		icon.setColor(choice.getColor());
		renderer.setIcon(icon);
		return renderer;
	}
	
	protected Color getBackgroundColor(ChoiceItem choice)
	{
		return choice.getColor();
	}
	
	protected ChoiceItem getChoiceItem(Object value)
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

	protected ChoiceItem emptyChoiceItem;
	private ColoredIcon icon;
}
