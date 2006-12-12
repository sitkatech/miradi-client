/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objecthelpers;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Vector;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.AccountingCode;
import org.conservationmeasures.eam.utils.DelimitedFileLoader;

public class AccountingCodeLoader
{
	public static AccountingCode[] load(String resourceFileName) throws Exception
	{

		InputStream is = EAM.class.getResourceAsStream(resourceFileName);
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		Vector fileVector = DelimitedFileLoader.getDelimitedContents(reader);
		reader.close();

		Vector accountingCodes = processVector(fileVector);
		return (AccountingCode[]) accountingCodes.toArray(new AccountingCode[0]);
	}

	public static AccountingCode[] load(BufferedReader reader) throws Exception
	{
		Vector fileVector = DelimitedFileLoader.getDelimitedContents(reader);

		Vector accountingCodes = processVector(fileVector);
		return (AccountingCode[]) accountingCodes.toArray(new AccountingCode[0]);
	}
	
	private static Vector processVector(Vector fileVector)
	{
		Vector AccountingCodes = new Vector();
		for(int vectorIndex = 0; vectorIndex < fileVector.size(); ++vectorIndex)
		{
			Vector row = (Vector) fileVector.get(vectorIndex);
			String code = (String) row.get(0);
			String label = (String) row.get(1);
			AccountingCodes.add(new AccountingCodeData(code, label));
		}
		return AccountingCodes;
	}

}
