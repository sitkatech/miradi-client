/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
*/ 
package org.miradi.objecthelpers;

import java.util.Vector;

public class RegionsFileLoader extends TwoLevelFileLoader
{
	public RegionsFileLoader(String fileNameToUse)
	{
		super(fileNameToUse);
	}

	@Override
	protected Vector<TwoLevelEntry> processVector(Vector<Vector<String>> fileVector)
	{
		Vector<TwoLevelEntry> entries = new Vector<TwoLevelEntry>();
		for (int i  = 0; i < fileVector.size(); ++i)
		{
			Vector row = fileVector.get(i);
			TwoLevelEntry entry = createEntry(row);
			entries.add(entry);
		}
		return entries;
	}

	protected TwoLevelEntry createEntry(Vector row)
	{
		String officeCode = (String) row.get(0);
		String officeName = (String) row.get(1);

		return new AlwaysSelectableTwoLevelEntry(officeCode, officeName);
	}
}
