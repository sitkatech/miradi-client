/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objecthelpers;

import java.util.Vector;

public class ManagingOfficeFileLoader extends TwoLevelFileLoader
{
	public ManagingOfficeFileLoader(String fileNameToUse)
	{
		super(fileNameToUse);
	}

	protected Vector processVector(Vector fileVector)
	{
		Vector entries = new Vector();
		for (int i  = 0; i < fileVector.size(); ++i)
		{
			Vector row = (Vector) fileVector.get(i);
			String officeName = (String) row.get(0);
			String officeCode = (String) row.get(1);
		
			entries.add(new TwoLevelEntry(officeCode, officeName));
		}
		return entries;
	}
}
