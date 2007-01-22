/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objectdata;


public class RatingData extends StringData
{
	public RatingData()
	{
		super();
	}
	
	public RatingData(String newValue) throws Exception
	{
		this();
		set(newValue);
	}

}
