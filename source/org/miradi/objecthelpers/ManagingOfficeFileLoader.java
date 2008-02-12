/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.objecthelpers;

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
			String officeCode = (String) row.get(0);
			String officeName = (String) row.get(3);
		
			entries.add(new TwoLevelEntry(officeCode, officeName));
		}
		return entries;
	}
}
