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

public class TncTerrestrialEcoRegionFileLoader extends TwoLevelFileLoader
{
	public TncTerrestrialEcoRegionFileLoader(String fileNameToUse)
	{
		super(fileNameToUse);
	}

	protected Vector<TwoLevelEntry> processVector(Vector<Vector<String>> fileVector)
	{
		Vector entries = new Vector();
		for (int i  = 0; i < fileVector.size(); ++i)
		{
			Vector row = fileVector.get(i);
			String code = (String) row.get(0);
			String name = (String) row.get(1);
		
			entries.add(new TwoLevelEntry(code, name));
		}
		return entries;
	}
}
