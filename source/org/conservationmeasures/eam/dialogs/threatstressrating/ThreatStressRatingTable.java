/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.threatstressrating;

import org.conservationmeasures.eam.utils.TableWithColumnWidthSaver;

public class ThreatStressRatingTable extends TableWithColumnWidthSaver
{
	public ThreatStressRatingTable(ThreatStressRatingTableModel threatStressRatingTableModel)
	{
		super(threatStressRatingTableModel);
	}

	public String getUniqueTableIdentifier()
	{
		return UNIQUE_IDENTIFIER;
	}

	public static final String UNIQUE_IDENTIFIER = "ThreatStressRatingTable";
}
