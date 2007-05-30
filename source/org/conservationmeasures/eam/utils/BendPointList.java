/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.utils;

public class BendPointList extends PointList
{
	public BendPointList()
	{
		super();
	}
	
	public BendPointList(BendPointList copyFrom)
	{
		super(copyFrom);
	}
	
	public BendPointList(EnhancedJsonObject json) throws Exception
	{
		super(json);
	}
	
	public BendPointList(String listAsJsonString) throws Exception
	{
		super(new EnhancedJsonObject(listAsJsonString));
	}
}
