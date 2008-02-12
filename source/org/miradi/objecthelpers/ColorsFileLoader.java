/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.objecthelpers;

import java.util.Vector;

public class ColorsFileLoader extends TwoLevelFileLoader
{
	public ColorsFileLoader()
	{
		super("");
	}
	
	protected Vector processVector(Vector fileVector)
	{
		Vector entries = new Vector();
		for (int i  = 0; i < fileVector.size(); ++i)
		{
			Vector row = (Vector) fileVector.get(i);

			String varName = (String) row.get(0);
			String color = (String) row.get(1);
		
			entries.add(new TwoLevelEntry(varName, color));
		}
		return entries;
	}
}
