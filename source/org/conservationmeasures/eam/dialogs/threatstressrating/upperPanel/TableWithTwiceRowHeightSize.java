/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.threatstressrating.upperPanel;

import javax.swing.table.TableModel;

import org.conservationmeasures.eam.utils.TableWithColumnWidthSaver;

abstract public class TableWithTwiceRowHeightSize extends TableWithColumnWidthSaver
{
	public TableWithTwiceRowHeightSize(TableModel model)
	{
		super(model);
		
		setRowHeight(getRowHeight() * 2);
	}
}
