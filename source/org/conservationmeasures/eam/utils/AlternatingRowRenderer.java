/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.utils;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class AlternatingRowRenderer extends DefaultTableCellRenderer
{
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
	{
		Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		if (!isSelected)
		{
			component.setBackground(BACKGROUND_COLORS[row % 2]);
			component.setForeground(Color.BLACK);
		}
		
		return component;
	}
	
	private static final Color[] BACKGROUND_COLORS = new Color[] { new Color(0xf0, 0xf0, 0xf0), Color.WHITE, };
}
