/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objecthelpers;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringBufferInputStream;
import java.util.Vector;

import org.conservationmeasures.eam.utils.DelimitedFileLoader;

public class AccountingCodeLoader
{
	public static AccountingCodeData[] load(String data) throws Exception
	{
		InputStream is = new StringBufferInputStream(data);
		return load(new BufferedReader(new InputStreamReader(is)));
	}

	public static AccountingCodeData[] load(BufferedReader reader) throws Exception
	{
		Vector fileVector = DelimitedFileLoader.getDelimitedContents(reader);
		reader.close();
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
