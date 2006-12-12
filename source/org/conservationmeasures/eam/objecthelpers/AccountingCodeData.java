/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objecthelpers;

public class AccountingCodeData
{
	
	public AccountingCodeData(String codeToUse, String labelToUse)
	{
		code = codeToUse;
		label = labelToUse;
	}

	public String getCode()
	{
		return code;
	}

	public String getLabel()
	{
		return label;
	}

	public String toString()
	{
		return getLabel();

	}

	String code;
	String label;
	
}
