/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.reports;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableModel;

import org.conservationmeasures.eam.utils.TableWithRowHeightSaver;

public class ReportSelectionTable extends TableWithRowHeightSaver
{
	public ReportSelectionTable(TableModel model)
	{
		super(model);
		
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	}

	@Override
	public String getUniqueTableIdentifier()
	{
		return "ReportSelectionTable";
	}
}
