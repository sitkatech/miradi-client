package org.conservationmeasures.eam.utils;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import org.conservationmeasures.eam.views.GenericTreeTableModel;
import org.conservationmeasures.eam.views.TreeTableWithColumnWidthSaving;

public class ColumnWidthSaver extends MouseAdapter
{
	public ColumnWidthSaver(TreeTableWithColumnWidthSaving tableToUse)
	{
		table = tableToUse;
	}

	public void mouseReleased(MouseEvent e)
	{
		saveColumnWidths();
	}
	
	private void saveColumnWidths()
	{
		for (int i = 0; i < model.getColumnCount(); ++i)
		{		
			model.getColumnTag(i);	
			//FIXME save each width
		}
	}

	//TODO make var into private.
	TreeTableWithColumnWidthSaving table;
	private GenericTreeTableModel model;
}
