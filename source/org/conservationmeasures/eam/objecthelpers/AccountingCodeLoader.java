/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objecthelpers;

import java.io.Reader;
import java.io.StringReader;
import java.util.Vector;

import org.conservationmeasures.eam.utils.DelimitedFileLoader;

public class AccountingCodeLoader
{
	public static AccountingCodeData[] load(String data) throws Exception
	{
		return load(new StringReader(data));
	}

	public static AccountingCodeData[] load(Reader reader) throws Exception
	{
		Vector fileVector = DelimitedFileLoader.getDelimitedContents(reader);
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
