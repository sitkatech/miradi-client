/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.threatstressrating.upperPanel;

import javax.swing.table.TableModel;

import org.miradi.dialogs.tablerenderers.ChoiceItemTableCellRendererWithGrayCells;
import org.miradi.dialogs.tablerenderers.DefaultFontProvider;
import org.miradi.utils.TableWithRowHeightSaver;

abstract public class AbstractTableWithChoiceItemRenderer extends TableWithRowHeightSaver
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
