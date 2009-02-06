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

import java.io.Reader;
import java.io.StringReader;
import java.util.Vector;

import org.miradi.utils.DelimitedFileLoader;

public class AccountingCodeLoader extends DelimitedFileLoader
{
	public static AccountingCodeData[] load(String data) throws Exception
	{
		return load(new StringReader(data));
	}

	public static AccountingCodeData[] load(Reader reader) throws Exception
	{
		Vector fileVector = new DelimitedFileLoader().getDelimitedContents(reader);
		return processVector(fileVector);
	}
	
	private static AccountingCodeData[] processVector(Vector fileVector)
	{
		Vector AccountingCodeData = new Vector();
		for(int vectorIndex = 0; vectorIndex < fileVector.size(); ++vectorIndex)
		{
			Vector row = (Vector) fileVector.get(vectorIndex);
			String code = (String) row.get(0);
			String label = (String) row.get(1);
			AccountingCodeData.add(new AccountingCodeData(code, label));
		}
		return (AccountingCodeData[]) AccountingCodeData.toArray(new AccountingCodeData[0]);
	}

}
