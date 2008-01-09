/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objecthelpers;

import java.util.Vector;

public class TncOperatingUnitsFileLoader extends TwoLevelFileLoader
{
	public TncOperatingUnitsFileLoader(String fileNameToUse)
	{
		super(fileNameToUse);
	}

	protected Vector processVector(Vector fileVector)
	{
		Vector entries = new Vector();
		for (int i  = 0; i < fileVector.size(); ++i)
		{
			Vector row = (Vector) fileVector.get(i);
			String operatingUnitCode = (String) row.get(0);
			String operatingUnitSourceName = (String) row.get(1);
		
			entries.add(new TwoLevelEntry(operatingUnitCode, operatingUnitSourceName));
		}
		return entries;
	}
}
