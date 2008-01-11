/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.tablerenderers;

import java.awt.Color;
import java.awt.Component;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JTable;

import org.conservationmeasures.eam.icons.ColoredIcon;
import org.conservationmeasures.eam.questions.ChoiceItem;

public class ChoiceItemTableCellRenderer extends TableCellRendererForObjects
{
	public ChoiceItemTableCellRenderer(RowColumnBaseObjectProvider providerToUse, FontForObjectTypeProvider fontProviderToUse)
	{
		super(providerToUse, fontProviderToUse);
		icon = new ColoredIcon();
	}

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int tableColumn)
	{
		JLabel renderer = (JLabel)super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, tableColumn);
		String labelText = getLabelText(value);

		if(!isSelected)
			renderer.setBackground(getBackgroundColor(value));

		renderer.setText(labelText);
		Icon configuredIcon = getConfiguredIcon(value);
		renderer.setIcon(configuredIcon);
		return renderer;
	}

	private String getLabelText(Object value)
	{
		ChoiceItem choice = getChoiceItem(value);
		if(choice == null)
			return "";
		return choice.getLabel();
	}

	protected Icon getConfiguredIcon(Object value)
	{
		if(value == null)
			return null;
		Color color = getBackgroundColor(value);
		if(color == null)
			return null;
		return getConfiguredIcon(color);
	}
	
	private Icon getConfiguredIcon(Color color)
	{
		icon.setColor(color);
		return icon;
	}
	
	protected Color getBackgroundColor(Object value)
	{
		ChoiceItem choice = getChoiceItem(value);
		if(choice == null)
			return Color.white;
		return choice.getColor();
	}
	
	private ChoiceItem getChoiceItem(Object value)
	{
		if(! (value instanceof ChoiceItem) )
			return null;
		return (ChoiceItem)value;
	}

	private ColoredIcon icon;
}
