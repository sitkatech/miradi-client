/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
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
