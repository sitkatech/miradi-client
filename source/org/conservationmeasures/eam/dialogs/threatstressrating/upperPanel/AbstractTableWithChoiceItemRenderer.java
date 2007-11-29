/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.threatstressrating.upperPanel;

import javax.swing.table.TableModel;

import org.conservationmeasures.eam.utils.TableWithColumnWidthSaver;

abstract public class AbstractTableWithChoiceItemRenderer extends TableWithColumnWidthSaver
{
	public AbstractTableWithChoiceItemRenderer(TableModel model)
	{
		super(model);
		
		setColumnRenderers();
	}

	private void setColumnRenderers()
	{
		//FIXME add renderer here and use this class for 3 upper threat rating tables
		//ChoiceItemTableCellRenderer renderer = new ChoiceItemTableCellRenderer();
		for (int i = 0; i < getColumnCount(); ++i)
		{
			//getColumnModel().getColumn(i).setCellRenderer();
		}
	}
	
}
