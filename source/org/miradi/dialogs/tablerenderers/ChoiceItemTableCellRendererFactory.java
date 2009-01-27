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

import java.awt.Color;
import java.awt.Component;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JTable;

import org.miradi.icons.ColoredIcon;
import org.miradi.questions.ChoiceItem;

public class ChoiceItemTableCellRendererFactory extends SingleLineObjectTableCellRendererFactory
{
	public ChoiceItemTableCellRendererFactory(RowColumnBaseObjectProvider providerToUse, FontForObjectTypeProvider fontProviderToUse)
	{
		this(providerToUse, fontProviderToUse, Color.WHITE);
	}
	
	public ChoiceItemTableCellRendererFactory(RowColumnBaseObjectProvider providerToUse, FontForObjectTypeProvider fontProviderToUse, Color defaultBackgroundColorToUse)
	{
		super(providerToUse, fontProviderToUse);
		icon = new ColoredIcon();
		setCellBackgroundColor(defaultBackgroundColorToUse);
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

	protected String getLabelText(Object value)
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
			return getCellBackgroundColor();
		
		return choice.getColor();
	}
	
	protected ChoiceItem getChoiceItem(Object value)
	{
		if(! (value instanceof ChoiceItem) )
			return null;
		return (ChoiceItem)value;
	}

	private ColoredIcon icon;
}
