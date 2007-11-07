/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.treetables;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;

import org.conservationmeasures.eam.dialogs.viability.TargetViabilityTreeTable;
import org.conservationmeasures.eam.questions.ChoiceItem;

public class ChoiceItemRenderer extends TreeTableCellRendererWithColor
{
	public ChoiceItemRenderer(TreeTableWithIcons treeTableToUse)
	{
		super(treeTableToUse);
	}
	
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int tableColumn)
	{
		Component renderer = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, tableColumn);
		TargetViabilityTreeTable targetViabilityTreeTable = (TargetViabilityTreeTable) table;
		String columnTag = targetViabilityTreeTable.getColumnTag(tableColumn);
		Color color = getBackgroundColor(columnTag);
		if(value != null && !value.equals(""))
			color = ((ChoiceItem) value).getColor();

		renderer.setBackground(color);
		if(isSelected)
			renderer.setBackground(table.getSelectionBackground());
		
		return renderer;
	}
}
