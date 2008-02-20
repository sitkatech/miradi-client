/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.tablerenderers;

import java.awt.Color;
import java.awt.Component;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JTable;

import org.miradi.icons.ColoredIcon;
import org.miradi.questions.ChoiceItem;

public class ChoiceItemTableCellRenderer extends TableCellRendererForObjects
{
	public ChoiceItemTableCellRenderer(RowColumnBaseObjectProvider providerToUse, FontForObjectTypeProvider fontProviderToUse)
	{
		this(providerToUse, fontProviderToUse, Color.WHITE);
	}
	
	public ChoiceItemTableCellRenderer(RowColumnBaseObjectProvider providerToUse, FontForObjectTypeProvider fontProviderToUse, Color defaultBackgroundColorToUse)
	{
		super(providerToUse, fontProviderToUse);
		icon = new ColoredIcon();
		defaultBackgroundColor = defaultBackgroundColorToUse;
	}

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int tableColumn)
	{
		JLabel renderer = (JLabel)super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, tableColumn);
		String labelText = getLabelText(value);

		if(!isSelected)
			renderer.setBackground(getBackgroundColor(getChoiceItem(value)));

		renderer.setText(labelText);
		Icon configuredIcon = getConfiguredIcon(getChoiceItem(value));
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

	protected Icon getConfiguredIcon(ChoiceItem choice)
	{
		if(choice == null)
			return null;
		if(choice.getIcon() != null)
			return choice.getIcon();
		
		Color color = getBackgroundColor(choice);
		if(choice.getColor() == null)
			return null;
		return getConfiguredIcon(color);
	}
	
	private Icon getConfiguredIcon(Color color)
	{
		icon.setColor(color);
		return icon;
	}
	
	protected Color getBackgroundColor(ChoiceItem choice)
	{
		if(choice == null || choice.getColor() == null)
			return defaultBackgroundColor;
		return choice.getColor();
	}
	
	protected ChoiceItem getChoiceItem(Object value)
	{
		if(! (value instanceof ChoiceItem) )
			return null;
		return (ChoiceItem)value;
	}

	private ColoredIcon icon;
	private Color defaultBackgroundColor;
}
