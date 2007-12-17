/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.threatstressrating.upperPanel;

import javax.swing.table.TableModel;

import org.conservationmeasures.eam.dialogs.tablerenderers.ChoiceItemTableCellRendererWithGrayCells;
import org.conservationmeasures.eam.dialogs.tablerenderers.DefaultFontProvider;

abstract public class AbstractTableWithChoiceItemRenderer extends TableWithTwiceRowHeightSize
{
	public AbstractTableWithChoiceItemRenderer(TableModel model)
	{
		super(model);
		
		setColumnRenderers();
	}

	private void setColumnRenderers()
	{
		MainThreatTableModel model = (MainThreatTableModel) getModel();
		ChoiceItemTableCellRendererWithGrayCells renderer = new ChoiceItemTableCellRendererWithGrayCells(model, new DefaultFontProvider());
		for (int i = 0; i < getColumnCount(); ++i)
		{
			getColumnModel().getColumn(i).setCellRenderer(renderer);
		}
	}
}
