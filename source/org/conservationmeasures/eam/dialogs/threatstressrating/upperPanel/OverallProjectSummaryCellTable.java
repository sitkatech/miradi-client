/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.threatstressrating.upperPanel;

import java.awt.Dimension;

import javax.swing.JTable;
import javax.swing.table.TableModel;

public class OverallProjectSummaryCellTable extends AbstractTableWithChoiceItemRenderer
{
	public OverallProjectSummaryCellTable(TableModel model)
	{
		super(model);
		
		setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		getTableHeader().setPreferredSize(new Dimension(0, 0));
		setForcedPreferredScrollableViewportWidth(TargetThreatLinkTable.PREFERRED_VIEWPORT_WIDTH);
	}

	public String getUniqueTableIdentifier()
	{
		return "";
	}
}
