/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.threatstressrating.upperPanel;

import org.conservationmeasures.eam.utils.TableWithColumnWidthSaver;


public class ThreatTable extends TableWithColumnWidthSaver
{
	public ThreatTable(MainThreatTableModel tableModel)
	{
		super(tableModel);
		setForcedPreferredScrollableViewportWidth(100);
		setForcedPreferredScrollableViewportHeight(TargetThreatLinkTable.PREFERRED_VIEWPORT_HEIGHT);
	}
	
	public String getUniqueTableIdentifier()
	{
		return UNIQUE_IDENTIFIER;
	}

	public static final String UNIQUE_IDENTIFIER = "ThreatsTable"; 
}
