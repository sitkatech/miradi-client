/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.treetables;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;

import org.conservationmeasures.eam.dialogs.fieldComponents.TreeNodeForRowProvider;
import org.conservationmeasures.eam.utils.TableWithTreeTableNodes;

public class AstrickRenderer extends TableCellRendererWithCustomAlignedFontText
{
	public AstrickRenderer(TableWithTreeTableNodes tableToUse)
	{
		super(tableToUse, tableToUse.getColumnAlignment());
		nodeProvider = tableToUse;
	}

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
	{
		JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		addAstrickToTaskRows(nodeProvider.getNodeForRow(row), label);

		return label;
	}
	
	TreeNodeForRowProvider nodeProvider;
}

