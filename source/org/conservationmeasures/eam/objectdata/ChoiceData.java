/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objectdata;


public class ChoiceData extends StringData
{
	public ChoiceData()
	{
		super();
	}
	
	public ChoiceData(String newValue) throws Exception
	{
		this();
		set(newValue);
	}

}
