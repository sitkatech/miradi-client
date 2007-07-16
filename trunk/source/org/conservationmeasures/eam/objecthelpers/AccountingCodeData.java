/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
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
