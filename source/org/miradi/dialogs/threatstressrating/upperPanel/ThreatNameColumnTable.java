/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.threatstressrating.upperPanel;

import org.miradi.dialogs.tablerenderers.BasicTableCellRenderer;


public class ThreatNameColumnTable extends AbstractTableWithChoiceItemRenderer
{
	public ThreatNameColumnTable(MainThreatTableModel tableModel)
	{
		super(tableModel);
		getColumnModel().getColumn(0).setCellRenderer(new BasicTableCellRenderer());
	}
		
	public String getUniqueTableIdentifier()
	{
		return UNIQUE_IDENTIFIER;
	}

	public static final String UNIQUE_IDENTIFIER = "ThreatsTable"; 
}
