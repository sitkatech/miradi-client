/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.objecthelpers;

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
