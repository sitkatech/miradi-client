/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.threatstressrating.upperPanel;


//TODO rename TheartNameColumnTable
public class ThreatTable extends TableWithTwiceRowHeightSize
{
	public ThreatTable(MainThreatTableModel tableModel)
	{
		super(tableModel);
	}
		
	public String getUniqueTableIdentifier()
	{
		return UNIQUE_IDENTIFIER;
	}

	public static final String UNIQUE_IDENTIFIER = "ThreatsTable"; 
}
