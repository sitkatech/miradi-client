/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objecthelpers;

import java.util.Vector;

public class TncFreshwaterEcoRegionFileLoader extends TwoLevelFileLoader
{
	public TncFreshwaterEcoRegionFileLoader(String fileNameToUse)
	{
		super(fileNameToUse);
	}

	protected Vector processVector(Vector fileVector)
	{
		Vector entries = new Vector();
		for (int i  = 0; i < fileVector.size(); ++i)
		{
			Vector row = (Vector) fileVector.get(i);
			String code = (String) row.get(0);
			String name = (String) row.get(1);
		
			entries.add(new TwoLevelEntry(code, name));
		}
		return entries;
	}
}
