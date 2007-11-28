/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.threatstressrating.upperPanel;

import java.awt.Dimension;

import org.conservationmeasures.eam.utils.TableWithColumnWidthSaver;

public class TargetSummaryRowTable extends TableWithColumnWidthSaver
{
	public TargetSummaryRowTable(TargetSummaryRowTableModel model)
	{
		super(model);
		
		getTableHeader().setPreferredSize(new Dimension(1, 0)); 
	}
	
	public String getUniqueTableIdentifier()
	{
		return UNIQUE_IDENTIFIER;
	}

	public static final String UNIQUE_IDENTIFIER = "TargetSummaryRowTable";
}
