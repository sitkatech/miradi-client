/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */

package org.conservationmeasures.eam.diagram.nodes;

import java.awt.Point;
import java.util.HashMap;

public class DataMap 
{
	public DataMap()
	{
		data = new HashMap();
	}
	
	public void put(String tag, Object dataToStore)
	{
		data.put(tag, dataToStore);
	}
	
	public int getInt(String tag)
	{
		return ((Integer)data.get(tag)).intValue();
	}
	
	public String getString(String tag)
	{
		return (String)data.get(tag);
	}
	
	public Point getPoint(String tag)
	{
		return (Point)data.get(tag);
	}
	

	private HashMap data;
}
