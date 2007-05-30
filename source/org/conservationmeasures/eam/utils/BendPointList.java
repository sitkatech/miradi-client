/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.utils;

import java.awt.Point;


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
		super();
		EnhancedJsonArray array = json.optJsonArray(TAG_POINTS);
		if(array == null)
			array = new EnhancedJsonArray();
		
		for(int i = 0; i < array.length(); ++i)
		{
			Point point = EnhancedJsonObject.convertToPoint(array.getString(i));
			add(point);
		}
	}
	
	public BendPointList(String listAsJsonString) throws Exception
	{
		super(new EnhancedJsonObject(listAsJsonString));
	}
}
