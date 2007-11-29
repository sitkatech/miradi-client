/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.threatstressrating.upperPanel;

import java.awt.Dimension;

import javax.swing.JTable;

import org.conservationmeasures.eam.utils.TableWithColumnWidthSaver;

public class TargetSummaryRowTable extends TableWithColumnWidthSaver
{
	public TargetSummaryRowTable(TargetSummaryRowTableModel model)
	{
		super(model);
		
		setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		getTableHeader().setPreferredSize(new Dimension(0, 0));
		setForcedPreferredScrollableViewportWidth(PREFERRED_VIEWPORT_WIDTH);
	}
	
	public String getUniqueTableIdentifier()
	{
		return UNIQUE_IDENTIFIER;
	}

	public static final String UNIQUE_IDENTIFIER = "TargetSummaryRowTable";
	public static final int PREFERRED_VIEWPORT_WIDTH = 400;
}
