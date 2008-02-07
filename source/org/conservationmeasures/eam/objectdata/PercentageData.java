/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objectdata;

public class PercentageData extends NumberData
{
	public PercentageData(String tagToUse)
	{
		super(tagToUse);
	}

	@Override
	public void set(String newValue) throws Exception
	{
		newValue = newValue.replace("%", "");
		super.set(newValue);
	}
}
